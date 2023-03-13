package net.novemberizing.orientalism.article;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import net.novemberizing.core.objects.Listener;
import net.novemberizing.orientalism.OrientalismApplicationDB;
import net.novemberizing.orientalism.OrientalismApplicationVolley;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ArticleRepository {
    private static final String Tag = "ArticleRepository";
    private static final String indexJsonUrl = "https://novemberizing.github.io/orientalism/index.json";
    private static Request<JsonElement> requestSync = null;

    public static void onSyncFail(VolleyError error) {
        if(error != null) {
            Log.e(Tag, error.toString());
        }

        synchronized (ArticleRepository.class) {
            requestSync = null;
        }
    }

    private static void syncByIndexJson(JsonElement json, Listener<List<Article>> listener) {
        JsonArray array = json.getAsJsonArray();
        for(JsonElement element : array) {
            Log.e(Tag, element.toString());
        }

        synchronized (ArticleRepository.class) {
            requestSync = null;
        }
    }

    public static void sync(Listener<List<Article>> listener) {
        synchronized (ArticleRepository.class) {
            if(requestSync == null) {
                requestSync = OrientalismApplicationVolley.json(indexJsonUrl,
                                                                JsonElement.class,
                                                                json -> syncByIndexJson(json, listener),
                                                                ArticleRepository::onSyncFail);
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
