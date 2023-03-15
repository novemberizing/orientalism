package net.novemberizing.orientalism;

import android.app.Application;
import android.util.Log;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.gson.Gson;

import net.novemberizing.orientalism.db.article.Article;
import net.novemberizing.orientalism.db.article.ArticleDailyWorker;
import net.novemberizing.orientalism.db.article.ArticleRepository;

import java.util.concurrent.TimeUnit;

public class OrientalismApplication extends Application {
    public static final String Tag = "OrientalismApplication";
    @Override
    public void onCreate(){
        super.onCreate();

        OrientalismApplicationGson.gen();
        OrientalismApplicationDB.gen(this);
        OrientalismApplicationVolley.gen(this);
        OrientalismApplicationPreference.gen(this);
        OrientalismApplicationWork.gen(this);
    }

    @Override
    public void onTerminate(){
        super.onTerminate();

        OrientalismApplicationDB.rem();

        Log.e(OrientalismApplication.Tag, "onTerminate()");
    }
}
