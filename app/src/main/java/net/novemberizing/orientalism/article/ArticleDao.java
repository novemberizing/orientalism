package net.novemberizing.orientalism.article;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ArticleDao {
    @Query("SELECT * FROM article ORDER BY uid DESC")
    LiveData<List<Article>> all();

    @Query("SELECT * FROM article ORDER BY uid DESC LIMIT 1")
    LiveData<Article> recent();

    @Insert
    void insert(Article article);

    @Delete
    void delete(Article article);
}
