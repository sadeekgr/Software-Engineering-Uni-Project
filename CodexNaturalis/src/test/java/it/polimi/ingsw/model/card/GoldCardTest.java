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

public class GoldCardTest {

    private GoldDeck goldDeck;
    private PlayerField field;

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

    @After
    public void tearDown() {
        goldDeck = null;
        field = null;
    }

    @Test
    public void invalidConstructorParamters(){
        Map<CornerPosition, Corner> frontCorners2 = new HashMap<>();
        Map<Symbol, Integer> requirements = new HashMap<>();
        frontCorners2.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.EMPTY));
        frontCorners2.put(CornerPosition.TOP_RIGHT, new Corner(false, null));
        frontCorners2.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.MANUSCRIPT));
        frontCorners2.put(CornerPosition.BOTTOM_RIGHT, new Corner(false, null));
        requirements.put(Symbol.ANIMAL, 3);

        assertThrows(InvalidSymbolException.class, ()->new GoldCardInt(Symbol.INKWELL, frontCorners2, requirements, 3, "000"));

        requirements.put(Symbol.INKWELL, 2);
        assertThrows(InvalidSymbolException.class, ()->new GoldCardInt(Symbol.ANIMAL, frontCorners2, requirements, 3, "000"));
    }

    @Test
    public void getCornerTest() throws PlayerExceptions {
        Corner corner;
        while (!goldDeck.isEmpty()) {
            GoldCard goldCard = (GoldCard) goldDeck.draw();
            for (CornerPosition pos : CornerPosition.values()) {
                corner = goldCard.getCorner(pos, false);
                assertTrue(corner.IsPresent());
                assertEquals(corner.getSymbol(), Symbol.EMPTY);
                corner = goldCard.getCorner(pos, true);
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

    @Test
    public void getSymbolsOnSideTest() throws PlayerExceptions {
        Corner corner;
        while (!goldDeck.isEmpty()) {
            GoldCard goldCard = (GoldCard) goldDeck.draw();
            ArrayList<Symbol> symbols = goldCard.getSymbolsOnSide(false);
            assertEquals(1, symbols.size());
            assertEquals(symbols.getFirst(), goldCard.getKingdom());
            symbols = goldCard.getSymbolsOnSide(true);
            for (Symbol symbol : symbols) {
                boolean valid_symbol = false;
                for (CornerPosition pos : CornerPosition.values()) {
                    corner = goldCard.getCorner(pos, true);
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

    @Test
    public void getKingdomTest() throws PlayerExceptions {
        while (!goldDeck.isEmpty()) {
            Symbol topSymbol = goldDeck.topCardKingdom();
            assertTrue(topSymbol.isKingdom());
            GoldCard goldCard = (GoldCard) goldDeck.draw();
            assertTrue(goldCard.getKingdom().isKingdom());
            assertEquals(goldCard.getKingdom(), topSymbol);
        }
    }

    @Test
    public void getRequirementsTest() throws PlayerExceptions {
        while (!goldDeck.isEmpty()){
            GoldCard goldCard = (GoldCard) goldDeck.draw();
            assertTrue(goldCard.getRequirements(false).isEmpty());
            Map<Symbol, Integer> requirements = goldCard.getRequirements(true);
            assertFalse(requirements.isEmpty());
            for (Symbol symbol : requirements.keySet()){
                assertTrue(symbol.isKingdom());
                assertTrue(requirements.get(symbol) >= 1);
            }
        }
    }

    @Test
    public void checkRequirementsTest() throws PlayerExceptions {
        while (!goldDeck.isEmpty()) {
            GoldCard goldCard = (GoldCard) goldDeck.draw();
            assertTrue(goldCard.checkRequirements(field, false));
            Map<Symbol, Integer> requirements = goldCard.getRequirements(true);
            boolean are_requirements_met = true;
            for (Symbol symbol : requirements.keySet()){
                if (!(field.getSymbolNum(symbol) >= requirements.get(symbol))){
                    are_requirements_met = false;
                    break;
                }
            }
            assertEquals(are_requirements_met, goldCard.checkRequirements(field, true));
        }
    }
}