package it.polimi.ingsw.utilities;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.exception.JsonLoadException;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.field.Position;
import it.polimi.ingsw.model.objective.DispositionObjective;
import it.polimi.ingsw.model.objective.Objective;
import it.polimi.ingsw.model.objective.SymbolObjective;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

public class GsonSingletonTest {
    @Test
    public void goldCards() {
        // valid path
        String path = "/goldCards.json";
        try {
            ArrayList<GoldCard> cards = GsonSingleton.loadJson(path, new TypeToken<List<GoldCard>>() {}.getType());
            assertEquals(40, cards.size());
            for (GoldCard c : cards) {
                assertNotNull(c);
                assertTrue(c.getClass().equals(GoldCardInt.class) || c.getClass().equals(GoldCardObject.class) || c.getClass().equals(GoldCardCorner.class));
                assertNotNull(c.getKingdom());
                assertNotNull(c.getRequirements(true));
                assertNotNull(c.getRequirements(false));
                for (CornerPosition p : CornerPosition.values()){
                    assertNotNull(c.getCorner(p, true));
                }
                for (CornerPosition p : CornerPosition.values()){
                    assertNotNull(c.getCorner(p, false));
                }
                if(c.getClass().equals(GoldCardInt.class)) {
                    GoldCardInt card = (GoldCardInt) c;
                    assertTrue(card.getScore() != 0);
                }
                else if(c.getClass().equals(GoldCardObject.class)){
                    GoldCardObject card = (GoldCardObject) c;
                    assertEquals(1, card.getScorePerSymbol());
                    assertNotNull(card.getSymbol());
                }
                else{
                    GoldCardCorner card = (GoldCardCorner) c;
                    assertEquals(2, card.getScorePerCorner());
                }
            }

            // check one for each type
            // GoldObject
            GoldCardObject goldObj = (GoldCardObject) cards.getFirst();
            assertEquals(Symbol.FUNGI, goldObj.getKingdom());
            assertEquals(Symbol.QUILL, goldObj.getSymbol());
            assertFalse(goldObj.getCorner(CornerPosition.TOP_LEFT, true).IsPresent());
            assertTrue(goldObj.getCorner(CornerPosition.TOP_RIGHT, true).IsPresent());
            assertTrue(goldObj.getCorner(CornerPosition.BOTTOM_LEFT, true).IsPresent());
            assertTrue(goldObj.getCorner(CornerPosition.BOTTOM_RIGHT, true).IsPresent());
            assertEquals(Symbol.EMPTY, goldObj.getCorner(CornerPosition.TOP_RIGHT, true).getSymbol());
            assertEquals(Symbol.EMPTY, goldObj.getCorner(CornerPosition.BOTTOM_LEFT, true).getSymbol());
            assertEquals(Symbol.QUILL, goldObj.getCorner(CornerPosition.BOTTOM_RIGHT, true).getSymbol());
            Map<Symbol, Integer> req = goldObj.getRequirements(true);
            assertTrue(req.containsKey(Symbol.FUNGI) && req.containsKey(Symbol.ANIMAL) && !req.containsKey(Symbol.PLANT) && !req.containsKey(Symbol.INSECT));
            assertEquals(2, (int) req.get(Symbol.FUNGI));
            assertEquals(1, (int) req.get(Symbol.ANIMAL));

            // GoldCorner
            GoldCardCorner goldCorner = (GoldCardCorner) cards.get(13);
            assertEquals(Symbol.PLANT, goldCorner.getKingdom());
            assertFalse(goldCorner.getCorner(CornerPosition.TOP_LEFT, true).IsPresent());
            assertTrue(goldCorner.getCorner(CornerPosition.TOP_RIGHT, true).IsPresent());
            assertTrue(goldCorner.getCorner(CornerPosition.BOTTOM_LEFT, true).IsPresent());
            assertTrue(goldCorner.getCorner(CornerPosition.BOTTOM_RIGHT, true).IsPresent());
            assertEquals(Symbol.EMPTY, goldCorner.getCorner(CornerPosition.TOP_RIGHT, true).getSymbol());
            assertEquals(Symbol.EMPTY, goldCorner.getCorner(CornerPosition.BOTTOM_LEFT, true).getSymbol());
            assertEquals(Symbol.EMPTY, goldCorner.getCorner(CornerPosition.BOTTOM_RIGHT, true).getSymbol());
            req = goldCorner.getRequirements(true);
            assertTrue(!req.containsKey(Symbol.FUNGI) && !req.containsKey(Symbol.ANIMAL) && req.containsKey(Symbol.PLANT) && req.containsKey(Symbol.INSECT));
            assertEquals(3, (int) req.get(Symbol.PLANT));
            assertEquals(1, (int) req.get(Symbol.INSECT));

            // GoldInt
            GoldCardInt goldInt = (GoldCardInt) cards.get(26);
            assertEquals(Symbol.ANIMAL, goldInt.getKingdom());
            assertEquals(3, goldInt.getScore());
            assertTrue(goldInt.getCorner(CornerPosition.TOP_LEFT, true).IsPresent());
            assertFalse(goldInt.getCorner(CornerPosition.TOP_RIGHT, true).IsPresent());
            assertTrue(goldInt.getCorner(CornerPosition.BOTTOM_LEFT, true).IsPresent());
            assertFalse(goldInt.getCorner(CornerPosition.BOTTOM_RIGHT, true).IsPresent());
            assertEquals(Symbol.EMPTY, goldInt.getCorner(CornerPosition.TOP_LEFT, true).getSymbol());
            assertEquals(Symbol.MANUSCRIPT, goldInt.getCorner(CornerPosition.BOTTOM_LEFT, true).getSymbol());
            req = goldInt.getRequirements(true);
            assertTrue(!req.containsKey(Symbol.FUNGI) && req.containsKey(Symbol.ANIMAL) && !req.containsKey(Symbol.PLANT) && !req.containsKey(Symbol.INSECT));
            assertEquals(3, (int) req.get(Symbol.ANIMAL));

        } catch (JsonLoadException e){
            fail();
        }

        // invalid path
        path = "invalidPath";
        try{
            GsonSingleton.loadJson(path, new TypeToken<List<GoldCard>>() {}.getType());
            fail();
        } catch (JsonLoadException ignored){}
    }

    @Test
    public void resourceCards() {
        // valid path
        String path = "/resourceCards.json";
        try {
            ArrayList<ResourceCard> cards = GsonSingleton.loadJson(path, new TypeToken<List<ResourceCard>>(){}.getType());
            for (ResourceCard c : cards) {
                assertNotNull(c);
                assertNotNull(c.getKingdom());
                for (CornerPosition p : CornerPosition.values()){
                    assertNotNull(c.getCorner(p, true));
                }
                for (CornerPosition p : CornerPosition.values()){
                    assertNotNull(c.getCorner(p, false));
                }
            }
            // check first card
            ResourceCard card = cards.getFirst();
            assertEquals(Symbol.FUNGI, card.getKingdom());
            assertEquals(0, card.getScore());
            assertTrue(card.getCorner(CornerPosition.TOP_LEFT, true).IsPresent());
            assertTrue(card.getCorner(CornerPosition.TOP_RIGHT, true).IsPresent());
            assertTrue(card.getCorner(CornerPosition.BOTTOM_LEFT, true).IsPresent());
            assertFalse(card.getCorner(CornerPosition.BOTTOM_RIGHT, true).IsPresent());
            assertEquals(Symbol.FUNGI, card.getCorner(CornerPosition.TOP_LEFT, true).getSymbol());
            assertEquals(Symbol.EMPTY, card.getCorner(CornerPosition.TOP_RIGHT, true).getSymbol());
            assertEquals(Symbol.FUNGI, card.getCorner(CornerPosition.BOTTOM_LEFT, true).getSymbol());

        } catch (JsonLoadException e){
            fail();
        }

        // invalid path
        path = "invalidPath";
        try{
            GsonSingleton.loadJson(path, new TypeToken<List<ResourceCard>>(){}.getType());
            fail();
        } catch (JsonLoadException ignored){}
    }

    @Test
    public void starterCards() {
        try {
            ArrayList<StarterCard> cards = GsonSingleton.loadJson("/starterCards.json", new TypeToken<List<StarterCard>>() {}.getType());
            for (StarterCard c : cards) {
                assertNotNull(c);
                assertNotNull(c.getKingdom());
                for (CornerPosition p : CornerPosition.values()) {
                    assertNotNull(c.getCorner(p, true));
                }
                for (CornerPosition p : CornerPosition.values()) {
                    assertNotNull(c.getCorner(p, false));
                }
                assertNotNull(c.getCenterSymbols(true));
                assertNotNull(c.getCenterSymbols(false));
            }

            // check first card
            StarterCard card = cards.getFirst();
            assertEquals(Symbol.EMPTY, card.getKingdom());

            assertTrue(card.getCorner(CornerPosition.TOP_LEFT, true).IsPresent());
            assertTrue(card.getCorner(CornerPosition.TOP_RIGHT, true).IsPresent());
            assertFalse(card.getCorner(CornerPosition.BOTTOM_LEFT, true).IsPresent());
            assertFalse(card.getCorner(CornerPosition.BOTTOM_RIGHT, true).IsPresent());
            assertEquals(Symbol.EMPTY, card.getCorner(CornerPosition.TOP_LEFT, true).getSymbol());
            assertEquals(Symbol.EMPTY, card.getCorner(CornerPosition.TOP_RIGHT, true).getSymbol());

            assertTrue(card.getCorner(CornerPosition.TOP_LEFT, false).IsPresent());
            assertTrue(card.getCorner(CornerPosition.TOP_RIGHT, false).IsPresent());
            assertTrue(card.getCorner(CornerPosition.BOTTOM_LEFT, false).IsPresent());
            assertTrue(card.getCorner(CornerPosition.BOTTOM_RIGHT, false).IsPresent());
            assertEquals(Symbol.FUNGI, card.getCorner(CornerPosition.TOP_LEFT, false).getSymbol());
            assertEquals(Symbol.ANIMAL, card.getCorner(CornerPosition.TOP_RIGHT, false).getSymbol());
            assertEquals(Symbol.PLANT, card.getCorner(CornerPosition.BOTTOM_LEFT, false).getSymbol());
            assertEquals(Symbol.INSECT, card.getCorner(CornerPosition.BOTTOM_RIGHT, false).getSymbol());

            ArrayList<Symbol> centerSymbols = card.getCenterSymbols(true);
            assertEquals(3, centerSymbols.size());
            assertTrue(centerSymbols.contains(Symbol.PLANT) && centerSymbols.contains(Symbol.ANIMAL) && centerSymbols.contains(Symbol.FUNGI) && !centerSymbols.contains(Symbol.INSECT));

        } catch (JsonLoadException e) {
            fail();
        }
    }

    @Test
    public void objectives() {
        // valid path
        String path = "/objectiveCards.json";
        try {
            ArrayList<Objective> cards = GsonSingleton.loadJson(path, new TypeToken<List<Objective>>(){}.getType());
            assertEquals(16, cards.size());
            for (Objective c : cards) {
                assertNotNull(c);
                assertTrue(c.getClass().equals(DispositionObjective.class) || c.getClass().equals(SymbolObjective.class));
                if(c.getClass().equals(SymbolObjective.class)){
                    SymbolObjective card = (SymbolObjective) c;
                    assertNotNull(card.getSymbols());
                }
                else{
                    DispositionObjective card = (DispositionObjective) c;
                    assertNotNull(card.getPatternKingdom());
                    assertNotNull(card.getPatternPosition());
                }
            }
            // check the first objective card and symbol card of the json
            DispositionObjective disObj = (DispositionObjective) cards.getFirst();
            assertEquals(2, disObj.getScore());
            assertArrayEquals(new Position[]{new Position(-1,-1), new Position(-2,-2)}, disObj.getPatternPosition());
            assertArrayEquals(new Symbol[]{Symbol.FUNGI, Symbol.FUNGI, Symbol.FUNGI}, disObj.getPatternKingdom());

            SymbolObjective symObj = (SymbolObjective) cards.get(8);
            assertEquals(2, symObj.getScore());
            Map<Symbol, Integer> symbols = symObj.getSymbols();
            assertEquals(3, (int) symbols.get(Symbol.FUNGI));
            for(Symbol s : Symbol.values()){
                if(s != Symbol.FUNGI){
                    assertNull(symbols.get(s));
                }
            }

        } catch (JsonLoadException e){
            System.out.println("ok");
            System.out.println(e.getMessage());
            fail();
        }

        // invalid path
        path = "invalidPath";
        try{
            GsonSingleton.loadJson(path, new TypeToken<List<Objective>>(){}.getType());
            fail();
        } catch (JsonLoadException ignored){}
    }
}
