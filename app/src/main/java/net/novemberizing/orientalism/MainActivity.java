package net.novemberizing.orientalism;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
    private NestedScrollView scroll;
    private Button today;
    private Button setting;

    private LiveData<List<Article>> random = null;

    private ArticleViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(OrientalismApplication.getOrientation(this)==ORIENTATION_PORTRAIT) {
            setContentView(R.layout.main_activity_portrait_layout);
        } else {
            setContentView(R.layout.main_activity_landscape_layout);
        }

        url = findViewById(R.id.main_activity_url);
        title = findViewById(R.id.main_activity_title);
        pronunciation = findViewById(R.id.main_activity_pronunciation);
        story = findViewById(R.id.main_activity_story);
        summary = findViewById(R.id.main_activity_summary);
        share = findViewById(R.id.main_activity_bottom_navigation_view_button_share);
        fab = findViewById(R.id.main_activity_floating_action_button);
        scroll = findViewById(R.id.main_activity_scroll_view);
        today = findViewById(R.id.main_activity_bottom_navigation_view_button_today);
        setting = findViewById(R.id.main_activity_bottom_navigation_view_button_setting);

        model = new ViewModelProvider(this).get(ArticleViewModel.class);

        Log.e(Tag, "=======================================> onCreate(...)");

        LiveData<Article> article = model.recent();

        Gson gson = OrientalismApplicationGson.get();

        ArticleRepository.recentSync(o -> {
            if(o != null) {
                Log.d(Tag, "ArticleRepository.recentSync(...)");
                Log.e(Tag, gson.toJson(o));
            }
        });

        setting.setOnClickListener(view -> OrientalismApplicationDialog.showSettingDialog(this));

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
                random.observe(this, list -> {
                    for(Article item : list) {
                        if(!item.url.equals(StringUtil.get(url.getText()))) {
                            setUrl(item.url);
                            setTitle(item.title);
                            setPronunciation(item.pronunciation);
                            setStory(item.story);
                            setSummary(item.summary);
                            scroll.fullScroll(ScrollView.FOCUS_UP);
                            random = null;
                            return;
                        }
                    }
                });
            }
        });

        today.setOnClickListener(view -> {
            article.observe(this, o-> {
                setUrl(o.url);
                setTitle(o.title);
                setPronunciation(o.pronunciation);
                setStory(o.story);
                setSummary(o.summary);
            });
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

        OrientalismApplicationNotification.set(this, "징갱취제", "요약");

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

    public void onConfigurationChanged(@NonNull Configuration config) {
        super.onConfigurationChanged(config);
        Log.e(Tag, "=========================> onConfigurationChanged");
        if(config.orientation == ORIENTATION_LANDSCAPE) {
            // ORIENTATION_PORTRAIT
            Log.e(Tag, "가로모드");
        } else {
            Log.e(Tag, "세로모드");
        }
    }
}
