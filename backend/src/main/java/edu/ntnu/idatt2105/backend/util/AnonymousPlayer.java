package edu.ntnu.idatt2105.backend.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * This class represents an anonymous player in the quiz game.
 *
 * @version 1.0 28.03.2024
 * @author brage
 */
@Data
@AllArgsConstructor
public class AnonymousPlayer {
    private String username;
    private int score;
    private List<Boolean> answers;

    /**
     * Method for checking if the player has answered a question.
     *
     * @param questionIndex The index of the question to check if the player has answered.
     * @return True if the player has answered the question, false if the player has not answered the question.
     */
    public boolean hasAnsweredQuestion(int questionIndex) {
        return answers.get(questionIndex);
    }
}
