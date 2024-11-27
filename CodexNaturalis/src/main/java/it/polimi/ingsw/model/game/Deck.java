package it.polimi.ingsw.model.game;

import it.polimi.ingsw.exception.PlayerExceptions;
import it.polimi.ingsw.model.card.PlayableCard;
import it.polimi.ingsw.model.card.Symbol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;

/**
 * Represents a deck of playable cards in the game.
 */
public class Deck {

    /**
     * Stack to hold the cards in the deck.
     */
    protected Stack<PlayableCard> cards;

    /**
     * Constructs an empty deck.
     */
    public Deck(){
        cards = new Stack<>();
    }

    /**
     * Constructs a deck with the given collection of cards.
     *
     * @param c the collection of playable cards to initialize the deck
     */
    public Deck(Collection<PlayableCard> c){
        cards = new Stack<>();
        cards.addAll(c);
    }

    /**
     * Checks if the deck is empty.
     *
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Draws a card from the deck.
     *
     * @return the drawn card
     * @throws PlayerExceptions if the deck is empty
     */
    public PlayableCard draw() throws PlayerExceptions {
        if(isEmpty()) {
            throw new PlayerExceptions(PlayerExceptions.ErrorCode.EMPTY_DECK, "The deck is empty!");
        }
        return cards.pop();
    }

    /**
     * Retrieves the symbol of the top card in the deck.
     *
     * @return the symbol of the top card
     * @throws PlayerExceptions if the deck is empty
     */
    public Symbol topCardKingdom() throws PlayerExceptions{
        if(isEmpty()) {
            return null;
        }
        return cards.peek().getKingdom();
    }

    /**
     * Shuffles the cards in the deck.
     */
    public void shuffle(){
        ArrayList<PlayableCard> tmp = new ArrayList<>(cards);
        Collections.shuffle(tmp);
        cards.clear();
        cards.addAll(tmp);
    }
}
