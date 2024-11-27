package it.polimi.ingsw.utilities;

import com.google.gson.*;
import it.polimi.ingsw.model.objective.DispositionObjective;
import it.polimi.ingsw.model.objective.Objective;
import it.polimi.ingsw.model.objective.SymbolObjective;

import java.lang.reflect.Type;

/**
 * Gson adapter for serializing and deserializing Objective objects to and from JSON.
 */
public class ObjectiveAdapter implements JsonSerializer<Objective>, JsonDeserializer<Objective> {

    /**
     * Serializes an Objective object into a JsonElement.
     *
     * @param src the Objective object to serialize
     * @param typeOfSrc the type of the source object
     * @param context the serialization context
     * @return the serialized JsonElement representing the Objective
     */
    @Override
    public JsonElement serialize(Objective src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("type", src.getClass().getSimpleName());
        json.add("data", context.serialize(src));
        return json;
    }

    /**
     * Deserializes a JsonElement into an Objective object.
     *
     * @param json the JsonElement to deserialize
     * @param typeOfT the type of the target object
     * @param context the deserialization context
     * @return the deserialized Objective object
     * @throws JsonParseException if there is an error during deserialization
     */
    @Override
    public Objective deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement data = jsonObject.get("data");

        return switch (type) {
            case "DispositionObjective" -> context.deserialize(data, DispositionObjective.class);
            case "SymbolObjective" -> context.deserialize(data, SymbolObjective.class);
            default -> throw new JsonParseException("Unknown element type: " + type);
        };
    }
}
