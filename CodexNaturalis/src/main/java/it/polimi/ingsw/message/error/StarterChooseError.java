package it.polimi.ingsw.message.error;

/**
 * Error message indicating an error occurred during starter card selection.
 */
public class StarterChooseError extends ErrorMessage {

    /**
     * Retrieves the error code associated with this starter card choose error message.
     *
     * @return The error code (in this case, 3).
     */
    @Override
    public int getErrorCode(){ return 3; }

    /**
     * Retrieves the error message describing the error in starter card selection.
     *
     * @return The error message string.
     */
    @Override
    public String getErrorMessage(){ return "Error in starter card choose."; }
}
