package it.polimi.ingsw.message.notify;

import it.polimi.ingsw.model.objective.Objective;
import it.polimi.ingsw.model.player.PlayerColor;

import java.util.List;

/**
 * This class represents a notification message that is sent at the end of a match.
 */
public class NotifyEndMatch extends NotifyMessage{
    private final int winners;
    private final List<PlayerColor> players;
    private final List<Integer> scores;
    private final List<Objective> objs;
    private final List<Integer> objScores;

    /**
     * Constructs a new NotifyEndMatch message.
     *
     * @param winners the number of winners
     * @param players the list of players
     * @param scores the list of scores for each player
     * @param objs the list of objectives
     * @param objScores the list of scores for each objective
     */
    public NotifyEndMatch(int winners, List<PlayerColor> players, List<Integer> scores, List<Objective> objs, List<Integer> objScores) {
        this.winners = winners;
        this.players = players;
        this.scores = scores;
        this.objs = objs;
        this.objScores = objScores;
    }

    /**
     * Returns the number of winners.
     *
     * @return the number of winners
     */
    public int getWinners() {
        return winners;
    }

    /**
     * Returns the list of players.
     *
     * @return the list of players
     */
    public List<PlayerColor> getPlayers() {
        return players;
    }

    /**
     * Returns the list of scores for each player.
     *
     * @return the list of scores for each player
     */
    public List<Integer> getScores() {
        return scores;
    }

    /**
     * Returns the list of objectives.
     *
     * @return the list of objectives
     */
    public List<Objective> getObjs() {
        return objs;
    }

    /**
     * Returns the list of scores for each objective.
     *
     * @return the list of scores for each objective
     */
    public List<Integer> getObjScores() {
        return objScores;
    }

    /**
     * Retrieves the type of this message, which is {@link NotifyType#END_MATCH}.
     *
     * @return The message type, which is {@link NotifyType#END_MATCH}.
     */
    @Override
    public NotifyType getNotifyType() {
        return NotifyType.END_MATCH;
    }
}
