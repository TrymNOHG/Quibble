package edu.ntnu.idatt2105.backend.controller.pub.category;


import edu.ntnu.idatt2105.backend.dto.category.MultipleCategoryDTO;
import edu.ntnu.idatt2105.backend.service.quiz.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller provides the public endpoint for categories.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 05.04.2024
 */
@RestController("publicCategory")
@EnableAutoConfiguration
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
@RequestMapping(value = "/api/v1/public/category")
public class CategoryController implements ICategoryController {

    private final QuizService quizService;

    @Override
    public ResponseEntity<MultipleCategoryDTO> getAllCategories() {
        return ResponseEntity.ok(quizService.getAllCategories());
    }
}
