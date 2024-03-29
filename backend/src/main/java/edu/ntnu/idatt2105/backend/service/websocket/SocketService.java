package edu.ntnu.idatt2105.backend.service.websocket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import edu.ntnu.idatt2105.backend.dto.quiz.QuestionStartedDTO;
import edu.ntnu.idatt2105.backend.dto.websocket.CreateGameDTO;
import edu.ntnu.idatt2105.backend.dto.websocket.JoinGameDTO;
import edu.ntnu.idatt2105.backend.dto.websocket.GameValidationDTO;
import edu.ntnu.idatt2105.backend.dto.websocket.SubmitAnswerDTO;
import edu.ntnu.idatt2105.backend.model.quiz.question.MultipleChoice;
import edu.ntnu.idatt2105.backend.model.quiz.question.Question;
import edu.ntnu.idatt2105.backend.service.JWTTokenService;
import edu.ntnu.idatt2105.backend.service.quiz.QuizService;
import edu.ntnu.idatt2105.backend.service.users.UserService;
import edu.ntnu.idatt2105.backend.util.Game;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * This class is responsible for handling the socket io service. It listens for events and sends events to the clients.
 * It also handles the game logic. Se the documentation for each websocket event for more information. A websocket
 * event is a websocket message that is sent from the client to the server or from the server to the client.
 * <p>Here is a list for all endpoints the client can send to the server:
 * <p>- createGame: Creates a game with the given quiz id. The user that creates the game is the host of the game.
 * <p>- joinGame: Joins a game with the given code. The user can join with a username or a jwt token. If the user joins
 * with a jwt token, the user is authenticated and added to the game with the email from the jwt token. If the user
 * joins without authentication, the user is added to the game with the username.
 * <p>- startGame: Starts the game if the user is the host of the game.
 * <p>- nextQuestion: Starts the next question if the user is the host of the game.
 * <p>- answerQuestion: Answers the current question in the game.
 * <p> Next are all endpoints the server can send to the client. These are the ones that the client listens for:
 * <p>- gameDoesNotExist: The game with the given code does not exist. Usually when a user enters the wrong code.
 * <p>- gameCreated: The game was successfully created and the game code is sent back to the client.
 * <p>- gameJoined: The user has successfully joined the game.
 * <p>- gameStarted: The gam notHost: The user ise has started.
 * <p>- not the host of the game.
 * <p>- questionStarted: The question has started. The question and alternatives are sent to the clients.
 * <p>- questionAnswered: The question has been answered.
 * <p>- gameEnded: The game has ended.
 * <p>- answerRevealed: The answer to the current question has been revealed.
 * <p>- yourScore: The score of the user is sent to the user.
 *
 * @version 1.0 28.03.2024
 * @author brage
 */
@Service
@RequiredArgsConstructor
public class SocketService {
    Logger logger = Logger.getLogger(SocketService.class.getName());
    private final SocketIOServer server;
    private final GameService gameService;
    private final JWTTokenService jwtTokenService;
    private final QuizService quizService;
    private final UserService userService;

    /**
     * Inits the socket io service. All listeners are added.
     */
    @PostConstruct
    public void initSocketIoService() {
        server.addConnectListener(this::onConnect);
        server.addDisconnectListener(this::onDisconnect);
        server.addEventListener("createGame", CreateGameDTO.class, this::createGame);
        server.addEventListener("joinGame", JoinGameDTO.class, this::joinGame);
        server.addEventListener("startGame", GameValidationDTO.class, this::startGame);
        server.addEventListener("nextQuestion", GameValidationDTO.class, this::nextQuestion);
        server.addEventListener("answerQuestion", SubmitAnswerDTO.class, this::answerQuestion);
        server.addEventListener("revealAnswer", GameValidationDTO.class, this::revealAnswer);
        server.addEventListener("beginAnswering", GameValidationDTO.class, this::beginAnswering);

        server.start();
    }

    /**
     * This method is called when a user connects to the socket io server. It logs the event to the backend.
     * The "onConnect" is automatically called when a user connects to the server.
     *
     * @param client The client that connected
     */
    private void onConnect(SocketIOClient client) {
        logger.info("a user connected: " + client.getSessionId());
    }

    /**
     * This method is called when a user disconnects from the socket io server. It logs the event to the backend.
     * If the host of a room leaves, the room is closed. The "onDisconnect" is
     * automatically called when a user disconnects
     *
     * @param client The client that disconnected
     */
    private void onDisconnect(SocketIOClient client) {
        logger.info("a user disconnected: " + client.getSessionId());
        if(gameService.deleteAnonUserFromGame(client.getSessionId())) {
            logger.info("Anon player deleted from game: " + client.getSessionId());
        }

        if(gameService.deleteGameFromUUID(client.getSessionId())) {
            server.getRoomOperations(getRoomCode(client)).sendEvent("gameEnded", "The game has ended");
            logger.info("Room deleted: " + client.getSessionId());
        }
    }

    /**
     * This method is called when the "createGame" event is received.
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
     * This method is called when the "joinGame" event is received.
     * It joins a game with the given code. If the game does not exist, the client is notified.
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
            if (!jwtTokenService.isValidToken(jwt))
                throw new RuntimeException("Invalid token"); // Lag bedre exception
            if (!game.addPlayer(userService.getUserByEmail(jwt.getSubject()))) {
                logger.info("User is already in the game, rejoining! User email: " + jwt.getSubject());
            }
            if (game.isStarted()) {
                client.sendEvent("gameStarted", "The game has already started");
                client.sendEvent("yourScore", game.getPlayers().stream().filter(
                        player -> player.getUser().getEmail().equals(jwt.getSubject())).findFirst().get().getScore()
                );
            }
        } else { // Anonymous user scenario
            // Rejoining won't really work, as the user will have another session ID when rejoining.
            // Instead, cookies or some other temp storage could be a solution
            if (!game.addPlayer(client.getSessionId(), data.username())) {
                logger.info("Anon user is already in the game, rejoining! User session ID: "
                        + client.getSessionId());
            }
        }

        //TODO: debug
        logger.info("anon players " + game.getAnonymousPlayers().toString());
        logger.info("Logged in players" + game.getPlayers().toString());

        client.joinRoom(data.code());
        client.sendEvent("gameJoined", "You joined a game with the code: " + game.getCode());
    }

    /**
     * This method is called when the "startGame" event is received.
     * It starts the game if the user is the host of the game. If the user is not the host of the game, the user is
     * notified. The "startGame" event is automatically called when the host of the game starts the game.
     *
     * @param client The client that sent the event
     * @param data The data sent with the event
     * @param ackRequest The ack request
     */
    private void startGame(SocketIOClient client, GameValidationDTO data, AckRequest ackRequest) {
        logger.info("startGame event received: " + data);
        Game game = validateIsHostAndReturnGame(client, data);
        if (game == null)
            return;
        game.startGame();
        server.getRoomOperations(game.getCode()).sendEvent("gameStarted", "The game has started");
    }

    /**
     * This method is called when the "nextQuestion" event is received.
     * It starts the next question if the user is the host of the game. If the user is not the host of the game, the
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
            server.getRoomOperations(game.getCode()).sendEvent("questionStarted", getQuestionStartedDTO(game)); // TODO: Send the question here.
        } else {
            server.getRoomOperations(game.getCode()).sendEvent("gameEnded", "The game has ended");
        }
    }

    private void beginAnswering(SocketIOClient client, GameValidationDTO data, AckRequest ackRequest) {
        logger.info("beginAnswering event received: " + data);
        Game game = validateIsHostAndReturnGame(client, data);
        if (game == null) {
            client.sendEvent("gameDoesNotExist", data);
            return;
        }
        String questionType = game.getCurrentQuestion().getQuestionType().toString();
        server.getRoomOperations(game.getCode()).sendEvent("beginAnswering", questionType);
    }

    /**
     * This method is called when the "answerQuestion" event is received.
     * It answers the current question in the game. If the game does not exist, the user is notified. If the question
     * is answered, the user gets notified. If the game has ended, the user gets notified.
     *
     * @param client The client that sent the question
     * @param data The data sent with the event
     * @param ackRequest The ack request
     */
    private void answerQuestion(SocketIOClient client, SubmitAnswerDTO data, AckRequest ackRequest) {
//        boolean isPlayerSignedIn = data.jwt() != null;
//
//        logger.info("answerQuestion event received: " + data);
//        String code = getRoomCode(client);
//
//        if (code.isEmpty()) {
//            client.sendEvent("gameDoesNotExist", data);
//            return;
//        }
//        Game game = gameService.getGame(code);
//        if (isPlayerSignedIn) {
//            Jwt jwt = jwtTokenService.getJwt(data.jwt());
//            if (!jwtTokenService.isValidToken(jwt)) {
//                client.sendEvent("invalidToken", "Invalid token");
//                return;
//            }
//
//
//        } else {
//            if (!game.answerQuestion(data, client.getSessionId())) {
//                client.sendEvent("gameEnded", "The game has ended");
//            }
//        }
//
//
//        if (game.answerQuestion(data)) {
//           client.sendEvent("questionAnswered", "The question has been answered");
//        } else {
//            client.sendEvent("gameEnded", "The game has ended");
//        }
    }

    /**
     * This method is called when the "revealAnswer" event is received.
     * It reveals the answer to the current question in the game. If the user is not the host of the game, the user is
     * notified. If the game does not exist, the user is notified.
     *
     * @param client
     * @param data
     * @param ackRequest
     */
    private void revealAnswer(SocketIOClient client, GameValidationDTO data, AckRequest ackRequest) {
        logger.info("revealAnswer event received from client: " + client.getSessionId());
        Game game = validateIsHostAndReturnGame(client, data);
        if (game == null)
            return;
        server.getRoomOperations(game.getCode()).sendEvent("answerRevealed", game.getCurrentQuestion().getCorrectAnswer());
    }



    /**
     * Converts a game to a QuestionStartedDTO. This is used to send the question to the clients.
     *
     * @param game The game to convert
     * @return The converted game
     */
    private QuestionStartedDTO getQuestionStartedDTO(Game game) {
        Question currentQuestion = game.getCurrentQuestion();
        return QuestionStartedDTO.builder()
                .question(currentQuestion.getQuestion())
                .options(currentQuestion.getChoices().stream().map(MultipleChoice::getAlternative).toList())
                .questionType(currentQuestion.getQuestionType().toString())
                .difficulty(currentQuestion.getDifficulty().toString())
                .build();
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

    private String getRoomCode(SocketIOClient client) {
        return client.getAllRooms().stream().filter(room -> !room.isBlank()).findFirst().orElseThrow();
    }
}
