package net.novemberizing.orientalism;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.android.volley.toolbox.Volley;

import net.novemberizing.orientalism.article.ArticleDailyWorker;
import net.novemberizing.orientalism.article.ArticleRepository;

import java.util.concurrent.TimeUnit;

public class OrientalismApplication extends Application {
    public static final String Tag = "OrientalismApplication";
    @Override
    public void onCreate(){
        super.onCreate();

        Log.e(OrientalismApplication.Tag, "onCreate()");

        OrientalismApplicationGson.gen();
        OrientalismApplicationDB.gen(this);
        OrientalismApplicationVolley.gen(this);

        ArticleRepository.sync();

        // Volley.newRequestQueue(this);

        // 기본 작업 등록
        WorkManager manager = WorkManager.getInstance(this);
        PeriodicWorkRequest.Builder builder = new PeriodicWorkRequest.Builder(ArticleDailyWorker.class, 900, TimeUnit.SECONDS);

        manager.enqueueUniquePeriodicWork("unique", ExistingPeriodicWorkPolicy.UPDATE, builder.build());
        // ;
    }

    @Override
    public void onTerminate(){
        super.onTerminate();

        OrientalismApplicationDB.rem();

        Log.e(OrientalismApplication.Tag, "onTerminate()");
    }
}
