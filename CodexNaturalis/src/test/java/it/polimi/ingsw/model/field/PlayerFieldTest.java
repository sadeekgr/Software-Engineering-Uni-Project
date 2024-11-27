package it.polimi.ingsw.model.field;

import it.polimi.ingsw.exception.InvalidSymbolException;
import it.polimi.ingsw.model.card.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PlayerFieldTest {

    private PlayerField playerField;

    @Before
    public void setUp() throws Exception {
        playerField = new PlayerField();
    }

    @After
    public void tearDown() throws Exception {
        playerField = null;
    }

    @Test
    public void addSymbols() throws InvalidSymbolException {
        StarterCard starterCard = createStarterCard();
        playerField.placeStarterCard(starterCard, true);

        Map<CornerPosition, Corner> frontCorners1 = new HashMap<>();
        frontCorners1.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.EMPTY));
        frontCorners1.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.INSECT));
        frontCorners1.put(CornerPosition.BOTTOM_LEFT, new Corner(false, null));
        frontCorners1.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.INKWELL));

        PlayableCard playableCard1 = new ResourceCard(Symbol.FUNGI, frontCorners1, 0, "000");
        playerField.placeCard(playableCard1, true, new Position(1,1));

        assertEquals(playerField.getSymbolNum(Symbol.FUNGI), 2);
        assertEquals(playerField.getSymbolNum(Symbol.INKWELL), 1);
        assertEquals(playerField.getSymbolNum(Symbol.EMPTY), 0);
        assertEquals(playerField.getSymbolNum(Symbol.ANIMAL), 1);
        assertEquals(playerField.getSymbolNum(Symbol.PLANT), 0);
        assertEquals(playerField.getSymbolNum(Symbol.INSECT), 1);
        assertEquals(playerField.getSymbolNum(Symbol.QUILL), 0);
        assertEquals(playerField.getSymbolNum(Symbol.MANUSCRIPT), 0);
    }

    @Test
    public void removeCoveredSymbols() throws InvalidSymbolException {
        StarterCard starterCard = createStarterCard();
        playerField.placeStarterCard(starterCard, true);

        Map<CornerPosition, Corner> frontCorners1 = new HashMap<>();
        frontCorners1.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners1.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.ANIMAL));
        frontCorners1.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.BOTTOM_RIGHT, new Corner(false, null));

        PlayableCard playableCard1;
        for(int i = 0; i < 2; i++){
            playableCard1 = new ResourceCard(Symbol.FUNGI, frontCorners1, 0, "000");
            playerField.placeCard(playableCard1, true, new Position(i+1,i+1));
        }

        assertEquals(playerField.getSymbolNum(Symbol.FUNGI), 4);
        assertEquals(playerField.getSymbolNum(Symbol.INKWELL), 2);
        assertEquals(playerField.getSymbolNum(Symbol.EMPTY), 0);
        assertEquals(playerField.getSymbolNum(Symbol.ANIMAL), 2);
        assertEquals(playerField.getSymbolNum(Symbol.PLANT), 0);
        assertEquals(playerField.getSymbolNum(Symbol.INSECT), 0);
        assertEquals(playerField.getSymbolNum(Symbol.QUILL), 0);
        assertEquals(playerField.getSymbolNum(Symbol.MANUSCRIPT), 0);
    }

    @Test
    public void placeStarterCard() throws InvalidSymbolException {
        StarterCard starterCard = createStarterCard();
        playerField.placeStarterCard(starterCard, true);
        ArrayList<CardPlacement> res = playerField.getCards();
        assertEquals(starterCard, res.getFirst().getCard());
        assertEquals(1, res.size());
    }

    @Test
    public void placeCard() throws InvalidSymbolException {
        StarterCard starterCard = createStarterCard();
        playerField.placeStarterCard(starterCard, true);

        Map<CornerPosition, Corner> frontCorners1 = new HashMap<>();
        frontCorners1.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.TOP_RIGHT, new Corner(false, null));
        frontCorners1.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners1.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));

        PlayableCard playableCard1 = new ResourceCard(Symbol.FUNGI, frontCorners1, 0, "000");
        Position position1 = new Position(1,1);
        playerField.placeCard(playableCard1, true, position1);


        PlayableCard playableCard2 = new ResourceCard(Symbol.FUNGI, frontCorners1, 0, "000");
        Position position2 = new Position(2,0);
        playerField.placeCard(playableCard2, true, position2);

        PlayableCard playableCard3 = new ResourceCard(Symbol.FUNGI, frontCorners1, 0, "000");
        Position position3 = new Position(0,2);
        playerField.placeCard(playableCard3, true, position3);

        ArrayList<CardPlacement> res = playerField.getCards();
        assertEquals(starterCard, res.get(0).getCard());
        assertEquals(playableCard1, res.get(1).getCard());
        assertEquals(playableCard2, res.get(2).getCard());
        assertEquals(playableCard3, res.get(3).getCard());
        assertEquals(4, res.size());
    }

    @Test
    public void isCardPlaceableAt() throws InvalidSymbolException {
        // StarterCard
        Map<CornerPosition, Corner> frontCorners = new HashMap<>();
        frontCorners.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.EMPTY));
        frontCorners.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.EMPTY));
        frontCorners.put(CornerPosition.BOTTOM_LEFT, new Corner(false, null));
        frontCorners.put(CornerPosition.BOTTOM_RIGHT, new Corner(false, null));
        Map<CornerPosition, Corner> backCorners = new HashMap<>();
        backCorners.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INSECT));
        backCorners.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.FUNGI));
        backCorners.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.PLANT));
        backCorners.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        ArrayList<Symbol> centerSymbols = new ArrayList<>();
        centerSymbols.add(Symbol.ANIMAL);
        centerSymbols.add(Symbol.INSECT);
        centerSymbols.add(Symbol.PLANT);
        StarterCard starterCard = new StarterCard(frontCorners, backCorners, centerSymbols, "000");
        playerField.placeStarterCard(starterCard, true);

        // card placeable
        Map<CornerPosition, Corner> frontCorners1 = new HashMap<>();
        frontCorners1.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.TOP_RIGHT, new Corner(false, null));
        frontCorners1.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners1.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard1 = new ResourceCard(Symbol.FUNGI, frontCorners1, 0, "000");
        Position position1 = new Position(1,1);
        assertTrue(playerField.isCardPlaceableAt(position1));
        playerField.placeCard(playableCard1, true, position1);

        // card not placeable (corner not present)
        Position position2 = new Position(2,2);
        assertFalse(playerField.isCardPlaceableAt(position2));

        // card not placeable (odd position)
        Position position3 = new Position(0,1);
        assertFalse(playerField.isCardPlaceableAt(position3));

        // card not placeable (position already occupied)
        Position position4 = new Position(2,2);
        assertFalse(playerField.isCardPlaceableAt(position4));
    }

    @Test
    public void getSymbolNum() throws InvalidSymbolException {
        StarterCard starterCard = createStarterCard();
        playerField.placeStarterCard(starterCard, true);

        // Card 1
        Map<CornerPosition, Corner> frontCorners1 = new HashMap<>();
        frontCorners1.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.TOP_RIGHT, new Corner(false, null));
        frontCorners1.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners1.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard1 = new ResourceCard(Symbol.FUNGI, frontCorners1, 0, "000");
        Position position1 = new Position(1,3);
        playerField.placeCard(playableCard1, true, position1);

        // Card 2
        Map<CornerPosition, Corner> frontCorners2 = new HashMap<>();
        Map<Symbol, Integer> requirements = new HashMap<>();
        frontCorners2.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.EMPTY));
        frontCorners2.put(CornerPosition.TOP_RIGHT, new Corner(false, null));
        frontCorners2.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.MANUSCRIPT));
        frontCorners2.put(CornerPosition.BOTTOM_RIGHT, new Corner(false, null));
        requirements.put(Symbol.ANIMAL, 3);
        PlayableCard playableCard2 = new GoldCardInt(Symbol.ANIMAL, frontCorners2, requirements, 3, "000");
        Position position2 = new Position(-2,-1);
        playerField.placeCard(playableCard2, true, position2);

        // Card 3
        Map<CornerPosition, Corner> frontCorners3 = new HashMap<>();
        frontCorners3.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.EMPTY));
        frontCorners3.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.ANIMAL));
        frontCorners3.put(CornerPosition.BOTTOM_LEFT, new Corner(false, null));
        frontCorners3.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard3 = new ResourceCard(Symbol.ANIMAL, frontCorners3, 0, "000");
        Position position3 = new Position(-2,3);
        playerField.placeCard(playableCard3, true, position3);

        assertEquals(playerField.getSymbolNum(Symbol.ANIMAL), 4);
        assertEquals(playerField.getSymbolNum(Symbol.FUNGI), 3);
        assertEquals(playerField.getSymbolNum(Symbol.PLANT), 0);
        assertEquals(playerField.getSymbolNum(Symbol.INSECT), 0);
        assertEquals(playerField.getSymbolNum(Symbol.QUILL), 0);
        assertEquals(playerField.getSymbolNum(Symbol.EMPTY), 0);
        assertEquals(playerField.getSymbolNum(Symbol.INKWELL), 1);
        assertEquals(playerField.getSymbolNum(Symbol.MANUSCRIPT), 1);
    }

    @Test
    public void getCards() throws InvalidSymbolException {

        // StarterCard
        StarterCard starterCard = createStarterCard();
        playerField.placeStarterCard(starterCard, true);

        // Card 1 (resource)
        Map<CornerPosition, Corner> frontCorners1 = new HashMap<>();
        frontCorners1.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.TOP_RIGHT, new Corner(false, null));
        frontCorners1.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners1.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard1 = new ResourceCard(Symbol.FUNGI, frontCorners1, 0, "000");
        Position position1 = new Position(1,3);
        playerField.placeCard(playableCard1, true, position1);

        // Card 2 (gold)
        Map<CornerPosition, Corner> frontCorners2 = new HashMap<>();
        Map<Symbol, Integer> requirements = new HashMap<>();
        frontCorners2.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.EMPTY));
        frontCorners2.put(CornerPosition.TOP_RIGHT, new Corner(false, null));
        frontCorners2.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.MANUSCRIPT));
        frontCorners2.put(CornerPosition.BOTTOM_RIGHT, new Corner(false, null));
        requirements.put(Symbol.ANIMAL, 3);
        PlayableCard playableCard2 = new GoldCardInt(Symbol.ANIMAL, frontCorners2, requirements, 3, "000");
        Position position2 = new Position(-2,-1);
        playerField.placeCard(playableCard2, true, position2);

        // Card 3 (resource)
        PlayableCard playableCard3 = new ResourceCard(Symbol.ANIMAL, frontCorners1, 0, "000");
        Position position3 = new Position(-2,3);
        playerField.placeCard(playableCard3, true, position3);

        assertEquals(starterCard, playerField.getCards().get(0).getCard());
        assertEquals(playableCard1, playerField.getCards().get(1).getCard());
        assertEquals(playableCard2, playerField.getCards().get(2).getCard());
        assertEquals(playableCard3, playerField.getCards().get(3).getCard());
    }


    public StarterCard createStarterCard () throws InvalidSymbolException {
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
        return new StarterCard(frontCorners, backCorners, centerSymbols, "000");
    }
}