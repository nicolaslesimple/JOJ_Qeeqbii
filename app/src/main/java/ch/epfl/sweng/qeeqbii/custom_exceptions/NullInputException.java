package ch.epfl.sweng.qeeqbii.custom_exceptions;

/**
 * Created by adrien on 11.11.17.
 */

public class NullInputException extends Exception {
    // Constructor that accepts a message
    public NullInputException(String message) {
        super(message);
    }
}