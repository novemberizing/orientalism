package net.novemberizing.orientalism.article;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ArticleViewModel extends AndroidViewModel {
    private ArticleRepository repository;

    private final LiveData<List<Article>> articles;
    private final LiveData<Article> recent;

    public ArticleViewModel(Application application) {
        super(application);

        repository = new ArticleRepository();
        articles = repository.articles();
        recent = repository.recent();
    }

    public LiveData<Article> recent(){
        return recent;
    }

    public LiveData<List<Article>> articles() {
        return articles;
    }

    public void insert(Article article) {
        repository.insert(article);
    }
}
