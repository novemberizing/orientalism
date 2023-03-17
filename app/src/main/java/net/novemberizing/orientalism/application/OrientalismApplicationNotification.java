package net.novemberizing.orientalism.application;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import net.novemberizing.orientalism.R;
import net.novemberizing.orientalism.MainActivity;
import net.novemberizing.orientalism.db.article.Article;

public class OrientalismApplicationNotification {
    private static final String NAME = "net.novemberizing.orientalism";
    private static final String DESCRIPTION =  "오늘의 한자";

    private static final String CHANNEL_ID = "net.novemberizing.orientalism";
    private static final Integer NOTIFICATION_ID = 1000;

    private static final Integer REQUEST_CODE = 1000;
    public static final String TITLE = "title";

    public static void gen(Context context) {
        Article article = Article.main();

        OrientalismApplicationNotification.set(context, article.title, article.summary);
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    public static void set(Context context, String title, String summary){
        // TODO: VERSION 2 REFACTORING
        if(OrientalismApplicationPreference.integer(context,OrientalismApplicationPreference.NOTIFICATION) == R.id.setting_dialog_notification_toggle_show) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, NAME, importance);
                channel.setDescription(DESCRIPTION);
                channel.setShowBadge(false);
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(TITLE, title);
            PendingIntent pending;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                pending = PendingIntent.getActivity(context, REQUEST_CODE, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            } else {
                pending = PendingIntent.getActivity(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification_icon)
                    .setContentTitle(title)
                    .setContentText(summary)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setSilent(true)
                    .setLocalOnly(true)
                    .setOngoing(true)
                    .setContentIntent(pending);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    public static void cancel(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
