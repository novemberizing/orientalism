package net.novemberizing.orientalism;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class OrientalismApplicationGson {
    private static Gson gson = null;

    public static void gen() {
        synchronized (OrientalismApplicationGson.class) {
            GsonBuilder builder = new GsonBuilder();
            builder.disableHtmlEscaping();
            gson = builder.create();
        }
    }

    public static Gson get() {
        return gson;
    }
}
