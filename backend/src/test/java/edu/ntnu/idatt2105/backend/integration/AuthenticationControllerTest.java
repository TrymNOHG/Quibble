package edu.ntnu.idatt2105.backend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2105.backend.dto.security.AuthenticationResponseDTO;
import edu.ntnu.idatt2105.backend.dto.users.UserRegisterDTO;
import edu.ntnu.idatt2105.backend.service.security.AuthenticationService;
import edu.ntnu.idatt2105.backend.service.security.JWTTokenGenerationService;
import edu.ntnu.idatt2105.backend.service.security.JWTTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private JWTTokenGenerationService jwtTokenGenerationService;
    @Autowired
    private JWTTokenService jwtTokenService;
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Test
    void Signup_test() throws Exception {
//        mockMvc.perform(post("/api/v1/public/auth/signup")
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .param("username", "username")
//                .param("email", "email@email.email")
//                .param("password", "password"))
//                .andExpect(status().isOk());
    }

    @BeforeEach
    void setUp() {
        try {
            authenticationService.registerUser(UserRegisterDTO.builder()
                    .username("test")
                    .email("test@test.test")
                    .password("password")
                    .build(), httpServletResponse, null);
        } catch (Exception ignored) {

        }
    }

    @Test
    void Login_test() throws Exception {
        // Username and password for basic auth
        String username = "test@test.test";
        String password = "password";

        // Create the basic auth header
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);

        mockMvc.perform(post("/api/v1/public/auth/login")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getNewAccessTokenFromRefreshTokenTest() throws Exception {
        String refreshToken = jwtTokenGenerationService.generateRefreshToken("test@test.test");

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/public/auth/refresh-token/get")
                        .cookie(new Cookie("refresh_token", refreshToken)))
                .andExpect(status().isOk())
                .andReturn();

        String body = mvcResult.getResponse().getContentAsString();
        String token = objectMapper.readValue(body, AuthenticationResponseDTO.class).token();

        assertTrue(jwtTokenService.isValidToken(jwtTokenService.getJwt(token)));
    }

}
