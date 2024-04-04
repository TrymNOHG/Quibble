package edu.ntnu.idatt2105.backend.exception.notfound;

public class QuestionNotFoundException extends NotFoundException {

    public QuestionNotFoundException() {
    }

    public QuestionNotFoundException(String message) {
        super("Question");
    }
}
