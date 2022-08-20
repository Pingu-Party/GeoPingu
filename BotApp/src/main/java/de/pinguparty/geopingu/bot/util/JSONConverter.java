package de.pinguparty.geopingu.bot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

/**
 * Utility class that provides static methods for the creation of {@link Object}s from JSON strings and the
 * transformation of {@link Object}s to JSON strings by using Jacksons {@link ObjectMapper}.
 */
public class JSONConverter {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        //Find and register additional modules
        objectMapper.findAndRegisterModules();
    }

    /**
     * Transforms a given object to a {@link JSONObject}. In case of failures, an empty {@link JSONObject}
     * is returned.
     *
     * @param object The object to transform
     * @return The resulting {@link JSONObject}
     */
    public static JSONObject toJSON(Object object) {
        try {
            //Transformation
            return new JSONObject(objectMapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            return new JSONObject();
        }
    }

    /**
     * Creates and returns an object of a given {@link Class} from a given {@link JSONObject}.
     * In case of failures, null is returned.
     *
     * @param jsonObject The {@link JSONObject} to create an object from
     * @param clazz      Reference to the target class of the object
     * @param <T>        The target class of the object
     * @return The resulting object
     */
    public static <T> T fromJSON(JSONObject jsonObject, Class<T> clazz) {
        //Sanity check
        if (jsonObject == null) return null;
        return fromJSON(jsonObject.toString(), clazz);
    }

    /**
     * Creates and returns an object of a given {@link Class} from a given JSON string.
     * In case of failures, null is returned.
     *
     * @param jsonString The JSON string to create an object from
     * @param clazz      Reference to the target class of the object
     * @param <T>        The target class of the object
     * @return The resulting object
     */
    public static <T> T fromJSON(String jsonString, Class<T> clazz) {
        //Sanity check
        if ((jsonString == null) || jsonString.isEmpty() || (clazz == null)) return null;
        try {
            //Creation
            return objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
