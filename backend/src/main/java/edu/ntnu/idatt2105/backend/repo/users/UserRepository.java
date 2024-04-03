package edu.ntnu.idatt2105.backend.repo.users;

import edu.ntnu.idatt2105.backend.model.users.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * This repository defines the CRUD operations for the User model.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 23.03.2024
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * Finds a user by their email.
     *
     * @param email The email of the user to find.
     * @return The user with the given email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by their username.
     *
     * @param username The username of the user to find.
     * @return The user with the given username.
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by their id.
     *
     * @param id The id of the user to find.
     * @return The user with the given id.
     */
    @NotNull Optional<User> findById(@NotNull Long id);

    /**
     * This method uses SpringRepository's fuzzy search algorithm to look for usernames containing the username
     * given as parameter.
     * @param username  The username to look for.
     * @return          A set of users objects matching the condition.
     */
    Optional<Set<User>> findByUsernameContainingIgnoreCase(String username);

    /**
     * This method uses SpringRepository's fuzzy search algorithm to look for usernames containing the username
     * given as parameter. It then retrieves a page of the results.
     * @param username  The username to look for.
     * @param pageable  Page specifications, such as size and page index.
     * @return          A set of users objects matching the condition.
     */
    Optional<Page<User>> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
}