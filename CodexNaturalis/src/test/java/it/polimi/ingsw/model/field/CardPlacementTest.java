package it.polimi.ingsw.model.field;

import it.polimi.ingsw.exception.CornerNotPresentException;
import it.polimi.ingsw.exception.NoCoveredCornerException;
import it.polimi.ingsw.model.card.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit tests for verifying the functionality of the CardPlacement class.
 * These tests cover card placement, corner retrieval, position checks, and exception handling.
 */
public class CardPlacementTest {

    private CardPlacement cardPlacement;
    private PlayableCard card;

    /**
     * Sets up the test environment by initializing a CardPlacement instance with a PlayableCard.
     * @throws Exception if setup fails
     */
    @Before
    public void setUp() throws Exception {
        int x = 1;
        int y = 1;
        Position position = new Position(x, y);

        Map<CornerPosition, Corner> frontCorners1 = new HashMap<>();
        frontCorners1.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.TOP_RIGHT, new Corner(false, null));
        frontCorners1.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners1.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        card = new ResourceCard(Symbol.FUNGI, frontCorners1, 0, "000");

        cardPlacement = new CardPlacement(true, position, card);
    }

    /**
     * Cleans up resources after each test by setting cardPlacement to null.
     * @throws Exception if teardown fails
     */
    @After
    public void tearDown() throws Exception {
        cardPlacement = null;
    }

    /**
     * Test case for verifying the retrieval of the placed card.
     */
    @Test
    public void getCard() {
        assertEquals(card, cardPlacement.getCard());
    }

    /**
     * Test cases for retrieving corners and checking their existence and symbols.
     */
    @Test
    public void getCorner() {
        assertTrue(cardPlacement.getCorner(CornerPosition.TOP_LEFT).IsPresent());
        assertEquals(Symbol.INKWELL, cardPlacement.getCorner(CornerPosition.TOP_LEFT).getSymbol());

        assertFalse(cardPlacement.getCorner(CornerPosition.TOP_RIGHT).IsPresent());
        assertThrows(CornerNotPresentException.class, ()->cardPlacement.getCorner(CornerPosition.TOP_RIGHT).getSymbol());

        assertTrue(cardPlacement.getCorner(CornerPosition.BOTTOM_LEFT).IsPresent());
        assertEquals(Symbol.FUNGI, cardPlacement.getCorner(CornerPosition.BOTTOM_LEFT).getSymbol());

        assertTrue(cardPlacement.getCorner(CornerPosition.BOTTOM_RIGHT).IsPresent());
        assertEquals(Symbol.ANIMAL, cardPlacement.getCorner(CornerPosition.BOTTOM_RIGHT).getSymbol());
    }

    /**
     * Test case for verifying if the card is placed on the front side.
     */
    @Test
    public void isFront() {
        assertTrue(cardPlacement.isFront());
    }

    /**
     * Test case for verifying the position of the placed card.
     */
    @Test
    public void getPosition() {
        assertEquals(1, cardPlacement.getPosition().x());
        assertEquals(1, cardPlacement.getPosition().y());
    }

    /**
     * Test case for verifying the kingdom symbol associated with the placed card.
     */
    @Test
    public void getKingdom() {
        assertEquals(Symbol.FUNGI, cardPlacement.getKingdom());
    }

    /**
     * Test cases for verifying corner coverage based on provided positions.
     */
    @Test
    public void isCornerCovered() {
        // Test for a position that should be covered
        assertTrue(cardPlacement.isCornerCovered(new Position(2, 2)));
        // Test for a position that should not be covered
        assertFalse(cardPlacement.isCornerCovered(new Position(0, 1)));
    }

    /**
     * Test cases for retrieving the covered corner position based on provided positions.
     */
    @Test
    public void getCoveredCornerPosition() {
        assertEquals(cardPlacement.getCoveredCornerPosition(new Position(2,2)), CornerPosition.TOP_RIGHT);
        assertEquals(cardPlacement.getCoveredCornerPosition(new Position(0,2)), CornerPosition.TOP_LEFT);
        assertEquals(cardPlacement.getCoveredCornerPosition(new Position(2,0)), CornerPosition.BOTTOM_RIGHT);
        assertEquals(cardPlacement.getCoveredCornerPosition(new Position(0,0)), CornerPosition.BOTTOM_LEFT);
        assertThrows(NoCoveredCornerException.class, ()->cardPlacement.getCoveredCornerPosition(new Position(4,7)));
    }
}