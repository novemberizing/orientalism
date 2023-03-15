package net.novemberizing.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GsonUtil {
    public static JsonElement get(JsonArray array, int index) {
        return array.get(index);
    }
    public static String str(JsonElement element) {
        return element.getAsString();
    }

    public static JsonObject obj(JsonElement element) {
        return element.getAsJsonObject();
    }

    public static JsonElement get(JsonObject obj, String key) {
        return obj.get(key);
    }

    public static Integer integer(JsonElement element){
        return element.getAsInt();
    }
}
