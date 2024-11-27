package it.polimi.ingsw.message;

import it.polimi.ingsw.model.card.PlayableCard;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.field.PlayerField;
import it.polimi.ingsw.model.objective.Objective;
import it.polimi.ingsw.model.player.PlayerColor;

import java.util.List;
import java.util.Map;

/**
 * Record representing a message containing the current state of the game.
 *
 * @param players           A map of player names to their respective colors.
 * @param gameOrder         A list defining the order of players in the current game round.
 * @param fields            A map associating each player's color with their respective {@link PlayerField}.
 * @param scores            A map of player colors to their current scores.
 * @param cardsBack         A map associating each player's color with an array of symbols representing their back cards.
 * @param personalObjective The personal {@link Objective} of the current player.
 * @param hand              An array of {@link PlayableCard}s representing the current player's hand.
 * @param commonObjectives  An array of {@link Objective}s representing the common objectives of the game.
 * @param topResourceDeck   The symbol representing the top resource deck card.
 * @param numResourceCard   The number of remaining resource cards in the deck.
 * @param topGoldDeck       The symbol representing the top gold deck card.
 * @param numGoldCard       The number of remaining gold cards in the deck.
 * @param market            An array of {@link PlayableCard}s representing the market cards.
 * @param currentPlayer     The color of the current player taking their turn.
 */
public record GameStateMessage(Map<String, PlayerColor> players, List<PlayerColor> gameOrder,
                               Map<PlayerColor, PlayerField> fields, Map<PlayerColor, Integer> scores,
                               Map<PlayerColor, Symbol[]> cardsBack, Objective personalObjective, PlayableCard[] hand,
                               Objective[] commonObjectives, Symbol topResourceDeck, int numResourceCard,
                               Symbol topGoldDeck, int numGoldCard, PlayableCard[] market,
                               PlayerColor currentPlayer) implements Message {

    /**
     * Retrieves the type of this message, which is {@link MessageType#GAMESTATE}.
     *
     * @return The message type, which is {@link MessageType#GAMESTATE}.
     */
    @Override
    public MessageType getType() {
        return MessageType.GAMESTATE;
    }
}
