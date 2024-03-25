package edu.ntnu.idatt2105.backend.controller.priv.quiz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller provides the private endpoint for quiz.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
@RestController
@EnableAutoConfiguration
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/api/v1/private/quiz")
public class QuizController {
}