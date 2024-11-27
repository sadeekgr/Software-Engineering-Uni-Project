package it.polimi.ingsw.utilities;

import com.google.gson.*;
import it.polimi.ingsw.model.card.*;

import java.lang.reflect.Type;

/**
 * Gson adapter for serializing and deserializing PlayableCard objects to and from JSON.
 */
public class PlayableCardAdapter implements JsonSerializer<PlayableCard>, JsonDeserializer<PlayableCard> {

    /**
     * Serializes a PlayableCard object into a JsonElement.
     *
     * @param src the PlayableCard object to serialize
     * @param typeOfSrc the type of the source object
     * @param context the serialization context
     * @return the serialized JsonElement representing the PlayableCard
     */
    @Override
    public JsonElement serialize(PlayableCard src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("type", src.getClass().getSimpleName());
        json.add("data", context.serialize(src));
        return json;
    }

    /**
     * Deserializes a JsonElement into a PlayableCard object.
     *
     * @param json the JsonElement to deserialize
     * @param typeOfT the type of the target object
     * @param context the deserialization context
     * @return the deserialized PlayableCard object
     * @throws JsonParseException if there is an error during deserialization
     */
    @Override
    public PlayableCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement data = jsonObject.get("data");

        return switch (type) {
            case "ResourceCard" -> context.deserialize(data, ResourceCard.class);
            case "GoldCardInt" -> context.deserialize(data, GoldCardInt.class);
            case "GoldCardObject" -> context.deserialize(data, GoldCardObject.class);
            case "GoldCardCorner" -> context.deserialize(data, GoldCardCorner.class);
            default -> throw new JsonParseException("Unknown element type: " + type);
        };
    }
}