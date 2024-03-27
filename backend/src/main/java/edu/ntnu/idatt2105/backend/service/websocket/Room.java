package edu.ntnu.idatt2105.backend.service.websocket;

import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String code;
    private List<User> players = new ArrayList<>();
    private List<String> anonymizedPlayers = new ArrayList<>();
    private User host;
    private int questionIndex = -1;
    private Quiz quiz;

    Room(String code, User host, Quiz quiz) {
        this.host = host;
        this.code = code;
        this.quiz = quiz;
    }


}
