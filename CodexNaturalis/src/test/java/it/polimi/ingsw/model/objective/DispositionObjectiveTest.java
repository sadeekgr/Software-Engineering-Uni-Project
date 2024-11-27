package it.polimi.ingsw.model.objective;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.field.PlayerField;
import it.polimi.ingsw.model.field.Position;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class DispositionObjectiveTest {
    private DispositionObjective objType1;
    Position[] pos1;
    private Symbol[] pattern1;
    private DispositionObjective objType2;
    Position[] pos2;
    private Symbol[] pattern2;
    private DispositionObjective objType3;
    Position[] pos3;
    private Symbol[] pattern3;
    private DispositionObjective objType4;
    Position[] pos4;
    private Symbol[] pattern4;
    private PlayerField field;

    /**
     * Sets up the test environment by initializing DispositionObjective instances,
     * populating positions and patterns, and placing resource cards on a PlayerField
     * to simulate game conditions for testing.
     *
     * @throws Exception if setup encounters an error.
     */
    @Before
    public void setUp() throws Exception {
        int score = 3;

        Position[] pos = new Position[2];
        Symbol[] pattern = new Symbol[3];

        pos[0] = new Position(1,1);
        pos[1] = new Position(2,2);
        pattern[0] = Symbol.FUNGI;
        pattern[1] = Symbol.FUNGI;
        pattern[2] = Symbol.FUNGI;

        objType1 = new DispositionObjective(pos, pattern, score, "000");
        pos1 = Arrays.copyOf(pos, 2);
        pattern1 = Arrays.copyOf(pattern, 3);

        pos = new Position[2];
        pos[0] = new Position(1,-1);
        pos[1] = new Position(2,-2);
        pattern = new Symbol[3];
        pattern[0] = Symbol.FUNGI;
        pattern[1] = Symbol.ANIMAL;
        pattern[2] = Symbol.ANIMAL;

        objType2 = new DispositionObjective(pos, pattern, score, "000");
        pos2 = Arrays.copyOf(pos, 2);
        pattern2 = Arrays.copyOf(pattern, 3);

        score = 2;
        pos = new Position[2];
        pos[0] = new Position(0,2);
        pos[1] = new Position(-1,3);
        pattern = new Symbol[3];
        pattern[0] = Symbol.PLANT;
        pattern[1] = Symbol.FUNGI;
        pattern[2] = Symbol.INSECT;

        objType3 = new DispositionObjective(pos, pattern, score, "000");
        pos3 = Arrays.copyOf(pos, 2);
        pattern3 = Arrays.copyOf(pattern, 3);

        score = 10;
        pos = new Position[2];
        pos[0] = new Position(1,1);
        pos[1] = new Position(2,2);
        pattern = new Symbol[3];
        pattern[0] = Symbol.INSECT;
        pattern[1] = Symbol.INSECT;
        pattern[2] = Symbol.INSECT;

        objType4 = new DispositionObjective(pos, pattern, score, "000");
        pos4 = Arrays.copyOf(pos, 2);
        pattern4 = Arrays.copyOf(pattern, 3);

        // create field
        field = new PlayerField();
        Map<CornerPosition, Corner> corner = new HashMap<>();
        for(CornerPosition p : CornerPosition.values()){
            corner.put(p, new Corner(true, Symbol.EMPTY));
        }

        // add starterCard
        field.placeStarterCard(new StarterCard(corner, corner, new ArrayList<>(), "000"), false);

        PlayableCard card;
        // insert 12 card FUNGI
        // obj 1 -> 4 completion
        for(int i = 0; i < 12; i++){
             card = new ResourceCard(Symbol.FUNGI, corner, 0, "000");
             field.placeCard(card, false, new Position(i+1,i+1));
        }

        // insert 11 card INSECT ANIMAL FUNGI FUNGI FUNGI ANIMAL ANIMAL FUNGI ANIMAL ANIMAL ANIMAL
        // obj 2 -> 2 completion
        card = new ResourceCard(Symbol.PLANT, corner, 0, "000");
        field.placeCard(card, true, new Position(1, -1));
        card = new ResourceCard(Symbol.ANIMAL, corner, 0, "000");
        field.placeCard(card, true, new Position(2, -2));
        card = new ResourceCard(Symbol.FUNGI, corner, 0, "000");
        field.placeCard(card, true, new Position(3, -3));
        card = new ResourceCard(Symbol.FUNGI, corner, 0, "000");
        field.placeCard(card, true, new Position(4, -4));
        card = new ResourceCard(Symbol.FUNGI, corner, 0, "000");
        field.placeCard(card, true, new Position(5, -5));
        card = new ResourceCard(Symbol.ANIMAL, corner, 0, "000");
        field.placeCard(card, true, new Position(6, -6));
        card = new ResourceCard(Symbol.ANIMAL, corner, 0, "000");
        field.placeCard(card, true, new Position(7, -7));
        card = new ResourceCard(Symbol.FUNGI, corner, 0, "000");
        field.placeCard(card, true, new Position(8, -8));
        card = new ResourceCard(Symbol.ANIMAL, corner, 0, "000");
        field.placeCard(card, true, new Position(9, -9));
        card = new ResourceCard(Symbol.ANIMAL, corner, 0, "000");
        field.placeCard(card, true, new Position(10, -10));
        card = new ResourceCard(Symbol.ANIMAL, corner, 0, "000");
        field.placeCard(card, true, new Position(11, -11));

        // insert card INSECT on (0, 2) to complete obj 3
        card = new ResourceCard(Symbol.INSECT, corner, 0, "000");
        field.placeCard(card, true, new Position(0, 2));

        //insert PLANT and INSECT on FUNGI line
        // obj 3 completion 1 + 4 = 5
        for(int i = 3; i < 13; i+=3){
            card = new ResourceCard(Symbol.PLANT, corner, 0, "000");
            field.placeCard(card, true, new Position(i, i-2));
            card = new ResourceCard(Symbol.INSECT, corner, 0, "000");
            field.placeCard(card, true, new Position(i-1, i+1));
        }
    }

    /**
     * Tests the calculation of objective scores for DispositionObjective instances.
     * Verifies that scores match expected values based on placed resource cards.
     */
    @Test
    public void calculateObjectiveScore() {
        // obj 1 : 4 completion * score 3 -> 12
        // obj 2 : 2 completion * score 3 -> 6
        // obj 3 : 5 completion * score 2 -> 10
        // obj 4 : 0 completion * score 10 -> 0
        assertEquals(12, objType1.calculateObjectiveScore(field));
        assertEquals(6, objType2.calculateObjectiveScore(field));
        assertEquals(10, objType3.calculateObjectiveScore(field));
        assertEquals(0, objType4.calculateObjectiveScore(field));
    }

    /**
     * Tests retrieval of pattern positions from DispositionObjective instances.
     * Verifies that retrieved positions match those set during setup.
     */
    @Test
    public void getPatternPosition() {
        assertArrayEquals(pos1, objType1.getPatternPosition());
        assertArrayEquals(pos2, objType2.getPatternPosition());
        assertArrayEquals(pos3, objType3.getPatternPosition());
        assertArrayEquals(pos4, objType4.getPatternPosition());
    }

    /**
     * Tests retrieval of pattern kingdoms (symbols) from DispositionObjective instances.
     * Verifies that retrieved kingdoms match expected patterns set during setup.
     */
    @Test
    public void getPatternKingdom() {
        assertArrayEquals(pattern1, objType1.getPatternKingdom());
        assertArrayEquals(pattern2, objType2.getPatternKingdom());
        assertArrayEquals(pattern3, objType3.getPatternKingdom());
        assertArrayEquals(pattern4, objType4.getPatternKingdom());
    }

    /**
     * Tests calculation of completion times for DispositionObjective instances.
     * Verifies that completion times match expected counts based on placed cards.
     */
    @Test
    public void calculateObjectiveCompletionTimes() {
        // obj 1 : 4 completion
        // obj 2 : 2 completion
        // obj 3 : 5 completion
        // obj 4 : 0 completion
        assertEquals(4, objType1.calculateObjectiveCompletionTimes(field));
        assertEquals(2, objType2.calculateObjectiveCompletionTimes(field));
        assertEquals(5, objType3.calculateObjectiveCompletionTimes(field));
        assertEquals(0, objType4.calculateObjectiveCompletionTimes(field));
    }
}