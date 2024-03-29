package edu.ntnu.idatt2105.backend.service.quiz;

import edu.ntnu.idatt2105.backend.model.quiz.question.Question;
import edu.ntnu.idatt2105.backend.model.quiz.question.QuestionType;
import edu.ntnu.idatt2105.backend.repo.quiz.question.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public boolean isCorrectAnswer(Question question, String answer) {
        return switch (question.getQuestionType()) {
            case MULTIPLE_CHOICE -> question.getChoices().stream().anyMatch(
                    choice -> choice.isCorrect() && choice.getAlternative().equalsIgnoreCase(answer)
            );
            case TRUE_FALSE, SHORT_ANSWER, LONG_ANSWER, FILL_IN_BLANK, FLASHCARD -> question.getAnswer().equalsIgnoreCase(answer);
            default -> question.getAnswer().equalsIgnoreCase(answer);
        };
    }
}
