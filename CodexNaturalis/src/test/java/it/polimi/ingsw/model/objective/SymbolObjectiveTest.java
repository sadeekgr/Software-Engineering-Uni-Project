package it.polimi.ingsw.model.objective;

import it.polimi.ingsw.model.card.Corner;
import it.polimi.ingsw.model.card.CornerPosition;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.field.PlayerField;

import it.polimi.ingsw.model.field.Position;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SymbolObjectiveTest {
    private SymbolObjective objective;
    private PlayerField field;

    @Before
    public void setUp() throws Exception {
        Map<Symbol, Integer> symbols = new HashMap<>();
        symbols.put(Symbol.FUNGI, 2);
        symbols.put(Symbol.QUILL, 1);
        int score = 3;
        objective = new SymbolObjective(symbols, score, "000");

        field = new PlayerField();
        Map<CornerPosition, Corner> corner = new HashMap<>();
        Corner cornerFUNGI = new Corner(true, Symbol.FUNGI);
        Corner cornerQUILL = new Corner(true, Symbol.QUILL);
        corner.put(CornerPosition.BOTTOM_LEFT, cornerFUNGI);
        corner.put(CornerPosition.BOTTOM_RIGHT, cornerFUNGI);
        corner.put(CornerPosition.TOP_LEFT, cornerQUILL);
        corner.put(CornerPosition.TOP_RIGHT, cornerQUILL);

        ResourceCard card = new ResourceCard(Symbol.FUNGI, corner, 0, "000");

        for(int i = 0; i < 5; i++) {
            field.placeCard(card, true, new Position(0, 2 * i));
        }
    }

    @Test
    public void calculateObjectiveScore() {
        // completion times 5, score 3 -> 15
        assertEquals(15, objective.calculateObjectiveScore(field));
    }

    @Test
    public void getSymbols() {
        Map<Symbol, Integer> symbols = objective.getSymbols();

        for(Symbol s : symbols.keySet()){
            int symNum = symbols.get(s);
            if(s == Symbol.FUNGI){
                assertEquals(2, symNum);
            } else if (s == Symbol.QUILL) {
                assertEquals(1, symNum);
            }
            else{
                fail();
            }
        }
    }

    @Test
    public void calculateObjectiveCompletionTimes() {
        // 10 FUNGI and 10 QUILL -> obj 2 FUNGI and 1 QUILL -> 5 completions
        int completions = objective.calculateObjectiveCompletionTimes(field);
        assertEquals(5, completions);
    }
}