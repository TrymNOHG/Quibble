package edu.ntnu.idatt2105.backend.integration;

import edu.ntnu.idatt2105.backend.controller.priv.images.ImageController;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.service.images.ImageService;
import edu.ntnu.idatt2105.backend.service.quiz.QuizService;
import edu.ntnu.idatt2105.backend.service.security.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageController.class)
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private QuizService quizService;

    @Test
    void saveFile_WhenCalledWithImageFile_ShouldReturnOk() throws Exception {
        // Mocking service layer behavior
        doNothing().when(imageService).saveQuizImage(any(MultipartFile.class), anyLong());

        // Mocking authentication and authorization
        User mockUser = new User(); // Simplified user object
        Quiz mockQuiz = new Quiz();
        mockQuiz.setQuizId(1L);
        mockQuiz.setAdmin(mockUser);
        SecurityContextHolder.getContext().setAuthentication(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        mockUser, null, Collections.emptyList()
                )
        );

        when(quizService.getQuizById(anyLong())).thenReturn(mockQuiz);
        when(authenticationService.getLoggedInUser()).thenReturn(mockUser);

        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "Test image content".getBytes()
        );

        mockMvc.perform(multipart("/api/v1/private/image/quiz/save")
                        .file(file)
                        .param("quizId", "1")
                        .with(user("user").password("password").roles("USER"))
                        .with(csrf())) // Include CSRF token
                .andExpect(status().isOk())
                .andExpect(content().string("Image saved"));

        // Verify interactions
        verify(imageService, times(1)).saveQuizImage(any(MultipartFile.class), eq(1L));
    }
}
