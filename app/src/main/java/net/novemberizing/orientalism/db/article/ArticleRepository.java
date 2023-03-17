package net.novemberizing.orientalism.db.article;

import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import net.novemberizing.core.GsonUtil;
import net.novemberizing.core.JsoupUtil;
import net.novemberizing.core.objects.Listener;
import net.novemberizing.orientalism.application.OrientalismApplicationDB;
import net.novemberizing.orientalism.application.OrientalismApplicationGson;
import net.novemberizing.orientalism.application.OrientalismApplicationVolley;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class ArticleRepository {
    private static final String Tag = "ArticleRepository";
    private static final String indexJsonUrl = "https://novemberizing.github.io/orientalism/index.json";

    private static RequestFuture<JsonArray> requestFutureSync = null;
    private static RequestFuture<JsonArray> requestFutureRecentSync = null;
    private static RequestFuture<JsonArray> requestFutureRefreshSync = null;

    public static void recentSync(Listener<Article> listener) {
        synchronized (ArticleRepository.class) {
            ArticleRepository repository = new ArticleRepository();
            requestFutureRecentSync = RequestFuture.newFuture();
            requestFutureRecentSync.setRequest(OrientalismApplicationVolley.json(indexJsonUrl,
                    JsonArray.class,
                    array -> {
                        String url = GsonUtil.str(GsonUtil.get(GsonUtil.obj(GsonUtil.get(array, 0)), "url"));
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
                                                        listener.on(article);
                                                    }
                                                });
                                                synchronized (ArticleRepository.class) {
                                                    requestFutureRecentSync = null;
                                                    listener.on(null);
                                                }
                                            },
                                            exception->{
                                                Log.e(Tag, exception.toString());
                                                synchronized (ArticleRepository.class) {
                                                    requestFutureRecentSync = null;
                                                    listener.on(null);
                                                }
                                            });
                                },
                                exception->{
                                    Log.e(Tag, exception.toString());
                                    synchronized (ArticleRepository.class) {
                                        requestFutureRecentSync = null;
                                        listener.on(null);
                                    }
                                }));
                    },
                    exception -> {
                        Log.e(Tag, exception.toString());
                        synchronized (ArticleRepository.class) {
                            requestFutureRecentSync = null;
                            listener.on(null);
                        }
                    }));
        }
    }

    @SuppressWarnings("unused")
    public static void sync(Listener<List<Article>> listener) {
        synchronized (ArticleRepository.class) {
            if(requestFutureSync == null) {
                ArrayList<Article> result = new ArrayList<>();
                ArticleRepository repository = new ArticleRepository();
                requestFutureSync = RequestFuture.newFuture();
                requestFutureSync.setRequest(OrientalismApplicationVolley.json(indexJsonUrl,
                        JsonArray.class,
                        array -> {
                            Thread thread = new Thread(() -> {
                                LinkedHashMap<String, RequestFuture<JsonArray>> requestMap = new LinkedHashMap<>();
                                synchronized (ArticleRepository.class) {
                                    for (JsonElement element : array) {
                                        String url = GsonUtil.str(GsonUtil.get(GsonUtil.obj(element), "url"));
                                        Integer total = GsonUtil.integer(GsonUtil.get(GsonUtil.obj(element), "total"));
                                        Integer category = Article.toCategory(url);
                                        if(repository.count(category) < total) {
                                            RequestFuture<JsonArray> req = RequestFuture.newFuture();
                                            requestMap.put(url, req);
                                            req.setRequest(OrientalismApplicationVolley.json(url,
                                                    JsonArray.class,
                                                    articles -> {
                                                        Gson gson = OrientalismApplicationGson.get();
                                                        LinkedHashMap<String, RequestFuture<String>> articleMap = new LinkedHashMap<>();
                                                        synchronized (ArticleRepository.class) {
                                                            for (JsonElement item : articles) {
                                                                Article article = gson.fromJson(item, Article.class);
                                                                RequestFuture<String> articleFuture = RequestFuture.newFuture();
                                                                articleMap.put(article.url, articleFuture);
                                                                articleFuture.setRequest(OrientalismApplicationVolley.str(article.url,
                                                                        html -> {
                                                                            article.story = JsoupUtil.str(html, "orientalism-content");
                                                                            result.add(article);
                                                                            repository.insert(article, o -> {
                                                                                synchronized (ArticleRepository.class) {
                                                                                    articleMap.remove(article.url);
                                                                                    if (articleMap.size() == 0) {
                                                                                        requestMap.remove(url);
                                                                                        if (requestMap.size() == 0) {
                                                                                            requestFutureSync = null;
                                                                                            if (listener != null) {
                                                                                                listener.on(result);
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            });
                                                                        },
                                                                        exception -> {
                                                                            synchronized (ArticleRepository.class) {
                                                                                articleMap.remove(article.url);
                                                                                if (articleMap.size() == 0) {
                                                                                    requestMap.remove(url);
                                                                                    if (requestMap.size() == 0) {
                                                                                        requestFutureSync = null;
                                                                                        if (listener != null) {
                                                                                            listener.on(result);
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }));
                                                            }
                                                        }
                                                    },
                                                    exception -> {
                                                        Log.e(Tag, exception.toString());
                                                        synchronized (ArticleRepository.class) {
                                                            requestMap.remove(url);
                                                            if (requestMap.size() == 0) {
                                                                requestFutureSync = null;
                                                                if (listener != null) {
                                                                    listener.on(result);
                                                                }
                                                            }
                                                        }
                                                    }));
                                        } else {
                                            synchronized (ArticleRepository.class) {
                                                requestFutureSync = null;
                                                if(listener != null) {
                                                    listener.on(result);
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                            thread.start();
                        },
                        exception->{
                            Log.e(Tag, exception.toString());
                            synchronized (ArticleRepository.class) {
                                requestFutureSync = null;
                                if(listener != null) {
                                    listener.on(result);
                                }
                            }
                        }));
            }
        }
    }

    public static void refreshSync(Listener<List<Article>> listener) {
        synchronized (ArticleRepository.class) {
            if(requestFutureRefreshSync == null) {
                ArrayList<Article> result = new ArrayList<>();
                ArticleRepository repository = new ArticleRepository();
                requestFutureRefreshSync = RequestFuture.newFuture();
                requestFutureRefreshSync.setRequest(OrientalismApplicationVolley.json(indexJsonUrl,
                        JsonArray.class,
                        array-> {
                            LinkedHashMap<String, RequestFuture<JsonArray>> requestMap = new LinkedHashMap<>();
                            synchronized (ArticleRepository.class) {
                                for(JsonElement element : array) {
                                    String url = GsonUtil.str(GsonUtil.get(GsonUtil.obj(element), "url"));
                                    RequestFuture<JsonArray> req = RequestFuture.newFuture();
                                    requestMap.put(url, req);
                                    req.setRequest(OrientalismApplicationVolley.json(url,
                                            JsonArray.class,
                                            articles->{
                                                Gson gson = OrientalismApplicationGson.get();
                                                LinkedHashMap<String, RequestFuture<String>> articleMap = new LinkedHashMap<>();
                                                synchronized (ArticleRepository.class) {
                                                    for(JsonElement item : articles) {
                                                        Article article = gson.fromJson(item, Article.class);
                                                        RequestFuture<String> articleFuture = RequestFuture.newFuture();
                                                        articleMap.put(article.url, articleFuture);
                                                        articleFuture.setRequest(OrientalismApplicationVolley.str(article.url,
                                                                html -> {
                                                                    article.story = JsoupUtil.str(html, "orientalism-content");
                                                                    result.add(article);
                                                                    repository.insert(article, o-> {
                                                                        synchronized (ArticleRepository.class) {
                                                                            articleMap.remove(article.url);
                                                                            if(articleMap.size() == 0) {
                                                                                requestMap.remove(url);
                                                                                if(requestMap.size() == 0) {
                                                                                    requestFutureRefreshSync = null;
                                                                                    if(listener != null) {
                                                                                        listener.on(result);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    });
                                                                },
                                                                exception -> {
                                                                    synchronized (ArticleRepository.class) {
                                                                        articleMap.remove(article.url);
                                                                        if(articleMap.size() == 0) {
                                                                            requestMap.remove(url);
                                                                            if(requestMap.size() == 0) {
                                                                                requestFutureRefreshSync = null;
                                                                                if(listener != null) {
                                                                                    listener.on(result);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }));
                                                    }
                                                }
                                            },
                                            exception->{
                                                Log.e(Tag, exception.toString());
                                                synchronized (ArticleRepository.class) {
                                                    requestMap.remove(url);
                                                    if(requestMap.size() == 0) {
                                                        requestFutureRefreshSync = null;
                                                        if(listener != null) {
                                                            listener.on(result);
                                                        }
                                                    }
                                                }
                                            }));
                                }
                            }
                        },
                        exception->{
                            Log.e(Tag, exception.toString());
                            synchronized (ArticleRepository.class) {
                                requestFutureRefreshSync = null;
                                if(listener != null) {
                                    listener.on(result);
                                }
                            }
                        }));
            }
        }
    }

    private final ArticleDao articleDao;
    private final LiveData<Article> recent;

    public ArticleRepository() {
        OrientalismApplicationDB db = OrientalismApplicationDB.get();

        articleDao = db.articleDao();
        recent = articleDao.recent();
    }

    public LiveData<List<Article>> random() {
        return articleDao.random();
    }

    public LiveData<Article> get(String title){
        return articleDao.get(title);
    }

    public void insert(Article article, Listener<Article> listener) {
        OrientalismApplicationDB.execute(()->{
            try {
                articleDao.insert(article);
            } catch(SQLiteConstraintException e) {
                Log.d(Tag, e.toString());
            } catch(Exception e) {
                Log.e(Tag, e.toString());
            }
            if(listener != null) {
                listener.on(article);
            }
        });
    }

    public LiveData<Article> recent(){
        return recent;
    }

    public Integer count(Integer category) {
        return articleDao.count(category);
    }
}
