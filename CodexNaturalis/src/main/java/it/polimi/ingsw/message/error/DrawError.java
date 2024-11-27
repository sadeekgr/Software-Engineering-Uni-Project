package it.polimi.ingsw.message.error;

/**
 * Error message indicating an error in drawing a card.
 */
public class DrawError extends ErrorMessage {

    /**
     * Returns the specific error code for the draw error.
     *
     * @return the error code, which is 6 for this specific error.
     */
    @Override
    public int getErrorCode(){ return 6; }

    /**
     * Returns the error message indicating a failure in drawing a card.
     *
     * @return a string describing the error, which is "Error in drawing card."
     */
    @Override
    public String getErrorMessage(){ return "Error in drawing card"; }
}
