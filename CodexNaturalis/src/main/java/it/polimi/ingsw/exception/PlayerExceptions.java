package it.polimi.ingsw.exception;

/**
 * Custom exception class for player-related errors in the game.
 */
public class PlayerExceptions extends Exception{
    private final ErrorCode errorCode;

    /**
     * Constructs a PlayerExceptions object with a specific error code and message.
     *
     * @param errorCode The error code associated with the exception.
     * @param message   The detail message.
     */
    public PlayerExceptions(ErrorCode errorCode, String message) {
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
     * Enumeration of error codes for player exceptions.
     */
    public enum ErrorCode {
        CARD_ALREADY_CHOSEN,
        CARD_ALREADY_PLAYED,
        DRAW_BEFORE_PLAY,
        EMPTY_DECK,
        INVALID_HAND_INDEX,
        INVALID_MARKET_CHOICE,
        INVALID_POSITION,
        CONFIGURATION_CHOICE_NOT_PERMITTED,
        NOT_YOUR_TURN,
        REQUIREMENTS_NOT_FULFILLED
    }
}
