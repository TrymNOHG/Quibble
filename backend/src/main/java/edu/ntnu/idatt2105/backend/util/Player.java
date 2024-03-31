package edu.ntnu.idatt2105.backend.util;

import edu.ntnu.idatt2105.backend.model.users.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.*;

/**
 * This class represents a signed in player in the quiz game.
 *
 * @version 1.0 31.03.2024
 * @author Brage Halvorsen Kvamme
 */
@Data
@RequiredArgsConstructor
public class Player {
    @NonNull private User user;
    @NonNull private UUID webSocketId;
    private int score = 0;
    private Map<Integer, Boolean> correctAnswers = new HashMap<>();
    private Map<Integer, Instant> answerTimes = new HashMap<>();
}
