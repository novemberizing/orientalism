package net.novemberizing.orientalism;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import net.novemberizing.orientalism.db.article.Article;

public class OrientalismApplicationDialog {
    private static final String Tag = "OrientalismApplicationDialog";
    public static AlertDialog createSettingDialog(Activity activity) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity, R.style.Theme_Orientalism_Dialog);
        View view;

        if(OrientalismApplication.getOrientation(activity)==ORIENTATION_PORTRAIT) {
            view = View.inflate(activity, R.layout.setting_dialog_portrait_layout, null);
        } else {
            view = View.inflate(activity, R.layout.setting_dialog_landscape_layout, null);
        }

        MaterialButtonToggleGroup notificationToggleBtn = view.findViewById(R.id.setting_dialog_notification_toggle);
        notificationToggleBtn.check(OrientalismApplicationPreference.integer(activity, OrientalismApplicationPreference.NOTIFICATION, R.id.setting_dialog_notification_toggle_show));
        notificationToggleBtn.addOnButtonCheckedListener((group, id, value) -> {
            if(value) {
                OrientalismApplicationPreference.set(activity, OrientalismApplicationPreference.NOTIFICATION, R.id.setting_dialog_notification_toggle_show);
                Article article = Article.main();
                OrientalismApplicationNotification.set(activity, article.title,article.summary);
            } else {
                OrientalismApplicationPreference.set(activity, OrientalismApplicationPreference.NOTIFICATION, 0);
                OrientalismApplicationNotification.cancel(activity);
            }
        });

        MaterialButtonToggleGroup themeToggleBtn = view.findViewById(R.id.setting_dialog_configure_theme_toggle);
        themeToggleBtn.check(OrientalismApplicationPreference.integer(activity, OrientalismApplicationPreference.THEME, 0));
        themeToggleBtn.addOnButtonCheckedListener((group, id, value) -> {
            if(value) {
                OrientalismApplicationPreference.set(activity, OrientalismApplicationPreference.THEME, id);
            } else {
                OrientalismApplicationPreference.del(activity, OrientalismApplicationPreference.THEME, id);
            }
        });

        builder.setPositiveButton(R.string.close, null);
        builder.setOnDismissListener(dialog -> {
            Log.e(Tag, "on dismiss");
            if(OrientalismApplicationPreference.integer(activity, OrientalismApplicationPreference.THEME) == R.id.setting_dialog_configure_theme_button_dark) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else if(OrientalismApplicationPreference.integer(activity, OrientalismApplicationPreference.THEME) == R.id.setting_dialog_configure_theme_button_light) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
        });

        builder.setView(view);

        return builder.create();
    }

    public static void showSettingDialog(Activity activity) {
        AlertDialog dialog = OrientalismApplicationDialog.createSettingDialog(activity);

        dialog.show();
        // dialog.

        Log.e(Tag, "dialog close");
        if(OrientalismApplicationPreference.integer(activity, OrientalismApplicationPreference.THEME) == R.id.setting_dialog_configure_theme_button_dark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if(OrientalismApplicationPreference.integer(activity, OrientalismApplicationPreference.THEME) == R.id.setting_dialog_configure_theme_button_light) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }
}
