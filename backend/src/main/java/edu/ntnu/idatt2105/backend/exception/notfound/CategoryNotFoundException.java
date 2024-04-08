package edu.ntnu.idatt2105.backend.exception.notfound;

/**
 * This class defines the not found exception for categories.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 05.04.2024
 */
public class CategoryNotFoundException extends NotFoundException{

    /**
     * Default constructor for category not found.
     */
    public CategoryNotFoundException() {
    }

    /**
     * Constructor for category not found with message.
     * @param message   Additional information.
     */
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
