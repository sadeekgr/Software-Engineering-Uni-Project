package it.polimi.ingsw.message;

import java.util.Date;
import java.util.Set;

/**
 * Represents a chat message in the game.
 * A chat message can be either public or private.
 */
public class ChatMessage implements Message {
    private String sender;
    private final String message;
    private final Date date;
    private final Set<String> recipients;

    /**
     * Constructs a public chat message.
     *
     * @param message The content of the message.
     * @param date    The date the message was sent.
     */
    public ChatMessage(String message, Date date) {
        this.message = message;
        this.date = date;
        recipients = null;
    }

    /**
     * Sets the sender of the chat message.
     *
     * @param sender The username of the sender.
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Constructs a private chat message.
     *
     * @param message    The content of the message.
     * @param date       The date the message was sent.
     * @param recipients The set of recipients of the message.
     */
    public ChatMessage(String message, Date date, Set<String> recipients) {
        this.message = message;
        this.date = date;
        this.recipients = recipients;
    }

    /**
     * Gets the sender of the chat message.
     *
     * @return The username of the sender.
     */
    public String getSender() {return sender;}

    /**
     * Gets the content of the chat message.
     *
     * @return The content of the message.
     */
    public String getMessage() { return message; }

    /**
     * Gets the date the chat message was sent.
     *
     * @return The date of the message.
     */
    public Date getDate() { return date; }

    /**
     * Gets the set of recipients of the chat message.
     *
     * @return The set of recipients.
     */
    public Set<String> getRecipients() { return recipients; }

    /**
     * Retrieves the type of this message, which is {@link MessageType#CHAT}.
     *
     * @return The message type, which is {@link MessageType#CHAT}.
     */
    @Override
    public MessageType getType() {
        return MessageType.CHAT;
    }

    /**
     * Represents the type of chat message, either public or private.
     */
    public enum ChatType {PUBLIC, PRIVATE}

    /**
     * Gets the type of chat message, either public or private.
     *
     * @return The ChatType of the message.
     */
    public ChatType getChatType() {
        if (recipients == null) return ChatType.PUBLIC;
        return ChatType.PRIVATE;
    }
}

