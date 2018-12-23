package ch.epfl.sweng.qeeqbii.custom_exceptions;

/**
 * Created by adrien on 02.12.17.
 */

public class IllegalNutrientKeyException extends Exception {
    // Constructor that accepts a message
    public IllegalNutrientKeyException(String message) {
        super(message);
    }
}
