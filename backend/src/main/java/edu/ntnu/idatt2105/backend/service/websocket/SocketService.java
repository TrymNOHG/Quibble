package edu.ntnu.idatt2105.backend.service.websocket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import edu.ntnu.idatt2105.backend.dto.websocket.CreateGameDTO;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.service.JWTTokenService;
import edu.ntnu.idatt2105.backend.service.QuizService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class SocketService {
    Logger logger = Logger.getLogger(SocketService.class.getName());
    private final SocketIOServer server;
    private final RoomService roomService;
    private final JWTTokenService jwtTokenService;
    private final QuizService quizService;

    @PostConstruct
    public void initSocketIoService() {

        server.addConnectListener(client -> {
            logger.info("a user connected: " + client.getSessionId());
        });

        server.addDisconnectListener(client -> {
            logger.info("a user disconnected: " + client.getSessionId());
        });

        server.addEventListener("createGame", CreateGameDTO.class, (client, data, ackRequest) -> {
            logger.info("createGame event received: " + data);
            String token = data.jwt();
            long userId = data.userId();
            UserDetails userDetails = jwtTokenService.getUserDetails(userId);
            Jwt jwt = jwtTokenService.getJwt(token);
            if (!jwtTokenService.isTokenValid(jwt, userDetails))
                throw new RuntimeException("Invalid token"); // Gjør noe bedre her

            Quiz quiz = quizService.getQuizById(data.quizId());
            String code = roomService.createRoom(userDetails.getUsername(), quiz);

            client.sendEvent("gameCreated", code);
        });

        // Add other event listeners here
        server.start();

    }

    @PreDestroy
    public void stopSocketIoService() {
        server.stop();
    }

    // Utility method to generate random numbers
    private int generateRandomFourDigitNumber() {
        return new Random().nextInt(9000) + 1000;
    }
}
