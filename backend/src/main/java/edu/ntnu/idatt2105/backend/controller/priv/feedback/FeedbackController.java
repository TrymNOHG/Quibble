package edu.ntnu.idatt2105.backend.controller.priv.feedback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/api/v1/private/feedback")
public class FeedbackController {
}
