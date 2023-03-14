package net.novemberizing.orientalism.db.article;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import net.novemberizing.core.DateUtil;
import net.novemberizing.core.FileUtil;
import net.novemberizing.orientalism.ExampleContent;

import java.io.File;
import java.util.Date;

@Entity(tableName = "article", indices = {@Index(value="url", unique = true), @Index(value="category")})
public class Article {
    public static Integer toCategory(String url) {
        Uri uri = Uri.parse(url);
        File f = new File(uri.getPath());
        return Integer.valueOf(FileUtil.getOnlyName(f));
    }
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "url")
    public String url;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "pronunciation")
    public String pronunciation;
    @ColumnInfo(name = "summary")
    public String summary;
    @ColumnInfo(name = "story")
    public String story;
    @ColumnInfo(name = "datetime")
    public Date datetime;
    @ColumnInfo(name = "category")
    public Integer category;
}
