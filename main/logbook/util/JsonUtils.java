package logbook.util;

import javax.json.JsonNumber;
import javax.json.JsonString;
import javax.json.JsonValue;

/**
 * JSONを読むutilです
 *
 */
public class JsonUtils {

    public static int getInt(JsonValue obj) {
        if (obj instanceof JsonNumber)
            return ((JsonNumber) obj).intValue();
        else if (obj instanceof JsonString)
            return Integer.parseInt(((JsonString) obj).getString());
        throw new IllegalArgumentException();
    }

    public static long getLong(JsonValue obj) {
        if (obj instanceof JsonNumber)
            return ((JsonNumber) obj).longValue();
        else if (obj instanceof JsonString)
            return Long.parseLong(((JsonString) obj).getString());
        throw new IllegalArgumentException();
    }

    public static double getDouble(JsonValue obj) {
        if (obj instanceof JsonNumber)
            return ((JsonNumber) obj).doubleValue();
        else if (obj instanceof JsonString)
            return Double.parseDouble(((JsonString) obj).getString());
        throw new IllegalArgumentException();
    }

}
