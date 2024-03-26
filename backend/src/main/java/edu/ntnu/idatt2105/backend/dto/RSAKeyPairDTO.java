package edu.ntnu.idatt2105.backend.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "jwt")
public record RSAKeyPairDTO(RSAPrivateKey rsaPrivateKey, RSAPublicKey rsaPublicKey) {
}