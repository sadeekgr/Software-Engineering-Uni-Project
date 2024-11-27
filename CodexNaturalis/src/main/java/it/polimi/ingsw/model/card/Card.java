package it.polimi.ingsw.model.card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Represents a card (objective ones not included)
 */
public abstract class Card implements Serializable {
    private final String id;

    /**
     * Map representing the corner positions and their associated corner objects for the front side of the card
     */
    protected final Map<CornerPosition, Corner> frontCorners;

    /**
     * Constructs a card with the specified id and front corners.
     *
     * @param id the unique identifier of the card
     * @param frontCorners the corners of the front side of the card
     */
    protected Card(String id, Map<CornerPosition, Corner> frontCorners) {
        this.id = id;
        this.frontCorners = frontCorners;
    }

    /**
     * Returns the corner object at the specified corner position and side of the card.
     *
     * @param cornerPosition the position of the corner
     * @param isFront {@code true} for the front side of the card, {@code false} for the back side
     * @return the corner object at the specified corner position and side
     */
    public abstract Corner getCorner(CornerPosition cornerPosition, boolean isFront);

    /**
     * Returns a list of symbols on the specified side of the card.
     *
     * @param isFront {@code true} for the front side of the card, {@code false} for the back side
     * @return a list of symbols on the specified side of the card
     */
    public abstract ArrayList<Symbol> getSymbolsOnSide(boolean isFront);

    /**
     * Returns the kingdom symbol of the card.
     *
     * @return the kingdom symbol of the card
     */
    public abstract Symbol getKingdom();

    /**
     * Returns the unique identifier of the card.
     *
     * @return the unique identifier of the card
     */
    public String getId() {return id;};

}
