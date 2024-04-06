package edu.ntnu.idatt2105.backend.unit.mapper;

import edu.ntnu.idatt2105.backend.dto.quiz.question.MultipleChoiceCreateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.MultipleChoiceDTO;
import edu.ntnu.idatt2105.backend.mapper.quiz.MultipleChoiceMapper;
import edu.ntnu.idatt2105.backend.model.quiz.question.MultipleChoice;
import edu.ntnu.idatt2105.backend.model.quiz.question.Question;
import edu.ntnu.idatt2105.backend.model.quiz.question.QuestionType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class QuizMapperTest {

    @Nested
    class MultipleChoice {

        @Test
        void Multiple_choice_to_dto_to_multiple_choice_test() {
            edu.ntnu.idatt2105.backend.model.quiz.question.MultipleChoice multipleChoice = MultipleChoiceMapper.INSTANCE.multipleChoiceDTOToMultipleChoice(
                    MultipleChoiceDTO.builder()
                            .multipleChoiceId(1L)
                            .alternative("alternative")
                            .isCorrect(false)
                            .questionId(1L)
                            .build()
            );
            assertNotNull(multipleChoice, "MultipleChoice should not be null");
            assertEquals(1L, multipleChoice.getMultipleChoiceId());
            assertEquals("alternative", multipleChoice.getAlternative());
            assertFalse(multipleChoice.isCorrect());
        }

        @Test
        void Multiple_choice_create_dto_to_multiple_choice_test() {
            edu.ntnu.idatt2105.backend.model.quiz.question.MultipleChoice multipleChoice = MultipleChoiceMapper.INSTANCE.multipleChoiceCreateDTOToMultipleChoice(
                    MultipleChoiceCreateDTO.builder()
                            .alternative("alternative")
                            .isCorrect(false)
                            .build()
            );
            assertNotNull(multipleChoice, "MultipleChoice should not be null");
            assertEquals("alternative", multipleChoice.getAlternative());
            assertFalse(multipleChoice.isCorrect());
        }

        @Test
        void Multiple_choice_to_dto_test() {
            edu.ntnu.idatt2105.backend.model.quiz.question.MultipleChoice multipleChoice = edu.ntnu.idatt2105.backend.model.quiz.question.MultipleChoice.builder()
                    .multipleChoiceId(1L)
                    .alternative("alternative")
                    .isCorrect(false)
                    .question(Question.builder()
                            .questionId(1L)
                            .question("test")
                            .questionType(QuestionType.MULTIPLE_CHOICE)
                            .build())
                    .build();
            MultipleChoiceDTO multipleChoiceDTO = MultipleChoiceMapper.INSTANCE.multipleChoiceToMultipleChoiceDTO(multipleChoice);
            assertNotNull(multipleChoiceDTO, "MultipleChoiceDTO should not be null");
            assertEquals(1L, multipleChoiceDTO.multipleChoiceId());
            assertEquals("alternative", multipleChoiceDTO.alternative());
            assertFalse(multipleChoiceDTO.isCorrect());
        }
    }



}
