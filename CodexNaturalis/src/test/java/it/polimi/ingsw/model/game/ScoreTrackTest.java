package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.player.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ScoreTrackTest {

    private ScoreTrack scoreTrack;
    private Player player1;
    private Player player2;

    @Before
    public void setUp() throws Exception {

        ArrayList<Player> players = new ArrayList<>();
        player1 = new Player(null);
        player2 = new Player(null);
        players.add(player1);
        players.add(player2);
        scoreTrack = new ScoreTrack(players);
    }

    @After
    public void tearDown() throws Exception {
        scoreTrack = null;
    }

    @Test
    public void isFinished() {

        assertFalse(scoreTrack.isFinished());

        scoreTrack.addScore(player1, 22);
        scoreTrack.addScore(player2, 15);
        assertTrue(scoreTrack.isFinished());

    }

    @Test
    public void addScore() {
        assertEquals(0, scoreTrack.getPlayerScore(player1));
        assertEquals(0, scoreTrack.getPlayerScore(player2));

        scoreTrack.addScore(player1, 22);
        scoreTrack.addScore(player2, 15);

        assertEquals(22, scoreTrack.getPlayerScore(player1));
        assertEquals(15, scoreTrack.getPlayerScore(player2));
    }

    @Test
    public void getPlayerScore() {
        scoreTrack.addScore(player1, 22);
        assertEquals(22, scoreTrack.getPlayerScore(player1));
    }

    @Test
    public void getBestScorers() {
        scoreTrack.addScore(player1, 22);
        scoreTrack.addScore(player2, 15);
        assertEquals(player1, scoreTrack.getBestScorers().getFirst());
        assertEquals(1, scoreTrack.getBestScorers().size());

        scoreTrack.addScore(player2, 7);
        assertEquals(2, scoreTrack.getBestScorers().size());
        assertTrue(scoreTrack.getBestScorers().contains(player1));
        assertTrue(scoreTrack.getBestScorers().contains(player2));
    }
}
