package it.polimi.ingsw.model.game;

import it.polimi.ingsw.exception.PlayerExceptions;
import it.polimi.ingsw.model.card.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * JUnit test class for testing the functionalities of the Deck class.
 */
public class DeckTest {

    private Deck deck;

    /**
     * Sets up the test environment before each test method runs.
     * Initializes a deck with a set of ResourceCard instances.
     *
     * @throws Exception if an error occurs during setup
     */
    @Before
    public void setUp() throws Exception {
        Map<CornerPosition, Corner> corner = new HashMap<>();
        for (CornerPosition p : CornerPosition.values()) {
            corner.put(p, new Corner(true, Symbol.EMPTY));
        }

        Collection<PlayableCard> cards = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            cards.add(new ResourceCard(Symbol.ANIMAL, corner, 0, "000"));
            cards.add(new ResourceCard(Symbol.FUNGI, corner, 0, "000"));
            cards.add(new ResourceCard(Symbol.INSECT, corner, 0, "000"));
            cards.add(new ResourceCard(Symbol.PLANT, corner, 0, "000"));
        }

        deck = new Deck(cards);

    }

    /**
     * Cleans up the test environment after each test method completes.
     *
     * @throws Exception if an error occurs during teardown
     */
    @After
    public void tearDown() throws Exception {
        deck = null;
    }

    /**
     * Test case to verify the isEmpty method of the Deck class.
     *
     * @throws PlayerExceptions if an unexpected exception occurs during the test
     */
    @Test
    public void isEmpty() throws PlayerExceptions {
        int i;
        for (i = 0; i < 4*10 + 1; i++) {
            if (i < 4*10) {
                assertFalse(deck.isEmpty());
                deck.draw();
            } else {
                assertTrue(deck.isEmpty());
            }
        }
    }

    /**
     * Test case to verify the draw method of the Deck class.
     *
     * @throws PlayerExceptions if an unexpected exception occurs during the test
     */
    @Test
    public void draw() throws PlayerExceptions {
        // Draw a card from the deck
        ResourceCard drawnCard = null;
        try {
            drawnCard = (ResourceCard) deck.draw();
        } catch (PlayerExceptions e) {
            fail();
        }

        assertNotNull(drawnCard);
        // Ensure that the drawn card is removed from the deck
        boolean isequal = false;

        while (!deck.isEmpty()) {
            if (deck.draw().equals(drawnCard)) {
                isequal = true;
                break;
            }
        }
        assertFalse(isequal);
    }

    /**
     * Test case to verify the topCardKingdom method of the Deck class.
     *
     * @throws PlayerExceptions if an unexpected exception occurs during the test
     */
    @Test
    public void topCardKingdom() throws PlayerExceptions {
        //top card of the kingdom stack
        Symbol topCard = deck.topCardKingdom();
        assertNotNull(topCard);

        assertSame(topCard, deck.draw().getKingdom());
        // Ensure that the top card is one of the kingdom cards
        assertTrue(topCard.isKingdom());
    }

    /**
     * Test case to verify the shuffle method of the Deck class.
     */
    @Test
    public void shuffle() {
        // initial order of cards
        Collection<PlayableCard> initialOrder = new Stack<>();
        initialOrder.addAll(deck.cards);

        // Shuffle the deck
        deck.shuffle();

        // shuffled order of cards
        Collection<PlayableCard> shuffledOrder = new Stack<>();
        shuffledOrder.addAll(deck.cards);

        assertNotEquals(initialOrder, shuffledOrder);
        assertEquals(initialOrder.size(), shuffledOrder.size());
        assertTrue(shuffledOrder.containsAll(initialOrder));
    }
}