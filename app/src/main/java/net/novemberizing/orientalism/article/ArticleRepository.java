package net.novemberizing.orientalism.article;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;

import com.android.volley.Response;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import net.novemberizing.orientalism.OrientalismApplicationDB;
import net.novemberizing.orientalism.OrientalismApplicationGson;
import net.novemberizing.orientalism.OrientalismApplicationVolley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;


public class ArticleRepository {
    private static final String Tag = "ArticleRepository";
    private static final String indexJsonUrl = "https://novemberizing.github.io/orientalism/index.json";

    private static RequestFuture<JsonArray> requestFutureSync = null;

    public static void sync() {
        synchronized (ArticleRepository.class) {
            if(requestFutureSync == null) {
                requestFutureSync = RequestFuture.newFuture();
                requestFutureSync.setRequest(OrientalismApplicationVolley.json(indexJsonUrl,
                        JsonArray.class,
                        array-> {
                            LinkedHashMap<String, RequestFuture<JsonArray>> requestMap = new LinkedHashMap<>();
                            for(JsonElement element : array) {
                                String url = element.getAsString();
                                RequestFuture<JsonArray> req = RequestFuture.newFuture();
                                requestMap.put(url, req);
                                req.setRequest(OrientalismApplicationVolley.json(url,
                                        JsonArray.class,
                                        articles->{
                                            Log.e(Tag, articles.toString());
                                            Gson gson = OrientalismApplicationGson.get();
                                            for(JsonElement item : articles) {
                                                Article article = gson.fromJson(item, Article.class);
                                                Log.e(Tag, article.url);
                                                Log.e(Tag, article.title);
                                                Log.e(Tag, article.summary);
                                                Log.e(Tag, article.story);
                                                Log.e(Tag, article.datetime);
                                                try {
                                                    Document document = Jsoup.connect(article.url).get();
                                                } catch (IOException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                article.story = null;



                                                // article.story = Jsoup
                                            }
                                            synchronized (ArticleRepository.class) {
                                                if(requestMap.size() == 0) {
                                                    requestFutureSync = null;
                                                }
                                            }
                                        },
                                        exception->{
                                            Log.e(Tag, exception.toString());
                                            synchronized (ArticleRepository.class) {
                                                if(requestMap.size() == 0) {
                                                    requestFutureSync = null;
                                                }
                                            }
                                        }));
                            }
                        },
                        exception->{
                            Log.e(Tag, exception.toString());
                            synchronized (ArticleRepository.class) {
                                requestFutureSync = null;
                            }
                        }));
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
