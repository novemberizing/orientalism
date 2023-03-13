package net.novemberizing.orientalism;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import net.novemberizing.orientalism.article.Article;
import net.novemberizing.orientalism.article.ArticleDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Article.class}, version = 1)
public abstract class OrientalismApplicationDB extends RoomDatabase {
    private static OrientalismApplicationDB instance;
    private static ExecutorService pool = Executors.newFixedThreadPool(4);

    public static void gen(Context context) {
        synchronized (OrientalismApplicationDB.class) {
            if(instance == null) {
                instance = Room.databaseBuilder(context.getApplicationContext(), OrientalismApplicationDB.class, "orientalism.db")
                        .build();
            }
        }
    }

    public static OrientalismApplicationDB get() {

        return instance;
    }

    public static void rem() {
        synchronized (OrientalismApplicationDB.class) {
            instance = null;
        }
    }

    public static void execute(Runnable runnable) {
        OrientalismApplicationDB.pool.execute(runnable);
    }

    public abstract ArticleDao articleDao();
}
