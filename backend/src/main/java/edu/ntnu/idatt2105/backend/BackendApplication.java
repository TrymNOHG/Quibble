package edu.ntnu.idatt2105.backend;

import edu.ntnu.idatt2105.backend.dto.security.RSAKeyPairDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Entry point for the backend application.
 *
 * @version 1.0 31.03.2024
 * @author Brage Halvorsen Kvamme
 * @author Trym Hamer Gudvangen
 */
@SpringBootApplication
@EnableConfigurationProperties(RSAKeyPairDTO.class)
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
