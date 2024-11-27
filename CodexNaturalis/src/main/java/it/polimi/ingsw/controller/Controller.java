package it.polimi.ingsw.controller;

import it.polimi.ingsw.exception.ChatException;
import it.polimi.ingsw.exception.PlayerExceptions;
import it.polimi.ingsw.lobby.Chat;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.message.action.*;
import it.polimi.ingsw.message.error.*;
import it.polimi.ingsw.model.field.Position;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.server.Connection;

/**
 * Controller class manages the game logic and interactions between players and the game model.
 * It handles actions, chat messages, updates, and disconnections.
 */
public class Controller {
    private final Connection c;
    private Player p;
    private Chat chat;
    private boolean disconnected;

    /**
     * Constructor for Controller class, initializes with a connection.
     *
     * @param connection The connection associated with this controller.
     */
    public Controller(Connection connection){
        c = connection;
        disconnected = false;
    }

    /**
     * Sets the player associated with this controller.
     *
     * @param player The player to set.
     */
    public void setPlayer(Player player){
        p = player;
    }

    /**
     * Retrieves the player associated with this controller.
     *
     * @return The player associated with this controller.
     */
    public Player getPlayer(){
        return p;
    }

    /**
     * Processes a game action message.
     *
     * @param m The game message containing the action to process.
     */
    public void action(GameMessage m){
        if(disconnected){
            return;
        }

        if (p == null){
            c.send(new MatchDoesNotExistError());
        }

        switch (m.getAction()){
            case GameAction.CHOOSE_STARTER:
                ChooseStarter starterMessage = (ChooseStarter) m;
                boolean isFront = starterMessage.isFront();

                try{
                    p.chooseStarterCardSide(isFront);
                } catch (PlayerExceptions e){
                    c.send(new StarterChooseError());
                }
                break;
            case GameAction.CHOOSE_OBJECTIVE:
                ChooseObjective objMessage = (ChooseObjective) m;
                int numObj = objMessage.getNumObj();

                try{
                    p.chooseObjective(numObj);
                } catch (PlayerExceptions e){
                    c.send(new ObjectiveChooseError());
                }
                break;
            case GameAction.PLAY_CARD:
                PlayCard playMessage = (PlayCard) m;
                int cardIndex = playMessage.getCardIndex();
                Position pos = playMessage.getPosition();
                boolean side = playMessage.getSide();

                try{
                    p.playCard(cardIndex, pos, side);
                } catch (PlayerExceptions e){
                    c.send(new PlayCardError());
                }
                break;
            case GameAction.DRAW_GOLD:
                try{
                    p.drawGold();
                } catch (PlayerExceptions e){
                    c.send(new DrawError());
                }
                break;
            case GameAction.DRAW_RESOURCE:
                try{
                    p.drawResource();
                } catch (PlayerExceptions e){
                    c.send(new DrawError());
                }
                break;
            case GameAction.DRAW_MARKET:
                DrawMarket marketMessage = (DrawMarket) m;
                int marketIndex = marketMessage.getMarketIndex();

                try{
                    p.drawMarket(marketIndex);
                } catch (PlayerExceptions e){
                    c.send(new DrawError());
                }
                break;
            default:
                c.send(new InvalidMessage());
        }
    }

    /**
     * Processes a chat message.
     *
     * @param m The chat message to process.
     */
    public void chatMessage(ChatMessage m){
        if (disconnected){
            return;
        }

        if (p == null){
            c.send(new MatchDoesNotExistError());
            return;
        }

        try {
            m.setSender(c.getUsername());
            chat.sendMessage(m);
        } catch (ChatException e){
            c.send(new ChatError());
        }
    }

    /**
     * Updates the connection with a message.
     *
     * @param m The message to send as an update.
     */
    public void update(Message m){
        if (disconnected){
            return;
        }

        c.send(m);
    }

    /**
     * Retrieves the username associated with the controller's connection.
     *
     * @return The username associated with the connection.
     */
    public String getUsername(){
        return c.getUsername();
    }

    /**
     * Sets the chat associated with this controller.
     *
     * @param chat The chat to set.
     */
    public void setChat(Chat chat){
        this.chat = chat;
    }

    /**
     * Retrieves the chat associated with this controller.
     *
     * @return The chat associated with this controller.
     */
    public Chat getChat(){
        return chat;
    }

    /**
     * Removes the chat associated with this controller.
     */
    public void removeChat(){
        this.chat = null;
    }

    /**
     * Sends a chat message through the connection.
     *
     * @param m The chat message to send.
     */
    public void sendChatMessage(ChatMessage m){
        if (disconnected){
            return;
        }

        c.send(m);
    }

    /**
     * Marks the controller as disconnected.
     * This could potentially be used to handle disconnection-related game logic.
     */
    public void disconnected(){
        disconnected = true;
    }

    /**
     * Checks if the controller is marked as disconnected.
     *
     * @return True if the controller is disconnected, false otherwise.
     */
    public boolean isDisconnected(){
        return disconnected;
    }

    /**
     * Sets the disconnected status of the controller.
     *
     * @param param The disconnected status to set.
     */
    public void setDisconnected(boolean param){
        disconnected = param;
    }

}
