package it.polimi.ingsw.exception;

/**
 * Exception thrown when an error occurs during the JSON data loading process.
 */
public class JsonLoadException extends Exception{

    /**
     * Constructs a new JsonLoadException with the specified detail message.
     *
     * @param m The detail message.
     */
    public JsonLoadException(String m){ super(m); }

    /**
     * Constructs a new JsonLoadException with the specified detail message and cause.
     *
     * @param m The detail message.
     * @param e   The cause of the exception.
     */
    public JsonLoadException(String m, Exception e){ super(m + e.getMessage()); }

    /**
     * Constructs a new JsonLoadException with the cause of the exception.
     *
     * @param e The cause of the exception.
     */
    public JsonLoadException(Exception e){ super(e.getMessage());}
}
