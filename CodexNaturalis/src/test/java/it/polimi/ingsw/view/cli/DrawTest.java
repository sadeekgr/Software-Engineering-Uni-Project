package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.exception.InvalidSymbolException;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.field.CardPlacement;
import it.polimi.ingsw.model.field.Position;
import it.polimi.ingsw.model.objective.DispositionObjective;
import it.polimi.ingsw.model.player.PlayerColor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests the generation of text representations for various DispositionObjective instances.
 * Prints formatted text for each objective's positions and patterns.
 */
public class DrawTest {
    @Test
    public void objectives() {
        int score = 3;

        Position[] pos = new Position[2];
        Symbol[] pattern = new Symbol[3];

        pos[0] = new Position(1,1);
        pos[1] = new Position(2,2);
        pattern[0] = Symbol.FUNGI;
        pattern[1] = Symbol.FUNGI;
        pattern[2] = Symbol.FUNGI;

        DispositionObjective objType1 = new DispositionObjective(pos, pattern, score, "000");

        pos = new Position[2];
        pos[0] = new Position(1,-1);
        pos[1] = new Position(2,-2);
        pattern = new Symbol[3];
        pattern[0] = Symbol.FUNGI;
        pattern[1] = Symbol.ANIMAL;
        pattern[2] = Symbol.ANIMAL;

        DispositionObjective objType2 = new DispositionObjective(pos, pattern, score, "000");

        score = 2;
        pos = new Position[2];
        pos[0] = new Position(0,2);
        pos[1] = new Position(-1,3);
        pattern = new Symbol[3];
        pattern[0] = Symbol.PLANT;
        pattern[1] = Symbol.FUNGI;
        pattern[2] = Symbol.INSECT;

        DispositionObjective objType3 = new DispositionObjective(pos, pattern, score, "000");

        score = 9;
        pos = new Position[2];
        pos[0] = new Position(1,1);
        pos[1] = new Position(2,2);
        pattern = new Symbol[3];
        pattern[0] = Symbol.INSECT;
        pattern[1] = Symbol.INSECT;
        pattern[2] = Symbol.INSECT;

        DispositionObjective objType4 = new DispositionObjective(pos, pattern, score, "000");

        for(String line : Draw.objective(objType1)){
            System.out.println(line);
        }
        for(String line : Draw.objective(objType2)){
            System.out.println(line);
        }
        for(String line : Draw.objective(objType3)){
            System.out.println(line);
        }
        for(String line : Draw.objective(objType4)){
            System.out.println(line);
        }
    }

    /**
     * Tests the generation of text representations for card placements and starter cards.
     * Prints formatted text for different card placement scenarios and starter card configurations.
     *
     * @throws InvalidSymbolException if an invalid symbol is encountered.
     */
    @Test
    public void cardPlacement() throws InvalidSymbolException {
        int x = 1;
        int y = 1;
        Position position = new Position(x, y);

        Map<CornerPosition, Corner> frontCorners1 = new HashMap<>();
        frontCorners1.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.TOP_RIGHT, new Corner(false, null));
        frontCorners1.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners1.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        ResourceCard card = new ResourceCard(Symbol.FUNGI, frontCorners1, 0, "000");

        CardPlacement cardPlacement = new CardPlacement(true, position, card);
        for(String line : Draw.cardPlacement(cardPlacement)){
            System.out.println(line);
        }

        cardPlacement = new CardPlacement(false, position, card);
        for(String line : Draw.cardPlacement(cardPlacement)){
            System.out.println(line);
        }

        StarterCard starter = new StarterCard(
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

        for(String line : Draw.cardPlacement(cardPlacement)){
            System.out.println(line);
        }

        cardPlacement = new CardPlacement(false, position, starter);
        for(String line : Draw.cardPlacement(cardPlacement)){
            System.out.println(line);
        }

        cardPlacement = new CardPlacement(true, position, starter);
        for(String line : Draw.cardPlacement(cardPlacement)){
            System.out.println(line);
        }
    }

    /**
     * Tests the generation of text representations for starter cards.
     * Prints formatted text for both sides and standard representations of a starter card.
     *
     * @throws InvalidSymbolException if an invalid symbol is encountered.
     */
    @Test
    public void starters() throws InvalidSymbolException {
        StarterCard starter = new StarterCard(
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

        for (String line : Draw.sideStarter(starter, true)) {
            System.out.println(line);
        }

        for (String line : Draw.starter(starter)) {
            System.out.println(line);
        }
    }

    /**
     * Tests the generation of text representations for front and back sides of resource cards.
     * Prints formatted text for card placements and representations of front and back sides of cards.
     *
     * @throws InvalidSymbolException if an invalid symbol is encountered.
     */
    @Test
    public void frontCards() throws InvalidSymbolException {
        int x = 1;
        int y = 1;
        Position position = new Position(x, y);

        Map<CornerPosition, Corner> frontCorners1 = new HashMap<>();
        frontCorners1.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.TOP_RIGHT, new Corner(false, null));
        frontCorners1.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners1.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        ResourceCard card = new ResourceCard(Symbol.FUNGI, frontCorners1, 0, "000");

        CardPlacement cardPlacement = new CardPlacement(true, position, card);

        for(String line : Draw.cardPlacement(cardPlacement)){
            System.out.println(line);
        }

        cardPlacement = new CardPlacement(false, position, card);
        for(String line : Draw.cardPlacement(cardPlacement)){
            System.out.println(line);
        }

        for(String line : Draw.backCard(Symbol.ANIMAL)){
            System.out.println(line);
        }

        for(String line : Draw.frontCard(card)){
            System.out.println(line);
        }
    }

    /**
     * Tests various simple utility methods in the Draw class.
     * Prints formatted text for representations of symbols, colors, and empty cards/objectives.
     *
     * @throws InvalidSymbolException if an invalid symbol is encountered.
     */
    @Test
    public void simpleMethods() throws InvalidSymbolException {
        for(String line : Draw.backCard(Symbol.ANIMAL)){
            System.out.println(line);
        }

        System.out.println(Draw.getSymbol(Symbol.ANIMAL));
        System.out.println(Draw.getColor(PlayerColor.RED));

        Map<CornerPosition, Corner> frontCorners1 = new HashMap<>();
        frontCorners1.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.TOP_RIGHT, new Corner(false, null));
        frontCorners1.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners1.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        ResourceCard card = new ResourceCard(Symbol.FUNGI, frontCorners1, 0, "000");

        for(String line : Draw.backCard(card)){
            System.out.println(line);
        }

        for(String line : Draw.emptyCard()){
            System.out.println(line);
        }

        for(String line : Draw.emptyObjective()){
            System.out.println(line);
        }
    }
}
