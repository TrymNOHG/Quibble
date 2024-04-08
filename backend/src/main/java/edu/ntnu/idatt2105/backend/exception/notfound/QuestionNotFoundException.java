package edu.ntnu.idatt2105.backend.exception.notfound;

/**
 * This exception handles questions that are not found.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 07.04.2024
 */
public class QuestionNotFoundException extends NotFoundException {

    /**
     * Default constructor for question not found exception.
     */
    public QuestionNotFoundException() {
    }

    /**
     * Constructor with message for question not found exception.
     * @param message   Additional information.
     */
    public QuestionNotFoundException(String message) {
        super("Question: " + message);
    }
}
