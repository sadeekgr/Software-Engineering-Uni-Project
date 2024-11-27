package it.polimi.ingsw.model.game;

import it.polimi.ingsw.exception.PlayerExceptions;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.Symbol;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceDeckTest {
    private ResourceDeck resourceDeck;

    @Before
    public void setUp() throws Exception {
        // Assuming the default JSON file path is used for testing
        resourceDeck = new ResourceDeck("/resourceCards.json");
    }

    @After
    public void tearDown() throws Exception {
        resourceDeck = null;
    }

    @Test
    public void isEmpty() throws PlayerExceptions {
        int i;
        for(i = 0; i<41; i++) {
            if(i<40) {
                assertFalse(resourceDeck.isEmpty());
                resourceDeck.draw();
            } else {
                assertTrue(resourceDeck.isEmpty());

            }
        }
    }

    @Test
    public void draw() throws PlayerExceptions {
        // Draw a card from the deck
        ResourceCard drawnCard = (ResourceCard) resourceDeck.draw();
        assertNotNull(drawnCard);
        // Ensure that the drawn card is removed from the deck
        boolean isequal = false;

        while (!resourceDeck.isEmpty()) {
            if (resourceDeck.draw().equals(drawnCard)) {
                isequal = true;
                break;
            }
        }
        assertFalse(isequal);
    }

    @Test
    public void topCardKingdom() throws PlayerExceptions {
        // Get the top card of the kingdom stack
        Symbol topCard = resourceDeck.topCardKingdom();
        assertNotNull(topCard);
        // Ensure that the top card is one of the kingdom cards
        assertTrue(topCard.isKingdom());
    }
}