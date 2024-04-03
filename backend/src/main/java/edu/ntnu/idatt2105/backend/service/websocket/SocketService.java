package edu.ntnu.idatt2105.backend.service.websocket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import edu.ntnu.idatt2105.backend.dto.websocket.*;
import edu.ntnu.idatt2105.backend.service.JWTTokenService;
import edu.ntnu.idatt2105.backend.service.quiz.QuestionService;
import edu.ntnu.idatt2105.backend.service.users.UserService;
import edu.ntnu.idatt2105.backend.util.Game;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;


import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * This class is responsible for handling the socket io service. It listens for events and sends events to the clients.
 * It also handles the game logic. See the documentation for each websocket event for more information. A websocket
 * event is a websocket message that is sent from the client to the server or from the server to the client. It is
 * important to have the right capitalization and spelling of the event names. The events are case-sensitive.
 * <p>Here is a list for all endpoints the client or host can send to the server:
 * <p> Host:
 * <p>- createGame
 * <p>- startGame
 * <p>- nextQuestion
 * <p>- beginAnswering
 * <p>- revealAnswer
 * <p>- getScoreBoard
 * <p> Client:
 * <p>- joinGame
 * <p>- answerQuestion
 * <p> Next are all endpoints the server can send to the client. These are the ones that the client/host listens for:
 * <p> Host and client:
 * <p>- gameDoesNotExist
 * <p>- gameEnded
 * <p>- invalidToken
 * <p> Host:
 * <p>- gameCreated
 * <p>- playerJoined
 * <p>- notHost
 * <p>- getQuestion
 * <p>- everyOneAnswered
 * <p>- getScoreBoard
 * <p> Client:
 * <p>- gameJoined
 * <p>- waitForQuestion
 * <p>- beginAnswering
 * <p>- questionAnswered
 * <p>- yourScore
 *
 * @version 1.1 30.03.2024
 * @author Brage Halvorsen Kvamme
 */
@Service
@RequiredArgsConstructor
public class SocketService {
    Logger logger = Logger.getLogger(SocketService.class.getName());
    private final SocketIOServer server;
    private final GameService gameService;
    private final JWTTokenService jwtTokenService;
    private final UserService userService;
    private final QuestionService questionService;
    private final Environment env;


    /**
     * Inits the socket io service. All event listeners are added.
     */
    @PostConstruct
    public void initSocketIoService() {
        AtomicBoolean isTest = new AtomicBoolean(false);
        Arrays.stream(env.getActiveProfiles()).forEach(env -> {
            if (env.equals("test")) {
                isTest.set(true);
            }
        });
        if(isTest.get()) {
            return;
        }
        server.addConnectListener(this::onConnect);
        server.addDisconnectListener(this::onDisconnect);
        server.addEventListener("createGame", CreateGameDTO.class, this::createGame);
        server.addEventListener("joinGame", JoinGameDTO.class, this::joinGame);
        server.addEventListener("startGame", GameValidationDTO.class, this::startGame);
        server.addEventListener("nextQuestion", GameValidationDTO.class, this::nextQuestion);
        server.addEventListener("answerQuestion", SubmitAnswerDTO.class, this::answerQuestion);
        server.addEventListener("revealAnswer", GameValidationDTO.class, this::revealAnswer);
        server.addEventListener("beginAnswering", GameValidationDTO.class, this::beginAnswering);
        server.addEventListener("getScoreBoard", GameValidationDTO.class, this::getScoreBoard);

        server.start();
    }

    /**
     * This method is automatically called when a user connects to the socket io server. It logs the event to the backend.
     * The "onConnect" is automatically called when a user connects to the server.
     *
     * @param client The client that connected
     */
    private void onConnect(SocketIOClient client) {
        logger.info("a user connected: " + client.getSessionId());
    }

    /**
     * This method is called when a user disconnects from the socket io server. It logs the event to the backend.
     * If the host of a room leaves, the room is closed. If an anonymous user leaves, the user is deleted from the game.
     *
     * @param client The client that disconnected
     */
    private void onDisconnect(SocketIOClient client) {
        logger.info("a user disconnected: " + client.getSessionId());
        if(gameService.deleteAnonUserFromGame(client.getSessionId())) {
            logger.info("Anon player deleted from game: " + client.getSessionId());
            String code = getRoomCode(client);
            String username = gameService.getGame(code).getAnonymousPlayers().get(client.getSessionId()).getUsername();
            server.getRoomOperations(code).sendEvent("playerLeft", username);
        }

        if(gameService.deleteGameFromUUID(client.getSessionId())) {
            server.getRoomOperations(getRoomCode(client)).sendEvent("gameEnded", "The game has ended");
            logger.info("Room deleted: " + client.getSessionId());
        }
    }

    /**
     * Send  "createGame" from host.
     * <p>
     * Listen for "invalidToken" and "gameCreated" from server.
     * <p>
     * It creates a game and sends the game code back to the client for it to be shared with others to
     * join the game. After creating the game, other clients can join the game with the "joinGame" event.
     *
     * @param client The client that sent the event
     * @param data The data sent with the event
     * @param ackRequest The ack request
     */
    @Transactional
    public void createGame(SocketIOClient client, CreateGameDTO data, AckRequest ackRequest) {
        logger.info("createGame event received: " + data);
        String token = data.jwt();
        Jwt jwt = jwtTokenService.getJwt(token);
        if (!jwtTokenService.isValidToken(jwt)) {
            client.sendEvent("invalidToken", "Invalid token");
            return;
        }

        String code = gameService.createGame(jwt.getSubject(), data.quizId(), client.getSessionId());

        client.joinRoom(code);
        logger.info("Host rooms: "+ getRoomCode(client));
        client.sendEvent("gameCreated", code);
    }

    /**
     * Send "joinGame" from client.
     * <p>
     * Listen for "gameDoesNotExist", "waitForQuestion", "gameJoined", "invalidToken" and "yourScore" from server.
     * <p>
     * Join a game with the given code. If the game does not exist, the client is notified.
     * If the game has already started, the client is notified. If the user is already in the game, the user is
     * rejoining the game. The user can join the game with a username or a jwt token. If the user joins with a jwt token,
     * the user is authenticated and the user is added to the game with the email from the jwt token. If the user joins
     * without authentication, the user is added to the game as with the username.
     * If the game is already started, the client gets notified. This way, the user doesn't get a screen that says
     * "waiting for host to start the game" if the game has already started.
     *
     * @param client The client that sent the event
     * @param data The data sent with the event
     * @param ackRequest The ack request
     */
    private void joinGame(SocketIOClient client, JoinGameDTO data, AckRequest ackRequest) {
        logger.info("joinGame event received: " + data);
        Game game = gameService.getGame(data.code());
        if (game == null) {
            client.sendEvent("gameDoesNotExist", data.code());
            return;
        }

        boolean hasToken = data.jwt() != null;
        if (hasToken) { // Logged in user scenario
            Jwt jwt = jwtTokenService.getJwt(data.jwt());
            if (!jwtTokenService.isValidToken(jwt)) {
                client.sendEvent("invalidToken", "Invalid token");
                return;
            }
            if (!game.addPlayer(userService.getUserByEmail(jwt.getSubject()), client.getSessionId())) {
                logger.info("User is already in the game, rejoining! WebsocketID updated. User email: " + jwt.getSubject());
            } else {
                server.getRoomOperations("playerJoined", jwt.getSubject());
            }
            if (game.isStarted()) {
                client.sendEvent("waitForQuestion", "The game has already started");
                client.sendEvent("yourScore", game.getPlayers().stream().filter(
                        player -> player.getUser().getEmail().equals(jwt.getSubject())).findFirst().get().getScore()
                );
            }

        } else { // Anonymous user scenario
            // Rejoining won't really work, as the user will have another session ID when rejoining.
            // Instead, cookies or some other temp storage could be a solution
            game.addPlayer(client.getSessionId(), data.username());
            server.getRoomOperations("playerJoined", data.username());

        }

        logger.info("anon players " + game.getAnonymousPlayers().toString());
        logger.info("Logged in players" + game.getPlayers().toString());

        client.joinRoom(data.code());
        client.sendEvent("gameJoined", "You joined a game with the code: " + game.getCode());
    }

    /**
     * <p>Send "startGame" from host.
     * <p>Listen to "getQuestion" from server.
     * <p>It starts the game if the user is the host of the game. If the user is not the host of the game, the user is
     * notified.
     *
     * @param client The client that sent the event
     * @param data The data sent with the event
     * @param ackRequest The ack request
     */
    @Transactional
    public void startGame(SocketIOClient client, GameValidationDTO data, AckRequest ackRequest) {
        logger.info("startGame event received: " + data);
        Game game = validateIsHostAndReturnGame(client, data);
        if (game == null)
            return;
        game.startGame();
        client.sendEvent("getQuestion", questionService.getQuestionDTO(game.getCurrentQuestion().getQuestionId()));
        server.getRoomOperations(game.getCode()).sendEvent("waitForQuestion", "The game is starting");
    }

    /**
     * <p>Send "nextQuestion" from host.
     * <p>Listen to "getQuestion" from server.
     * <p>It starts the next question if the user is the host of the game. If the user is not the host of the game, the
     * user is notified. If the game has ended, the user is notified. The "nextQuestion" event is automatically called
     * when the host of the game starts the next question.
     *
     * @param client The client that sent the event
     * @param data The data sent with the event
     * @param ackRequest The ack request
     */
    private void nextQuestion(SocketIOClient client, GameValidationDTO data, AckRequest ackRequest) {
        logger.info("nextQuestion event received: " + data);
        Game game = validateIsHostAndReturnGame(client, data);
        if (game == null)
            return;
        if (game.nextQuestion()) {
            // Send the question and alternatives to everyone in the room. The host can show the question on a screen.
            // The players can answer the question.
            client.sendEvent(
                    "getQuestion", questionService.getQuestionDTO(game.getCurrentQuestion().getQuestionId())
            );
            server.getRoomOperations(game.getCode()).sendEvent(
                    "waitForQuestion", "The question has started. Look at the screen!"
            );
        } else {
            server.getRoomOperations(game.getCode()).sendEvent("gameEnded", "The game has ended");
        }
    }

    /**
     * <p>Send "beginAnswering" from host.
     * <p>Client should listen to "getQuestion" from server.
     * <p>It begins the answering phase of the game. The host can show the question on a screen and the players can answer
     * the question.
     *
     * @param client The client that sent the event
     * @param data The data sent with the event
     * @param ackRequest The ack request
     */
    private void beginAnswering(SocketIOClient client, GameValidationDTO data, AckRequest ackRequest) {
        logger.info("beginAnswering event received: " + data);
        Game game = validateIsHostAndReturnGame(client, data);
        if (game == null) {
            client.sendEvent("gameDoesNotExist", data);
            return;
        }
        SendAlternativesDTO alternatives = gameService.getAlternatives(game.getCode());
        gameService.beginAnsweringTimer(game.getCode());
        server.getRoomOperations(game.getCode()).sendEvent("beginAnswering", alternatives);
    }

    /**
     * <p>Send "answerQuestion" from client.
     * <p>Listen to "questionAnswered" from server.
     * <p>It answers a question in the game room. If the answer is correct, the player gets points. If the user is not
     * signed in, the user is notified. If the game does not exist, the user is notified. If the user has already
     *
     * @param client The client that sent the question
     * @param data The data sent with the event
     * @param ackRequest The ack request
     */
    private void answerQuestion(SocketIOClient client, SubmitAnswerDTO data, AckRequest ackRequest) {
        boolean isPlayerSignedIn = data.jwt() != null;

        logger.info("answerQuestion event received: " + data);
        String code = getRoomCode(client);

        if (code.isEmpty()) {
            client.sendEvent("gameDoesNotExist", data);
            return;
        }
        Game game = gameService.getGame(code);
        String correctAnswer = questionService.getCorrectAnswer(game.getCurrentQuestion().getQuestionId());
        logger.info("Correct answer: " + correctAnswer + ", your answer: " + data.answer());
        if (game.everyoneAnswered()) {
            server.getRoomOperations(code).sendEvent("everyoneAnswered", "Every user has answered the question");
        }
        if (isPlayerSignedIn) {
            logger.info("Player is signed in and answering a question");
            Jwt jwt = jwtTokenService.getJwt(data.jwt());
            if (!jwtTokenService.isValidToken(jwt)) {
                client.sendEvent("invalidToken", "Invalid token");
                return;
            }
            if (game.answerQuestion(data.answer(), jwt.getSubject(), correctAnswer)) {
                logger.info("Player answered the question");
                client.sendEvent("questionAnswered", "The question has been answered");
            } else {
                logger.info("Player did not answer the question");
                client.sendEvent("questionAnswered", "Too late, or you have already answered the question");
            }
        } else {
            if (game.answerQuestionAnon(data.answer(), client.getSessionId(), correctAnswer)) {
                logger.info("Player answered the question");
                client.sendEvent("questionAnswered", "The question has been answered");
            } else {
                logger.info("Player did not answer the question");
                client.sendEvent("questionAnswered", "Too late, or you have already answered the question");
            }
        }
    }

    /**
     * <p>Send "revealAnswer" from host.
     * <p>Listen to "answerRevealed" from server.
     * <p>It reveals the answer to the question. The host can show the answer on a screen. The players can see if they
     * answered the question correctly or not.
     *
     * @param client The client that sent the event
     * @param data The data sent with the event
     * @param ackRequest The ack request
     */
    private void revealAnswer(SocketIOClient client, GameValidationDTO data, AckRequest ackRequest) {
        logger.info("revealAnswer event received from client: " + client.getSessionId());
        Game game = validateIsHostAndReturnGame(client, data);
        if (game == null)
            return;
        server.getAllClients().forEach(playerClient -> {
            if (playerClient.getAllRooms().contains(game.getCode())) {
                playerClient.sendEvent("answerRevealed", game.answeredCorrect(playerClient.getSessionId()));
            }
        });
    }

    /**
     * <p>Send "getScoreBoard" from host.
     * <p>Listen to "getScoreBoard" from server.
     * <p>It gets the scoreboard of the game. The host can show the scoreboard on a screen. The players can see their
     * score on their screen.
     *
     * @param client The client that sent the event
     * @param data The data sent with the event
     * @param ackRequest The ack request
     */
    private void getScoreBoard(SocketIOClient client, GameValidationDTO data, AckRequest ackRequest) {
        logger.info("getScoreBoard event received from client: " + client.getSessionId());
        Game game = validateIsHostAndReturnGame(client, data);
        if (game == null)
            return;
        LeaderboardDTO leaderboard = game.getLeaderboard();
        client.sendEvent("getScoreBoard", leaderboard);
        List<UUID> players = game.getAllPlayerUUIDs();
        for (UUID player : players) {
            logger.info("score of player: " +game.getPlayerScore(player));
            server.getClient(player).sendEvent("yourScore", game.getPlayerScore(player));
        }
    }

    /**
     * Validates that the user is the host of the game. If the user is not the host of the game, the user is notified.
     * If the game does not exist, the user is notified.
     *
     * @param client The client that sent the event
     * @param data The data sent with the event
     * @return The game if the user is the host of the game. Null if the user is not the host of the game or the game
     * does not exist.
     */
    private Game validateIsHostAndReturnGame(SocketIOClient client, GameValidationDTO data) {
        String code = getRoomCode(client);
        if (code == null) {
            client.sendEvent("gameDoesNotExist");
            return null;
        }
        Game game = gameService.getGame(code);
        if (game == null) {
            client.sendEvent("gameDoesNotExist", code);
            return null;
        }
        if (!gameService.isGameHost(code, data.jwt())) {
            client.sendEvent("notHost", "You are not the host of this game");
            return null;
        }
        return game;
    }

    /**
     * Gets the room code of the client. The room code is the code of the game room that the client is in.
     *
     * @param client The client to get the room code from
     * @return The room code of the client
     */
    private String getRoomCode(SocketIOClient client) {
        logger.info("all rooms from lient " + client.getSessionId() + ": " + client.getAllRooms().toString());
        return client.getAllRooms().stream().filter(room -> !room.isBlank()).findFirst().orElse(null);
    }
}
