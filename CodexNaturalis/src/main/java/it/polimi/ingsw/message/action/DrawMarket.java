package it.polimi.ingsw.message.action;

/**
 * A class representing a message for the "draw market" game action.
 * This class extends {@link GameMessage} and specifies the action type as {@link GameAction#DRAW_MARKET}.
 */
public class DrawMarket extends GameMessage {
    private final int marketIndex;

    /**
     * Constructs a DrawMarket message with the specified market index.
     *
     * @param marketIndex The index of the market to draw from.
     */
    public DrawMarket(int marketIndex){
        this.marketIndex = marketIndex;
    }

    /**
     * Gets the market index.
     *
     * @return The index of the market to draw from.
     */
    public int getMarketIndex(){
        return marketIndex;
    }

    /**
     * Gets the action type for this message.
     *
     * @return The action type {@link GameAction#DRAW_MARKET}.
     */
    @Override
    public GameAction getAction(){
        return GameAction.DRAW_MARKET;
    }
}
