package it.polimi.ingsw.model.game;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.field.CardPlacement;
import it.polimi.ingsw.model.objective.Objective;
import it.polimi.ingsw.model.player.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MatchTest {
    private Match match;
    private Player player1;
    private Player player2;

    @Before
    public void setUp() throws Exception {
        match = new Match(0);
    }

    @After
    public void tearDown() throws Exception {
        match = null;
        player1 = null;
        player2 = null;
    }

    private void init() {
        try {
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
            System.out.println(e.getMessage());
            // Handle any exceptions by failing gracefully
            fail();
        }
    }

    @Test
    public void takeSeat() {
        try {
            assertTrue(match.getPlayers().isEmpty());

            ConnectionPlaceholder conn1 = new ConnectionPlaceholder();
            ConnectionPlaceholder conn2 = new ConnectionPlaceholder();
            Controller c1 = conn1.getController();
            Controller c2 = conn2.getController();
            match.takeSeat(c1);
            player1 = c1.getPlayer();
            match.takeSeat(c2);
            player2 = c2.getPlayer();

            assertEquals(2, match.getPlayers().size());
            assertTrue(match.getPlayers().contains(player1));
            assertTrue(match.getPlayers().contains(player2));
        } catch (MatchExceptions e) {
            fail();
        }
    }

    @Test
    public void leaveSeat() {
        try {
            assertEquals(0, match.getPlayers().size());

            ConnectionPlaceholder conn1 = new ConnectionPlaceholder();
            ConnectionPlaceholder conn2 = new ConnectionPlaceholder();
            Controller c1 = conn1.getController();
            Controller c2 = conn2.getController();
            match.takeSeat(c1);
            player1 = c1.getPlayer();
            match.takeSeat(c2);
            player2 = c2.getPlayer();

            assertEquals(2, match.getPlayers().size());
            assertTrue(match.getPlayers().contains(player1));
            assertTrue(match.getPlayers().contains(player2));
            match.leaveSeat(c1);
            assertEquals(1, match.getPlayers().size());
            assertFalse(match.getPlayers().contains(player1));
            assertTrue(match.getPlayers().contains(player2));

        } catch (MatchExceptions e) { //matchfullexception è di takeseat
            fail();
        }
    }

    @Test
    public void chooseStarterCard() {
        try {
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

            assertThrows(PlayerExceptions.class, ()->match.chooseStarterCardSide(player1, true));

            matchThread.start();

            // Allow time for match setup before asking for starter cards
            Thread.sleep(100);

            // Players choose starter cards
            match.chooseStarterCardSide(player1, true);
            assertThrows(PlayerExceptions.class, ()->match.chooseStarterCardSide(player1, true));

            match.chooseStarterCardSide(player2, true);
            assertThrows(PlayerExceptions.class, ()->match.chooseStarterCardSide(player1, true));

            // Allow time for match setup before asking for objectives
            Thread.sleep(100);

            // Players choose objectives
            match.chooseObjective(player1, 0);
            match.chooseObjective(player2, 0);

            Thread.sleep(100); // Wait for the match thread to finish the setup
            matchThread.interrupt(); // stop the thread, is waiting for the game to end
        } catch (Exception e) {
            System.out.println(e.getMessage());
            // Handle any exceptions by failing gracefully
            fail();
        }
    }

    @Test
    public void chooseObjective(){
        try {
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

            assertThrows(PlayerExceptions.class, ()->match.chooseObjective(player1, 0));

            matchThread.start();

            // Allow time for match setup before asking for starter cards
            Thread.sleep(100);

            // Players choose starter cards
            match.chooseStarterCardSide(player1, true);
            assertThrows(PlayerExceptions.class, ()->match.chooseObjective(player1, 0));
            match.chooseStarterCardSide(player2, true);

            // Allow time for match setup before asking for objectives
            Thread.sleep(100);

            // Players choose objectives
            match.chooseObjective(player1, 0);
            assertThrows(PlayerExceptions.class, ()->match.chooseObjective(player1, 0));
            match.chooseObjective(player2, 0);
            assertThrows(PlayerExceptions.class, ()->match.chooseObjective(player1, 0));

            Thread.sleep(100); // Wait for the match thread to finish the setup
            matchThread.interrupt(); // stop the thread, is waiting for the game to end
        } catch (Exception e) {
            // Handle any exceptions by failing gracefully
            fail();
        }
    }

    @Test
    public void startMatch() throws PlayerExceptions {
        init();

        ArrayList<Player> players = match.getPlayers();
        assertEquals(2, players.size());
        assertTrue(players.contains(player1) && players.contains(player2));
        assertTrue(match.getStartingPlayer().equals(player1) || match.getStartingPlayer().equals(player2));
        assertEquals(match.getStartingPlayer(), match.getCurrentPlayer());

        ScoreTrack scores = match.getScoreTrack();
        for(Player p : players){
            assertEquals(0, scores.getPlayerScore(p));
        }

        PlayableCard[] market = match.getMarket();
        for(int i = 0; i < 4; i++){
            assertNotNull(market[i]);
            if(i < 2){
                assertTrue(market[i] instanceof ResourceCard);
            } else {
                assertTrue(market[i] instanceof GoldCard);
            }
        }

        for (int i = 0; i < 36; i++) {
            PlayableCard goldCard = match.drawGold();
            assertNotNull(goldCard);
            assertTrue(goldCard instanceof GoldCard);
        }

        assertThrows(PlayerExceptions.class, ()->match.drawGold());

        for (int i = 0; i < 34; i++) {
            PlayableCard resourceCard = match.drawResource();
            assertNotNull(resourceCard);
            assertTrue(resourceCard instanceof ResourceCard);
        }

        assertThrows(PlayerExceptions.class, ()->match.drawResource());

        Objective[] objectives = match.getObjectives();
        for(Objective o : objectives){
            assertNotNull(o);
        }

        assertNotNull(player1.getObjective());
        for(Player p : players){
            assertNotNull(p.getObjective());
            assertNotNull(p.getColor());
            List<CardPlacement> cards = p.getPlayedCards();
            assertEquals(1, cards.size());
            Card card = cards.getFirst().getCard();
            assertTrue(card instanceof StarterCard);

            PlayableCard[] hand = p.getHand();
            int res = 0;
            int gold = 0;
            for(PlayableCard c : hand) {
                assertNotNull(c);
                if(c instanceof ResourceCard){
                    res += 1;
                } else if (c instanceof GoldCard) {
                    gold += 1;
                } else {
                    fail();
                }
            }
            assertTrue(res == 2 && gold == 1);
        }
    }

    @Test
    public void getPlayers() {
        try {
            ConnectionPlaceholder conn1 = new ConnectionPlaceholder();
            ConnectionPlaceholder conn2 = new ConnectionPlaceholder();
            Controller c1 = conn1.getController();
            Controller c2 = conn2.getController();
            match.takeSeat(c1);
            player1 = c1.getPlayer();
            match.takeSeat(c2);
            player2 = c2.getPlayer();
        } catch (MatchExceptions e) {
            fail();
        }
        assertNotNull(match.getPlayers());
        assertEquals(2, match.getPlayers().size());
    }

    @Test
    public void getScoreTrack() {
        init();

        Player curr = match.getStartingPlayer();
        if(curr.equals(player1)) {
            match.updateScoreTrack(22);
            match.nextTurn();
            match.updateScoreTrack(15);
        } else {
            match.updateScoreTrack(15);
            match.nextTurn();
            match.updateScoreTrack(22);
        }

        assertNotNull(match.getScoreTrack());
        assertEquals(22, match.getScoreTrack().getPlayerScore(player1));
    }

    @Test
    public void getStartingPlayer() {
        init();

        Player first = match.getStartingPlayer();
        assertNotNull(first);
        assertTrue(player1.equals(first) | player2.equals(first));

    }

    @Test
    public void getCurrentPlayer() {
        init();

        Player first = match.getStartingPlayer();
        match.nextTurn();
        Player second = match.getCurrentPlayer();

        assertNotNull(second);

        if(player1.equals(first)) {

            assertEquals(player2, second);
        } else {
            assertEquals(player1, second);
        }
    }

    @Test
    public void getTopGoldKingdom() throws PlayerExceptions {
        init();

       for(int i = 0; i<10; i++) {
            Symbol s = match.getTopGoldKingdom();
            Symbol x = match.drawGold().getKingdom();
            assertEquals(s,x);
        }

    }

    @Test
    public void getTopResourceKingdom() throws PlayerExceptions {
        init();

        for(int i = 0; i<10; i++) {
            Symbol s = match.getTopResourceKingdom();
            Symbol x = match.drawResource().getKingdom();
            assertEquals(s,x);
        }

    }

    @Test
    public void drawGold() throws PlayerExceptions {
        init();

        for (int i = 0; i < 36; i++) {
            PlayableCard goldCard = match.drawGold();
            assertNotNull(goldCard);
            assertTrue(goldCard instanceof GoldCard);
        }

        assertThrows(PlayerExceptions.class, ()->match.drawGold());
    }

    @Test
    public void drawResource() throws PlayerExceptions {
        init();

        for (int i = 0; i < 34; i++) {
            PlayableCard resourceCard = match.drawResource();
            assertNotNull(resourceCard);
            assertTrue(resourceCard instanceof ResourceCard);
        }

        assertThrows(PlayerExceptions.class, ()->match.drawResource());
    }

    @Test
    public void drawMarket() throws PlayerExceptions {
        init();

        assertThrows(ArrayIndexOutOfBoundsException.class, ()->match.drawMarket(-1));

        for (int i = 0; i < 37; i++){
            PlayableCard playableCard = match.drawMarket(2);
            assertNotNull(playableCard);
            assertTrue(playableCard instanceof GoldCard);
        }
        assertThrows(PlayerExceptions.class, ()->match.drawMarket(2));

        for (int i = 0; i < 35; i++){
            PlayableCard playableCard = match.drawMarket(0);
            assertNotNull(playableCard);
            assertTrue(playableCard instanceof ResourceCard);
        }
        assertThrows(PlayerExceptions.class, ()->match.drawMarket(0));
    }

    @Test
    public void updateScoreTrack() {
        init();

        int previousScore;
        int addScore;

        Player curr = match.getStartingPlayer();
        previousScore = match.getScoreTrack().getPlayerScore(curr);
        addScore = 2;
        match.updateScoreTrack(addScore);
        assertEquals(match.getScoreTrack().getPlayerScore(curr), previousScore+addScore);
        match.nextTurn();
        curr = match.getCurrentPlayer();
        previousScore = match.getScoreTrack().getPlayerScore(curr);
        addScore = 7;
        match.updateScoreTrack(addScore);
        assertEquals(match.getScoreTrack().getPlayerScore(curr), previousScore+addScore);
        match.nextTurn();
        curr = match.getCurrentPlayer();
        previousScore = match.getScoreTrack().getPlayerScore(curr);
        addScore = 4;
        match.updateScoreTrack(addScore);
        assertEquals(match.getScoreTrack().getPlayerScore(curr), previousScore+addScore);
    }

    @Test
    public void endConditionMet() throws JsonLoadException, PlayerExceptions {
        init();

        for (int i = 0; i < 36; i++){
            match.drawGold();
        }
        for (int i = 0; i < 34; i++){
            match.drawResource();
        }
        assertTrue(match.endConditionMet());

        match = new Match(0);
        init();

        int addScore = 20;
        match.updateScoreTrack(addScore);
        assertTrue(match.getScoreTrack().isFinished());
        assertTrue(match.endConditionMet());
    }


    @Test
    public void nextTurn() {
        try {
            // Assign seats to players
            ConnectionPlaceholder conn1 = new ConnectionPlaceholder();
            ConnectionPlaceholder conn2 = new ConnectionPlaceholder();
            ConnectionPlaceholder conn3 = new ConnectionPlaceholder();
            ConnectionPlaceholder conn4 = new ConnectionPlaceholder();
            Controller c1 = conn1.getController();
            Controller c2 = conn2.getController();
            Controller c3 = conn3.getController();
            Controller c4 = conn4.getController();
            match.takeSeat(c1);
            player1 = c1.getPlayer();
            match.takeSeat(c2);
            player2 = c2.getPlayer();
            match.takeSeat(c3);
            Player player3 = c3.getPlayer();
            match.takeSeat(c4);
            Player player4 = c4.getPlayer();

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
            match.chooseStarterCardSide(player2, true);
            match.chooseStarterCardSide(player3, true);
            match.chooseStarterCardSide(player4, true);

            // Allow time for match setup before asking for objectives
            Thread.sleep(100);

            // Players choose objectives
            match.chooseObjective(player1, 0);
            match.chooseObjective(player2, 0);
            match.chooseObjective(player3, 0);
            match.chooseObjective(player4, 0);

            Thread.sleep(100); // Wait for the match thread to finish the setup
            matchThread.interrupt(); // stop the thread, is waiting for the game to end
        } catch (Exception e) {
            fail();
        }

        Player first_player = match.getStartingPlayer();
        match.nextTurn();
        Player second_player = match.getCurrentPlayer();
        match.nextTurn();
        Player third_player = match.getCurrentPlayer();
        match.nextTurn();
        Player fourth_player = match.getCurrentPlayer();
        match.nextTurn();
        for (int i = 0; i < 24; i++){
            if (i%4 == 0){
                assertEquals(match.getCurrentPlayer(), first_player);
            }
            if (i%4 == 1){
                assertEquals(match.getCurrentPlayer(), second_player);
            }
            if (i%4 == 2){
                assertEquals(match.getCurrentPlayer(), third_player);
            }
            if (i%4 == 3){
                assertEquals(match.getCurrentPlayer(), fourth_player);
            }
            match.nextTurn();
        }
    }

    @Test
    public void getNextPlayer() {
        init();

        if (!match.getCurrentPlayer().equals(player1)){
            assertEquals(player1, match.getNextPlayer());
        } else {
            assertEquals(player2, match.getNextPlayer());
        }
    }

    @Test
    public void getObjectives() {
        init();

        Objective[] objectives = match.getObjectives();
        for(Objective o : objectives){
            assertNotNull(o);
        }
        assertNotEquals(objectives[0], objectives[1]);

        Objective[] objectives2 = match.getObjectives();
        assertArrayEquals(objectives, objectives2);
    }

    @Test
    public void getMarket() {
        init();

        PlayableCard[] market = match.getMarket();
        for(int i = 0; i < 4; i++){
            try {
                assertEquals(match.drawMarket(i), market[i]);
            } catch (Exception e){
                fail();
            }
        }
    }

    @Test
    public void endMatch() throws MatchExceptions, InterruptedException, PlayerExceptions {
        // normal end
        init();
        match.endMatch();

        // disconnection
        match = new Match(0);
        init();
        match.endForDisconnection();

        // disconnection during setUp
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
        match.chooseStarterCardSide(player2, true);

        // Allow time for match setup before asking for objectives
        Thread.sleep(100);

        match.endForDisconnection();
    }

    @Test
    public void lastRound() {
        init();

        assertFalse(match.lastRound());
        match.updateScoreTrack(15);

        match.nextTurn();
        assertFalse(match.lastRound());

        match.updateScoreTrack(22);
        match.nextTurn();
        assertTrue(match.lastRound());
    }

    @Test
    public void load() {
        match = new Match(0);

        // Assign seats to players
        ConnectionPlaceholder conn1 = new ConnectionPlaceholder();
        ConnectionPlaceholder conn2 = new ConnectionPlaceholder();
        Controller c1 = conn1.getController();
        Controller c2 = conn2.getController();
        List<Controller> controllers = new ArrayList<>();
        controllers.add(c1);
        controllers.add(c2);

        //invalid path
        assertThrows(IOException.class, () -> match.load(controllers));
    }

    @Test
    public void resumeGame() throws InterruptedException {
        // simulate the load starting a real game
        init();

        // Start the match in a new thread
        Thread matchThread = new Thread(() -> {
            try {
                match.resumeGame();
            } catch (Exception e) {
                // Will throw
            }
        });
        matchThread.start();

        Thread.sleep(100);
        match.endMatch();
    }
}