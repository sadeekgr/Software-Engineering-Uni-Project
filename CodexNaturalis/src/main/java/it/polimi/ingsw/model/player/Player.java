package it.polimi.ingsw.model.player;

import it.polimi.ingsw.exception.*;

import it.polimi.ingsw.message.notify.NotifyCardPlayed;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.field.CardPlacement;
import it.polimi.ingsw.model.field.PlayerField;
import it.polimi.ingsw.model.field.Position;
import it.polimi.ingsw.model.game.Match;
import it.polimi.ingsw.model.objective.Objective;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a player in the game.
 */
public class Player {

    // The match associated to the player.
    private final Match match;
    // The color associated to the player.
    private PlayerColor playerColor;
    // Player's hand of cards.
    private PlayableCard[] hand;
    // Player's personal objective.
    private Objective objective;
    // Player's field.
    private final PlayerField playerField;
    private  boolean cardPlayedThisTurn;

    /**
     * Constructs a new player instance.
     *
     * @param match the match the player belongs to
     */
    public Player(Match match){
        this.match = match;
        hand = new PlayableCard[3];
        playerField = new PlayerField();
        objective = null;
        playerColor = null;
        cardPlayedThisTurn = false;
    }

    /**
     * Constructs a new player instance with all initial parameters.
     *
     * @param match        The match the player belongs to.
     * @param hand         The initial hand of cards for the player.
     * @param playerField  The initial player field configuration.
     * @param objective    The initial personal objective for the player.
     * @param playerColor  The color associated with the player.
     */
    public Player(Match match, PlayableCard[] hand, PlayerField playerField, Objective objective, PlayerColor playerColor){
        this.match = match;
        this.hand = hand;
        this.playerField = playerField;
        this.objective = objective;
        this.playerColor = playerColor;
        cardPlayedThisTurn = false;
    }

    /**
     * Chooses the side of the starter card for this player.
     *
     * @param side The side of the starter card to choose.
     * @throws PlayerExceptions If it's not the appropriate time to choose the starter card or if the starter card side has already been chosen.
     */
    public void chooseStarterCardSide(boolean side) throws PlayerExceptions {
        match.chooseStarterCardSide(this, side);
    }

    /**
     * Chooses the personal objective for this player.
     *
     * @param num The number corresponding to the personal objective to choose.
     * @throws PlayerExceptions If it's not the appropriate time to choose the personal objective or the personal objective has already been chosen.
     */
    public void chooseObjective(int num) throws PlayerExceptions {
        match.chooseObjective(this, num);
    }

    /**
     * Retrieves the list of cards played by the player.
     *
     * @return A list of CardPlacement objects representing the cards played by the player.
     */
    public List<CardPlacement> getPlayedCards(){
        return playerField.getCards();
    }

    /**
     * Retrieves the hand of cards held by the player.
     *
     * @return An array of PlayableCard objects representing the player's hand.
     */
    public PlayableCard[] getHand(){
        return Arrays.copyOf(hand, 3);
    }

    /**
     * Places the starter card on the player's field.
     *
     * @param card The starter card to place.
     * @param side The side of the starter card to place.
     */
    public void placeStarterCard (StarterCard card, boolean side) {
        playerField.placeStarterCard(card, side);
    }

    /**
     * Plays a card from the player's hand at the specified index to the given position on the field.
     *
     * @param index     The index of the card to be played from the player's hand.
     * @param position  The position on the field where the card will be played.
     * @param isFront   Specifies whether the card will be placed with its front side up or down.
     *                  {@code true} for placing the card with the front side up, {@code false} for placing it with the back side up.
     * @throws PlayerExceptions If the specified index is not valid for the player's hand or if it's not the player's turn to play a card or if a card has already been played during this turn or if the requirements for playing the card are not fulfilled or if the specified position is not valid for card placement.
     */
    public void playCard(int index, Position position, boolean isFront) throws PlayerExceptions {
        if(isNotMyTurn()){
            throw new PlayerExceptions(PlayerExceptions.ErrorCode.NOT_YOUR_TURN, "You cannot play cards in other players turn!");
        }

        if(cardPlayedThisTurn){
            throw new PlayerExceptions(PlayerExceptions.ErrorCode.CARD_ALREADY_PLAYED, "You cannot play more than one card each turn!!");
        }

        if(index < 0 || index > 2 || hand[index] == null) {
            throw new PlayerExceptions(PlayerExceptions.ErrorCode.INVALID_HAND_INDEX, "There isn't any card at the chosen index!");
        }

        PlayableCard card = hand[index];

        if(!card.checkRequirements(playerField, isFront)){
            throw new PlayerExceptions(PlayerExceptions.ErrorCode.REQUIREMENTS_NOT_FULFILLED,"You can't play this card: requirements not fulfilled");
        }

        if (!playerField.isCardPlaceableAt(position)) {
            throw new PlayerExceptions(PlayerExceptions.ErrorCode.INVALID_POSITION, "You can't play this card at " + position + " !");
        }

        hand[index] = null;
        int score = card.calcScore(playerField, isFront, position);
        playerField.placeCard(card, isFront, position);
        match.updateScoreTrack(score);
        cardPlayedThisTurn = true;

        match.broadcast(new NotifyCardPlayed(playerColor, index, card, position, isFront, score));

        // Don't draw cards in the last round
        if(match.lastRound()){
            endTurn();
        }
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param c the card to add to the player's hand
     */
    private void addToHand(PlayableCard c){
        // The hand cannot be full; to draw a card, a card must be played first
        for(int i = 0; i < hand.length; i++){
            if(hand[i] == null){
                hand[i] = c;
                return;
            }
        }
    }

    /**
     * Checks if the player is allowed to draw cards based on the game's current state.
     *
     * @throws PlayerExceptions If it's not the player's turn to draw cards or if the player attempts to draw cards before playing a card during their turn.
     */
    private void checkDrawCondition() throws PlayerExceptions {
        if(isNotMyTurn()){
            throw new PlayerExceptions(PlayerExceptions.ErrorCode.NOT_YOUR_TURN,"You cannot draw in other players turn!");
        }

        if(!cardPlayedThisTurn){
            throw new PlayerExceptions(PlayerExceptions.ErrorCode.DRAW_BEFORE_PLAY, "You must first place a card and then draw!");
        }
    }

    /**
     * Draws a card from the market at the specified index and adds it to the player's hand.
     *
     * @param index The index of the card to draw from the market.
     * @throws PlayerExceptions If it's not the player's turn to draw or if the player tries to draw before playing a card or if the provided index is invalid for the market.
     */
    public void drawMarket(int index) throws PlayerExceptions {
        checkDrawCondition();

        PlayableCard c = match.drawMarket(index);
        addToHand(c);

        endTurn();
    }

    /**
     * Draws a gold card from the game's resources and adds it to the player's hand.
     *
     * @throws PlayerExceptions If it's not the player's turn to draw or if the player tries to draw before playing a card or if there is no gold card available to draw.
     */
    public void drawGold() throws PlayerExceptions {
        checkDrawCondition();

        PlayableCard c = match.drawGold();
        addToHand(c);

        endTurn();
    }

    /**
     * Draws a resource card from the game's resources and adds it to the player's hand.
     *
     * @throws PlayerExceptions If it's not the player's turn to draw or if the player tries to draw before playing a card or if there are no resource cards available to draw.
     */
    public void drawResource() throws PlayerExceptions {
        checkDrawCondition();

        PlayableCard c = match.drawResource();
        addToHand(c);

        endTurn();
    }

    /**
     * Sets the player's hand.
     *
     * @param playerHand the array of playable cards to set as the player's hand
     */
    public void setHand(PlayableCard[] playerHand){
        this.hand = playerHand;
    }

    /**
     * Sets the color of the player.
     *
     * @param color The color to set for the player.
     */
    public void setPlayerColor(PlayerColor color){
        playerColor = color;
    }

    /**
     * Sets the player's personal objective.
     *
     * @param obj The personal objective to set for the player.
     */
    public void setObjective(Objective obj){
        objective = obj;
    }

    /**
     * Gets the color of the player.
     *
     * @return the color of the player
     */
    public PlayerColor getColor() {
        return this.playerColor;
    }

    /**
     * Gets the player's objective.
     *
     * @return the player's objective
     */
    public Objective getObjective() {
        return this.objective;
    }

    /**
     * Gets the player's field
     *
     * @return the player's field
     */
    public PlayerField getPlayerField() {
        List<CardPlacement> cards = playerField.getCards();
        PlayerField p = new PlayerField();

        if(cards!=null) {
            if (!cards.isEmpty()) {
                CardPlacement placement = cards.getFirst();
                p.placeStarterCard((StarterCard) placement.getCard(), placement.isFront());
            }

            for (int i = 1; i < cards.size(); i++) {
                CardPlacement placement = cards.get(i);
                p.placeCard((PlayableCard) placement.getCard(), placement.isFront(), placement.getPosition());
            }
        }

        return p;
    }

    /**
     * Calculates the score for the player's personal objective.
     *
     * @return the score for the personal objective
     */
    public int calculatePersonalObjectiveScore(){
        if(objective == null){
            return 0;
        }
        return objective.calculateObjectiveScore(playerField);
    }

    /**
     * Calculates the score for a given objective.
     *
     * @param o the objective for which to calculate the score
     * @return the score for the given objective
     */
    public int calculateObjectiveScore(Objective o){
        if(o == null){
            return 0;
        }
        return o.calculateObjectiveScore(playerField);
    }

    /**
     * Checks whether it's currently not the player's turn in the game.
     *
     * @return True if it's not the player's turn, False otherwise.
     */
    private boolean isNotMyTurn(){
        return !this.equals(match.getCurrentPlayer());
    }

    /**
     * Ends the player's turn, advancing the game to the next turn.
     */
    private void endTurn(){
        match.nextTurn();
        cardPlayedThisTurn = false;
    }
}