package edu.ntnu.idatt2105.backend.service.websocket;

import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private final Random random = new Random();
    private final UserRepository userRepository;


    /**
     * Generates a random code for a room that is not already taken. If the code is taken, it generates a new one.
     *
     * @return A randomly generated code that is not already in the rooms map.
     */
    private String generateRandomCode() {
        String code;
        do {
            code = random.ints(48, 123)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(4) // Change this to increase the length of the code
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        } while (rooms.containsKey(code));
        return code;
    }

    public String createRoom(String hostEmail, Quiz quiz) {
        User host = userRepository.findByEmail(hostEmail).orElseThrow();
        String code = generateRandomCode();
        Room room = new Room(code, host, quiz);
        rooms.put(code, room);
        return code;
    }

    public Room getRoom(String code) {
        return rooms.get(code);
    }

    public void removeRoom(String code) {
        rooms.remove(code);
    }
}
