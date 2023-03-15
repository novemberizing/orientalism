package net.novemberizing.orientalism;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import net.novemberizing.core.StringUtil;
import net.novemberizing.orientalism.db.article.Article;
import net.novemberizing.orientalism.db.article.ArticleRepository;
import net.novemberizing.orientalism.db.article.ArticleViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String Tag = "MainActivity";
    private TextView url;
    private TextView title;
    private TextView pronunciation;
    private TextView summary;
    private TextView story;
    private Button share;
    private FloatingActionButton fab;

    private LiveData<Article> random = null;

    private ArticleViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        url = findViewById(R.id.main_activity_url);
        title = findViewById(R.id.main_activity_title);
        pronunciation = findViewById(R.id.main_activity_pronunciation);
        story = findViewById(R.id.main_activity_story);
        summary = findViewById(R.id.main_activity_summary);
        share = findViewById(R.id.main_activity_bottom_navigation_view_button_share);
        fab = findViewById(R.id.main_activity_floating_action_button);

        model = new ViewModelProvider(this).get(ArticleViewModel.class);

        LiveData<Article> article = model.recent();
        LiveData<List<Article>> articles = model.articles();

        Gson gson = OrientalismApplicationGson.get();

        ArticleRepository.recentSync(o -> {
            if(o != null) {
                Log.d(Tag, "ArticleRepository.recentSync(...)");
                Log.e(Tag, gson.toJson(o));
            }
        });

        share.setOnClickListener(view -> {
            String value = StringUtil.get(title.getText()) +
                    " (" +
                    StringUtil.get(pronunciation.getText()) +
                    ")\n\n" +
                    StringUtil.get(summary.getText()) +
                    "\n\n" +
                    StringUtil.get(url.getText());

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, value);
            startActivity(Intent.createChooser(intent, value));
        });



        fab.setOnClickListener(view -> {
            Log.e(Tag, "fab click");
            if(random == null) {
                random = model.random();
                random.observe(this, o -> {
                    setUrl(o.url);
                    setTitle(o.title);
                    setPronunciation(o.pronunciation);
                    setStory(o.story);
                    setSummary(o.summary);
                    random = null;
                });
            }
        });


        {
            String json = OrientalismApplicationPreference.str(this, OrientalismApplicationPreference.MAIN);
            Article o = gson.fromJson(json, Article.class);
            setUrl(o.url);
            setTitle(o.title);
            setPronunciation(o.pronunciation);
            setStory(o.story);
            setSummary(o.summary);
        }

        article.observe(this, o -> {
            if(o != null) {
                Log.e(Tag, gson.toJson(o));
                setUrl(o.url);
                setTitle(o.title);
                setPronunciation(o.pronunciation);
                setStory(o.story);
                setSummary(o.summary);
                OrientalismApplicationPreference.set(this, "main", gson.toJson(o));
            }
        });


        // TODO: VERSION 1 REFACTORING
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
    private void setUrl(String value) {
        url.setText(value);
    }
    private void setTitle(String value) {
        title.setText(value);
    }

    private void setPronunciation(String value) {
        pronunciation.setText(value);
    }

    private void setSummary(String value) {
//        SpannableString string = new SpannableString(value);
//        Typeface font = ResourcesCompat.getFont(this, R.font.notoserifkr_regular);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            string.setSpan(new TypefaceSpan(font), 0, string.length(), SPAN_INCLUSIVE_INCLUSIVE);
//        }
//        string.setSpan(new RelativeSizeSpan(1.0f), 0,value.length(), SPAN_INCLUSIVE_INCLUSIVE);
//        summary.setText(string);
        summary.setText(value);
    }

    public void setStory(String value) {
        SpannableStringBuilder builder = OrientalismApplicationHtml.from(value);
        builder.setSpan(new RelativeSizeSpan(1.2f), 0, builder.length(), SPAN_INCLUSIVE_INCLUSIVE);
        Typeface font = ResourcesCompat.getFont(this, R.font.notoserifkr_regular);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            builder.setSpan(new TypefaceSpan(font), 0, builder.length(), SPAN_INCLUSIVE_INCLUSIVE);
        }
        story.setText(builder);
    }
}
