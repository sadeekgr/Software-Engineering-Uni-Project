package it.polimi.ingsw.exception;

/**
 * Custom exception class for network-related errors in the game.
 */
public class NetworkExceptions extends Exception {
    private final ErrorCode errorCode;

    /**
     * Constructs a NetworkExceptions object with a specific error code and message.
     *
     * @param errorCode The error code associated with the exception.
     * @param message   The detail message.
     */
    public NetworkExceptions(ErrorCode errorCode, String message) {
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
     * Enumeration of error codes for network exceptions.
     */
    public enum ErrorCode {
        CLIENT_ALREADY_LOGGED,
        GAME_NOT_FOUND,
        INVALID_GAME_PARAMETER,
        PLAYER_ALREADY_IN_GAME,
        PLAYER_NOT_IN_GAME,
        USERNAME_NOT_AVAILABLE
    }
}
