package net.novemberizing.orientalism.application;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.gson.Gson;

import net.novemberizing.orientalism.R;
import net.novemberizing.orientalism.db.article.Article;

import java.util.Map;

public class OrientalismApplicationPreference {
    private static final String NAME = "orientalism";
    public static final String MAIN = "main";
    public static final String NOTIFICATION = "notification";
    public static final String THEME = "theme";
    public static final String RECENT = "recent";

    public static void gen(Context context) {
        Gson gson = OrientalismApplicationGson.get();

        Article article = Article.main();

        OrientalismApplicationPreference.gen(context, OrientalismApplicationPreference.NOTIFICATION, R.id.setting_dialog_notification_toggle_show);
        OrientalismApplicationPreference.gen(context, OrientalismApplicationPreference.THEME, R.id.setting_dialog_configure_theme_button_system);
        OrientalismApplicationPreference.gen(context, OrientalismApplicationPreference.MAIN, gson.toJson(article));
        OrientalismApplicationPreference.gen(context, OrientalismApplicationPreference.RECENT, article.title);

        int mode = OrientalismApplicationPreference.integer(context, OrientalismApplicationPreference.THEME, R.id.setting_dialog_configure_theme_button_system);
        if(mode == R.id.setting_dialog_configure_theme_button_dark) {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
        } else if(mode == R.id.setting_dialog_configure_theme_button_light) {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    public static void del(Context context, String key, Integer value) {
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        int v = preferences.getInt(key, value);
        if(v == value) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(key);
            editor.apply();
        }
    }

    @SuppressWarnings("unused")
    public static void del(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        Map<String, ?> map = preferences.getAll();
        SharedPreferences.Editor editor = preferences.edit();
        for(String key : map.keySet()) {
            editor.remove(key);
        }
        editor.apply();
    }

    public static void gen(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        Map<String, ?> map = preferences.getAll();
        if(map.get(key) == null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

    public static void gen(Context context, String key, Integer value) {
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        Map<String, ?> map = preferences.getAll();
        if(map.get(key) == null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(key, value);
            editor.apply();
        }
    }

    public static void set(Context context, String key, Integer value) {
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
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

    public static Integer integer(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);

        return preferences.getInt(key, 0);
    }

    public static Integer integer(Context context, String key, Integer defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);

        return preferences.getInt(key, defaultValue);
    }
}
