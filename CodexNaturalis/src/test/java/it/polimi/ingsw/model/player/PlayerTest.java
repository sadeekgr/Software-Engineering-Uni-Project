package it.polimi.ingsw.model.player;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.field.CardPlacement;
import it.polimi.ingsw.model.field.PlayerField;
import it.polimi.ingsw.model.field.Position;
import it.polimi.ingsw.model.game.ConnectionPlaceholder;
import it.polimi.ingsw.model.game.Match;

import it.polimi.ingsw.model.objective.DispositionObjective;
import it.polimi.ingsw.model.objective.Objective;
import it.polimi.ingsw.model.objective.SymbolObjective;
import it.polimi.ingsw.utilities.GsonSingleton;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class PlayerTest {

    private Match match;
    private Player player1;
    private Player player2;
    private PlayerField field;

    @Before
    public void setUp() throws JsonLoadException, InvalidSymbolException, PlayerExceptions {
        try {
            match = new Match(0);
            // Assign seats to players
            ConnectionPlaceholder conn1 = new ConnectionPlaceholder();
            ConnectionPlaceholder conn2 = new ConnectionPlaceholder();
            Controller c1 = conn1.getController();
            Controller c2 = conn2.getController();
            match.takeSeat(c1);
            player1 = c1.getPlayer();
            match.takeSeat(c2);
            player2 = c2.getPlayer();

            // Start the match in a new thread
            Thread matchThread = new Thread(() -> {
                try {
                    match.startMatch();
                } catch (Exception e) {
                    // Will throw
                }
            });
            matchThread.start();

            // Allow time for match setup before asking for starter cards
            Thread.sleep(300);

            // Players choose starter cards
            match.chooseStarterCardSide(player1, true);
            match.chooseStarterCardSide(player2, true);

            // Allow time for match setup before asking for objectives
            Thread.sleep(100);

            // Players choose objectives
            match.chooseObjective(player1, 0);
            match.chooseObjective(player2, 0);

            Thread.sleep(100); // Wait for the match thread to finish the setup
            matchThread.interrupt(); // stop the thread, is waiting for the game to end
        } catch (Exception e) {
            // Handle any exceptions by failing
            fail();
        }

        field = new PlayerField();
        Player curr = match.getCurrentPlayer();
        if (curr.equals(player2)) {
            player2 = player1;
            player1 = curr;
        }
        // set up players' field
        PlayableCard[] hand = new PlayableCard[3];
        Map<CornerPosition, Corner> corner = new HashMap<>();

        for (int i = 0; i < 2; i++){
            curr = match.getCurrentPlayer();
            hand[0] = new ResourceCard(Symbol.FUNGI, corner, 0, "000");
            curr.setHand(hand);
            curr.playCard(0, new Position(1,1), false);
            curr.drawResource();
        }
        for (int i = 0; i < 2; i++){
            curr = match.getCurrentPlayer();
            hand[0] = new ResourceCard(Symbol.FUNGI, corner, 0, "000");
            curr.setHand(hand);
            curr.playCard(0, new Position(2,2), false);
            curr.drawResource();
        }
        for (int i = 0; i < 2; i++) {
            curr = match.getCurrentPlayer();
            hand[0] = new ResourceCard(Symbol.PLANT, corner, 0, "000");
            curr.setHand(hand);
            curr.playCard(0, new Position(1,3), false);
            curr.drawResource();
        }
        for (int i = 0; i < 2; i++) {
            curr = match.getCurrentPlayer();
            hand[0] = new ResourceCard(Symbol.FUNGI, corner, 0, "000");
            curr.setHand(hand);
            curr.playCard(0, new Position(2,0), false);
            curr.drawResource();
        }
        for (int i = 0; i < 2; i++) {
            curr = match.getCurrentPlayer();
            hand[0] = new ResourceCard(Symbol.INSECT, corner, 0, "000");
            curr.setHand(hand);
            curr.playCard(0, new Position(3, -1), false);
            curr.drawResource();
        }
        for (int i = 0; i < 2; i++) {
            curr = match.getCurrentPlayer();
            hand[0] = new ResourceCard(Symbol.ANIMAL, corner, 0, "000");
            curr.setHand(hand);
            curr.playCard(0, new Position(4, -2), false);
            curr.drawResource();
        }
        for (int i = 0; i < 2; i++) {
            curr = match.getCurrentPlayer();
            hand[0] = new ResourceCard(Symbol.INSECT, corner, 0, "000");
            curr.setHand(hand);
            curr.playCard(0, new Position(0, 2), false);
            curr.drawResource();
        }
        for (int i = 0; i < 2; i++) {
            curr = match.getCurrentPlayer();
            hand[0] = new ResourceCard(Symbol.PLANT, corner, 0, "000");
            curr.setHand(hand);
            curr.playCard(0, new Position(3, 1), false);
            curr.drawResource();
        }
        for (int i = 0; i < 2; i++) {
            curr = match.getCurrentPlayer();
            hand[0] = new ResourceCard(Symbol.INSECT, corner, 0, "000");
            curr.setHand(hand);
            curr.playCard(0, new Position(-1, 1), false);
            curr.drawResource();
        }
        for (int i = 0; i < 2; i++) {
            curr = match.getCurrentPlayer();
            hand[0] = new ResourceCard(Symbol.INSECT, corner, 0, "000");
            curr.setHand(hand);
            curr.playCard(0, new Position(5, -3), false);
            curr.drawResource();
        }
        Map<CornerPosition, Corner> corner1 = new HashMap<>();
        for(CornerPosition p : CornerPosition.values()){
            corner1.put(p, new Corner(true, Symbol.QUILL));
        }
        for (int i = 0; i < 2; i++) {
            curr = match.getCurrentPlayer();
            hand[0] = new ResourceCard(Symbol.ANIMAL, corner1, 0, "000");
            curr.setHand(hand);
            curr.playCard(0, new Position(-1, 3), true);
            curr.drawResource();
        }

        Map<CornerPosition, Corner> corner2 = new HashMap<>();
        for(CornerPosition p : CornerPosition.values()){
            corner2.put(p, new Corner(true, Symbol.MANUSCRIPT));
        }
        for (int i = 0; i < 2; i++) {
            curr = match.getCurrentPlayer();
            hand[0] = new ResourceCard(Symbol.ANIMAL, corner2, 0, "000");
            curr.setHand(hand);
            curr.playCard(0, new Position(-2, 4), true);
            curr.drawResource();
        }
    }

    @After
    public void tearDown() {
        match = null;
        player1 = null;
        player2 = null;
        field = null;
    }

    @Test
    public void chooseStarterCardSide(){
        try {
            match = new Match(0);
            // Assign seats to players
            ConnectionPlaceholder conn1 = new ConnectionPlaceholder();
            ConnectionPlaceholder conn2 = new ConnectionPlaceholder();
            Controller c1 = conn1.getController();
            Controller c2 = conn2.getController();
            match.takeSeat(c1);
            player1 = c1.getPlayer();
            match.takeSeat(c2);
            player2 = c2.getPlayer();

            // Start the match in a new thread
            Thread matchThread = new Thread(() -> {
                try {
                    match.startMatch();
                } catch (Exception e) {
                    // Will throw
                }
            });
            matchThread.start();

            // Allow time for match setup before asking for starter cards
            Thread.sleep(100);

            // Players choose starter cards
            match.chooseStarterCardSide(player1, true);
            match.chooseStarterCardSide(player2, false);

            // Allow time for match setup before asking for objectives
            Thread.sleep(100);

            // Players choose objectives
            match.chooseObjective(player1, 0);
            match.chooseObjective(player2, 0);

            Thread.sleep(100); // Wait for the match thread to finish the setup
            matchThread.interrupt(); // stop the thread, is waiting for the game to end
        } catch (Exception e) {
            // Handle any exceptions by failing gracefully
            fail();
        }

        assertTrue(player1.getPlayedCards().getFirst().isFront());
        assertFalse(player2.getPlayedCards().getFirst().isFront());
    }

    @Test
    public void chooseObjective(){
        try {
            match = new Match(0);
            // Assign seats to players
            ConnectionPlaceholder conn1 = new ConnectionPlaceholder();
            ConnectionPlaceholder conn2 = new ConnectionPlaceholder();
            Controller c1 = conn1.getController();
            Controller c2 = conn2.getController();
            match.takeSeat(c1);
            player1 = c1.getPlayer();
            match.takeSeat(c2);
            player2 = c2.getPlayer();

            // Start the match in a new thread
            Thread matchThread = new Thread(() -> {
                try {
                    match.startMatch();
                } catch (Exception e) {
                    // Will throw
                }
            });
            matchThread.start();

            // Allow time for match setup before asking for starter cards
            Thread.sleep(100);

            // Players choose starter cards
            match.chooseStarterCardSide(player1, true);
            match.chooseStarterCardSide(player2, false);

            // Allow time for match setup before asking for objectives
            Thread.sleep(100);

            // Players choose objectives
            match.chooseObjective(player1, 0);
            match.chooseObjective(player2, 1);

            Thread.sleep(100); // Wait for the match thread to finish the setup
            matchThread.interrupt(); // stop the thread, is waiting for the game to end
        } catch (Exception e) {
            // Handle any exceptions by failing gracefully
            fail();
        }

        assertNotNull(player1.getObjective());
        assertNotNull(player2.getObjective());
    }

    @Test
    public void getHand() throws InvalidSymbolException {
        Player curr = match.getCurrentPlayer();
        PlayableCard[] hand = new PlayableCard[3];
        Map<CornerPosition, Corner> frontCorners1 = new HashMap<>();
        frontCorners1.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners1.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard1 = new ResourceCard(Symbol.FUNGI, frontCorners1, 0, "000");

        Map<CornerPosition, Corner> frontCorners2 = new HashMap<>();
        frontCorners2.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners2.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.INKWELL));
        frontCorners2.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners2.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard2 = new ResourceCard(Symbol.FUNGI, frontCorners2, 0, "000");

        Map<CornerPosition, Corner> frontCorners3 = new HashMap<>();
        frontCorners3.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners3.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.INKWELL));
        frontCorners3.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners3.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard3 = new ResourceCard(Symbol.FUNGI, frontCorners3, 0, "000");


        hand[0] = playableCard1;
        hand[1] = playableCard2;
        hand[2] = playableCard3;
        curr.setHand(hand);

        PlayableCard[] tmp;
        tmp = curr.getHand();

        assertEquals(tmp[0], playableCard1);
        assertEquals(tmp[1], playableCard2);
        assertEquals(tmp[2], playableCard3);
    }

    @Test
    public void getPlayedCards() throws InvalidSymbolException, PlayerExceptions {
        Player curr = match.getCurrentPlayer();
        Map<CornerPosition, Corner> frontCorners1 = new HashMap<>();
        frontCorners1.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners1.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard1 = new ResourceCard(Symbol.FUNGI, frontCorners1, 0, "000");

        PlayableCard[] hand = new PlayableCard[1];

        hand[0] = playableCard1;

        curr.setHand(hand);

        curr.playCard(0, new Position(4,0), true);
        List<CardPlacement> tmp = curr.getPlayedCards();
        assertEquals(tmp.getLast().getCard(), playableCard1);
    }

    @Test
    public void placeStarterCard() throws InvalidSymbolException {
        //Player curr = match.getCurrentPlayer();
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

        assertEquals(starterCard, field.getCards().getFirst().getCard());
    }

    @Test
    public void playCard() throws InvalidSymbolException, PlayerExceptions {
        Player curr = match.getCurrentPlayer();
        Player tmp;
        if (player1.equals(curr)){
            tmp = player2;
        } else {
            tmp = player1;
        }
        Map<CornerPosition, Corner> frontCorners1 = new HashMap<>();
        frontCorners1.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners1.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard1 = new ResourceCard(Symbol.FUNGI, frontCorners1, 0, "000");
        PlayableCard[] hand = new PlayableCard[1];
        hand[0] = playableCard1;
        curr.setHand(hand);
        // test if the InvalidPositionException is launched
        Player finalCurr2 = curr;
        assertThrows(PlayerExceptions.class, ()-> finalCurr2.playCard(0, new Position(1,1), true));
        curr.playCard(0, new Position(4,0), true);

        assertEquals(playableCard1, curr.getPlayedCards().getLast().getCard());

        // curr already played the card above!!!
        Player finalCurr1 = curr;
        assertThrows(PlayerExceptions.class, ()-> finalCurr1.playCard(0, new Position(2,3), true));
        // // test if the NotYourTurnException is launched
        Player finalTmp = tmp;
        assertThrows(PlayerExceptions.class, ()-> finalTmp.playCard(0, new Position(2,2), true));

        // test if the InvalidHandIndexException is launched
        match.nextTurn();
        curr = match.getCurrentPlayer();
        Player finalCurr = curr;
        assertThrows(PlayerExceptions.class, ()-> finalCurr.playCard(7, new Position(2,3), true));

        Map<CornerPosition, Corner> frontCorners2 = new HashMap<>();
        frontCorners2.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners2.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.INKWELL));
        frontCorners2.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners2.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));

        // test if the RequirementsNotFulfilledException is launched
        Map<Symbol, Integer> requirements = new HashMap<>();
        requirements.put(Symbol.PLANT, 5);
        PlayableCard playableCard2 = new GoldCardInt(Symbol.FUNGI, frontCorners2, requirements, 2, "000");
        PlayableCard[] hand2 = new PlayableCard[1];
        hand2[0] = playableCard2;
        curr.setHand(hand2);
        Player finalCurr3 = curr;
        assertThrows(PlayerExceptions.class, ()-> finalCurr3.playCard(0, new Position(1,1), true));

    }

    @Test
    public void checkDrawCondition() throws InvalidSymbolException {
        Player curr = match.getCurrentPlayer();
        Player tmp;
        if (player1.equals(curr)){
            tmp = player2;
        } else {
            tmp = player1;
        }

        PlayableCard[] hand = new PlayableCard[3];
        Map<CornerPosition, Corner> frontCorners1 = new HashMap<>();
        frontCorners1.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners1.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard1 = new ResourceCard(Symbol.FUNGI, frontCorners1, 0, "000");

        Map<CornerPosition, Corner> frontCorners2 = new HashMap<>();
        frontCorners2.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners2.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.INKWELL));
        frontCorners2.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners2.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard2 = new ResourceCard(Symbol.FUNGI, frontCorners2, 0, "000");

        Map<CornerPosition, Corner> frontCorners3 = new HashMap<>();
        frontCorners3.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners3.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.INKWELL));
        frontCorners3.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners3.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard3 = new ResourceCard(Symbol.FUNGI, frontCorners3, 0, "000");

        hand[0] = playableCard1;
        hand[1] = playableCard2;
        hand[2] = playableCard3;
        curr.setHand(hand);

        Player finalTmp = tmp;
        assertThrows(PlayerExceptions.class, finalTmp::drawResource);
        assertThrows(PlayerExceptions.class, curr::drawResource);
    }

    @Test
    public void drawMarket() throws InvalidSymbolException, PlayerExceptions {
        Player curr = match.getCurrentPlayer();
        PlayableCard[] hand = new PlayableCard[3];
        PlayableCard[] hand2 = new PlayableCard[3];
        Map<CornerPosition, Corner> frontCorners1 = new HashMap<>();
        frontCorners1.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners1.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard1 = new ResourceCard(Symbol.FUNGI, frontCorners1, 0, "000");

        Map<CornerPosition, Corner> frontCorners2 = new HashMap<>();
        frontCorners2.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners2.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.INKWELL));
        frontCorners2.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners2.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard2 = new ResourceCard(Symbol.FUNGI, frontCorners2, 0, "000");

        Map<CornerPosition, Corner> frontCorners3 = new HashMap<>();
        frontCorners3.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners3.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.INKWELL));
        frontCorners3.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners3.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard3 = new ResourceCard(Symbol.FUNGI, frontCorners3, 0, "000");


        hand[0] = playableCard1;
        hand[1] = playableCard2;
        hand[2] = playableCard3;

        hand2[0] = playableCard1;
        hand2[1] = playableCard2;
        hand2[2] = playableCard3;

        curr.setHand(hand);

        curr.playCard(2, new Position(4,0), true);
        curr.drawMarket(2);

        assertNotNull(curr.getHand()[2]);
        assertTrue(curr.getHand()[2] instanceof GoldCard);

        curr = match.getCurrentPlayer();
        curr.setHand(hand2);
        curr.playCard(2, new Position(4,0), true);
        curr.drawMarket(0);

        assertNotNull(curr.getHand()[0]);
        assertTrue(curr.getHand()[0] instanceof ResourceCard);

    }

    @Test
    public void drawGold() throws InvalidSymbolException, PlayerExceptions {
        Player curr = match.getCurrentPlayer();
        PlayableCard[] hand = new PlayableCard[3];
        Map<CornerPosition, Corner> frontCorners1 = new HashMap<>();
        frontCorners1.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners1.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard1 = new ResourceCard(Symbol.FUNGI, frontCorners1, 0, "000");

        Map<CornerPosition, Corner> frontCorners2 = new HashMap<>();
        frontCorners2.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners2.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.INKWELL));
        frontCorners2.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners2.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard2 = new ResourceCard(Symbol.FUNGI, frontCorners2, 0, "000");

        Map<CornerPosition, Corner> frontCorners3 = new HashMap<>();
        frontCorners3.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners3.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.INKWELL));
        frontCorners3.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners3.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard3 = new ResourceCard(Symbol.FUNGI, frontCorners3, 0, "000");

        hand[0] = playableCard1;
        hand[1] = playableCard2;
        hand[2] = playableCard3;

        curr.setHand(hand);

        curr.playCard(2, new Position(4,0), true);
        curr.drawGold();

        assertNotNull(curr.getHand()[2]);
        assertTrue(curr.getHand()[2] instanceof GoldCard);
    }

    @Test
    public void drawResource() throws InvalidSymbolException, PlayerExceptions {
        Player curr = match.getCurrentPlayer();
        PlayableCard[] hand = new PlayableCard[3];
        Map<CornerPosition, Corner> frontCorners1 = new HashMap<>();
        frontCorners1.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners1.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard1 = new ResourceCard(Symbol.FUNGI, frontCorners1, 0, "000");

        Map<CornerPosition, Corner> frontCorners2 = new HashMap<>();
        frontCorners2.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners2.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.INKWELL));
        frontCorners2.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners2.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard2 = new ResourceCard(Symbol.FUNGI, frontCorners2, 0, "000");

        Map<CornerPosition, Corner> frontCorners3 = new HashMap<>();
        frontCorners3.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners3.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.INKWELL));
        frontCorners3.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners3.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard3 = new ResourceCard(Symbol.FUNGI, frontCorners3, 0, "000");

        hand[0] = playableCard1;
        hand[1] = playableCard2;
        hand[2] = playableCard3;

        curr.setHand(hand);

        curr.playCard(2, new Position(4,0), true);
        curr.drawResource();

        assertNotNull(curr.getHand()[2]);
        assertTrue(curr.getHand()[2] instanceof ResourceCard);
    }

    @Test
    public void setHand() throws InvalidSymbolException {
        Player curr = match.getCurrentPlayer();
        PlayableCard[] hand = new PlayableCard[3];
        Map<CornerPosition, Corner> frontCorners1 = new HashMap<>();
        frontCorners1.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.EMPTY));
        frontCorners1.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners1.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard1 = new ResourceCard(Symbol.FUNGI, frontCorners1, 0, "000");

        Map<CornerPosition, Corner> frontCorners2 = new HashMap<>();
        frontCorners2.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners2.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.PLANT));
        frontCorners2.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.EMPTY));
        frontCorners2.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.EMPTY));
        PlayableCard playableCard2 = new ResourceCard(Symbol.FUNGI, frontCorners2, 0, "000");

        Map<CornerPosition, Corner> frontCorners3 = new HashMap<>();
        frontCorners3.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.EMPTY));
        frontCorners3.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.FUNGI));
        frontCorners3.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.EMPTY));
        frontCorners3.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard3 = new ResourceCard(Symbol.FUNGI, frontCorners3, 0, "000");

        hand[0] = playableCard1;
        hand[1] = playableCard2;
        hand[2] = playableCard3;

        curr.setHand(hand);

        assertNotNull(hand[0]);
        assertNotNull(hand[1]);
        assertNotNull(hand[2]);
        assertNotEquals(hand[0], hand[1]);
        assertNotEquals(hand[1], hand[2]);
        assertEquals(hand[0], playableCard1);
        assertEquals(hand[1], playableCard2);
        assertEquals(hand[2], playableCard3);
    }

    @Test
    public void setPlayerColor(){
        Player curr = match.getCurrentPlayer();
        curr.setPlayerColor(PlayerColor.YELLOW);

        assertNotNull(curr.getColor());
        assertEquals(curr.getColor(), PlayerColor.YELLOW);
    }

    @Test
    public void setObjective() throws JsonLoadException {
        Player curr = match.getCurrentPlayer();
        List<Objective> objectives = GsonSingleton.loadJson("/objectiveCards.json", new TypeToken<List<Objective>>() {}.getType());
        for(Objective objective : objectives) {

            curr.setObjective(objective);

            assertEquals(objective, curr.getObjective());
        }
    }

    @Test
    public void getColor() {
        Player curr = match.getCurrentPlayer();
        curr.setPlayerColor(PlayerColor.RED);

        assertEquals(curr.getColor(), PlayerColor.RED);
    }

    @Test
    public void getPlayerField() {

    }


    @Test
    public void getObjective() throws JsonLoadException {
        List<Objective> objectives = GsonSingleton.loadJson("/objectiveCards.json", new TypeToken<List<Objective>>() {}.getType());
        boolean validObjective = false;
        for (Objective objective : objectives){
            if (player1.getObjective() instanceof DispositionObjective dispositionObjectivePlayer && objective instanceof DispositionObjective dispositionObjective){
                Symbol[] kingdoms = dispositionObjective.getPatternKingdom();
                Symbol[] kingdomsPlayer = dispositionObjectivePlayer.getPatternKingdom();
                boolean validKingdoms = true;
                for (int i = 0; i < kingdomsPlayer.length; i++) {
                    if (!kingdoms[i].equals(kingdomsPlayer[i])){
                        validKingdoms = false;
                        break;
                    }
                }
                if (validKingdoms){
                    Position[] positions = dispositionObjective.getPatternPosition();
                    Position[] positionsPlayer = dispositionObjectivePlayer.getPatternPosition();
                    boolean validPositions = true;
                    for (int i = 0; i < positions.length; i++) {
                        if (!(positions[i].x() == positionsPlayer[i].x() && positions[i].y() == positionsPlayer[i].y())){
                            validPositions = false;
                            break;
                        }
                    }
                    if (validPositions){
                        validObjective = true;
                        break;
                    }
                }
            }
            else if (objective instanceof SymbolObjective symbolObjective && player1.getObjective() instanceof SymbolObjective symbolObjectivePlayer){
                Map<Symbol, Integer> symbols = symbolObjective.getSymbols();
                Map<Symbol, Integer> symbolsPlayer = symbolObjectivePlayer.getSymbols();
                boolean validSymbols = false;
                for (Symbol symbol : symbols.keySet()){
                    if (Objects.equals(symbols.get(symbol), symbolsPlayer.get(symbol))){
                        validSymbols = true;
                        break;
                    }
                }
                if (validSymbols){
                    validObjective = true;
                    break;
                }
            }
        }
        assertTrue(validObjective);
    }

    @Test
    public void calculatePersonalObjectiveScore() {
        Objective personalObjective = player1.getObjective();
        assertEquals(player1.calculatePersonalObjectiveScore(), player1.calculateObjectiveScore(personalObjective));
    }

    @Test
    public void calculateObjectiveScore() {
        DispositionObjective obj = new DispositionObjective(
                new Position[]{
                        new Position(-1, -1),
                        new Position(-2, -2)
                },
                new Symbol[]{
                        Symbol.FUNGI,
                        Symbol.FUNGI,
                        Symbol.FUNGI
                },
                3,
                "000"
        );

        assertEquals(0, player1.calculateObjectiveScore(obj));
    }

    @Test
    public void secondConstructor() throws InvalidSymbolException {
        Player curr = match.getCurrentPlayer();
        PlayableCard[] hand = new PlayableCard[3];
        Map<CornerPosition, Corner> frontCorners1 = new HashMap<>();
        frontCorners1.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.EMPTY));
        frontCorners1.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.INKWELL));
        frontCorners1.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.FUNGI));
        frontCorners1.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard1 = new ResourceCard(Symbol.FUNGI, frontCorners1, 0, "000");

        Map<CornerPosition, Corner> frontCorners2 = new HashMap<>();
        frontCorners2.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.INKWELL));
        frontCorners2.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.PLANT));
        frontCorners2.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.EMPTY));
        frontCorners2.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.EMPTY));
        PlayableCard playableCard2 = new ResourceCard(Symbol.FUNGI, frontCorners2, 0, "000");

        Map<CornerPosition, Corner> frontCorners3 = new HashMap<>();
        frontCorners3.put(CornerPosition.TOP_LEFT, new Corner(true, Symbol.EMPTY));
        frontCorners3.put(CornerPosition.TOP_RIGHT, new Corner(true, Symbol.FUNGI));
        frontCorners3.put(CornerPosition.BOTTOM_LEFT, new Corner(true, Symbol.EMPTY));
        frontCorners3.put(CornerPosition.BOTTOM_RIGHT, new Corner(true, Symbol.ANIMAL));
        PlayableCard playableCard3 = new ResourceCard(Symbol.FUNGI, frontCorners3, 0, "000");

        hand[0] = playableCard1;
        hand[1] = playableCard2;
        hand[2] = playableCard3;

        Objective objective = new DispositionObjective(
                new Position[]{
                        new Position(-1, -1),
                        new Position(-2, -2)
                },
                new Symbol[]{
                        Symbol.FUNGI,
                        Symbol.FUNGI,
                        Symbol.FUNGI
                },
                3,
                "000"
        );

        new Player(match, hand, new PlayerField(), objective, PlayerColor.RED);
    }
}