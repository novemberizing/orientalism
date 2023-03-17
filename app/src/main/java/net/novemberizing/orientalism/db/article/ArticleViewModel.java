package net.novemberizing.orientalism.db.article;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ArticleViewModel extends AndroidViewModel {
    private final ArticleRepository repository;
    private final LiveData<Article> recent;

    public ArticleViewModel(Application application) {
        super(application);

        repository = new ArticleRepository();
        recent = repository.recent();
    }

    public LiveData<Article> recent(){
        return recent;
    }

    public LiveData<List<Article>> random(){
        return repository.random();
    }
}
