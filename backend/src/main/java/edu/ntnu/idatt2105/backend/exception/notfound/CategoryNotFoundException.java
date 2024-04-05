package edu.ntnu.idatt2105.backend.exception.notfound;

/**
 * This class defines the not found exception for categories.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 05.04.2024
 */
public class CategoryNotFoundException extends NotFoundException{

    public CategoryNotFoundException() {
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
