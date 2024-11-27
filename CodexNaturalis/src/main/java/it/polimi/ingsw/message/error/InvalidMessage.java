package it.polimi.ingsw.message.error;

/**
 * Error message indicating that a received message is invalid.
 */
public class InvalidMessage extends ErrorMessage {

    /**
     * Returns the specific error code for the invalid message error.
     *
     * @return the error code, which is 1 for this specific error.
     */
    @Override
    public int getErrorCode(){ return 1; }

    /**
     * Returns the error message indicating that the message is invalid.
     *
     * @return a string describing the error, which is "Invalid Message."
     */
    @Override
    public String getErrorMessage(){ return "Invalid Message."; }
}
