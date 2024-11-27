package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.lobby.LobbyInfo;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.message.action.*;
import it.polimi.ingsw.message.error.ErrorMessage;
import it.polimi.ingsw.message.lobby.*;
import it.polimi.ingsw.message.notify.*;
import it.polimi.ingsw.model.card.PlayableCard;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.field.PlayerField;
import it.polimi.ingsw.model.field.Position;
import it.polimi.ingsw.model.objective.Objective;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.View;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Represents the CLI (Command Line Interface)
 * It prints the game onto the terminal and enables the player to play the game through various terminal commands
 */
public class Cli extends View {
    private static final Logger logger = Logger.getLogger(Cli.class.getName());
    private boolean waitRes;
    private boolean success;
    private final Scanner scanner;

    // Draw variables, contain the representation of their corresponding object
    private String[] gold;
    private String[] resource;
    private final String[][] market;
    private final String[][] hand;
    private final String[][] commonObjs;
    private String[] personalObj;

    // Game variables
    private boolean inLobby;
    private boolean inGame;
    private boolean matchStarted;
    private StarterCard starterCardToChoose;
    private Objective[] objectivesToChoose;
    private boolean gameEnded;

    private final int FIELD_HEIGHT = 24;
    private final int FIELD_WIDTH = 120;

    private final FieldWindow fieldWin;
    private final ChatWindow chatWin;
    private final QueueWindow cmdWin;

    private int lastLine;
    private boolean reconnecting;

    Cli.Focus focus;

    enum Focus { PLAYER1, PLAYER2, PLAYER3, PLAYER4, PUBLIC, PRIVATE }

    /**
     * Constructs the CLI and sets up its parameters
     */
    public Cli(){
        super();
        scanner = new Scanner(System.in);

        // initialize draw vars
        gold = Draw.emptyCard();
        resource = Draw.emptyCard();
        market = new String[][]{Draw.emptyCard(), Draw.emptyCard(), Draw.emptyCard(), Draw.emptyCard()};
        hand = new String[][]{Draw.emptyCard(), Draw.emptyCard(), Draw.emptyCard()};
        commonObjs = new String[][]{Draw.emptyObjective(), Draw.emptyObjective()};
        personalObj = Draw.emptyObjective();

        success = false;
        waitRes = false;

        // initialize game vars
        fieldWin = new FieldWindow(FIELD_HEIGHT, FIELD_WIDTH);
        chatWin = new ChatWindow(FIELD_HEIGHT, FIELD_WIDTH);
        cmdWin = new QueueWindow(20, FIELD_WIDTH);

        inLobby = false;
        inGame = false;
        matchStarted= false;
        gameEnded = false;

        starterCardToChoose = null;
        objectivesToChoose = null;

        lastLine = 0;
        reconnecting = false;
    }

    /**
     * Handles the setup for the application
     * This includes connection, setting the game state
     * Then, it enters the main menu function
     */
    @Override
    public void run() {
        try {
            init();

            configuration();

            boolean isRmiConnection = getConnectionType();
            if(isRmiConnection){
                setUpRMIConnection();
            } else {
                setUpSocketConnection();
            }

            String username = login();
            setGameState(username);

            mainMenu();
        } catch (Exception e){
            logger.severe("Error During main loop: " + e.getMessage());
        } finally {
            cleanUp();
            closeConnection();
        }
    }

    // Draw methods
    /**
     * Loads the representation of the player's hand into its corresponding variable in the CLI
     */
    private void drawHand(){
        PlayableCard[] cards = getGameState().getHand();
        for (int i = 0; i < cards.length; i++) {
            hand[i] = Draw.frontCard(cards[i]);
        }
    }
    /**
     * Loads the representation of the table (market cards and decks) into their corresponding variables in the CLI
     */
    private void drawTable(){
        PlayableCard[] marketCards = getGameState().getMarket();
        Symbol topGold = getGameState().getTopGoldDeck();
        Symbol topResource = getGameState().getTopResourceDeck();

        market[0] = Draw.frontCard(marketCards[0]);
        market[1] = Draw.frontCard(marketCards[1]);
        market[2] = Draw.frontCard(marketCards[2]);
        market[3] = Draw.frontCard(marketCards[3]);
        gold = Draw.backCard(topGold);
        resource = Draw.backCard(topResource);
    }
    /**
     * Loads the representation of the objectives into their corresponding variables in the CLI
     */
    private void drawObjectives(){
        commonObjs[0] = Draw.objective(getGameState().getCommonObjectives()[0]);
        commonObjs[1] = Draw.objective(getGameState().getCommonObjectives()[1]);
    }
    /**
     * Loads the representation of the personal objective into its corresponding variable in the CLI
     */
    private void drawPersonalObjective(){ personalObj = Draw.objective(getGameState().getPersonalObjective()); }

    private String[][] getDrawHand(){ return hand; }
    private String[][] getDrawMarket(){ return market; }
    private String[][] getDrawCommonObjs(){ return commonObjs; }
    private String[] getDrawPersonalObj(){ return personalObj; }
    private String[] getDrawGoldDeck(){ return gold; }
    private String[] getDrawResourceDeck(){ return resource; }


    //Utility functions, mainly help with the formatting of the text
    /**
     * Pads string with spaces
     * @param string string to pad
     * @param length required length of the resulting string
     * @return padded string
     */
    private String pad(String string, int length){
        return String.format("%-" + length + "s", string);
    }
    /**
     * Centers the given string within a spaces
     * @param string string to center
     * @param length required length of the resulting string
     * @return Centered string, surrounded with spaces
     */
    private String centerPad(String string, int length){
        if (string == null || length <= string.length()) {
            return string;
        }
        int totalPadding = length - string.length();
        int paddingStart = totalPadding / 2;
        int paddingEnd = totalPadding - paddingStart;
        return String.format("%" + paddingStart + "s%s%" + paddingEnd + "s", "", string, "");
    }
    /**
     * Clears the terminal
     */
    private void clearTerminal(){
        System.out.println(ANSI.MOVE_CURSOR_TO_START + ANSI.CLEAR_SCREEN);
        System.out.flush();
    }

    /**
     * Clears the command window and changes it to the previous command lines
     */
    private void cleanCmd(){
        int len = cmdWin.getLength();
        List<String> lines = new ArrayList<>();
        for(int i = lastLine; i < len; i++){
            lines.add(cmdWin.getLine(i));
        }
        cmdWin.clear();
        for(String line : lines){
            cmdWin.addLine(line);
        }
        lastLine = cmdWin.getLength();
    }
    /**
     * Formats a scoreboard line with the player's name and score
     * @param name player's name
     * @param score player's score
     * @return string representing the scoreboard line.
     */
    private String formatScoreboardLine(String name, int score){
        return " " + (name.length() < 8 ? pad(name, 7) : name.substring(0, 5) + "..") + // name
                " " + (score < 9 ? " " + score : score) + // score
                " " + "█".repeat(score) + " ".repeat(33 - score); // draw score
    }
    /**
     * Formats a chat message
     * @param m chat message to format
     * @return string representing the chat message
     */
    private String formatChatMessage(ChatMessage m){
        return m.getSender() + ": " + m.getMessage();
    }
    /**
     * Formats a list of chat recipients
     * @param chatId ID of the chat (null for public chat)
     * @return formatted list of the chat recipients if chatID is not null, otherwise it returns "PUBLIC CHAT"
     */
    private String formatRecipients(Set<String> chatId) {
        if(chatId == null) {
            return "PUBLIC CHAT";
        }

        StringBuilder formattedRecipients = new StringBuilder();
        int i = 0;
        for (String recipient : chatId) {
            formattedRecipients.append(recipient);
            if (i < chatId.size() - 1) {
                formattedRecipients.append(", ");
            }
            i++;
        }
        return formattedRecipients.toString();
    }
    /**
     * Sets the focus on the current player's field
     */
    private void focusMyField() {
        switch (getGameState().getGameOrder().indexOf(getGameState().getPlayers().get(getGameState().getUser()))){
            case 0:
                focus = Cli.Focus.PLAYER1;
                break;
            case 1:
                focus = Cli.Focus.PLAYER2;
                break;
            case 2:
                focus = Cli.Focus.PLAYER3;
                break;
            case 3:
                focus = Cli.Focus.PLAYER4;
                break;
        }
    }
    /**
     * Sends a message to the server and waits for a success response
     * @param m message sent to the server
     * @return true if successful, otherwise false
     */
    private boolean successResponse(Message m){
        success = false;
        waitRes = true;
        sendMessage(m);

        try {
            while (waitRes){
                Thread.sleep(100);
            }
        } catch (InterruptedException ignored){}
        return success;
    }


    // Methods responsible for printing onto the terminal and its formatting
    /**
     * Prints the current state of the game (including player fields,
     * objectives, scoreboard, chat messages etc.)
     */
    synchronized private void printGame() {
        if(!inGame || gameEnded){
            return;
        }

        clearTerminal();

        String[] field = fieldWin.getLines();
        String[][] market = getDrawMarket();
        String[] gold = getDrawGoldDeck();
        String[] resource = getDrawResourceDeck();
        String[][] commonObjs = getDrawCommonObjs();
        String[][] hand = getDrawHand();
        String[] personalObj = getDrawPersonalObj();

        String[] scoreBoard = new String[4];
        String[] banner = new String[3];

        List<PlayerColor> colorOrder = getGameState().getGameOrder();
        List<String> names = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();
        for(PlayerColor c : colorOrder){
            names.add(getGameState().getName(c));
            scores.add(getGameState().getScores().get(c));
        }

        if(names.size() == 2){
            banner[0] = "─────────────────────────────┬─────────────────────────────┬─────────────────────────────┬──────────────────────────────┐";
            banner[1] = (focus == Cli.Focus.PLAYER1 ?  ANSI.HIGHLIGHT : "") + centerPad(names.getFirst(), 29) +  ANSI.RESET + "│" +
                    (focus == Cli.Focus.PLAYER2 ?  ANSI.HIGHLIGHT : "") + centerPad(names.get(1), 29) +  ANSI.RESET + "│" +
                    (focus == Cli.Focus.PUBLIC ?  ANSI.HIGHLIGHT : "") + "         Public Chat         " +  ANSI.RESET + "│" +
                    (focus == Cli.Focus.PRIVATE ?  ANSI.HIGHLIGHT : "") + "         Private Chat         " +  ANSI.RESET + "│";
            banner[2] = "─────────────────────────────┴─────────────────────────────┴─────────────────────────────┴──────────────────────────────┤";

            scoreBoard[0] = "                                             ";
            scoreBoard[1] = Draw.getColor(colorOrder.getFirst()) + formatScoreboardLine(names.getFirst(), scores.getFirst()) +  ANSI.RESET;
            scoreBoard[2] = Draw.getColor(colorOrder.get(1)) + formatScoreboardLine(names.get(1), scores.get(1)) +  ANSI.RESET;
            scoreBoard[3] = "                                             ";
        } else if(names.size() == 3){
            banner[0] = "───────────────────────┬───────────────────────┬───────────────────────┬───────────────────────┬────────────────────────┐";
            banner[1] = (focus == Cli.Focus.PLAYER1 ?  ANSI.HIGHLIGHT : "") + centerPad(names.getFirst(), 23)  +  ANSI.RESET +  "│" +
                    (focus == Cli.Focus.PLAYER2 ?  ANSI.HIGHLIGHT : "") + centerPad(names.get(1), 23)  +  ANSI.RESET +  "│" +
                    (focus == Cli.Focus.PLAYER3 ?  ANSI.HIGHLIGHT : "") + centerPad(names.get(2), 23)  +  ANSI.RESET +  "│" +
                    (focus == Cli.Focus.PUBLIC ?  ANSI.HIGHLIGHT : "") + "      Public Chat      "  +  ANSI.RESET +  "│" +
                    (focus == Cli.Focus.PRIVATE ?  ANSI.HIGHLIGHT : "") + "      Private Chat      "  +  ANSI.RESET +  "│";
            banner[2] = "───────────────────────┴───────────────────────┴───────────────────────┴───────────────────────┴────────────────────────┤";

            scoreBoard[0] = Draw.getColor(colorOrder.getFirst()) + formatScoreboardLine(names.getFirst(), scores.getFirst()) +  ANSI.RESET;
            scoreBoard[1] = Draw.getColor(colorOrder.get(1)) + formatScoreboardLine(names.get(1), scores.get(1)) +  ANSI.RESET;
            scoreBoard[2] = Draw.getColor(colorOrder.get(2)) + formatScoreboardLine(names.get(2), scores.get(2)) +  ANSI.RESET;
            scoreBoard[3] = "                                             ";
        } else {
            banner[0] = "───────────────────┬───────────────────┬───────────────────┬───────────────────┬───────────────────┬────────────────────┐";
            banner[1] = (focus == Cli.Focus.PLAYER1 ?  ANSI.HIGHLIGHT : "") + centerPad(names.getFirst(), 19)  +  ANSI.RESET +  "│" +
                    (focus == Cli.Focus.PLAYER2 ?  ANSI.HIGHLIGHT : "") + centerPad(names.get(1), 19)  +  ANSI.RESET +  "│" +
                    (focus == Cli.Focus.PLAYER3 ?  ANSI.HIGHLIGHT : "") + centerPad(names.get(2), 19)  +  ANSI.RESET +  "│" +
                    (focus == Cli.Focus.PLAYER4 ?  ANSI.HIGHLIGHT : "") + centerPad(names.get(3), 19)  +  ANSI.RESET +  "│" +
                    (focus == Cli.Focus.PUBLIC ?  ANSI.HIGHLIGHT : "") + "    Public Chat    "  +  ANSI.RESET +  "│" +
                    (focus == Cli.Focus.PRIVATE ?  ANSI.HIGHLIGHT : "") + "    Private Chat    "  +  ANSI.RESET +  "│";
            banner[2] = "───────────────────┴───────────────────┴───────────────────┴───────────────────┴───────────────────┴────────────────────┤";

            scoreBoard[0] = Draw.getColor(colorOrder.getFirst()) + formatScoreboardLine(names.getFirst(), scores.getFirst()) +  ANSI.RESET;
            scoreBoard[1] = Draw.getColor(colorOrder.get(1)) + formatScoreboardLine(names.get(1), scores.get(1)) +  ANSI.RESET;
            scoreBoard[2] = Draw.getColor(colorOrder.get(2)) + formatScoreboardLine(names.get(2), scores.get(2)) +  ANSI.RESET;
            scoreBoard[3] = Draw.getColor(colorOrder.get(3)) + formatScoreboardLine(names.get(3), scores.get(3)) +  ANSI.RESET;
        }

        System.out.println("┌─────────────────────────────────────────────┬" + banner[0]);
        System.out.println("│                   TABLE                     │" + banner[1]);
        System.out.println("│         INDEX 0             INDEX 1         ├" + banner[2]);
        System.out.println("│  " + market[0][0] + " " + market[1][0] + "  │" + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(0), FIELD_WIDTH) : ANSI.padSkipANSI(field[0], FIELD_WIDTH)) + "│");
        System.out.println("│  " + market[0][1] + " " + market[1][1] + "  │" + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(1), FIELD_WIDTH) : ANSI.padSkipANSI(field[1], FIELD_WIDTH)) + "│");
        System.out.println("│  " + market[0][2] + " " + market[1][2] + "  │" + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(2), FIELD_WIDTH) : ANSI.padSkipANSI(field[2], FIELD_WIDTH)) + "│");
        System.out.println("│  " + market[0][3] + " " + market[1][3] + "  │" + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(3), FIELD_WIDTH) : ANSI.padSkipANSI(field[3], FIELD_WIDTH)) + "│");
        System.out.println("│  " + market[0][4] + " " + market[1][4] + "  │" + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(4), FIELD_WIDTH) : ANSI.padSkipANSI(field[4], FIELD_WIDTH)) + "│");
        System.out.println("│         INDEX 2             INDEX 3         │"     + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(5), FIELD_WIDTH) : ANSI.padSkipANSI(field[5], FIELD_WIDTH)) + "│");
        System.out.println("│  " + market[2][0] + " " + market[3][0] + "  │" + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(6), FIELD_WIDTH) : ANSI.padSkipANSI(field[6], FIELD_WIDTH)) + "│");
        System.out.println("│  " + market[2][1] + " " + market[3][1] + "  │" + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(7), FIELD_WIDTH) : ANSI.padSkipANSI(field[7], FIELD_WIDTH)) + "│");
        System.out.println("│  " + market[2][2] + " " + market[3][2] + "  │" + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(8), FIELD_WIDTH) : ANSI.padSkipANSI(field[8], FIELD_WIDTH)) + "│");
        System.out.println("│  " + market[2][3] + " " + market[3][3] + "  │" + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(9), FIELD_WIDTH) : ANSI.padSkipANSI(field[9], FIELD_WIDTH)) + "│");
        System.out.println("│  " + market[2][4] + " " + market[3][4] + "  │" + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(10), FIELD_WIDTH) : ANSI.padSkipANSI(field[10], FIELD_WIDTH)) + "│");
        System.out.println("│     RESOURCE DECK          GOLD DECK        │"     + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(11), FIELD_WIDTH) : ANSI.padSkipANSI(field[11], FIELD_WIDTH)) + "│");
        System.out.println("│  " + resource[0] + " " + gold[0] + "  │"       + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(12), FIELD_WIDTH) : ANSI.padSkipANSI(field[12], FIELD_WIDTH)) + "│");
        System.out.println("│  " + resource[1] + " " + gold[1] + "  │"       + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(13), FIELD_WIDTH) : ANSI.padSkipANSI(field[13], FIELD_WIDTH)) + "│");
        System.out.println("│  " + resource[2] + " " + gold[2] + "  │"       + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(14), FIELD_WIDTH) : ANSI.padSkipANSI(field[14], FIELD_WIDTH)) + "│");
        System.out.println("│  " + resource[3] + " " + gold[3] + "  │"       + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(15), FIELD_WIDTH) : ANSI.padSkipANSI(field[15], FIELD_WIDTH)) + "│");
        System.out.println("│  " + resource[4] + " " + gold[4] + "  │"       + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(16), FIELD_WIDTH) : ANSI.padSkipANSI(field[16], FIELD_WIDTH)) + "│");
        System.out.println("│              COMMON OBJECTIVES              │"       + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(17), FIELD_WIDTH) : ANSI.padSkipANSI(field[17], FIELD_WIDTH)) + "│");
        System.out.println("│  " + commonObjs[0][0] + "   " + commonObjs[1][0] + "  │" + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(18), FIELD_WIDTH) : ANSI.padSkipANSI(field[18], FIELD_WIDTH)) + "│");
        System.out.println("│  " + commonObjs[0][1] + "   " + commonObjs[1][1] + "  │" + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(19), FIELD_WIDTH) : ANSI.padSkipANSI(field[19], FIELD_WIDTH)) + "│");
        System.out.println("│  " + commonObjs[0][2] + "   " + commonObjs[1][2] + "  │" + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(20), FIELD_WIDTH) : ANSI.padSkipANSI(field[20], FIELD_WIDTH)) + "│");
        System.out.println("│  " + commonObjs[0][3] + "   " + commonObjs[1][3] + "  │" + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(21), FIELD_WIDTH) : ANSI.padSkipANSI(field[21], FIELD_WIDTH)) + "│");
        System.out.println("│  " + commonObjs[0][4] + "   " + commonObjs[1][4] + "  │" + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(22), FIELD_WIDTH) + "│" : ANSI.padSkipANSI(field[22], FIELD_WIDTH) + "┤"));
        System.out.println("│  " + commonObjs[0][5] + "   " + commonObjs[1][5] + "  │" + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? pad(chatWin.getLine(23), FIELD_WIDTH) : ANSI.padSkipANSI(field[23], FIELD_WIDTH)) + "│");
        System.out.println("├─────────────────────────────────────────────┼──────────────────────────────────────────────────────────────────────────────" + (focus == Cli.Focus.PUBLIC || focus == Cli.Focus.PRIVATE ? "─────────────────────────────────────" : "┴─────┴─────┴─────┴─────┴─────┴─────┴")  + "─────┤");
        System.out.println("│                SCOREBOARD                   │                                       YOUR HAND AND OBJECTIVE                                " + personalObj[0] + "       │");
        System.out.println("│" +            scoreBoard[0]              + "│              " + hand[0][0] + "   " + hand[1][0] + "   " + hand[2][0] + "              " + personalObj[1] + "       │");
        System.out.println("│" +            scoreBoard[1]              + "│              " + hand[0][1] + "   " + hand[1][1] + "   " + hand[2][1] + "              " + personalObj[2] + "       │");
        System.out.println("│" +            scoreBoard[2]              + "│              " + hand[0][2] + "   " + hand[1][2] + "   " + hand[2][2] + "              " + personalObj[3] + "       │");
        System.out.println("│" +            scoreBoard[3]              + "│              " + hand[0][3] + "   " + hand[1][3] + "   " + hand[2][3] + "              " + personalObj[4] + "       │");
        System.out.println("│            0    5    10   15   20   25   30 │              " + hand[0][4] + "   " + hand[1][4] + "   " + hand[2][4] + "              " + personalObj[5] + "       │");
        System.out.println("└────────────┴────┴────┴────┴────┴────┴────┴──┴────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘");

        int lenCmd = cmdWin.getLength();
        for (int i = 0; i < lenCmd; i++){
            System.out.println(cmdWin.getLine(i));
        }

        System.out.println("Type h,help to see all the commands");
        System.out.print(" > ");
    }
    /**
     * Prints the main menu
     */
    private void printMainMenu(){
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.println("│                         Main Menu                       │");
        System.out.println("├───────────────────┬─────────────────────────────────────┤");
        System.out.println("│ create <NUM>      │ Create a new lobby for NUM players  │");
        System.out.println("│ get               │ See all the available lobbies       │");
        System.out.println("│ h, help           │ Show all commands                   │");
        System.out.println("│ join <ID>         │ Join the lobby with the given ID    │");
        System.out.println("│ q, quit           │ Quit the program                    │");
        System.out.println("└───────────────────┴─────────────────────────────────────┘");
        System.out.println();
    }
    /**
     * Prints the lobby menu, with the number of messages set to 10 by default
     */
    private void printLobbyMenu(){
        printLobbyMenu(10);
    }
    /**
     * Prints the lobby menu
     * @param numMsg number of messages
     */
    private void printLobbyMenu(int numMsg){
        clearTerminal();

        List<ChatMessage> chat = getGameState().getPublicChat();
        List<ChatMessage> messages = chat.subList(Math.max(0, chat.size() - numMsg), chat.size());

        System.out.println();
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.println("│                        Lobby Menu                       │");
        System.out.println("├───────────────────┬─────────────────────────────────────┤");
        System.out.println("│ leave             │ Leave the lobby                     │");
        System.out.println("│ send <MESSAGE>    │ Send MESSAGE in the public chat     │");
        System.out.println("└───────────────────┴─────────────────────────────────────┘");
        System.out.println();
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.println("│                        Public Chat                      │");
        System.out.println("├─────────────────────────────────────────────────────────┤");

        for (ChatMessage msg : messages){
            System.out.println("│" + pad(formatChatMessage(msg), 57) + "│");
            System.out.println("├─────────────────────────────────────────────────────────┤");
        }

        System.out.println("└─────────────────────────────────────────────────────────┘");
        System.out.println();
        System.out.print(" > ");
    }
    /**
     * Prints the available game commands
     */
    private void printGameMenu(){
        cmdWin.addLine("                          Game Menu");
        cmdWin.addLine("gold                                   Draw a gold card");
        cmdWin.addLine("market <INDEX>                         Draw a card from the market with the given INDEX");
        cmdWin.addLine("resource                               Draw a resource card");
        cmdWin.addLine("h, help                                Show all commands");
        cmdWin.addLine("play <INDEX> <POSITION> <SIDE>         Play a card from your hand at the specified INDEX, with a specified SIDE (front or back) to the specified POSITION (written as x,y) on your field");
        cmdWin.addLine("send                                   Send a message in the open chat");
        cmdWin.addLine("chat                                   Show the public chat");
        cmdWin.addLine("chat <REC_1> <REC_2> ...               Show the private chat between you and REC_1 ...");
        cmdWin.addLine("field                                  Show your own player field");
        cmdWin.addLine("field <COLOR>                          Show the field of the player with the specified COLOR");
        cmdWin.addNewLine();
    }
    /**
     * Prints the scoreboard and other details at the end of the game
     * @param msg message containing game results and details
     */
    private void printScoreboard(NotifyEndMatch msg){
        clearTerminal();
        System.out.print("\n                   Game Ended");
        if(msg.getWinners() == -1){ //disconnection during set up
            System.out.println(" Due to Disconnection during set up\n");
        }
        else {
            if(msg.getWinners() == 0) //disconnection during game
                System.out.println(" Due to Disconnection during the game\n");

            List<Objective> objs = msg.getObjs();
            List<PlayerColor> players = msg.getPlayers();
            List<Integer> scores = msg.getScores();
            List<Integer> objScores = msg.getObjScores();
            Map<String, PlayerColor> names = getGameState().getPlayers();

            int maxLenName = 0;
            for(String name : names.keySet()){
                if(name.length() > maxLenName){
                    maxLenName = name.length();
                }
            }
            int lenPrefix = 7 + maxLenName + 54 + 5;

            int lenInsideText = 30 + lenPrefix;
            System.out.println("\n┌" + "─".repeat(lenInsideText) + "┐");
            System.out.println("│" + centerPad("SCOREBOARD", lenInsideText) + "│");
            System.out.println("├" + "─".repeat(lenInsideText) + "┤");

            int i;
            for(i = 0; i < players.size(); i++){
                String[] obj = Draw.objective(objs.get(i));
                PlayerColor color = players.get(i);

                String username = getGameState().getName(color);

                String pos = msg.getWinners() > 0 ? (i < msg.getWinners() ? "   1   " : "   " + (2 + i - msg.getWinners()) + "   ") : "       ";
                String name = Draw.getColor(color) + pad(username, maxLenName) + ANSI.RESET;
                String strScore = "   score: " + pad("" + scores.get(i), 3) + " " + Draw.getColor(color) + "█".repeat(scores.get(i) - objScores.get(i)) + ANSI.RESET + ANSI.GOLD + "█".repeat(objScores.get(i)) + " ".repeat(40 - scores.get(i)) + ANSI.RESET;
                String padding = " ".repeat(5);

                System.out.println("│" + ANSI.padSkipANSI(" ".repeat(lenPrefix) + obj[0], lenInsideText) + "│");
                System.out.println("│" + ANSI.padSkipANSI(" ".repeat(lenPrefix) + obj[1], lenInsideText) + "│");
                System.out.println("│" + ANSI.padSkipANSI(pos + name + strScore + padding + obj[2], lenInsideText) + "│");
                System.out.println("│" + ANSI.padSkipANSI(" ".repeat(lenPrefix) + obj[3], lenInsideText) + "│");
                System.out.println("│" + ANSI.padSkipANSI(" ".repeat(lenPrefix) + obj[4], lenInsideText) + "│");
                System.out.println("│" + ANSI.padSkipANSI(" ".repeat(lenPrefix) + obj[5], lenInsideText) + "│");
                System.out.println("│" + " ".repeat(lenInsideText) + "│");
            }
            System.out.println("└" + "─".repeat(lenInsideText) + "┘");
        }

        System.out.println("\n\nPRESS ENTER TO GO BACK");
    }

    private void init() {
        System.out.println("Starting General Cli");
    }
    private void cleanUp() {
        System.out.println("Exiting the game...");
    }

    /**
     * Makes the player choose a starter card
     */
    private void chooseStarterCard(){
        while(starterCardToChoose == null){
            try {
                Thread.sleep(100);
                if(gameEnded){
                    return;
                }
            } catch (InterruptedException ignored){}
        }

        System.out.println("Choose starter card side, type 'choose-starter' ");
        System.out.println("   front                 back");
        for(String s : Draw.starter(starterCardToChoose)){
            System.out.println(s);
        }

        String line;
        while(true){
            System.out.println();
            System.out.println("type front or back");
            System.out.print(" > ");
            line = scanner.nextLine().trim();

            if(gameEnded){
                return;
            }

            if(line.equals("front")){
                sendMessage(new ChooseStarter(true));
                break;
            } else if(line.equals("back")){
                sendMessage(new ChooseStarter(false));
                break;
            } else {
                System.out.println("Invalid Command. Try Again!!");
            }
        }

        System.out.println();
        System.out.print("Waiting for other players...");
    }
    /**
     * Makes the player choose an objective
     */
    private void chooseObjective(){
        while(objectivesToChoose == null){
            try {
                Thread.sleep(100);
                if(gameEnded){
                    return;
                }
            } catch (InterruptedException ignored){}
        }

        String[] obj0 = Draw.objective(objectivesToChoose[0]);
        String[] obj1 = Draw.objective(objectivesToChoose[1]);

        System.out.println("Choose objective card");
        System.out.println("   objective 1           objective 2");
        for(int i = 0; i < obj0.length; i++){
            System.out.println(obj0[i] +  "    " + obj1[i]);
        }

        String line;
        while(true){
            System.out.println();
            System.out.println("type 1 or 2");
            System.out.print(" > ");
            line = scanner.nextLine().trim();

            if(gameEnded){
                return;
            }

            if(line.equals("1")){
                sendMessage(new ChooseObjective(0));
                break;
            } else if(line.equals("2")){
                sendMessage(new ChooseObjective(1));
                break;
            } else {
                System.out.println("Invalid Command. Try Again!!");
            }
        }

        System.out.println();
        System.out.print("Waiting for other players...");
    }

    /**
     * Configures the connection from the terminal
     */
    private void configuration() {
        while (true){
            clearTerminal();
            System.out.println("┌──────────────────────────────────────────────────────────┐");
            System.out.println("│                         Settings                         │");
            System.out.println("├──────────────────────────────────────────────────────────┤");
            System.out.println("│ Server IP Address    : " + pad(getServerAddress(), 34) + "│");
            System.out.println("│ Server SOCKET port   : " + pad("" + getSocketPort(), 34) + "│");
            System.out.println("│ Server RMI port      : " + pad("" + getRmiPort(), 34) + "│");
            System.out.println("└──────────────────────────────────────────────────────────┘");

            String line;
            while (true) {
                System.out.println(" 1. change server address");
                System.out.println(" 2. change SOCKET port");
                System.out.println(" 3. change RMI port");
                System.out.println("Enter to continue");
                System.out.print(" > ");
                line = scanner.nextLine().trim();

                if(Objects.equals(line, "")){
                    return;
                } else if (Objects.equals(line, "1")){
                    System.out.print("Insert Server Address: ");
                    String address = scanner.nextLine().trim();
                    if(setServerAddress(address)){
                        break;
                    }
                    System.out.println("Invalid Server Address");
                } else if (Objects.equals(line, "2")){
                    System.out.print("Insert SOCKET port: ");
                    line = scanner.nextLine().trim();
                    try {
                        int port = Integer.parseInt(line);
                        if (setSocketPort(port)) {
                            break;
                        }
                    } catch (NumberFormatException ignored){}
                    System.out.println("Invalid Port");
                } else if(Objects.equals(line, "3")){
                    System.out.print("Insert RMI port: ");
                    line = scanner.nextLine().trim();
                    try {
                        int port = Integer.parseInt(line);
                        if(setRmiPort(port)){
                            break;
                        }
                    } catch (NumberFormatException ignored){}
                    System.out.println("Invalid Port");
                } else {
                    System.out.println("Invalid Command. Try Again!!");
                }
            }
        }
    }
    /**
     * Gets the desired connection type from the player
     */
    private boolean getConnectionType() {
        System.out.println("Choose connection type");
        System.out.println("1. RMI");
        System.out.println("2. SOCKET");
        System.out.println("default(Enter) SOCKET");
        System.out.println();

        String line;
        while(true){
            System.out.print(" > ");
            line = scanner.nextLine().trim();
            switch (line){
                case "":
                case "2":
                    return false;
                case "1":
                    return true;
                default:
                    System.out.println("Invalid Choice. Try Again!");
            }
        }
    }
    /**
     * Handles the login process from the CLI
     */
    private String login() {
        String username;
        while (true){
            System.out.println("Choose Your Username!!");
            System.out.print("Username : ");
            username = scanner.nextLine().trim();

            if (!username.isEmpty() && successResponse(new LoginMessage(username))) return username;

            System.out.println("Invalid Username. Try Again\n");
        }
    }

    /**
     * Manages the main menu of the CLI
     * This includes match creation, joining, listing available matches, and quitting the application
     * Also manages disconnection and reconnection during a match
     */
    private void mainMenu() {
        if(reconnecting){
            gameMenu();
        }

        printMainMenu();

        while(true){
            System.out.print(" > ");

            String cmd = scanner.nextLine().trim().toLowerCase();
            String[] cmdParts = cmd.split(" ");

            switch (cmdParts[0]){
                case "create":
                    int numPlayers;
                    try {
                        numPlayers = Integer.parseInt(cmdParts[1]);

                        inLobby = true;
                        if(successResponse(new CreateLobbyMessage(numPlayers))) {
                            lobbyMenu();

                            if (matchStarted && !gameEnded) {
                                gameMenu();
                            }

                            matchStarted = false;
                            gameEnded = false;
                            setGameState(getGameState().getUser()); // Clear previous data from gameState
                            printMainMenu();
                        } else {
                            inLobby = false;
                        }
                    } catch (Exception ignored){
                        System.out.println("Invalid Command");
                    }
                    break;
                case "get":
                    successResponse(new GetLobbiesMessage());
                    break;
                case "join":
                    try {
                        int lobbyId = Integer.parseInt(cmdParts[1]);
                        inLobby = true;
                        if(successResponse(new JoinLobbyMessage(lobbyId))) {
                            lobbyMenu();

                            if (matchStarted && !gameEnded) {
                                gameMenu();
                            }

                            matchStarted = false;
                            gameEnded = false;
                            setGameState(getGameState().getUser()); // Clear previous data from gameState
                            printMainMenu();
                        } else {
                            inLobby = false;
                        }
                    } catch (Exception ignored){
                        System.out.println("Invalid Command");
                    }
                    break;
                case "q":
                case "quit":
                    return; // exit the game
                case "h":
                case "help":
                    printMainMenu();
                    break;
                default:
                    System.out.println("Unknown command. Type help to see all the available commands");
            }
        }
    }

    /**
     * Manages the lobby and the chat functionality
     */
    private void lobbyMenu() {
        if(matchStarted){
            return;
        }
        printLobbyMenu();

        boolean running = true;
        while(running){
            String cmd = scanner.nextLine().trim();
            String[] cmdParts = cmd.split(" ");

            switch (cmdParts[0].toLowerCase()){
                case "send":
                    String msg = String.join(" ", Arrays.copyOfRange(cmdParts, 1, cmdParts.length));
                    sendMessage(new ChatMessage(msg, new Date()));
                    break;
                case "leave":
                    if(successResponse(new LeaveLobbyMessage())) {
                        running = false;
                        inLobby = false;
                    }
                    break;
                default:
                    if(inLobby)
                        System.out.println("Unknown command \"" + cmd + "\". Try again.");
            }

            if(matchStarted){
                return;
            } else {
                printLobbyMenu();
            }
        }
    }

    /**
     * Manages the game loop in the CLI
     * Handles all game functionalities including gameplay actions (drawing and playing cards, viewing players' fields),
     * chat functionality etc. and updates the terminal accordingly
     */
    private void gameMenu() {
        if(reconnecting) {
            while (reconnecting) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }

            if(!inGame)
                return;
        }
        else {
            if (gameEnded) {
                return;
            }

            clearTerminal();
            chooseStarterCard();

            if (gameEnded)
                return;

            chooseObjective();

            if (gameEnded)
                return;

            while (!inGame) {
                try {
                    Thread.sleep(100);
                    if (gameEnded)
                        return;
                } catch (InterruptedException ignored) {}
            }
        }

        chatWin.showChat(null, getGameState().getPublicChat());

        while(inGame) {
            cleanCmd();
            printGame();

            String cmd = scanner.nextLine().trim();
            if(gameEnded){
                return;
            }

            String[] cmdParts = cmd.split(" ");

            cmdWin.addLine("> " + cmd);

            switch (cmdParts[0].toLowerCase()) {
                case "send":
                    if(focus != Cli.Focus.PUBLIC && focus != Cli.Focus.PRIVATE){
                        cmdWin.addLine("use the command 'chat' to open the chat before attempting to send a message");
                        break;
                    }

                    Set<String> recipients = chatWin.getId();

                    String msg = String.join(" ", Arrays.copyOfRange(cmdParts, 1, cmdParts.length));

                    sendMessage(new ChatMessage(msg, new Date(), recipients));
                    break;
                case "chat":
                    if (cmdParts.length == 1){ // public chat
                        focus = Cli.Focus.PUBLIC;
                        chatWin.showChat(null, getGameState().getPublicChat());
                    }
                    else{ // private chat
                        List<String> listRecipients = Arrays.asList(cmdParts).subList(1, cmdParts.length);
                        Set<String> chatRecipients = new HashSet<>(listRecipients);

                        chatRecipients.remove(getGameState().getUser()); // remove the use from the recipients if present

                        Set<String> players = getGameState().getPlayers().keySet();
                        if(!players.containsAll(chatRecipients)){
                            cmdWin.addLine("Invalid recipients");
                            break;
                        }

                        if(players.size() - 1 == chatRecipients.size()){ // all players are presents -> public chat
                            focus = Cli.Focus.PUBLIC;
                            chatWin.showChat(null, getGameState().getPublicChat());
                        }
                        else {
                            focus = Cli.Focus.PRIVATE;
                            Map<Set<String>, List<ChatMessage>> privateChats = getGameState().getPrivateChats();
                            if (!privateChats.containsKey(chatRecipients)){
                                getGameState().createChat(chatRecipients);
                                privateChats = getGameState().getPrivateChats();
                            }

                            List<ChatMessage> messages = privateChats.get(chatRecipients);
                            chatWin.showChat(chatRecipients, messages);
                        }
                    }
                    break;
                case "play":
                    if(cmdParts.length < 4){
                        cmdWin.addLine("Empty parameters");
                    }

                    int index;
                    try{
                        index = Integer.parseInt(cmdParts[1]);
                    } catch (Exception e){
                        cmdWin.addLine("Invalid index");
                        break;
                    }

                    String[] positions = cmdParts[2].split(",");
                    if(positions.length != 2){
                        cmdWin.addLine("Invalid position");
                        cmdWin.addLine("Valid parameters: integer,integer");
                        break;
                    }

                    int x, y;
                    try{
                        x = Integer.parseInt(positions[0]);
                        y = Integer.parseInt(positions[1]);
                    } catch (NumberFormatException e){
                        cmdWin.addLine("Invalid position");
                        cmdWin.addLine("Valid parameters: integer,integer");
                        break;
                    }

                    boolean invalid = false;
                    String strSide = cmdParts[3];

                    boolean side = false;
                    switch (strSide){
                        case "FRONT":
                        case"front":
                            side = true;
                            break;
                        case "0":
                        case "BACK":
                        case "back":
                            break;
                        default:
                            cmdWin.addLine("Invalid side");
                            cmdWin.addLine("Valid parameters: FRONT, front or BACK, back");
                            invalid = true;
                    }

                    if(invalid){
                        break;
                    }

                    sendMessage(new PlayCard(index, new Position(x, y), side));
                    break;
                case "gold":
                    sendMessage(new DrawGold());
                    break;
                case "resource":
                    sendMessage(new DrawResource());
                    break;
                case "market":
                    if(cmdParts.length < 2){
                        cmdWin.addLine("Empty parameter");
                    }
                    else{
                        try {
                            sendMessage(new DrawMarket(Integer.parseInt(cmdParts[1])));
                        } catch (NumberFormatException ignored) {
                            cmdWin.addLine("Invalid market index");
                        }
                    }
                    break;
                case "field":
                    if(cmdParts.length == 1){ // show your field
                        PlayerField myField = getGameState().getMyField();
                        fieldWin.draw(myField);
                        focusMyField();
                    }
                    else{
                        String strColor = cmdParts[1].toUpperCase();
                        PlayerColor color;
                        try {
                            color = PlayerColor.valueOf(strColor);
                            if(!getGameState().getPlayers().containsValue(color)){
                                cmdWin.addLine("Invalid player color");
                                cmdWin.addLine("Valid parameters: " + getGameState().getGameOrder().stream().map(Enum::toString).collect(Collectors.joining(", ")));
                                break;
                            }
                        } catch (IllegalArgumentException e){
                            cmdWin.addLine("Invalid player color");
                            cmdWin.addLine("Valid parameters: " + getGameState().getGameOrder().stream().map(Enum::toString).collect(Collectors.joining(", ")));
                            break;
                        }

                        PlayerField field = getGameState().getFields().get(color);
                        fieldWin.draw(field);
                        switch (getGameState().getGameOrder().indexOf(color)){
                            case 0:
                                focus = Cli.Focus.PLAYER1;
                                break;
                            case 1:
                                focus = Cli.Focus.PLAYER2;
                                break;
                            case 2:
                                focus = Cli.Focus.PLAYER3;
                                break;
                            case 3:
                                focus = Cli.Focus.PLAYER4;
                                break;
                        }
                    }
                    break;
                case "h":
                case "help":
                    printGameMenu();
                    break;
                default:
                    cmdWin.addLine("Unknown command \"" + cmd + "\". Try again.");
            }
        }
    }

    /**
     * Handles a chat message
     * Updates the CLI to manage and show public or private chats (in-game or in the lobby).
     * @param chatMsg chat message received
     */
    @Override
    protected void onChatMessage(ChatMessage chatMsg) {
        if(inGame){
            String s = (Objects.equals(chatMsg.getSender(), getGameState().getUser()) ? "You" : chatMsg.getSender() ) + " sent a message (";

            if(chatMsg.getChatType() == ChatMessage.ChatType.PUBLIC) {
                chatWin.newMessage(null);
                s += "public chat";
            } else {
                Set<String> chatId = chatMsg.getRecipients();
                chatId.add(chatMsg.getSender());
                chatId.remove(getGameState().getUser());

                chatWin.newMessage(chatId);
                s += "private chat: " + formatRecipients(chatId);
            }

            s += ")";

            cmdWin.addLine(s);

            printGame();
        } else { // Lobby
            if(inLobby) { // match not started
                printLobbyMenu();
            }
        }
    }


    // The following functions handle various types of messages received from the server
    // They then update the CLI based on what was received
    /**
     * Handles a successful message coming from the server
     * @param m SuccessMessage object
     */
    @Override
    protected void onSuccessMessage(SuccessMessage m) {
        success = true;
        String successMsg = "Success: " + m.successType();
        if (!inGame) {
            System.out.println(successMsg);
        }

        switch (m.successType()){
            case CREATE:
            case JOIN:
            case LOGIN:
            case GAME:
            case LEAVE:
                waitRes = false;
        }
    }
    /**
     * Handles an error message coming from the server
     * @param m ErrorMessage object
     */
    @Override
    protected void onErrorMessage(ErrorMessage m) {
        String errorMsg = "Error code: " + m.getErrorCode() + "\nError Message: " + m.getErrorMessage();
        if (inGame) {
            cmdWin.addLine(errorMsg);
        } else {
            System.out.println(errorMsg);
        }
        waitRes = false;
    }
    /**
     * Handles a response message about available lobbies and prints their info
     * @param m GetLobbiesResponseMessage object
     */
    @Override
    protected void onGetLobbiesMessage(GetLobbiesResponseMessage m) {
        System.out.println("┌─────────────────────────────────┐");
        System.out.println("│             Lobbies             │");
        System.out.println("├─────────────────────────────────┤");
        for (LobbyInfo lobbyInfo : m.getLobbies()){
            String s = "ID : " + lobbyInfo.getId() + "     " + lobbyInfo.getNumCurrentPlayers() + "/" + lobbyInfo.getMaxPlayers();
            if(s.length() % 2 == 0){
                s += " ";
            }
            String pad = " ".repeat((33 - s.length()) / 2);
            System.out.println("│" + pad + s + pad + "│");
            System.out.println("├─────────────────────────────────┤");
        }
        System.out.println("└─────────────────────────────────┘");
        waitRes = false;
    }
    @Override
    protected void onLobbyInfoMessage(LobbyInfoMessage m) {} // forse non serve
    /**
     * Handles a GameStateMessage coming from the server and sets up variables for the game in the CLI
     * @param m GameStateMessage object
     */
    @Override
    protected void onGameStateMessage(GameStateMessage m) {
        getGameState().load(m);
        inGame = true;
        inLobby = false;
        gameEnded = false;
        matchStarted = true;

        // set up game
        drawTable();
        drawHand();
        drawObjectives();
        drawPersonalObjective();
        fieldWin.draw(getGameState().getMyField());
        focusMyField();

        reconnecting = false;
    }
    /**
     * Handles a GameReconnectionMessage coming from the server, for reconnecting to a match
     * @param m GameReconnectionMessage object
     */
    @Override
    protected void onReconnectionMessage(GameReconnectionMessage m) {
        reconnecting = true;
        success = true;
        waitRes = false;
        System.out.println("\n\n Reconnecting to game...\n\n");
    }
    /**
     * Handles a GameReconnectionMessage, signifying a failed reconnection
     * @param m FailedGameReconnectionMessage object
     */
    @Override
    protected void onReconnectionFailedMessage(FailedGameReconnectionMessage m) {
        System.out.println("Reconnection Failed...");
        inGame = false;
        reconnecting = false;
    }
    /**
     * Handles an unknown message coming from the server
     * @param m Message object
     */
    @Override
    protected void onUnknownMessage(Message m) {
        String unknownMsg = "Arrived unknown message type: " + m.getType();
        if (inGame) {
            cmdWin.addLine(unknownMsg);
            printGame();
        } else {
            System.out.println(unknownMsg);
        }
    }
    /**
     * Handles the notification that the match has started and notifies the player by printing on the terminal
     * @param m NotifyMatchStarted object
     */
    @Override
    protected void onMatchStartedMessage(NotifyMatchStarted m) {
        if(inLobby) {
            System.out.println("Match Started!!");
            System.out.println("Press Enter to start");
            inLobby = false;
        }

        cmdWin.addLine("Match Started!!");
        matchStarted = true;
    }
    /**
     * Handles the notification to choose a starter card by storing the starter card
     * @param notifyMsg NotifyToChooseStarter object
     */
    @Override
    protected void onChooseStarterMessage(NotifyToChooseStarter notifyMsg) { starterCardToChoose = notifyMsg.getStarter(); }
    /**
     * Handles the notification to choose an objective by storing the objective
     * @param notifyMsg NotifyToChooseObjective object
     */
    @Override
    protected void onChooseObjectiveMessage(NotifyToChooseObjective notifyMsg) { objectivesToChoose = notifyMsg.getObjective(); }

    /**
     * Calls the drawTable function upon receiving the NotifyCardState notifyMsg
     * @param notifyMsg NotifyCardState object
     */
    @Override
    protected void onCardStateMessage(NotifyCardState notifyMsg) { drawTable(); }
    /**
     * Handles the notification that a player has drawn a card
     * @param notifyMsg NotifyDraw object
     */
    @Override
    protected void onDrawMessage(NotifyDraw notifyMsg) {
        PlayerColor who = notifyMsg.getWho();
        String s = Draw.getColor(who) + getGameState().getName(who) + ANSI.RESET + " : drew a card from the ";
        s += switch (notifyMsg.getWhere()){
            case GOLD -> "gold deck";
            case RESOURCE -> "resource deck";
            case MARKET -> "market";
        };
        cmdWin.addLine(s);

        drawTable();
        drawHand();
        printGame();
    }
    /**
     * Handles the notification of a player's turn
     * @param notifyMsg NotifyTurn object
     */
    @Override
    protected void onNotifyTurnMessage(NotifyTurn notifyMsg) {
        if(notifyMsg.getColor() == getGameState().getPlayers().get(getGameState().getUser())){
            cmdWin.addLine("It's your turn!!");
        } else {
            cmdWin.addLine("It's " + Draw.getColor(notifyMsg.getColor()) + getGameState().getName(notifyMsg.getColor()) + ANSI.RESET + " turn!!");
        }
        printGame();
    }
    /**
     * Handles the notification about the ending of the initial setup
     * @param notifyMsg NotifySetUpFinished object
     */
    @Override
    protected void onSetUpEndedMessage(NotifySetUpFinished notifyMsg) {
        focusMyField();
        inGame = true;
    }
    /**
     * Handles the notification that a card was played by a player
     * @param notifyMsg NotifyCardPlayed object
     */
    @Override
    protected void onCardPlayedMessage(NotifyCardPlayed notifyMsg) {
        PlayerColor who = notifyMsg.getWho();
        cmdWin.addLine(Draw.getColor(who) + getGameState().getName(who) + ANSI.RESET + " : played a " + (notifyMsg.getSide() ? "front" : "back") + " card in " + notifyMsg.getPosition() + "    score: " + notifyMsg.getScore());

        fieldWin.update();
        drawHand();
        printGame();
    }
    /**
     * Calls drawHand function upon receiving NotifyPlayerHand notifyMsg
     * @param notifyMsg NotifyPlayerHand object
     */
    @Override
    protected void onPlayerHandMessage(NotifyPlayerHand notifyMsg) { drawHand(); }
    @Override
    protected void onStarterCardsMessage(NotifyStarterCards notifyMsg) { System.out.println(" Done\nAll players have chosen their starter card side\n"); }
    /**
     * Handles the notification that the match has ended
     * @param notifyMsg NotifyEndMatch object
     */
    @Override
    protected void onEndMatchMessage(NotifyEndMatch notifyMsg) {
        inGame = false;
        inLobby = false;
        gameEnded = true;

        printScoreboard(notifyMsg);
    }
    @Override
    protected void onLastRoundMessage(NotifyLastRound notifyMsg) {
        cmdWin.addLine("Last lap started!!!");
        printGame();
    }
    @Override
    protected void onObjectiveConfirmationMessage(NotifyChosenObjective notifyMsg) { drawPersonalObjective(); }
    @Override
    protected void onStarterConfirmationMessage(NotifyChosenStarter notifyMsg) { fieldWin.draw(getGameState().getMyField()); }
    @Override
    protected void onGlobalObjectiveMessage(NotifyGlobalObjectives notifyMsg) { drawObjectives(); }
    /**
     * Handles the notification of failing to start a match by printing a message and exiting the program
     * @param m FailedToStartMatchMessage object
     */
    @Override
    protected void onFailedToStartMatchMessage(FailedToStartMatchMessage m){
        System.out.println("\n\nFailed To Start Match!!\nServer internal error!!\n\n");
        System.exit(0);
    }
    /**
     * Handles the notification of receiving an unknown NotifyMessage m
     * @param m NotifyMessage object
     */
    @Override
    protected void onUnknownNotifyMessage(NotifyMessage m) {
        cmdWin.addLine("Unknown notify message.");
        printGame();
    }
    /**
     * Handles disconnection by printing a message and exiting the program
     */
    @Override
    public void disconnection(){
        System.out.println("\n\nLost Connection with the server!!\n\n");
        System.exit(0);
    }
}
