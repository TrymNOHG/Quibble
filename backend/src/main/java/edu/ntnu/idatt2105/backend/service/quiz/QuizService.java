package edu.ntnu.idatt2105.backend.service.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizUpdateDTO;
import edu.ntnu.idatt2105.backend.exception.notfound.QuizNotFoundException;
import edu.ntnu.idatt2105.backend.mapper.quiz.QuizMapper;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final Logger LOGGER = LoggerFactory.getLogger(QuizService.class);

    private final QuizRepository quizRepository;
    private final QuizMapper quizMapper;

    public QuizLoadDTO updateQuiz(QuizUpdateDTO quizUpdateDTO) {
        //TODO: check that user trying to change is owner or collaborator
        LOGGER.info("Attempting to retrieve quiz with id: " + quizUpdateDTO.quizId());
        Quiz quiz = quizRepository.findById(quizUpdateDTO.quizId())
                .orElseThrow(() -> new QuizNotFoundException("Id: " + quizUpdateDTO.quizId()));

        if (quizUpdateDTO.newName() != null) {
            LOGGER.info("Updating quiz name");
            quiz.setQuizName(quizUpdateDTO.newName());
        }

        if (quizUpdateDTO.newDescription() != null) {
            LOGGER.info("Updating quiz description");
            quiz.setQuizDescription(quizUpdateDTO.newDescription());
        }

        Quiz savedQuiz = quizRepository.save(quiz);

        return quizMapper.quizToQuizLoadDTO(savedQuiz);
    }

}
