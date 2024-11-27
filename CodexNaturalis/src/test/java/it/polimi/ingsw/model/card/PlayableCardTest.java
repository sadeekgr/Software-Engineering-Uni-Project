package it.polimi.ingsw.model.card;

import it.polimi.ingsw.exception.CornerNotPresentException;
import it.polimi.ingsw.exception.InvalidSymbolException;
import it.polimi.ingsw.exception.JsonLoadException;
import it.polimi.ingsw.exception.PlayerExceptions;
import it.polimi.ingsw.model.field.PlayerField;
import it.polimi.ingsw.model.field.Position;
import it.polimi.ingsw.model.game.Deck;
import it.polimi.ingsw.model.game.GoldDeck;
import it.polimi.ingsw.model.game.ResourceDeck;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * JUnit test class for testing the functionalities of {@link PlayableCard}.
 * This test class sets up a scenario with {@link ResourceDeck}, {@link GoldDeck}, and {@link PlayerField},
 * and tests different methods of {@link PlayableCard} and its subclasses {@link ResourceCard} and {@link GoldCard}.
 */
public class PlayableCardTest {

    private Deck resourceDeck;
    private Deck goldDeck;
    private PlayerField field;

    /**
     * Sets up the initial state for each test case by initializing a {@link ResourceDeck}, {@link GoldDeck},
     * and {@link PlayerField}, and placing several {@link ResourceCard}s on the field.
     * @throws JsonLoadException if there's an issue loading JSON data.
     * @throws InvalidSymbolException if an invalid symbol is encountered.
     */
    @Before
    public void setUp() throws JsonLoadException, InvalidSymbolException {
        // set up resource deck
        resourceDeck = new ResourceDeck();
        resourceDeck.shuffle();
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
     * Cleans up the resources after each test case by resetting {@code resourceDeck}, {@code goldDeck},
     * and {@code field} to {@code null}.
     */
    @After
    public void tearDown() {
        resourceDeck = null;
        goldDeck = null;
        field = null;
    }

    /**
     * Tests the {@link PlayableCard#getKingdom()} method of {@link PlayableCard}.
     * Verifies that the kingdom symbol is retrieved correctly for both resource and gold decks.
     * @throws PlayerExceptions if there's an issue with player-related exceptions.
     */
    @Test
    public void getKingdomTest() throws PlayerExceptions {
        while (!goldDeck.isEmpty()) {
            Symbol topSymbol = goldDeck.topCardKingdom();
            assertTrue(topSymbol.isKingdom());
            PlayableCard playableCard = goldDeck.draw();
            assertTrue(playableCard.getKingdom().isKingdom());
            assertEquals(playableCard.getKingdom(), topSymbol);
        }
        while (!resourceDeck.isEmpty()){
            Symbol topSymbol = resourceDeck.topCardKingdom();
            assertTrue(topSymbol.isKingdom());
            PlayableCard playableCard = resourceDeck.draw();
            assertTrue(playableCard.getKingdom().isKingdom());
            assertEquals(playableCard.getKingdom(), topSymbol);
        }
    }

    /**
     * Tests the {@link PlayableCard#getCorner(CornerPosition, boolean)} method of {@link PlayableCard}.
     * Verifies that corners are retrieved correctly for both resource and gold decks.
     * @throws PlayerExceptions if there's an issue with player-related exceptions.
     */
    @Test
    public void getCornerTest() throws PlayerExceptions {
        Corner corner;
        while (!goldDeck.isEmpty()) {
            PlayableCard playableCard = goldDeck.draw();
            for (CornerPosition pos : CornerPosition.values()) {
                corner = playableCard.getCorner(pos, false);
                assertTrue(corner.IsPresent());
                assertEquals(corner.getSymbol(), Symbol.EMPTY);
                corner = playableCard.getCorner(pos, true);
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
        while (!resourceDeck.isEmpty()) {
            PlayableCard playableCard = resourceDeck.draw();
            for (CornerPosition pos : CornerPosition.values()) {
                corner = playableCard.getCorner(pos, false);
                assertTrue(corner.IsPresent());
                assertEquals(corner.getSymbol(), Symbol.EMPTY);
                corner = playableCard.getCorner(pos, true);
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
     * Tests the {@link PlayableCard#getSymbolsOnSide(boolean)} method of {@link PlayableCard}.
     * Verifies that symbols on the side are retrieved correctly for both resource and gold decks.
     * @throws PlayerExceptions if there's an issue with player-related exceptions.
     */
    @Test
    public void getSymbolsOnSideTest() throws PlayerExceptions {
        Corner corner;
        while (!resourceDeck.isEmpty()){
            PlayableCard playableCard = resourceDeck.draw();
            ArrayList<Symbol> symbols = playableCard.getSymbolsOnSide(false);
            assertEquals(symbols.size(), 1);
            assertEquals(symbols.getFirst(), playableCard.getKingdom());
            symbols = playableCard.getSymbolsOnSide(true);
            for (Symbol symbol : symbols) {
                boolean valid_symbol = false;
                for (CornerPosition pos : CornerPosition.values()) {
                    corner = playableCard.getCorner(pos, true);
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
        while (!goldDeck.isEmpty()) {
            PlayableCard playableCard = goldDeck.draw();
            ArrayList<Symbol> symbols = playableCard.getSymbolsOnSide(false);
            assertEquals(symbols.size(), 1);
            assertEquals(symbols.getFirst(), playableCard.getKingdom());
            symbols = playableCard.getSymbolsOnSide(true);
            for (Symbol symbol : symbols) {
                boolean valid_symbol = false;
                for (CornerPosition pos : CornerPosition.values()) {
                    corner = playableCard.getCorner(pos, true);
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
     * Tests the {@link PlayableCard#getRequirements(boolean)} method of {@link PlayableCard}.
     * Verifies that requirements are retrieved correctly for both resource and gold decks.
     * @throws PlayerExceptions if there's an issue with player-related exceptions.
     */
    @Test
    public void getRequirementsTest() throws PlayerExceptions {
        while (!goldDeck.isEmpty()){
            PlayableCard playableCard = goldDeck.draw();
            assertTrue(playableCard.getRequirements(false).isEmpty());
            Map<Symbol, Integer> requirements = playableCard.getRequirements(true);
            assertFalse(requirements.isEmpty());
            for (Symbol symbol : requirements.keySet()){
                assertTrue(symbol.isKingdom());
                assertTrue(requirements.get(symbol) >= 1);
            }
        }
        while (!resourceDeck.isEmpty()){
            PlayableCard playableCard = resourceDeck.draw();
            assertTrue(playableCard.getRequirements(true).isEmpty());
            assertTrue(playableCard.getRequirements(false).isEmpty());
        }
    }

    /**
     * Tests the {@link PlayableCard#checkRequirements(PlayerField, boolean)} method of {@link PlayableCard}.
     * Verifies that requirements checking works correctly for both resource and gold decks.
     * @throws PlayerExceptions if there's an issue with player-related exceptions.
     */
    @Test
    public void checkRequirementsTest() throws PlayerExceptions {
        while (!resourceDeck.isEmpty()){
            PlayableCard playableCard = resourceDeck.draw();
            assertTrue(playableCard.checkRequirements(field, false));
            assertTrue(playableCard.checkRequirements(field, true));
        }
        while (!goldDeck.isEmpty()) {
            PlayableCard playableCard = goldDeck.draw();
            assertTrue(playableCard.checkRequirements(field, false));
            Map<Symbol, Integer> requirements = playableCard.getRequirements(true);
            boolean are_requirements_met = true;
            for (Symbol symbol : requirements.keySet()){
                if (!(field.getSymbolNum(symbol) >= requirements.get(symbol))){
                    are_requirements_met = false;
                    break;
                }
            }
            assertEquals(are_requirements_met, playableCard.checkRequirements(field, true));
        }
    }

    /**
     * Tests the {@link PlayableCard#calcScore(PlayerField, boolean, Position)} method of {@link PlayableCard}.
     * Verifies that score calculation is correct for both resource and gold decks.
     * @throws PlayerExceptions if there's an issue with player-related exceptions.
     */
    @Test
    public void calcScoreTest() throws PlayerExceptions {
        while (!goldDeck.isEmpty()) {
            PlayableCard playableCard = goldDeck.draw();
            GoldCard goldCard = (GoldCard) playableCard;
            assertEquals(goldCard.calcScore(field, true, new Position(2, 0)), playableCard.calcScore(field, true, new Position(2, 0)));
            assertEquals(goldCard.calcScore(field, false, new Position(-1, 1)), playableCard.calcScore(field, false, new Position(-1, 1)));
            assertEquals(goldCard.calcScore(field, true, new Position(-1, 5)), playableCard.calcScore(field, true, new Position(-1, 5)));
            assertEquals(goldCard.calcScore(field, true, new Position(-1, 1)), playableCard.calcScore(field, true, new Position(-1, 1)));
            assertEquals(goldCard.calcScore(field, true, new Position(4, -2)), playableCard.calcScore(field, true, new Position(4, -2)));
        }
        while (!resourceDeck.isEmpty()){
            PlayableCard playableCard = resourceDeck.draw();
            ResourceCard resourceCard = (ResourceCard) playableCard;
            assertEquals(resourceCard.calcScore(field, false, new Position(1, 5)), playableCard.calcScore(field, false, new Position(1, 5)));
            assertEquals(resourceCard.calcScore(field, false, new Position(-1, 1)), playableCard.calcScore(field, false, new Position(-1, 1)));
            assertEquals(resourceCard.calcScore(field, true, new Position(2, 0)), playableCard.calcScore(field, true, new Position(2, 0)));
            assertEquals(resourceCard.calcScore(field, true, new Position(4, -2)), playableCard.calcScore(field, true, new Position(4, -2)));
        }
    }
}
