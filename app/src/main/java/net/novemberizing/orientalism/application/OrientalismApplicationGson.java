package net.novemberizing.orientalism.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.novemberizing.orientalism.application.gson.OrientalismApplicationGsonDateAdapter;

import java.util.Date;

public class OrientalismApplicationGson {
    private static Gson gson = null;

    public static void gen() {
        synchronized (OrientalismApplicationGson.class) {
            GsonBuilder builder = new GsonBuilder();

            builder.disableHtmlEscaping();
            builder.registerTypeAdapter(Date.class, new OrientalismApplicationGsonDateAdapter());

            gson = builder.create();
        }
    }

    public static Gson get() {
        return gson;
    }
}
