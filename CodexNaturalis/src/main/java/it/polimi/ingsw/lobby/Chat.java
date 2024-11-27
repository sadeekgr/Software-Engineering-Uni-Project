package it.polimi.ingsw.lobby;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exception.ChatException;
import it.polimi.ingsw.message.ChatMessage;

import java.util.*;

/**
 * Represents the chat functionality for communication between players.
 */
public class Chat {

    private final Map<String, Controller> users;                            // Map of usernames to Controller objects
    private final List<ChatMessage> publicChat;                             // List of public chat messages
    private final Map<Set<String>, List<ChatMessage>> privateChat;          // Map of sets of usernames to private chat messages


    /**
     * Constructs a Chat object.
     */
    public Chat() {
        this.publicChat = new ArrayList<>();
        this.users = new HashMap<>();
        this.privateChat = new HashMap<>();
    }

    /**
     * Adds a user to the chat.
     *
     * @param controller The Controller object representing the user.
     */
    public void addUser(Controller controller){
        users.put(controller.getUsername(), controller);
        controller.setChat(this);
    }

    /**
     * Removes a user from the chat.
     *
     * @param controller The Controller object representing the user to be removed.
     */
    public void removeUser(Controller controller){
        users.remove(controller.getUsername());
        controller.removeChat();
    }

    /**
     * Sends a chat message to the specified recipients. If the message type is public, it is broadcasted to all users.
     * If the message type is private, it is sent only to the specified recipients.
     *
     * @param message The ChatMessage object representing the message to be sent.
     * @throws ChatException If there is an error sending the message, such as invalid recipients or missing users.
     */
    public void sendMessage(ChatMessage message) throws ChatException {
        if (message.getChatType() == ChatMessage.ChatType.PUBLIC){
            sendAll(message);
        } else {
            Set<String> toSend = new HashSet<>();
            toSend.add(message.getSender());
            toSend.addAll(message.getRecipients());

            if(!users.keySet().containsAll(toSend)){
                throw new ChatException();
            }

            if (toSend.size() == users.size()){ //public chat
                for (String user : toSend){
                    if (!users.containsKey(user)){
                        throw new ChatException();
                    }
                }
                sendAll(message);
            }
            else if (toSend.size() > 1){
                List<ChatMessage> chat;
                if (!privateChat.containsKey(toSend)) {
                    chat = createPrivateChat(toSend);
                } else {
                    chat = privateChat.get(toSend);
                }
                chat.add(message);
                for (String user : toSend){
                    users.get(user).sendChatMessage(message);
                }
            }
            else{
                throw new ChatException();
            }
        }
    }

    /**
     * Sends a chat message to all users in the lobby.
     *
     * @param m The ChatMessage object representing the message to be sent.
     */
    private void sendAll(ChatMessage m){
        publicChat.add(m);
        for (Controller user : users.values()){
            user.sendChatMessage(m);
        }
    }

    /**
     * Creates a private chat between the specified users.
     *
     * @param chatUsers The set of usernames representing the users participating in the private chat.
     * @return The list of ChatMessage objects representing the private chat.
     * @throws ChatException If there is an error creating the private chat, such as invalid or missing users.
     */
    private List<ChatMessage> createPrivateChat(Set<String> chatUsers) throws ChatException {
        for (String user : chatUsers){
           if (!users.containsKey(user)){
               throw new ChatException();
           }
        }
        List<ChatMessage> chat = new ArrayList<>();
        privateChat.put(chatUsers, chat);
        return chat;
    }
}
