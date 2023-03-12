package net.novemberizing.orientalism;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.novemberizing.orientalism.article.Article;
import net.novemberizing.orientalism.article.ArticleRepository;
import net.novemberizing.orientalism.article.ArticleViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String Tag = "MainActivity";
    private TextView title;
    private TextView secondaryTitle;
    private TextView story;

    private ArticleViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        title = findViewById(R.id.main_activity_title);
        secondaryTitle = findViewById(R.id.main_activity_secondary_title);
        story = findViewById(R.id.main_activity_story);

        ArticleRepository.sync();

        // Request<JsonElement> req = OrientalismApplicationVolley.json("https://novemberizing.github.io/orientalism/index.json", JsonElement.class, res->{ Log.e(Tag, res.toString()); }, error->{ error.printStackTrace();});

        model = new ViewModelProvider(this).get(ArticleViewModel.class);

        LiveData<Article> article = model.recent();
        LiveData<List<Article>> articles = model.articles();

        articles.observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                for(Article value : articles) {
                    Log.e("Main", Integer.toString(value.uid));
                    Log.e("Main", value.title);
                    Log.e("Main", value.url);
                    Log.e("Main", value.summary);
                    Log.e("Main", value.story);
                    Log.e("Main", value.datetime);
                }
            }
        });

        article.observe(this, new Observer<Article>() {
            @Override
            public void onChanged(Article article) {
                Log.e("Main", Integer.toString(article.uid));
                Log.e("Main", article.title);
                Log.e("Main", article.url);
                Log.e("Main", article.summary);
                Log.e("Main", article.story);
                Log.e("Main", article.datetime);
            }
        });

        // model.insert(Article.gen("https://novemberizing.github.io/orientalism/posts/2023/03/09/%E5%B7%A6%E8%A2%92.html"));

//        // article.insert(Article.gen());
//        // LiveData<List<Article>> list = article.articles();
//        list.observe(this, new Observer<List<Article>>() {
//
//            @Override
//            public void onChanged(List<Article> articles) {
//                Log.e("Main", "onChanged");
//                for(Article value : articles) {
//                    Log.e("Main", Integer.toString(value.uid));
//                    Log.e("Main", value.title);
//                    Log.e("Main", value.url);
//                    Log.e("Main", value.summary);
//                    Log.e("Main", value.story);
//                    Log.e("Main", value.datetime);
//                }
//            }
//        });

        // OrientalismApplicationDB orientalismApplicationDB = OrientalismApplicationDB.get(this);

        // 데이터베이스에 삽입하기

        setTitle("懲羹吹韲");
        setSecondaryTitle("징갱취제");
        setStory(ExampleContent.get());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = new String("net.novemberizing.orientalism");
            String description = new String("hello world");
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("net.novemberizing.orientalism", name, importance);
            channel.setDescription(description);
            channel.setShowBadge(false);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "net.novemberizing.orientalism")
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle("懲羹吹韲")
                .setContentText("뜨거운 국에 덴 나머지 냉채를 불고 먹는다. 곧 한 번 실패한 것 때문에 모든 일에 지나치게 조심하는 것을 비유하는 말이다.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSilent(true)
                .setLocalOnly(true)
                .setOngoing(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1001, builder.build());
    }

    private void setArticle(Article article) {

    }

    private void setTitle(String value) {
        SpannableString string = new SpannableString(value);
        Typeface font = ResourcesCompat.getFont(this, R.font.notoserifhk_light);
        string.setSpan(new RelativeSizeSpan(4f), 0,value.length(), SPAN_INCLUSIVE_INCLUSIVE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Log.e("Simplefeed", "font apply");
            string.setSpan(new TypefaceSpan(font), 0,value.length(), SPAN_INCLUSIVE_INCLUSIVE);
        } else {
            Log.e("Simplefeed", "font not apply");
        }
        title.setText(string);
    }

    private void setSecondaryTitle(String value) {
        SpannableString string = new SpannableString(value);
        Typeface font = ResourcesCompat.getFont(this, R.font.notoserifhk_light);
        string.setSpan(new RelativeSizeSpan(2f), 0,value.length(), SPAN_INCLUSIVE_INCLUSIVE);
        secondaryTitle.setText(string);
    }

    public void setStory(String value) {
        story.setText(value);
    }
}
