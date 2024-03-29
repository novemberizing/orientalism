package net.novemberizing.orientalism.db.article;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ArticleDao {
    @Query("SELECT * FROM article ORDER BY datetime DESC LIMIT 1")
    LiveData<Article> recent();

    @Query("SELECT COUNT(*) FROM article WHERE category=:category")
    Integer count(Integer category);

    @Query("SELECT * FROM article ORDER BY RANDOM() LIMIT 2")
    LiveData<List<Article>> random();

    @Query("SELECT * FROM article WHERE title=:title LIMIT 1")
    LiveData<Article> get(String title);

    @Insert
    void insert(Article article);
}
