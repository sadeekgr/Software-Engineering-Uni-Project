package it.polimi.ingsw.model.game;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.exception.JsonLoadException;

import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.utilities.GsonSingleton;

import java.util.List;

/**
 * Represents a deck of resource cards.
 * Inherits from the Deck class.
 */
public class ResourceDeck extends Deck{

    /**
     * Constructs a ResourceDeck object by loading resource cards from the default JSON file.
     *
     * @throws JsonLoadException if there is an error loading the JSON file.
     */
    public ResourceDeck() throws JsonLoadException {
        // Load resource cards from the default JSON file and add them to the deck
        cards.addAll(load("/resourceCards.json"));
    }

    /**
     * Constructs a ResourceDeck object by loading resource cards from a specified JSON file.
     *
     * @param filePath The path to the JSON file containing resource cards.
     * @throws JsonLoadException if there is an error loading the JSON file.
     */
    public ResourceDeck(String filePath) throws JsonLoadException {
        // Load resource cards from the JSON file and add them to the deck
        cards.addAll(load(filePath));

    }

    /**
     * Loads a list of ResourceCard objects from a JSON file.
     *
     * @param filepath the path to the JSON file containing ResourceCard data
     * @return a list of ResourceCard objects loaded from the JSON file
     * @throws JsonLoadException if there is an error loading the JSON file
     */
    public static List<ResourceCard> load(String filepath) throws JsonLoadException {
        return GsonSingleton.loadJson(filepath, new TypeToken<List<ResourceCard>>(){}.getType());
    }
}
