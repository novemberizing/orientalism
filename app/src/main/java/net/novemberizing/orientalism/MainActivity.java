package net.novemberizing.orientalism;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import net.novemberizing.core.StringUtil;
import net.novemberizing.orientalism.db.article.Article;
import net.novemberizing.orientalism.db.article.ArticleRepository;
import net.novemberizing.orientalism.db.article.ArticleViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView url;
    private TextView title;
    private TextView pronunciation;
    private TextView summary;
    private TextView story;
    private ImageView badge;
    private NestedScrollView scrollView;
    private LiveData<List<Article>> random = null;
    private ArticleViewModel model;
    private LiveData<Article> article = null;
    private Gson gson;

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
        scrollView = findViewById(R.id.main_activity_scroll_view);
        badge = findViewById(R.id.main_activity_bottom_navigation_view_button_today_badge);

        Button shareBtn = findViewById(R.id.main_activity_bottom_navigation_view_button_share);
        Button todayBtn = findViewById(R.id.main_activity_bottom_navigation_view_button_today);
        Button settingBtn = findViewById(R.id.main_activity_bottom_navigation_view_button_setting);
        FloatingActionButton nextBtn = findViewById(R.id.main_activity_floating_action_button);
        
        model = new ViewModelProvider(this).get(ArticleViewModel.class);

        gson = OrientalismApplicationGson.get();

        badge.setVisibility(View.INVISIBLE);

        article = model.recent();

        ArticleRepository.recentSync(this::recentSync);

        settingBtn.setOnClickListener(this::openSettingDialog);
        shareBtn.setOnClickListener(this::share);
        todayBtn.setOnClickListener(this::viewTodayArticle);
        nextBtn.setOnClickListener(this::viewNextArticle);

        Article recent = gson.fromJson(OrientalismApplicationPreference.str(this, OrientalismApplicationPreference.MAIN), Article.class);

        setArticle(this, recent);

        OrientalismApplicationNotification.set(this, recent.title, recent.summary);
    }

    private void setArticle(Context context, Article article) {
        if(article != null) {
            setUrl(article.url);
            setTitle(article.title);
            setPronunciation(article.pronunciation);
            setStory(article.story);
            setSummary(article.summary);
            OrientalismApplicationPreference.set(context, OrientalismApplicationPreference.MAIN, gson.toJson(article));
        }
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
    private void share(View view) {
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
    }
    private void viewNextArticle(View view) {
        if(random == null) {
            random = model.random();
            random.observe(this, list -> {
                for(Article item : list) {
                    if(!item.url.equals(StringUtil.get(url.getText()))) {
                        setArticle(this, item);
                        scrollView.fullScroll(ScrollView.FOCUS_UP);
                        random = null;
                        return;
                    }
                }
            });
        }
    }
    private void viewTodayArticle(View view) {
        article.observe(this, o-> {
            setArticle(this, o);
            OrientalismApplicationNotification.set(this, o.title, o.summary);
            scrollView.fullScroll(ScrollView.FOCUS_UP);
            badge.setVisibility(View.INVISIBLE);
        });
    }
    private void recentSync(Article article) {
        if(article != null) {
            String title = OrientalismApplicationPreference.str(this, OrientalismApplicationPreference.RECENT);
            if(!title.equals(article.title)) {
                runOnUiThread(() -> badge.setVisibility(View.VISIBLE));
                OrientalismApplicationNotification.set(this, article.title, article.summary);
                OrientalismApplicationPreference.set(this, OrientalismApplicationPreference.RECENT, article.title);
            }
        }
    }
    private void openSettingDialog(View view) {
        OrientalismApplicationDialog.showSettingDialog(this);
    }
}
