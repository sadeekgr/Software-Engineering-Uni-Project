package it.polimi.ingsw.exception;

/**
 * Custom exception class for match-related errors in the game.
 */
public class MatchExceptions extends Exception {
    private final ErrorCode errorCode;

    /**
     * Constructs a MatchExceptions object with a specific error code and message.
     *
     * @param errorCode The error code associated with the exception.
     * @param message   The detail message.
     */
    public MatchExceptions(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Gets the error code associated with this exception.
     *
     * @return The error code.
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * Enumeration of error codes for match exceptions.
     */
    public enum ErrorCode {
        MATCH_FULL,
        MATCH_ALREADY_STARTED,
        INVALID_NUMBER_OF_PLAYERS,
        PLAYER_NOT_FOUND
    }
}
