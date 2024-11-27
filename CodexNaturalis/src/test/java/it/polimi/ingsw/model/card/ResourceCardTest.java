package it.polimi.ingsw.model.card;

import it.polimi.ingsw.exception.CornerNotPresentException;
import it.polimi.ingsw.exception.InvalidSymbolException;
import it.polimi.ingsw.exception.JsonLoadException;
import it.polimi.ingsw.exception.PlayerExceptions;
import it.polimi.ingsw.model.field.PlayerField;
import it.polimi.ingsw.model.field.Position;
import it.polimi.ingsw.model.game.ResourceDeck;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * JUnit test class for testing the functionalities of {@link ResourceCard}.
 * This test class sets up a scenario with {@link ResourceDeck} and {@link PlayerField},
 * and tests different methods of {@link ResourceCard}.
 */
public class ResourceCardTest {

    private ResourceDeck resourceDeck;
    private ResourceCard resourceCard;
    private PlayerField field;

    /**
     * Sets up the initial state for each test case by initializing a {@link ResourceDeck}
     * and {@link PlayerField}, and placing several {@link ResourceCard}s on the field.
     * @throws InvalidSymbolException if an invalid symbol is encountered.
     * @throws JsonLoadException if there's an issue loading JSON data.
     */
    @Before
    public void setUp() throws InvalidSymbolException, JsonLoadException {
        // set up resource deck
        resourceDeck = new ResourceDeck();
        resourceDeck.shuffle();
        // set up player field
        field = new PlayerField();
        Map<CornerPosition, Corner> corner = new HashMap<>();
        for(CornerPosition p : CornerPosition.values()){
            corner.put(p, new Corner(true, Symbol.EMPTY));
        }

        field.placeStarterCard(new StarterCard(corner, corner, new ArrayList<>(), "000"), false);

        PlayableCard card;
        for(int i = 0; i < 2; i++){
            card = new ResourceCard(Symbol.FUNGI, corner, 0, "000");
            field.placeCard(card, false, new Position(i+1,i+1));
        }
        card = new ResourceCard(Symbol.PLANT, corner, 0, "000");
        field.placeCard(card, true, new Position(1, -1));
        card = new ResourceCard(Symbol.FUNGI, corner, 0, "000");
        field.placeCard(card, true, new Position(2, -2));
        card = new ResourceCard(Symbol.INSECT, corner, 0, "000");
        field.placeCard(card, true, new Position(3, -3));
        card = new ResourceCard(Symbol.ANIMAL, corner, 0, "000");
        field.placeCard(card, true, new Position(4, -4));
        card = new ResourceCard(Symbol.INSECT, corner, 0, "000");
        field.placeCard(card, true, new Position(0, 2));
        card = new ResourceCard(Symbol.PLANT, corner, 0, "000");
        field.placeCard(card, true, new Position(3, 1));
        card = new ResourceCard(Symbol.INSECT, corner, 0, "000");
        field.placeCard(card, true, new Position(3, -1));
        card = new ResourceCard(Symbol.INSECT, corner, 0, "000");
        field.placeCard(card, false, new Position(5, -3));
        Map<CornerPosition, Corner> corner1 = new HashMap<>();
        for(CornerPosition p : CornerPosition.values()){
            corner1.put(p, new Corner(true, Symbol.QUILL));
        }
        card = new ResourceCard(Symbol.ANIMAL, corner1, 0, "000");
        field.placeCard(card, true, new Position(-1, 3));
        Map<CornerPosition, Corner> corner2 = new HashMap<>();
        for(CornerPosition p : CornerPosition.values()){
            corner2.put(p, new Corner(true, Symbol.MANUSCRIPT));
        }
        card = new ResourceCard(Symbol.ANIMAL, corner2, 0, "000");
        field.placeCard(card, true, new Position(-2, 4));
    }

    /**
     * Cleans up the resources after each test case by resetting {@code resourceDeck}
     * and {@code field} to {@code null}.
     */
    @After
    public void tearDown(){
        resourceDeck = null;
        field = null;
    }

    /**
     * Tests the {@link ResourceCard#getCorner(CornerPosition, boolean)} method of {@link ResourceCard}.
     * Verifies that corners are retrieved correctly for each card drawn from {@link ResourceDeck}.
     * @throws PlayerExceptions if there's an issue with player-related exceptions.
     */
    @Test
    public void getCornerTest() throws PlayerExceptions {
        Corner corner;
        while (!resourceDeck.isEmpty()) {
            resourceCard = (ResourceCard) resourceDeck.draw();
            for (CornerPosition pos : CornerPosition.values()) {
                corner = resourceCard.getCorner(pos, false);
                assertTrue(corner.IsPresent());
                assertEquals(corner.getSymbol(), Symbol.EMPTY);
                corner = resourceCard.getCorner(pos, true);
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

    /**
     * Tests the {@link ResourceCard#getSymbolsOnSide(boolean)} method of {@link ResourceCard}.
     * Verifies that symbols on the side are retrieved correctly for each card drawn from {@link ResourceDeck}.
     * @throws PlayerExceptions if there's an issue with player-related exceptions.
     */
    @Test
    public void getSymbolsOnSideTest() throws PlayerExceptions {
        Corner corner;
        while (!resourceDeck.isEmpty()){
            resourceCard = (ResourceCard) resourceDeck.draw();
            ArrayList<Symbol> symbols = resourceCard.getSymbolsOnSide(false);
            assertEquals(symbols.size(), 1);
            assertEquals(symbols.getFirst(), resourceCard.getKingdom());
            symbols = resourceCard.getSymbolsOnSide(true);
            for (Symbol symbol : symbols) {
                boolean valid_symbol = false;
                for (CornerPosition pos : CornerPosition.values()) {
                    corner = resourceCard.getCorner(pos, true);
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
     * Tests the {@link ResourceCard#getKingdom()} method of {@link ResourceCard}.
     * Verifies that the kingdom symbol is retrieved correctly for each card drawn from {@link ResourceDeck}.
     * @throws PlayerExceptions if there's an issue with player-related exceptions.
     */
    @Test
    public void getKingdomTest() throws PlayerExceptions {
        while (!resourceDeck.isEmpty()){
            Symbol topSymbol = resourceDeck.topCardKingdom();
            assertTrue(topSymbol.isKingdom());
            resourceCard = (ResourceCard) resourceDeck.draw();
            assertTrue(resourceCard.getKingdom().isKingdom());
            assertEquals(resourceCard.getKingdom(), topSymbol);
        }
    }

    /**
     * Tests the {@link ResourceCard#getScore()} method of {@link ResourceCard}.
     * Verifies that the score returned is either 0 or 1 for each card drawn from {@link ResourceDeck}.
     * @throws PlayerExceptions if there's an issue with player-related exceptions.
     */
    @Test
    public void getScoreTest() throws PlayerExceptions {
        while (!resourceDeck.isEmpty()){
            boolean invalidScore = false;
            resourceCard = (ResourceCard) resourceDeck.draw();
            int score = resourceCard.getScore();
            if (score != 0 && score != 1){
                invalidScore = true;
            }
            assertFalse(invalidScore);
        }
    }

    /**
     * Tests the {@link ResourceCard#getRequirements(boolean)} method of {@link ResourceCard}.
     * Verifies that requirements are correctly retrieved for each card drawn from {@link ResourceDeck}.
     * @throws PlayerExceptions if there's an issue with player-related exceptions.
     */
    @Test
    public void getRequirementsTest() throws PlayerExceptions {
        while (!resourceDeck.isEmpty()){
            resourceCard = (ResourceCard) resourceDeck.draw();
            assertTrue(resourceCard.getRequirements(true).isEmpty());
            assertTrue(resourceCard.getRequirements(false).isEmpty());
        }
    }

    /**
     * Tests the {@link ResourceCard#checkRequirements(PlayerField, boolean)} method of {@link ResourceCard}.
     * Verifies that requirements checking works correctly for each card drawn from {@link ResourceDeck}.
     * @throws PlayerExceptions if there's an issue with player-related exceptions.
     */
    @Test
    public void checkRequirementsTest() throws PlayerExceptions {
        while (!resourceDeck.isEmpty()){
            resourceCard = (ResourceCard) resourceDeck.draw();
            assertTrue(resourceCard.checkRequirements(field, false));
            assertTrue(resourceCard.checkRequirements(field, true));
        }
    }

    /**
     * Tests the {@link ResourceCard#calcScore(PlayerField, boolean, Position)} method of {@link ResourceCard}.
     * Verifies that score calculation is correct for each card drawn from {@link ResourceDeck}.
     * @throws PlayerExceptions if there's an issue with player-related exceptions.
     */
    @Test
    public void calcScoreTest() throws PlayerExceptions {
        while (!resourceDeck.isEmpty()){
            resourceCard = (ResourceCard) resourceDeck.draw();
            assertEquals(0, resourceCard.calcScore(field, false, new Position(1, 5)));
            assertEquals(0, resourceCard.calcScore(field, false, new Position(-1, 1)));
            assertEquals(resourceCard.getScore(), resourceCard.calcScore(field, true, new Position(1, 5)));
            assertEquals(resourceCard.getScore(), resourceCard.calcScore(field, true, new Position(-1, 1)));
        }
    }

    @Test
    public void toStringTest() throws InvalidSymbolException {
        Map<CornerPosition, Corner> frontCorners2 = new HashMap<>();
        Map<Symbol, Integer> requirements = new HashMap<>();
        frontCorners2.put(CornerPosition.TOP_LEFT, new Corner(false, Symbol.EMPTY));
        frontCorners2.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.MANUSCRIPT));
        frontCorners2.put(CornerPosition.BOTTOM_LEFT, new Corner(false, Symbol.MANUSCRIPT));
        frontCorners2.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.EMPTY));
        requirements.put(Symbol.ANIMAL, 3);

        ResourceCard card = new ResourceCard(Symbol.ANIMAL, frontCorners2, 2, "000");
        System.out.println(card.toString());
    }
}
