package it.polimi.ingsw.exception;

/**
 * Exception thrown when expecting a covered corner when none was present.
 */
public class NoCoveredCornerException extends RuntimeException{

    /**
     * Constructs a new NoCoveredCornerException with the specified detail message.
     *
     * @param m The detail message.
     */
    public NoCoveredCornerException(String m){ super(m); }
}