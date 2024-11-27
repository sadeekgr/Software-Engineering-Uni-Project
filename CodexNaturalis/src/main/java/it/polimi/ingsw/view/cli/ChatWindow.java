package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.message.ChatMessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The ChatWindow class represents a chat interface with functionalities to display and manage chat messages.
 */
public class ChatWindow {
    private final String[] lines;
    private final int height;
    private final int width;
    private int head; //index of first element
    private int tail; //index where to insert

    private Set<String> id;
    private List<ChatMessage> messages;

    /**
     * Constructs a ChatWindow with specified height and width.
     *
     * @param height the number of lines the chat window can display
     * @param width the maximum number of characters per line
     */
    public ChatWindow(int height, int width) {
        lines = new String[height];
        this.height = height;
        this.width = width;
        head = -1;
        tail = 0;

        id = null;
    }

    /**
     * Clears the chat window.
     */
    public void clear(){
        head = -1;
        tail = 0;
    }

    /**
     * Displays the chat with specified private chat IDs and chat messages.
     *
     * @param privateChat the set of IDs for private chat
     * @param messages the list of chat messages
     */
    public void showChat(Set<String> privateChat, List<ChatMessage> messages){
        if (privateChat == null || privateChat.isEmpty()){
            id = null;
        } else {
            id = new HashSet<>();
            id.addAll(privateChat);
        }
        this.messages = messages;
        update();
    }

    /**
     * Updates the chat window with the current messages.
     */
    public void update(){
        clear();
        List<ChatMessage> messages = this.messages.subList(Math.max(0, this.messages.size() - lines.length), this.messages.size());
        for(ChatMessage msg : messages){
            addLine(formatChatMessage(msg));
        }
    }

    /**
     * Gets the set of private chat IDs.
     *
     * @return the set of private chat IDs
     */
    public Set<String> getId(){ return id; }

    /**
     * Formats a chat message for display.
     *
     * @param m the chat message to format
     * @return the formatted chat message string
     */
    private String formatChatMessage(ChatMessage m){
        return m.getSender() + ": " + m.getMessage() + " ".repeat(5) + m.getDate();
    }

    /**
     * Handles the addition of a new message to the chat.
     *
     * @param chat the set of IDs for the new chat message
     */
    public void newMessage(Set<String> chat){
        if(id == null || chat == null){
            if(id == chat){
                update();
            }
        } else if(id.containsAll(chat) && chat.containsAll(id)){
            update();
        }
    }

    /**
     * Adds a line to the chat window, breaking it into multiple lines if necessary.
     *
     * @param message the message to add
     */
    private void addLine(String message){
        String[] msgLines = message.split("\n");

        List<String> linesToAdd = new ArrayList<>();
        for (String msg : msgLines){
            for (int i = 0; i < msg.length(); i += width){
                String substr = msg.substring(i, Math.min(i + width, msg.length()));
                linesToAdd.add(substr);
            }
        }

        for(String line : linesToAdd){
            add(line);
        }
    }

    /**
     * Adds a single line to the chat window.
     *
     * @param line the line to add
     */
    private void add(String line){
        if(head == -1){
            head = 0;
            tail = 1;
            lines[0] = line;

        } else {
            if (tail == head) {
                head += 1;
                if (head == height) {
                    head = 0;
                }
            }

            lines[tail] = line;
            tail += 1;
            if (tail == height) {
                tail = 0;
            }
        }
    }

    /**
     * Gets the number of lines currently in the chat window.
     *
     * @return the number of lines
     */
    public int getLength(){
        return head == -1 ? 0 : (tail > head ? tail - head : height - head + tail);
    }

    /**
     * Gets a specific line from the chat window by index.
     *
     * @param index the index of the line to retrieve
     * @return the line at the specified index, or an empty string if the index is out of bounds
     */
    public String getLine(int index){ return getLength() > index ? lines[(head + index) % height] : ""; }
}
