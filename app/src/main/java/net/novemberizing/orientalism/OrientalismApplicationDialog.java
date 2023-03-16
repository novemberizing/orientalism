package net.novemberizing.orientalism;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;

public class OrientalismApplicationDialog {
    private static final String Tag = "OrientalismApplicationDialog";
    public static AlertDialog createSettingDialog(Activity activity) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view;

        if(OrientalismApplication.getOrientation(activity)==ORIENTATION_PORTRAIT) {
            view = inflater.inflate(R.layout.setting_dialog_portrait_layout, null, false);
        } else {
            view = inflater.inflate(R.layout.setting_dialog_landscape_layout, null, false);
        }

        MaterialSwitch notificationSwitchBtn = view.findViewById(R.id.setting_dialog_show_notification_switch);
        MaterialButtonToggleGroup themeToggleBtn = view.findViewById(R.id.setting_dialog_configure_theme_toggle);

        notificationSwitchBtn.setChecked(OrientalismApplicationPreference.bool(activity, OrientalismApplicationPreference.NOTIFICATION, true));
        notificationSwitchBtn.setOnCheckedChangeListener((button, value) -> {
            OrientalismApplicationPreference.set(activity, OrientalismApplicationPreference.NOTIFICATION, value);
            if(value) {
                // TODO: 최신 리소스를 가지고 올 것
                OrientalismApplicationNotification.set(activity, "징갱취제","요약");
            } else {
                OrientalismApplicationNotification.cancel(activity);
            }
        });

        themeToggleBtn.check(OrientalismApplicationPreference.integer(activity, OrientalismApplicationPreference.THEME, R.id.setting_dialog_configure_theme_button_system));
        themeToggleBtn.addOnButtonCheckedListener((group, id, value)-> {
            if(value) {
                OrientalismApplicationPreference.set(activity, OrientalismApplicationPreference.THEME, id);
            }
        });

        builder.setPositiveButton(R.string.close, (dialog, i) -> {
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
    }
}
