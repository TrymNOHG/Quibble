package edu.ntnu.idatt2105.backend.util;

import edu.ntnu.idatt2105.backend.dto.websocket.LeaderboardDTO;
import edu.ntnu.idatt2105.backend.dto.websocket.PlayerScoreDTO;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.quiz.question.Question;
import edu.ntnu.idatt2105.backend.model.users.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.Triple;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class represents a room in the quiz game.
 *
 * @version 1.0 31.03.2024
 * @see Player
 * @see AnonymousPlayer
 * @author Brage Halvorsen Kvamme
 */
@Data
@Slf4j
public class Game {
    private String code;
    private final List<Player> players = new ArrayList<>();
    private final Map<UUID, AnonymousPlayer> anonymousPlayers = new HashMap<>();
    private User host;
    private UUID hostUUID;
    private int questionIndex = 0;
    private Quiz quiz;
    private boolean started = false;
    private List<Question> questions;
    private Map<Integer, Instant> answerStart = new HashMap<>();

    /**
     * Constructor for the Room class.
     *
     * @param code The code of the room.
     * @param host The host of the room.
     * @param quiz The quiz that the room is playing.
     */
    public Game(String code, User host, Quiz quiz, List<Question> questions, UUID hostUUID) {
        this.host = host;
        this.code = code;
        this.quiz = quiz;
        this.hostUUID = hostUUID;
        this.questions = questions;
    }

    /**
     * Adds a player that is already authenticated to the game room.
     *
     * @param user The user to add to the game room.
     * @return True if the player was added, false if the player was already in the room.
     */
    public boolean addPlayer(User user, UUID webSocketId) {
        for (Player player : players) {
            if (player.getUser().getEmail().equals(user.getEmail())) {
                player.setWebSocketId(webSocketId);
                return false;
            }
        }
        return players.add(new Player(user, webSocketId));
    }

    /**
     * Adds an anonymous player to the game room.
     *
     * @param sessionID The websocket session ID of the player.
     * @param username The username of the player.
     */
    public void addPlayer(UUID sessionID, String username, String imageId) {
        anonymousPlayers.put(sessionID, new AnonymousPlayer(username, imageId));
    }

    /**
     * Gets the leaderboard in the game room. The leaderboard is a list of players in the room, sorted by score.
     * Each element in the leaderboard contains the profile picture link, username and score of the player, in that
     * exact order.
     *
     * @return The leaderboard of the game room.
     */
    public LeaderboardDTO getLeaderboard() {
        // Add the signed in players to the leaderboard
        LeaderboardDTO leaderboardDTO = LeaderboardDTO.builder()
                .player(new ArrayList<>())
                .build();
        for (Player player : players) {
            User user = player.getUser();
            assert leaderboardDTO.player() != null;
            leaderboardDTO.player().add(PlayerScoreDTO.builder()
                    .username(user.getUsername())
                    .score(player.getScore())
                    .imageId(user.getUserId().toString())
                    .build());
        }

        anonymousPlayers.forEach(
                (key, value) -> {
                    assert leaderboardDTO.player() != null;
                    leaderboardDTO.player().add(PlayerScoreDTO.builder()
                            .imageId(value.getImageId())
                            .username(value.getUsername())
                            .score(value.getScore())
                            .build());
                }
        ); // This adds the anonymous players to the leaderboard
        return leaderboardDTO;
    }

    /**
     * Starts the game in the game room.
     */
    public void startGame() {
        started = true;
        this.questionIndex = 0;
    }

    /**
     * Gets the next question in the game room. If there is no next question, the game is over. Can't get next question
     * if the game has not started.
     *
     * @return True if there is a next question, false otherwise.
     */
    public boolean nextQuestion() {
        return ++this.questionIndex < quiz.getQuestions().size() && started;
    }

    /**
     * Gets the current question in the game room.
     *
     * @return The current question in the game room.
     */
    public Question getCurrentQuestion() {
        return questions.get(this.questionIndex);
    }

    /**
     * Answers a question in the game room. If the answer is correct, the player gets points. The player is anonymous.
     *
     * @param answer The answer to the question.
     * @param userId The UUID of the player.
     * @param correctAnswer The correct answer to the question.
     * @return False if the answer was answered in time or has not been answered before, true otherwise.
     */
    public boolean answerQuestionAnon(String answer, UUID userId, String correctAnswer) {
        int maxQuestionTime = 90;
        Instant now = Instant.now();
        AnonymousPlayer anonPlayer = anonymousPlayers.get(userId);
        if (anonPlayer.getAnswerTimes().get(questionIndex) != null) {
            log.info("Already answered");
            return false;
        }
        anonPlayer.getAnswerTimes().put(questionIndex, now);
        float seconds = now.getEpochSecond() - answerStart.get(questionIndex).getEpochSecond();
        if (seconds > maxQuestionTime) {
            log.info("Answered too late");
            return false;
        }
        if (answer.equals(correctAnswer)) {
            if(anonPlayer.getCorrectAnswers().containsKey(questionIndex)){
                log.info("Already answered");
                return false;
            }
            anonPlayer.getCorrectAnswers().put(questionIndex, true);
            anonPlayer.setScore((int) (anonPlayer.getScore() + (1000 * (1 - seconds / maxQuestionTime))));
            log.info("Question answered correctly, you got " + (int) (1000 * (1 - seconds / maxQuestionTime)) + " points!");
        } else {
            log.info("Question answered incorrectly");
            anonPlayer.getCorrectAnswers().put(questionIndex, false);
        }
        return true;

    }

    /**
     * Answers a question in the game room. If the answer is correct, the player gets points.
     *
     * @param answer The answer to the question.
     * @param email The email of the player.
     * @param correctAnswer The correct answer to the question.
     * @return False if the answer was answered in time or has not been answered before, true otherwise.
     */
    public boolean answerQuestion(String answer, String email, String correctAnswer) {
        int maxQuestionTime = 90;
        Instant now = Instant.now();
        Player player = players.stream().filter(p -> p.getUser().getEmail().equals(email)).findFirst().orElseThrow();
        if (player.getAnswerTimes().get(questionIndex) != null) {
            return false;
        }
        player.getAnswerTimes().put(questionIndex, now);
        float seconds = now.getEpochSecond() - answerStart.get(questionIndex).getEpochSecond();
        if (seconds > maxQuestionTime) {
            return false;
        }
        if (answer.equals(correctAnswer)) {
            if(player.getCorrectAnswers().containsKey(questionIndex)){
                return false;
            }
            player.getCorrectAnswers().put(questionIndex, true);
            player.setScore((int) (player.getScore() + (1000 * (1 - seconds / maxQuestionTime))));
        } else {
            player.getCorrectAnswers().put(questionIndex, false);
        }
        return true;
    }

    /**
     * Checks if a player has answered the current question correctly.
     *
     * @param sessionId The UUID of the player.
     * @return True if the player has answered the question correctly, false otherwise.
     */
    public boolean answeredCorrect(UUID sessionId) {
        AtomicBoolean isCorrect = new AtomicBoolean(false);
        players.forEach(player -> {
            if (player.getWebSocketId().equals(sessionId)) {
                isCorrect.set(player.getCorrectAnswers().getOrDefault(questionIndex, false));
            }
        });
        if (isCorrect.get()) {
            return true;
        }
        AnonymousPlayer anonPlayer = anonymousPlayers.getOrDefault(sessionId, null);
        if (anonPlayer == null) {
            return false;
        } else {
            return anonPlayer.getCorrectAnswers().getOrDefault(questionIndex, false);
        }
    }

    /**
     * Starts the timer for the current question.
     */
    public void startTimer() {
        answerStart.put(questionIndex, Instant.now());
    }

    /**
     * Gets the UUIDs of every player in the game
     *
     * @return A list of the UUIDs of every player in the game.
     */
    public List<UUID> getAllPlayerUUIDs() {
        List<UUID> uuids = new ArrayList<>();
        players.forEach(player -> uuids.add(player.getWebSocketId()));
        anonymousPlayers.forEach((key, value) -> uuids.add(key));
        return uuids;
    }

    /**
     * Gets the score of a player in the game.
     *
     * @param player The UUID of the player.
     * @return The score of the player.
     */
    public int getPlayerScore(UUID player) {
        for (Player p : players) {
            if (p.getWebSocketId().equals(player)) {
                return p.getScore();
            }
        }
        return anonymousPlayers.get(player).getScore();
    }

    /**
     * Checks if everyone has answered the current question.
     *
     * @return True if everyone has answered the current question, false otherwise.
     */
    public boolean everyoneAnswered() {
        for (Player player : players) {
            if (player.getAnswerTimes().containsKey(questionIndex)) {
                return false;
            }
        }
        for (AnonymousPlayer player : anonymousPlayers.values()) {
            if (player.getAnswerTimes().containsKey(questionIndex)) {
                return false;
            }
        }
        return true;
    }
}
