package ch.epfl.sweng.qeeqbii.custom_exceptions;

/**
 * Created by adrien on 19.11.17.
 */

public class AlreadyOpenException extends Exception {
    // Constructor that accepts a message
    public AlreadyOpenException(String message) {
        super(message);
    }
}
