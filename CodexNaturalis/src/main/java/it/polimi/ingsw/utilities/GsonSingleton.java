package it.polimi.ingsw.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.exception.JsonLoadException;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.GoldCard;
import it.polimi.ingsw.model.card.PlayableCard;
import it.polimi.ingsw.model.objective.Objective;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * Utility class for creating a Gson instance configured with custom adapters for serializing and deserializing
 * PlayableCard and Objective objects.
 */
public class GsonSingleton {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Card.class, new CardAdapter())
            .registerTypeAdapter(PlayableCard.class, new PlayableCardAdapter())
            .registerTypeAdapter(GoldCard.class, new GoldCardAdapter())
            .registerTypeAdapter(Objective.class, new ObjectiveAdapter())
            .create();

    /**
     * Retrieves a Gson instance configured with custom adapters objects.
     *
     * @return Gson instance configured with custom adapters
     */
    public static Gson getGson() {
        return gson;
    }

    public static <T> T loadJson(String filePath, Type type) throws JsonLoadException {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty.");
        }

        InputStream inputStream = GsonSingleton.class.getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new JsonLoadException("File not found: " + filePath);
        }

        try (InputStreamReader reader = new InputStreamReader(inputStream)){
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            throw new JsonLoadException("Error loading JSON from file: " + filePath + ". Error: " + e.getMessage());
        }
    }
}
