package edu.ntnu.idatt2105.backend.exception.exists;

/**
 * An exception that is thrown when an entity already exists in the system.
 *
 * @author Brage Halvorsen Kvamme, Trym Hamer Gudvangen
 * Source of code: IDATT2106 2023 Project,
 * <a href="https://github.com/TrymNOHG/smartfood/blob/development/backend/src/main/java/edu/ntnu/idatt2106_2023_06/backend/exception/exists/ExistsException.java">...</a>
 */
public class ExistsException extends RuntimeException{

    /**
     * This method constructs a new ExistsException with the given attribute and value.
     *
     * @param attribute The name of the attribute that already exists, given as a String
     * @param value     The value of the attribute that already exists, given as a String
     */
    public ExistsException(String attribute, String value) {
        super(String.format("%s with id %s already exists", attribute, value));
    }

    /**
     * This method constructs a new ExistsException with the given attribute and value.
     *
     * @param attribute The name of the attribute that already exists, given as a String
     * @param value     The value of the attribute that already exists, given as a Long object
     */
    public ExistsException(String attribute, Long value) {
        super(String.format("%s with id %d already exists", attribute, value));
    }

    /**
     * This method constructs a new ExistsException with the given type, attribute, and value.
     *
     * @param type      The type of the entity that already exists, given as a String
     * @param attribute The name of the attribute that already exists, given as a String
     * @param value     The value of the attribute that already exists, given as a String
     */
    public ExistsException(String type, String attribute, String value) {
        super(String.format("%s with %s: %s, already exists", type, attribute, value));
    }

}