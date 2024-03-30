package edu.ntnu.idatt2105.backend.integration;

import edu.ntnu.idatt2105.backend.config.SocketIoConfig;
import edu.ntnu.idatt2105.backend.controller.pub.authentication.AuthenticationController;
import edu.ntnu.idatt2105.backend.dto.users.UserRegisterDTO;
import edu.ntnu.idatt2105.backend.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.awt.*;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
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

    @Test
    void Signup_test() throws Exception {
        mockMvc.perform(post("/api/v1/public/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "username",
                            "email": "email@mail.mail",
                            "password": "password"
                        }"""))
                .andExpect(status().isOk());
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

}
