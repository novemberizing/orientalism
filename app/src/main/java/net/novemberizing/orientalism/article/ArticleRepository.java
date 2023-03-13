package net.novemberizing.orientalism.article;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import net.novemberizing.core.GsonUtil;
import net.novemberizing.core.JsoupUtil;
import net.novemberizing.core.objects.Listener;
import net.novemberizing.orientalism.OrientalismApplicationDB;
import net.novemberizing.orientalism.OrientalismApplicationGson;
import net.novemberizing.orientalism.OrientalismApplicationVolley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;


public class ArticleRepository {
    private static final String Tag = "ArticleRepository";
    private static final String indexJsonUrl = "https://novemberizing.github.io/orientalism/index.json";

    private static RequestFuture<JsonArray> requestFutureSync = null;
    private static RequestFuture<JsonArray> requestFutureRecentSync = null;

    public static void recentSync() {
        synchronized (ArticleRepository.class) {
            ArticleRepository repository = new ArticleRepository();
            requestFutureRecentSync = RequestFuture.newFuture();
            requestFutureRecentSync.setRequest(OrientalismApplicationVolley.json(indexJsonUrl,
                    JsonArray.class,
                    array -> {
                        String url = GsonUtil.str(GsonUtil.get(array, 0));
                        RequestFuture<JsonArray> req = RequestFuture.newFuture();
                        req.setRequest(OrientalismApplicationVolley.json(url,
                                JsonArray.class,
                                articles->{
                                    Gson gson = OrientalismApplicationGson.get();
                                    JsonElement element = GsonUtil.get(articles, 0);
                                    Article article = gson.fromJson(element, Article.class);
                                    OrientalismApplicationVolley.str(article.url,
                                            html -> {
                                                article.story = JsoupUtil.str(html, "orientalism-content");
                                                repository.insert(article, o-> {
                                                    synchronized (ArticleRepository.class) {
                                                        requestFutureRecentSync = null;
                                                    }
                                                });
                                                synchronized (ArticleRepository.class) {
                                                    requestFutureRecentSync = null;
                                                }
                                            },
                                            exception->{
                                                Log.e(Tag, exception.toString());
                                                synchronized (ArticleRepository.class) {
                                                    requestFutureRecentSync = null;
                                                }
                                            });
                                },
                                exception->{
                                    Log.e(Tag, exception.toString());
                                    synchronized (ArticleRepository.class) {
                                        requestFutureRecentSync = null;
                                    }
                                }));
                    },
                    exception -> {
                        Log.e(Tag, exception.toString());
                        synchronized (ArticleRepository.class) {
                            requestFutureRecentSync = null;
                        }
                    }));
        }
    }

    public static void sync() {
        synchronized (ArticleRepository.class) {
            if(requestFutureSync == null) {
                ArticleRepository repository = new ArticleRepository();
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
                                            Gson gson = OrientalismApplicationGson.get();
                                            for(JsonElement item : articles) {
                                                Article article = gson.fromJson(item, Article.class);
                                                OrientalismApplicationVolley.str(article.url,
                                                        html -> {
                                                            article.story = JsoupUtil.str(html, "orientalism-content");
                                                            repository.insert(article, o-> {
                                                                synchronized (ArticleRepository.class) {
                                                                    requestMap.remove(url);
                                                                    if(requestMap.size() == 0) {
                                                                        requestFutureSync = null;
                                                                    }
                                                                }
                                                            });
                                                        },
                                                        exception -> {
                                                            synchronized (ArticleRepository.class) {
                                                                requestMap.remove(url);
                                                                if(requestMap.size() == 0) {
                                                                    requestFutureSync = null;
                                                                }
                                                            }
                                                        });
                                            }
                                        },
                                        exception->{
                                            Log.e(Tag, exception.toString());
                                            synchronized (ArticleRepository.class) {
                                                requestMap.remove(url);
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

    public void insert(Article article, Listener<Article> listener) {
        OrientalismApplicationDB.execute(()->{
            try {
                articleDao.insert(article);
            } catch(Exception e) {
                Log.e(Tag, e.toString());
            }
            if(listener != null) {
                listener.on(article);
            }
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
