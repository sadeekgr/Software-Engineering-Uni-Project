package it.polimi.ingsw.model.card;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.exception.CornerNotPresentException;
import it.polimi.ingsw.exception.InvalidSymbolException;
import it.polimi.ingsw.exception.JsonLoadException;
import it.polimi.ingsw.utilities.GsonSingleton;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * JUnit test class for testing the functionalities of {@link StarterCard}.
 * This test class loads starter cards from a JSON file and tests different methods of {@link StarterCard}.
 */
public class StarterCardTest {

    private List<StarterCard> starterCardList;

    /**
     * Sets up the initial state for each test case by loading starter cards from a JSON file.
     * @throws JsonLoadException if there's an issue loading JSON data.
     */
    @Before
    public void setUp() throws JsonLoadException {
        starterCardList = GsonSingleton.loadJson("/starterCards.json", new TypeToken<List<StarterCard>>() {}.getType());;
    }

    /**
     * Cleans up the resources after each test case by resetting {@code starterCardList} to {@code null}.
     */
    @After
    public void tearDown() {
        starterCardList = null;
    }

    /**
     * Tests the {@link StarterCard#getKingdom()} method of {@link StarterCard}.
     * Verifies that the kingdom symbol is {@link Symbol#EMPTY} for each starter card.
     */
    @Test
    public void getKingdomTest() {
        for (StarterCard starterCard : starterCardList) {
            assertEquals(starterCard.getKingdom(), Symbol.EMPTY);
        }
    }

    /**
     * Tests the {@link StarterCard#getCorner(CornerPosition, boolean)} method of {@link StarterCard}.
     * Verifies that corners are retrieved correctly for each starter card.
     */
    @Test
    public void getCornerTest() {
        Corner corner;
        for (StarterCard starterCard : starterCardList) {
            for (CornerPosition position : CornerPosition.values()) {
                corner = starterCard.getCorner(position, false);
                assertTrue(corner.IsPresent());
                Symbol symbolToCheck = corner.getSymbol();
                boolean valid_symbol = false;
                for (Symbol symbol : Symbol.values()) {
                    if (symbol == symbolToCheck) {
                        valid_symbol = true;
                        break;
                    }
                }
                assertTrue(valid_symbol);
                corner = starterCard.getCorner(position, true);
                if (!corner.IsPresent()) {
                    try {
                        corner.getSymbol();
                        fail();
                        break;
                    }
                    catch (CornerNotPresentException ignored){}
                }
                else {
                    symbolToCheck = corner.getSymbol();
                    valid_symbol = false;
                    for (Symbol symbol : Symbol.values()) {
                        if (symbol == symbolToCheck) {
                            valid_symbol = true;
                            break;
                        }
                    }
                    assertTrue(valid_symbol);
                }
            }
        }
    }

    /**
     * Tests the {@link StarterCard#getSymbolsOnSide(boolean)} method of {@link StarterCard}.
     * Verifies that symbols on the side are retrieved correctly for each starter card.
     */
    @Test
    public void getSymbolsOnSideTest() {
        Corner corner;
        for (StarterCard starterCard : starterCardList) {
            ArrayList<Symbol> symbols = starterCard.getSymbolsOnSide(true);
            for (Symbol symbol : symbols) {
                boolean valid_symbol = false;
                for (CornerPosition pos : CornerPosition.values()) {
                    corner = starterCard.getCorner(pos, true);
                    if (corner.IsPresent()) {
                        if (symbol == corner.getSymbol()) {
                            valid_symbol = true;
                            break;
                        }
                    }
                }
                for (Symbol symbol1 : starterCard.getCenterSymbols(true))
                    if (symbol == symbol1) {
                        valid_symbol = true;
                        break;
                    }
                assertTrue(valid_symbol);
            }
            symbols = starterCard.getSymbolsOnSide(false);
            for (Symbol symbol : symbols) {
                boolean valid_symbol = false;
                for (CornerPosition pos : CornerPosition.values()) {
                    corner = starterCard.getCorner(pos, false);
                    if (corner.IsPresent()) {
                        if (symbol == corner.getSymbol()) {
                            valid_symbol = true;
                            break;
                        }
                    }
                }
                assertTrue(valid_symbol);
            }
        }
    }

    /**
     * Tests the {@link StarterCard#getCenterSymbols(boolean)} method of {@link StarterCard}.
     * Verifies that center symbols are retrieved correctly for each starter card.
     */
    @Test
    public void getCenterSymbolsTest() {
        for (StarterCard starterCard : starterCardList) {
            ArrayList<Symbol> symbols = starterCard.getCenterSymbols(false);
            assertEquals(0, symbols.size());
            symbols = starterCard.getCenterSymbols(true);
            for (Symbol symbol : symbols) {
                boolean valid_symbol = false;
                for (Symbol possibleSymbol : starterCard.getSymbolsOnSide(true)) {
                    if (symbol == possibleSymbol) {
                        valid_symbol = true;
                        break;
                    }
                }
                assertTrue(valid_symbol);
            }
        }
    }

    @Test
    public void constructorException() {
        Map<CornerPosition, Corner> frontCorners = new HashMap<>();
        frontCorners.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.ANIMAL));
        frontCorners.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.EMPTY));
        frontCorners.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.EMPTY));
        frontCorners.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.FUNGI));
        Map<CornerPosition, Corner> backCorners = new HashMap<>();
        backCorners.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.PLANT));
        backCorners.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.ANIMAL));
        backCorners.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        backCorners.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.INSECT));
        ArrayList<Symbol> centerSymbols = new ArrayList<>();
        centerSymbols.add(Symbol.INKWELL);

        assertThrows(InvalidSymbolException.class, () -> new StarterCard(frontCorners, backCorners, centerSymbols, "000"));
    }
}
