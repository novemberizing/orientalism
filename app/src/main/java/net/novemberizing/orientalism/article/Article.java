package net.novemberizing.orientalism.article;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import net.novemberizing.core.DateUtil;
import net.novemberizing.orientalism.ExampleContent;

import java.util.Date;

@Entity(tableName = "article", indices = {@Index(value="url", unique = true)})
public class Article {
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
    public String datetime;
}
