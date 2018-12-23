package ch.epfl.sweng.qeeqbii.custom_exceptions;

/**
 * Created by adrien on 03.12.17.
 */

public class BadlyFormatedFile extends Exception {
    // Constructor that accepts a message
    public BadlyFormatedFile(String message) {
        super(message);
    }
}
