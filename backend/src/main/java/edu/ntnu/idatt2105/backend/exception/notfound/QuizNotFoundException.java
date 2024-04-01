package edu.ntnu.idatt2105.backend.exception.notfound;

public class QuizNotFoundException extends NotFoundException{

    public QuizNotFoundException() {
    }

    public QuizNotFoundException(String message) {
        super("Quiz");
    }
}
