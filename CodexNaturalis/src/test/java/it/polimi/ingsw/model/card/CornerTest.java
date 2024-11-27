package it.polimi.ingsw.model.card;

import it.polimi.ingsw.exception.CornerNotPresentException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Unit test class for the {@link Corner} class.
 * This class tests the functionality of the {@link Corner} class,
 * including checking whether a corner is present and verifying the symbol
 * associated with a corner.
 */
public class CornerTest {

    /**
     * List of {@link Corner} objects to be used in the tests.
     */
    ArrayList<Corner> corners;

    /**
     * Sets up the test environment before each test method.
     * Initializes the corners list and populates it with {@link Corner} objects.
     * Adds {@link Corner} objects with each symbol from the {@link Symbol} enum,
     * and one {@link Corner} object with no symbol.
     */
    @Before
    public void setUp() {
        corners = new ArrayList<>();
        for (Symbol symbol : Symbol.values()) {
            corners.add(new Corner(true, symbol));
        }
        corners.add(new Corner(false, null));
    }

    /**
     * Cleans up the test environment after each test method.
     * Sets the corners list to null.
     */
    @After
    public void tearDown() {
        corners = null;
    }

    /**
     * Tests the {@link Corner#getSymbol()} method.
     * Verifies that if a corner is not present, a {@link CornerNotPresentException} is thrown.
     * If a corner is present, verifies that the returned symbol is a valid {@link Symbol}.
     */
    @Test
    public void getSymbolTest() {
        for (Corner corner : corners) {
            if (!corner.IsPresent()) {
                try {
                    corner.getSymbol();
                    fail();
                    break;
                }
                catch (CornerNotPresentException ignored){}
            }
            else {
                Symbol symbolToCheck = corner.getSymbol();
                boolean valid_symbol = false;
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

    /**
     * Tests the {@link Corner#IsPresent()} method.
     * Verifies that if a corner is not present, a {@link CornerNotPresentException} is thrown
     * when attempting to get the symbol.
     * If a corner is present, verifies that the returned symbol is a valid {@link Symbol}.
     */
    @Test
    public void isPresentTest() {
        for (Corner corner : corners) {
            if (!corner.IsPresent()) {
                try {
                    corner.getSymbol();
                    fail();
                    break;
                }
                catch (CornerNotPresentException ignored){}
            }
            else {
                Symbol symbolToCheck = corner.getSymbol();
                boolean valid_symbol = false;
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