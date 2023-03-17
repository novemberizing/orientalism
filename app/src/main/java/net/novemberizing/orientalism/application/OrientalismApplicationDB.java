package net.novemberizing.orientalism.application;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import net.novemberizing.orientalism.application.db.OrientalismApplicationDBTypeConverter;
import net.novemberizing.orientalism.db.article.Article;
import net.novemberizing.orientalism.db.article.ArticleDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Article.class}, version = 1)
@TypeConverters({OrientalismApplicationDBTypeConverter.class})
public abstract class OrientalismApplicationDB extends RoomDatabase {
    private static final String name = "orientalism.db";
    private static OrientalismApplicationDB instance;
    private static final ExecutorService pool = Executors.newFixedThreadPool(4);

    public static void gen(Context context) {
        synchronized (OrientalismApplicationDB.class) {
            if(instance == null) {
                instance = Room.databaseBuilder(context.getApplicationContext(), OrientalismApplicationDB.class, name)
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
