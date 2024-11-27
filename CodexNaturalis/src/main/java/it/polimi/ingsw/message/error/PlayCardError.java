package it.polimi.ingsw.message.error;

/**
 * Error message indicating an error occurred while playing a card.
 */
public class PlayCardError extends ErrorMessage {

    /**
     * Retrieves the error code associated with this play card error message.
     *
     * @return The error code (in this case, 5).
     */
    @Override
    public int getErrorCode(){ return 5; }

    /**
     * Retrieves the error message describing the error in playing a card.
     *
     * @return The error message string.
     */
    @Override
    public String getErrorMessage(){ return "Error in play card."; }
}
