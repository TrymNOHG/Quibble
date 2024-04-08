package edu.ntnu.idatt2105.backend.exception;

import org.springframework.security.access.AccessDeniedException;

/**
 * This custom exception represents unauthorized actions by a user.
 *
 * @author Trym Hamer Gudvangen
 */
public class UnauthorizedException extends AccessDeniedException {

    /**
     * A default constructor for unauthorized actions.
     */
    public UnauthorizedException() {
        super("Unauthorized access to user.");
    }

    /**
     * This constructor creates an exception for unauthorized actions.
     * @param username  The username being used to perform the actions.
     */
    public UnauthorizedException(String username) {
        super(String.format("Access by user %s is not authorized", username));
    }

    /**
     * This constructor takes in the message and cause of the unauthorized exception.
     * @param msg       A message for what happened.
     * @param cause     The action that was not unauthorized.
     */
    public UnauthorizedException(String msg, Throwable cause) {
        super(msg, cause);
    }



}
