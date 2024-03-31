package edu.ntnu.idatt2105.backend.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents an anonymous player in the quiz game.
 *
 * @version 1.0 28.03.2024
 * @author Brage Halvorsen Kvamme
 */
@Data
@RequiredArgsConstructor
public class AnonymousPlayer {
    @NonNull private String username;
    private int score = 0;
    private Map<Integer, Boolean> correctAnswers = new HashMap<>();
    private Map<Integer, Instant> answerTimes = new HashMap<>();
}
