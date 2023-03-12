package net.novemberizing.orientalism.article;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import net.novemberizing.orientalism.OrientalismApplicationDB;
import net.novemberizing.orientalism.OrientalismApplicationVolley;

import java.util.ArrayList;
import java.util.List;

public class ArticleRepository {
    private static final String Tag = "ArticleRepository";
    private static final String indexJsonUrl = "https://novemberizing.github.io/orientalism/index.json";
    private static Request<JsonElement> requestSync = null;
    private static Request<JsonElement> requestMonthlySync = null;

    public static void onSyncSuccess(JsonElement json) {
        JsonArray array = json.getAsJsonArray();
        for(JsonElement element : array) {
            String url = element.getAsString();
            Log.e(Tag, url);
        }
        synchronized (ArticleRepository.class) {
            requestSync = null;
        }
    }

    public static void onSyncFail(VolleyError error) {
        if(error != null) {
            Log.e(Tag, error.toString());
        }

        synchronized (ArticleRepository.class) {
            requestSync = null;
        }
    }

    /**
     * sync 는 done notify 를 ...
     *
     */
    public static void sync() {
        synchronized (ArticleRepository.class) {
            if(requestSync == null) {
                requestSync = OrientalismApplicationVolley.json(indexJsonUrl, JsonElement.class, ArticleRepository::onSyncSuccess, ArticleRepository::onSyncFail);
            }
        }
    }

    private ArticleDao articleDao;
    private LiveData<List<Article>> articles;
    private LiveData<Article> recent;

    public ArticleRepository() {
        OrientalismApplicationDB db = OrientalismApplicationDB.get();

        articleDao = db.articleDao();
        articles = articleDao.all();
        recent = articleDao.recent();
    }

    public void insert(Article article) {
        OrientalismApplicationDB.execute(()->{
            articleDao.insert(article);
            Log.e("Orientalism", "inserted");
        });
    }

    public LiveData<Article> recent(){
        return recent;
    }

    public LiveData<List<Article>> articles(){
        return articles;
    }
}
