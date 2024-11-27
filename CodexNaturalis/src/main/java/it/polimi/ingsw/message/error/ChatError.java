package it.polimi.ingsw.message.error;

/**
 * Error message indicating an error in chat functionality.
 */
public class ChatError extends ErrorMessage{

    /**
     * Returns the specific error code for the chat error.
     *
     * @return the error code, which is 27 for this specific error.
     */
    @Override
    public int getErrorCode() {
        return 27;
    }

    /**
     * Returns the error message indicating a failure in chat operations.
     *
     * @return a string describing the error, which is "Chat Error."
     */
    @Override
    public String getErrorMessage() {
        return "Chat Error";
    }
}
