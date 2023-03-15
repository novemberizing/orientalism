package net.novemberizing.orientalism;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import net.novemberizing.orientalism.db.article.Article;

public class OrientalismApplicationPreference {
    private static final String NAME = "orientalism";

    public static final String MAIN = "main";

    public static void gen(Context context) {
        String main = OrientalismApplicationPreference.str(context, OrientalismApplicationPreference.MAIN);
        if(main == null) {
            Gson gson = OrientalismApplicationGson.get();
            OrientalismApplicationPreference.set(context, OrientalismApplicationPreference.MAIN, gson.toJson(Article.main()));
        }
    }

    public static void set(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String str(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return preferences.getString(key, null);
    }
}
