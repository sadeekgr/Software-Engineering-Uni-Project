package it.polimi.ingsw.utilities;

import com.google.gson.*;
import it.polimi.ingsw.model.card.GoldCard;
import it.polimi.ingsw.model.card.GoldCardCorner;
import it.polimi.ingsw.model.card.GoldCardInt;
import it.polimi.ingsw.model.card.GoldCardObject;

import java.lang.reflect.Type;

public class GoldCardAdapter implements JsonSerializer<GoldCard>, JsonDeserializer<GoldCard> {
    /**
     * Serializes a GoldCard object into a JsonElement.
     *
     * @param src       the Objective object to serialize
     * @param typeOfSrc the type of the source object
     * @param context   the serialization context
     * @return the serialized JsonElement representing the Objective
     */
    @Override
    public JsonElement serialize(GoldCard src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("type", src.getClass().getSimpleName());
        json.add("data", context.serialize(src));
        return json;
    }

    /**
     * Deserializes a JsonElement into a GoldCard object.
     *
     * @param json    the JsonElement to deserialize
     * @param typeOfT the type of the target object
     * @param context the deserialization context
     * @return the deserialized Objective object
     * @throws JsonParseException if there is an error during deserialization
     */
    @Override
    public GoldCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement data = jsonObject.get("data");

        return switch (type) {
            case "GoldCardInt" -> context.deserialize(data, GoldCardInt.class);
            case "GoldCardObject" -> context.deserialize(data, GoldCardObject.class);
            case "GoldCardCorner" -> context.deserialize(data, GoldCardCorner.class);
            default -> throw new JsonParseException("Unknown element type: " + type);
        };
    }
}