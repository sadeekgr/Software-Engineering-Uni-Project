package it.polimi.ingsw.model.card;

import it.polimi.ingsw.exception.CornerNotPresentException;
import it.polimi.ingsw.exception.InvalidSymbolException;
import it.polimi.ingsw.exception.JsonLoadException;
import it.polimi.ingsw.exception.PlayerExceptions;
import it.polimi.ingsw.model.field.PlayerField;
import it.polimi.ingsw.model.field.Position;
import it.polimi.ingsw.model.game.GoldDeck;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * JUnit test class for testing the functionalities of {@link GoldCardInt}.
 * This test class sets up a scenario with a {@link GoldDeck}, {@link PlayerField}, and various {@link ResourceCard}s,
 * then tests different methods of {@link GoldCardInt} for correctness.
 */
public class GoldCardIntTest {

    private GoldDeck goldDeck;
    private GoldCardInt goldCardInt;
    private PlayerField field;

    /**
     * Sets up the initial state for each test case by initializing a {@link GoldDeck}, {@link PlayerField}, and placing
     * several {@link ResourceCard}s on the field.
     * @throws JsonLoadException if there's an issue loading JSON data.
     * @throws InvalidSymbolException if an invalid symbol is encountered.
     */
    @Before
    public void setUp() throws JsonLoadException, InvalidSymbolException {
        // set up gold deck
        goldDeck = new GoldDeck();
        goldDeck.shuffle();
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
        field.placeCard(card, false, new Position(1, -1));
        card = new ResourceCard(Symbol.FUNGI, corner, 0, "000");
        field.placeCard(card, false, new Position(2, -2));
        card = new ResourceCard(Symbol.INSECT, corner, 0, "000");
        field.placeCard(card, false, new Position(3, -3));
        card = new ResourceCard(Symbol.ANIMAL, corner, 0, "000");
        field.placeCard(card, false, new Position(4, -4));
        card = new ResourceCard(Symbol.INSECT, corner, 0, "000");
        field.placeCard(card, false, new Position(0, 2));
        card = new ResourceCard(Symbol.PLANT, corner, 0, "000");
        field.placeCard(card, false, new Position(3, 1));
        card = new ResourceCard(Symbol.INSECT, corner, 0, "000");
        field.placeCard(card, false, new Position(3, -1));
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
     * Cleans up the resources after each test case by resetting {@code goldDeck} and {@code field} to {@code null}.
     */
    @After
    public void tearDown() {
        goldDeck = null;
        field = null;
    }

    /**
     * Tests the {@link GoldCardInt#getRequirements(boolean)} method of {@link GoldCardInt}.
     * Verifies that requirements are retrieved correctly for both active and inactive states.
     * @throws PlayerExceptions if there's an issue with player-related exceptions.
     */
    @Test
    public void getRequirementsTest() throws PlayerExceptions {
        while (!goldDeck.isEmpty()){
            GoldCard goldCard = (GoldCard) goldDeck.draw();
            if (goldCard instanceof GoldCardInt){
                goldCardInt = (GoldCardInt) goldCard;
                assertTrue(goldCardInt.getRequirements(false).isEmpty());
                Map<Symbol, Integer> requirements = goldCardInt.getRequirements(true);
                assertFalse(requirements.isEmpty());
                for (Symbol symbol : requirements.keySet()){
                    assertTrue(symbol.isKingdom());
                    assertTrue(requirements.get(symbol) >= 1);
                }
            }
        }
    }

    /**
     * Tests the {@link GoldCardInt#checkRequirements(PlayerField, boolean)} method of {@link GoldCardInt}.
     * Verifies that requirements checking works correctly for both active and inactive states.
     * @throws PlayerExceptions if there's an issue with player-related exceptions.
     */
    @Test
    public void checkRequirementsTest() throws PlayerExceptions {
        while (!goldDeck.isEmpty()) {
            GoldCard goldCard = (GoldCard) goldDeck.draw();
            if (goldCard instanceof GoldCardInt) {
                goldCardInt = (GoldCardInt) goldCard;
                assertTrue(goldCardInt.checkRequirements(field, false));
                Map<Symbol, Integer> requirements = goldCardInt.getRequirements(true);
                boolean are_requirements_met = true;
                for (Symbol symbol : requirements.keySet()){
                    if (!(field.getSymbolNum(symbol) >= requirements.get(symbol))){
                        are_requirements_met = false;
                        break;
                    }
                }
                assertEquals(are_requirements_met, goldCardInt.checkRequirements(field, true));
            }
        }
    }

    /**
     * Tests the {@link GoldCardInt#getCorner(CornerPosition, boolean)} method of {@link GoldCardInt}.
     * Verifies that corners are retrieved correctly for both active and inactive states.
     * @throws PlayerExceptions if there's an issue with player-related exceptions.
     */
    @Test
    public void getCornerTest() throws PlayerExceptions {
        Corner corner;
        while (!goldDeck.isEmpty()) {
            GoldCard goldCard = (GoldCard) goldDeck.draw();
            if (goldCard instanceof GoldCardInt) {
                goldCardInt = (GoldCardInt) goldCard;
                for (CornerPosition pos : CornerPosition.values()) {
                    corner = goldCardInt.getCorner(pos, false);
                    assertTrue(corner.IsPresent());
                    assertEquals(corner.getSymbol(), Symbol.EMPTY);
                    corner = goldCardInt.getCorner(pos, true);
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
    }

    /**
     * Tests the {@link GoldCardInt#getSymbolsOnSide(boolean)} method of {@link GoldCardInt}.
     * Verifies that symbols on the side are retrieved correctly for both active and inactive states.
     * @throws PlayerExceptions if there's an issue with player-related exceptions.
     */
    @Test
    public void getSymbolsOnSideTest() throws PlayerExceptions {
        Corner corner;
        while (!goldDeck.isEmpty()) {
            GoldCard goldCard = (GoldCard) goldDeck.draw();
            if (goldCard instanceof GoldCardInt) {
                goldCardInt = (GoldCardInt) goldCard;
                ArrayList<Symbol> symbols = goldCardInt.getSymbolsOnSide(false);
                assertEquals(symbols.size(), 1);
                assertEquals(symbols.getFirst(), goldCardInt.getKingdom());
                symbols = goldCardInt.getSymbolsOnSide(true);
                for (Symbol symbol : symbols) {
                    boolean valid_symbol = false;
                    for (CornerPosition pos : CornerPosition.values()) {
                        corner = goldCardInt.getCorner(pos, true);
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
    }

    /**
     * Tests the {@link GoldCardInt#getKingdom()} method of {@link GoldCardInt}.
     * Verifies that the kingdom symbol is retrieved correctly.
     * @throws PlayerExceptions if there's an issue with player-related exceptions.
     */
    @Test
    public void getKingdomTest() throws PlayerExceptions {
        while (!goldDeck.isEmpty()) {
            Symbol topSymbol = goldDeck.topCardKingdom();
            assertTrue(topSymbol.isKingdom());
            GoldCard goldCard = (GoldCard) goldDeck.draw();
            if (goldCard instanceof GoldCardInt) {
                goldCardInt = (GoldCardInt) goldCard;
                assertTrue(goldCard.getKingdom().isKingdom());
                assertEquals(goldCardInt.getKingdom(), topSymbol);
            }
        }
    }

    /**
     * Tests the {@link GoldCardInt#getScore()} method of {@link GoldCardInt}.
     * Verifies that the score is calculated correctly.
     * @throws PlayerExceptions if there's an issue with player-related exceptions.
     */
    @Test
    public void getScoreTest() throws PlayerExceptions {
        while (!goldDeck.isEmpty()) {
            GoldCard goldCard = (GoldCard) goldDeck.draw();
            if (goldCard instanceof GoldCardInt) {
                boolean invalidScore = false;
                goldCardInt = (GoldCardInt) goldCard;
                int score = goldCardInt.getScore();
                if (score != 3 && score != 5){
                    invalidScore = true;
                }
                assertFalse(invalidScore);
            }
        }
    }

    /**
     * Tests the {@link GoldCardInt#calcScore(PlayerField, boolean, Position)} method of {@link GoldCardInt}.
     * Verifies that the score calculation is correct for different positions on the field.
     * @throws PlayerExceptions if there's an issue with player-related exceptions.
     */
    @Test
    public void calcScoreTest() throws PlayerExceptions {
        while (!goldDeck.isEmpty()) {
            GoldCard goldCard = (GoldCard) goldDeck.draw();
            if (goldCard instanceof GoldCardInt) {
                goldCardInt = (GoldCardInt) goldCard;
                assertEquals(0, goldCardInt.calcScore(field, false, new Position(1, 5)));
                assertEquals(0, goldCardInt.calcScore(field, false, new Position(-1, 1)));
                assertEquals(goldCardInt.getScore(), goldCardInt.calcScore(field, true, new Position(1, 5)));
                assertEquals(goldCardInt.getScore(), goldCardInt.calcScore(field, true, new Position(-1, 1)));
            }
        }
    }

    @Test
    public void toStringTest() throws InvalidSymbolException {
        Map<CornerPosition, Corner> frontCorners2 = new HashMap<>();
        Map<Symbol, Integer> requirements = new HashMap<>();
        frontCorners2.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.EMPTY));
        frontCorners2.put(CornerPosition.TOP_RIGHT, new Corner(false, null));
        frontCorners2.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.MANUSCRIPT));
        frontCorners2.put(CornerPosition.BOTTOM_RIGHT, new Corner(false, null));
        requirements.put(Symbol.ANIMAL, 3);

        GoldCardInt card = new GoldCardInt(Symbol.ANIMAL, frontCorners2, requirements, 2, "000");
        System.out.println(card.toString());
    }
}
