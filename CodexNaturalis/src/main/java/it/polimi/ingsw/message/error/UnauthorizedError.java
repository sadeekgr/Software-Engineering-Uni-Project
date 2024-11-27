package it.polimi.ingsw.message.error;

/**
 * Error message indicating an unauthorized action due to lack of login.
 */
public class UnauthorizedError extends ErrorMessage {

    /**
     * Retrieves the error code associated with this unauthorized error message.
     *
     * @return The error code (in this case, 8).
     */
    @Override
    public int getErrorCode(){ return 8; }

    /**
     * Retrieves the error message describing the unauthorized action.
     *
     * @return The error message string.
     */
    @Override
    public String getErrorMessage(){ return "Login required for this action."; }
}
