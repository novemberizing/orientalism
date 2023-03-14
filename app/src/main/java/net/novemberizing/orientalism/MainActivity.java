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
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import net.novemberizing.orientalism.db.article.Article;
import net.novemberizing.orientalism.db.article.ArticleRepository;
import net.novemberizing.orientalism.db.article.ArticleViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String Tag = "MainActivity";
    private TextView title;
    private TextView pronunciation;
    private TextView summary;
    private TextView story;
    private LinearLayout layout;

    private ArticleViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        title = findViewById(R.id.main_activity_title);
        pronunciation = findViewById(R.id.main_activity_pronunciation);
        story = findViewById(R.id.main_activity_story);
        summary = findViewById(R.id.main_activity_summary);
        layout = findViewById(R.id.main_activity_layout);

        // layout.setBackgroundResource(R.drawable.bamboo_g0d4e6fe68_1920);

        ArticleRepository.recentSync(article -> Log.d(Tag, "ArticleRepository.recentSync(...)"));

        model = new ViewModelProvider(this).get(ArticleViewModel.class);

        LiveData<Article> article = model.recent();
        LiveData<List<Article>> articles = model.articles();

        article.observe(this, o -> {
            if(o != null) {
                setTitle(o.title);
                setPronunciation(o.pronunciation);
                setStory(o.story);
                setSummary(o.summary);
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

    private void setPronunciation(String value) {
        SpannableString string = new SpannableString(value);
        Typeface font = ResourcesCompat.getFont(this, R.font.notoserifkr_regular);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            string.setSpan(new TypefaceSpan(font), 0, string.length(), SPAN_INCLUSIVE_INCLUSIVE);
        }
        string.setSpan(new RelativeSizeSpan(1.6f), 0,value.length(), SPAN_INCLUSIVE_INCLUSIVE);
        pronunciation.setText(string);
    }

    private void setSummary(String value) {
        SpannableString string = new SpannableString(value);
        Typeface font = ResourcesCompat.getFont(this, R.font.notoserifkr_regular);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            string.setSpan(new TypefaceSpan(font), 0, string.length(), SPAN_INCLUSIVE_INCLUSIVE);
        }
        string.setSpan(new RelativeSizeSpan(1.0f), 0,value.length(), SPAN_INCLUSIVE_INCLUSIVE);
        summary.setText(string);
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
