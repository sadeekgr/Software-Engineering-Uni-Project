package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.exception.InvalidSymbolException;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.field.PlayerField;
import it.polimi.ingsw.model.field.Position;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FieldWindowTest {
    private FieldWindow window;
    @Before
    public void setUp(){
        window = new FieldWindow(15, 100);
    }

    @Test
    public void draw() throws InvalidSymbolException {
        PlayerField field = new PlayerField();

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
        centerSymbols.add(Symbol.FUNGI);
        StarterCard starterCard = new StarterCard(frontCorners, backCorners, centerSymbols, "000");

        field.placeStarterCard(starterCard, true);

        Map<CornerPosition, Corner> frontCorners1 = new HashMap<>();
        frontCorners1.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.TOP_RIGHT, new Corner(false, null));
        frontCorners1.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners1.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));

        PlayableCard playableCard1 = new ResourceCard(Symbol.FUNGI, frontCorners1, 0, "000");
        Position position1 = new Position(1,1);
        field.placeCard(playableCard1, true, position1);


        PlayableCard playableCard2 = new ResourceCard(Symbol.ANIMAL, frontCorners1, 0, "000");
        Position position2 = new Position(2,0);
        field.placeCard(playableCard2, true, position2);

        PlayableCard playableCard3 = new ResourceCard(Symbol.PLANT, frontCorners1, 0, "000");
        Position position3 = new Position(0,2);
        field.placeCard(playableCard3, true, position3);

        window.draw(field);
        for(String line : window.getLines()){
            System.out.println(line);
        }
    }
}
