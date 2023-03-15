package net.novemberizing.orientalism;

import android.content.Context;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import net.novemberizing.orientalism.db.article.ArticleDailyWorker;

import java.util.concurrent.TimeUnit;

public class OrientalismApplicationWork {
    public static void gen(Context context) {
        WorkManager manager = WorkManager.getInstance(context);
        // TODO: REMOVE THIS BEFORE RELEASE
        PeriodicWorkRequest.Builder builder = new PeriodicWorkRequest.Builder(ArticleDailyWorker.class, 900, TimeUnit.SECONDS);
        // TODO: APPLY THIS BEFORE RELEASE
        // PeriodicWorkRequest.Builder builder = new PeriodicWorkRequest.Builder(ArticleDailyWorker.class, 1, TimeUnit.DAYS);

        manager.enqueueUniquePeriodicWork("unique", ExistingPeriodicWorkPolicy.UPDATE, builder.build());
    }
}
