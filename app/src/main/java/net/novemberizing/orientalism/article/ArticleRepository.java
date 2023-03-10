package net.novemberizing.orientalism.article;

import android.util.Log;

import androidx.lifecycle.LiveData;

import net.novemberizing.orientalism.OrientalismApplicationDB;

import java.util.List;

public class ArticleRepository {
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
