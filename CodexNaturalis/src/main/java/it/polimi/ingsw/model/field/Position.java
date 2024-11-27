package it.polimi.ingsw.model.field;

import java.io.Serializable;

/**
 * Represents a position on the player's field.
 *
 * @param x Integer used to store the X coordinate
 * @param y Integer used to store the Y coordinate
 */
public record Position(int x, int y) implements Serializable {
    /**
     * Calculates the sum of the x and y coordinates.
     *
     * @return The sum of the x and y coordinates.
     */
    public int sum() {
        return x + y;
    }

    /**
     * Calculates the distance between this position and another position.
     *
     * @param pos The other position.
     * @return A new Position object representing the distance between the two positions.
     */
    public Position distance(Position pos) {
        return new Position(pos.x() - x, pos.y() - y);
    }

}
