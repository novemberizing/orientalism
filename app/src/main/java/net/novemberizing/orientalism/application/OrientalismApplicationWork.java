package net.novemberizing.orientalism.application;

import android.content.Context;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import net.novemberizing.orientalism.db.article.ArticleDailyWorker;

import java.util.concurrent.TimeUnit;

public class OrientalismApplicationWork {
    public static void gen(Context context) {
        WorkManager manager = WorkManager.getInstance(context);

        PeriodicWorkRequest.Builder builder = new PeriodicWorkRequest.Builder(ArticleDailyWorker.class, 1, TimeUnit.DAYS);

        manager.enqueueUniquePeriodicWork("unique", ExistingPeriodicWorkPolicy.UPDATE, builder.build());
    }
}
