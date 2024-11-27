package it.polimi.ingsw.message.error;

/**
 * Error message indicating a login error.
 */
public class LoginError extends ErrorMessage {

    /**
     * Retrieves the error code associated with this login error message.
     *
     * @return The error code (in this case, 7).
     */
    @Override
    public int getErrorCode(){ return 7; }

    /**
     * Retrieves the error message describing the login error.
     *
     * @return The error message string.
     */
    @Override
    public String getErrorMessage(){ return "Login Error."; }
}
