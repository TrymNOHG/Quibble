package edu.ntnu.idatt2105.backend.dto.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * This record provides a data transfer object for encryption key pair.
 * @param rsaPrivateKey The private RSA key.
 * @param rsaPublicKey  The public RSA key.
 */
@ConfigurationProperties(prefix = "jwt")
public record RSAKeyPairDTO(RSAPrivateKey rsaPrivateKey, RSAPublicKey rsaPublicKey) {
}