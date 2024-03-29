package edu.ntnu.idatt2105.backend.util;

import edu.ntnu.idatt2105.backend.dto.websocket.SubmitAnswerDTO;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.quiz.question.Question;
import edu.ntnu.idatt2105.backend.model.users.User;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.misc.Triple;

import java.util.*;

/**
 * This class represents a room in the quiz game.
 */
@Data
public class Game {
    private String code;
    private final List<Player> players = new ArrayList<>();
    private final Map<UUID, Pair<String, Integer>> anonymousPlayers = new HashMap<>();
    private User host;
    private UUID hostUUID;
    private int questionIndex = -1;
    private Quiz quiz;
    private boolean started = false;
    private List<Question> questions;

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
    public boolean addPlayer(User user) {
        for (Player player : players) {
            if (player.getUser().getEmail().equals(user.getEmail())) {
                return false;
            }
        }
        return players.add(new Player(user, 0, new ArrayList<>()));
    }

    /**
     * Adds an anonymous player to the game room.
     *
     * @param sessionID The websocket session ID of the player.
     * @param username The username of the player.
     * @return True if the player was added, false if the player was already in the room.
     */
    public boolean addPlayer(UUID sessionID, String username) {
        if (anonymousPlayers.containsKey(sessionID)) {
            anonymousPlayers.put(sessionID, new Pair<>(username, 0));
            return false;
        }
        anonymousPlayers.put(sessionID, new Pair<>(username, 0));
        return true;
    }

    /**
     * Gets the leaderboard in the game room. The leaderboard is a list of players in the room, sorted by score.
     * Each element in the leaderboard contains the profile picture link, username and score of the player, in that
     * exact order.
     *
     * @return The leaderboard of the game room.
     */
    public List<Triple<String, String, Integer>> getLeaderboard() {
        List<Triple<String, String, Integer>> leaderboard = new ArrayList<>();
        // Add the signed in players to the leaderboard
        for (Player player : players) {
            User user = player.getUser();
            leaderboard.add(new Triple<>(user.getProfilePicLink(), user.getUsername(), player.getScore()));
        }

        String shrekImage = "https://i.pinimg.com/550x/91/77/a1/9177a1f681a6f81b52d945199a9114e5.jpg";
        anonymousPlayers.forEach(
                (key, value) -> leaderboard.add(new Triple<>(shrekImage, value.a, value.b))
        ); // This adds the anonymous players to the leaderboard
        leaderboard.sort(Comparator.comparingInt(l -> -l.c)); // Compares the players by score
        return leaderboard;
    }

    public void startGame() {
        started = true;
        questionIndex = 0;
    }

    public boolean nextQuestion() {
        return ++questionIndex < quiz.getQuestions().size();
    }

    public Question getCurrentQuestion() {
        return questions.get(questionIndex);
    }

    public boolean answerQuestionAnon(String username, String answer) {
//        if (data.jwt() != null) {
//            for (Player player : players) {
//                if (player.getUser().getEmail().equals())
//            }
//        }
        return false;

    }

    public boolean answerQuestion(String email, String answer) {
//        if (data.jwt() != null) {
//            for (Player player : players) {
//                if (player.getUser().getEmail().equals())
//            }
//        }
        return false;
    }
}
