package it.polimi.ingsw.model.card;

import it.polimi.ingsw.exception.InvalidSymbolException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for verifying the functionality of different types of cards:
 * StarterCard, GoldCardInt, and ResourceCard.
 */
public class CardTest {

    private StarterCard starter;
    private GoldCard gold;
    private ResourceCard resource;

    /**
     * Sets up the test environment by initializing instances of StarterCard, GoldCardInt, and ResourceCard.
     * @throws InvalidSymbolException if an invalid symbol is encountered during setup
     */
    @Before
    public void setUp() throws InvalidSymbolException {
        starter = new StarterCard(
                new HashMap<>() {
                    {
                        put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.FUNGI));
                        put(CornerPosition.TOP_RIGHT, new Corner(false, null));
                        put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.INKWELL));
                        put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.EMPTY));
                    }
                },
                new HashMap<>() {
                    {
                        put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.FUNGI));
                        put(CornerPosition.TOP_RIGHT, new Corner(false, null));
                        put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.INKWELL));
                        put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.EMPTY));
                    }
                },
                new ArrayList<>(Arrays.asList(Symbol.INSECT, Symbol.FUNGI)),
                "000"
        );

        gold = new GoldCardInt(
                Symbol.FUNGI,
                new HashMap<>() {
                    {
                        put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.FUNGI));
                        put(CornerPosition.TOP_RIGHT, new Corner(false, null));
                        put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.INKWELL));
                        put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.EMPTY));
                    }
                },
                new HashMap<>() {
                    {
                        put(Symbol.INSECT, 3);
                    }
                },
                3,
                "000"
        );

        resource = new ResourceCard(
                Symbol.FUNGI,
                new HashMap<>() {
                    {
                        put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.FUNGI));
                        put(CornerPosition.TOP_RIGHT, new Corner(false, null));
                        put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.INKWELL));
                        put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.EMPTY));
                    }
                },
                2,
                "000"
        );
    }

    /**
     * Test case for verifying the ID retrieval for each type of card.
     */
    @Test
    public  void getIdTest() {
        assertEquals(starter.getId(), "000");
        assertEquals(resource.getId(), "000");
        assertEquals(gold.getId(), "000");
    }

    /**
     * Test cases for retrieving corners from each type of card.
     */
    @Test
    public void getCornerTest() {
        assertTrue(starter.getCorner(CornerPosition.TOP_LEFT, true).IsPresent());
        assertEquals(Symbol.FUNGI, starter.getCorner(CornerPosition.TOP_LEFT, true).getSymbol());
        assertFalse(starter.getCorner(CornerPosition.TOP_RIGHT, true).IsPresent());
        assertTrue(starter.getCorner(CornerPosition.BOTTOM_LEFT, true).IsPresent());
        assertEquals(Symbol.INKWELL, starter.getCorner(CornerPosition.BOTTOM_LEFT, true).getSymbol());
        assertTrue(starter.getCorner(CornerPosition.BOTTOM_RIGHT, true).IsPresent());
        assertEquals(Symbol.EMPTY, starter.getCorner(CornerPosition.BOTTOM_RIGHT, true).getSymbol());

        assertTrue(starter.getCorner(CornerPosition.TOP_LEFT, false).IsPresent());
        assertEquals(Symbol.FUNGI, starter.getCorner(CornerPosition.TOP_LEFT, false).getSymbol());
        assertFalse(starter.getCorner(CornerPosition.TOP_RIGHT, false).IsPresent());
        assertTrue(starter.getCorner(CornerPosition.BOTTOM_LEFT, false).IsPresent());
        assertEquals(Symbol.INKWELL, starter.getCorner(CornerPosition.BOTTOM_LEFT, false).getSymbol());
        assertTrue(starter.getCorner(CornerPosition.BOTTOM_RIGHT, false).IsPresent());
        assertEquals(Symbol.EMPTY, starter.getCorner(CornerPosition.BOTTOM_RIGHT, false).getSymbol());

        assertTrue(gold.getCorner(CornerPosition.TOP_LEFT, true).IsPresent());
        assertEquals(Symbol.FUNGI, gold.getCorner(CornerPosition.TOP_LEFT, true).getSymbol());
        assertFalse(gold.getCorner(CornerPosition.TOP_RIGHT, true).IsPresent());
        assertTrue(gold.getCorner(CornerPosition.BOTTOM_LEFT, true).IsPresent());
        assertEquals(Symbol.INKWELL, gold.getCorner(CornerPosition.BOTTOM_LEFT, true).getSymbol());
        assertTrue(gold.getCorner(CornerPosition.BOTTOM_RIGHT, true).IsPresent());
        assertEquals(Symbol.EMPTY, gold.getCorner(CornerPosition.BOTTOM_RIGHT, true).getSymbol());

        assertTrue(resource.getCorner(CornerPosition.TOP_LEFT, true).IsPresent());
        assertEquals(Symbol.FUNGI, resource.getCorner(CornerPosition.TOP_LEFT, true).getSymbol());
        assertFalse(resource.getCorner(CornerPosition.TOP_RIGHT, true).IsPresent());
        assertTrue(resource.getCorner(CornerPosition.BOTTOM_LEFT, true).IsPresent());
        assertEquals(Symbol.INKWELL, resource.getCorner(CornerPosition.BOTTOM_LEFT, true).getSymbol());
        assertTrue(resource.getCorner(CornerPosition.BOTTOM_RIGHT, true).IsPresent());
        assertEquals(Symbol.EMPTY, resource.getCorner(CornerPosition.BOTTOM_RIGHT, true).getSymbol());

        for(CornerPosition pos :CornerPosition.values()){
            assertTrue(gold.getCorner(pos, false).IsPresent());
            assertEquals(Symbol.EMPTY, gold.getCorner(pos, false).getSymbol());

            assertTrue(resource.getCorner(pos, false).IsPresent());
            assertEquals(Symbol.EMPTY, resource.getCorner(pos, false).getSymbol());
        }
    }

    /**
     * Test cases for retrieving symbols on sides for each type of card.
     */
    @Test
    public void getSymbolsOnSideTest() {
        ArrayList<Symbol> symbols = new ArrayList<>(Arrays.asList(Symbol.INSECT, Symbol.FUNGI, Symbol.FUNGI, Symbol.INKWELL));
        assertEquals(symbols, starter.getSymbolsOnSide(true));

        symbols = new ArrayList<>(Arrays.asList(Symbol.FUNGI, Symbol.INKWELL));
        assertEquals(symbols, starter.getSymbolsOnSide(false));
        assertEquals(symbols, gold.getSymbolsOnSide(true));
        assertEquals(symbols, resource.getSymbolsOnSide(true));

        symbols = new ArrayList<>(List.of(Symbol.FUNGI));
        assertEquals(symbols, gold.getSymbolsOnSide(false));
        assertEquals(symbols, resource.getSymbolsOnSide(false));
    }

    /**
     * Test cases for retrieving the kingdom symbol for each type of card.
     */
    @Test
    public void getKingdomTest() {
        assertEquals(Symbol.EMPTY, starter.getKingdom());
        assertEquals(Symbol.FUNGI, gold.getKingdom());
        assertEquals(Symbol.FUNGI, resource.getKingdom());

    }
}