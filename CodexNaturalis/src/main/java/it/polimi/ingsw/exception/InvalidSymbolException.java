package it.polimi.ingsw.exception;

/**
 * Exception thrown when an invalid symbol is specified.
 */
public class InvalidSymbolException extends Exception{

    /**
     * Constructs a new InvalidSymbolException with the specified detail message.
     *
     * @param m The detail message.
     */
    public InvalidSymbolException(String m) {
        super(m);
    }
}
