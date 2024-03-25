package edu.ntnu.idatt2105.backend.repo.users;

import edu.ntnu.idatt2105.backend.model.users.RefreshToken;
import edu.ntnu.idatt2105.backend.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This repository creates the CRUD operation for the Token entity.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 23.03.2024
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>, JpaSpecificationExecutor<RefreshToken> {

    /**
     * Finds a token by the token string.
     *
     * @param refreshToken The token string.
     * @return The token entity.
     */
    Optional<RefreshToken> findByToken(String refreshToken);

    /**
     * Finds all tokens by the user's username.
     *
     * @param username The user's username.
     * @return A list of tokens.
     */
    List<RefreshToken> findAllByUser_Username(String username);
}
