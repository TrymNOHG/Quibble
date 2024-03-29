package edu.ntnu.idatt2105.backend.util;

import edu.ntnu.idatt2105.backend.model.users.User;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * This class represents a player in the quiz game.
 */
@Data
@AllArgsConstructor
public class Player {
    private User user;
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
