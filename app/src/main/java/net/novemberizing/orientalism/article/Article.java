package net.novemberizing.orientalism.article;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import net.novemberizing.core.DateUtil;
import net.novemberizing.orientalism.ExampleContent;

@Entity(tableName = "article", indices = {@Index(value="url", unique = true)})
public class Article {
    public static Article gen(String url) {
        Article o = new Article();

        o.url = url;
        o.title = "左袒";
        o.summary = "웃옷의 왼쪽 어깨를 벗는다. 남에게 편을 드는 것, 곧 동의하는 일을 의미한다.";
        o.story = ExampleContent.article1();
        o.datetime = DateUtil.str();

        return o;
    }
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "url")
    public String url;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "summary")
    public String summary;
    @ColumnInfo(name = "story")
    public String story;
    @ColumnInfo(name = "datetime")
    public String datetime;

}
