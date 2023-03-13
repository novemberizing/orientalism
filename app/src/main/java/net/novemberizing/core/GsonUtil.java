package net.novemberizing.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class GsonUtil {
    public static JsonElement get(JsonArray array, int index) {
        return array.get(index);
    }
    public static String str(JsonElement element) {
        return element.getAsString();
    }
}
