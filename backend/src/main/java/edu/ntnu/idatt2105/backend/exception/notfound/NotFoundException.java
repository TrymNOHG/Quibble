package edu.ntnu.idatt2105.backend.exception.notfound;

/**
 * This exception is thrown when something cannot be found.
 *
 * @author Trym Hamer Gudvangen, Brage Halvorsen Kvamme
 * @version 1.0 07.04.2024
 */
public class NotFoundException extends RuntimeException{

    /**
     * Default constructor for not found exception.
     */
    public NotFoundException() {
    }

    /**
     * Constructor with message for not found exception.
     * @param message   Additional information.
     */
    public NotFoundException(String message) {
        super("%s is not found. " + message);
    }

    /**
     * Constructor with message and throwable cause for not found exception.
     * @param message   Additional information.
     * @param cause     Cause of the exception
     */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with throwable cause for not found exception.
     * @param cause Cause of the exception.
     */
    public NotFoundException(Throwable cause) {
        super(cause);
    }

}
