package it.polimi.ingsw.view.cli;

import java.util.ArrayList;
import java.util.List;

/**
 * The QueueWindow class represents a queue-like text window for displaying messages,
 * with functionalities to add, clear, and retrieve messages.
 */
public class QueueWindow {
    private final String[] lines;
    private final int height;
    private final int width;
    private int head; //index of first element
    private int tail; //index where to insert

    /**
     * Constructs a QueueWindow with the specified height and width.
     *
     * @param height the number of lines the queue window can display
     * @param width the maximum number of characters per line
     */
    public QueueWindow(int height, int width) {
        lines = new String[height];
        this.height = height;
        this.width = width;
        head = -1;
        tail = 0;
    }

    /**
     * Clears the queue window.
     */
    public void clear(){
        head = -1;
        tail = 0;
    }

    /**
     * Adds a message to the queue window, splitting it into multiple lines if necessary.
     *
     * @param message the message to add
     */
    public void addLine(String message){
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
     * Adds an empty line to the queue window.
     */
    public void addNewLine(){ add(""); }

    /**
     * Adds a single line to the queue window.
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
     * Gets the number of lines currently in the queue window.
     *
     * @return the number of lines
     */
    public int getLength(){
        return head == -1 ? 0 : (tail > head ? tail - head : height - head + tail);
    }

    /**
     * Gets a specific line from the queue window by index.
     *
     * @param index the index of the line to retrieve
     * @return the line at the specified index
     */
    public String getLine(int index){ return lines[(head + index) % height]; }
}
