package it.polimi.ingsw.model.game;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.exception.JsonLoadException;

import it.polimi.ingsw.model.card.GoldCard;
import it.polimi.ingsw.utilities.GsonSingleton;

import java.util.List;


/**
 * Represents a deck of Gold cards.
 * Inherits from the Deck class.
 */
public class GoldDeck extends Deck {

    /**
     * Constructs a GoldDeck object by loading gold cards from the default JSON file.
     *
     * @throws JsonLoadException if there is an error loading the JSON file.
     */
    public GoldDeck() throws JsonLoadException {
        // Load gold cards from the default JSON file and add them to the deck
        cards.addAll(load("/goldCards.json"));
    }

    /**
     * Loads a list of GoldCard objects from a JSON file.
     *
     * @param filepath the path to the JSON file containing GoldCard data
     * @return a list of GoldCard objects loaded from the JSON file
     * @throws JsonLoadException if there is an error loading the JSON file
     */
    public static List<GoldCard> load(String filepath) throws JsonLoadException {
        return GsonSingleton.loadJson(filepath, new TypeToken<List<GoldCard>>(){}.getType());
    }
}