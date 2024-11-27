package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Represents the score track for a game.
 */
public class ScoreTrack {

    private final Map<Player, Integer> playerScores;


    /**
     * Constructs a new ScoreTrack object.
     *
     * @param players List of players in the game
     */
    public ScoreTrack(ArrayList<Player> players){
        playerScores = new HashMap<>();

        // Initialize scores for each player to 0
        for(Player p : players){
            playerScores.put(p, 0);
        }
    }

    /**
     * Constructs a new ScoreTrack object by copying another ScoreTrack.
     *
     * @param scoreTrack The ScoreTrack object to copy
     */
    public ScoreTrack(ScoreTrack scoreTrack){
        this.playerScores = scoreTrack.playerScores;
    }

    /**
     * Checks if the game is finished.
     *
     * @return True if the game is finished, otherwise false
     */
    public boolean isFinished() {
        for (Player px : playerScores.keySet()) {
            if(getPlayerScore(px)>=20) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds score to a player.
     *
     * @param p The player to whom the score is added
     * @param s The score to be added
     */
    public void addScore(Player p, int s) {
        for (Player px : playerScores.keySet()) {
            if (px.equals(p)) {
                int a = getPlayerScore(px);
                playerScores.put(px, a + s);
            }
        }
    }

    /**
     * Gets the score of a player.
     *
     * @param p The player whose score is to be retrieved
     * @return The score of the player
     */
    public int getPlayerScore(Player p) {
        return playerScores.get(p);
    }

    /**
     * Gets the players with the best score(s).
     *
     * @return List of players with the best score(s)
     */
    public ArrayList<Player> getBestScorers() {
        // find best score
        int bestScore = 0;
        for (Player p : playerScores.keySet()) {
            if(bestScore < getPlayerScore(p)) {
                bestScore = getPlayerScore(p);
            }
        }

        // return players with best score
        ArrayList<Player> bestScorers = new ArrayList<>();
        for (Player p : playerScores.keySet()) {
            if(getPlayerScore(p) == bestScore){
                bestScorers.add(p);
            }
        }

        return bestScorers;
    }

    /**
     * Gets the players ordered by their score in descending order.
     *
     * @return List of players ordered by score (highest to lowest)
     */
    public List<Player> getPlayersOrderedByScore(){
        List<Player> playersList = new ArrayList<>(playerScores.keySet());

        playersList.sort((a, b) -> Integer.compare(getPlayerScore(b), getPlayerScore(a)));

        return playersList;
    }
}

