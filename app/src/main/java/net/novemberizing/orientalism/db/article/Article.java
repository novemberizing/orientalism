package net.novemberizing.orientalism.db.article;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import net.novemberizing.core.DateUtil;
import net.novemberizing.core.FileUtil;

import java.io.File;
import java.util.Date;

@Entity(tableName = "article", indices = {@Index(value="url", unique = true), @Index(value="category")})
public class Article {
    public static Article main() {
        Article o = new Article();

        o.url = "https://novemberizing.github.io/orientalism/posts/2023/03/14/%E8%87%A5%E8%96%AA%E5%98%97%E8%86%BD.html";
        o.title = "臥薪嘗膽";
        o.pronunciation = "와신상담";
        o.summary = "섶 위에서 잠을 자고 쓸개를 핥는다. 곧 목적을 달성하기 위해 온갖 괴로움을 참고 견디는 것을 비유하는 말이다.";
        o.story = "<p>춘추시대 오나라와 월나라는 참으로 깊은 원수지간이었다. 몸시 나쁜 사이를 일컬어 ‘오월지간’이라고 하거니와, 원수끼리 같은 운명이 되어버린 것을 ‘오월동주’라고 하는 것처럼 복수를 하고 또 되갚음을 하는 숙명의 라이벌이었다. 그래서 그 사이에는 많은 이야깃거리가 탄생했다. 와신상담도 그 중 하나이다.</p>\n" +
                "<p>기원전 497년, 월왕 윤상이 죽고 그 아들 구천이 즉위하자, 오왕 합려는 그때를 틈타 이듬해 월나라를 공격했다. 그러나 그 싸움에서 합려는 대패했을 뿐만 아니라, 적의 화살에 부상한 손가락의 상처가 악화되는 바람에 목숨을 잃었다. 임종 때 합려는 태자인 부차에게 반드시 구천을 쳐서 원수를 갚으라고 유언했다.</p>\n" +
                "<p>오왕이 된 부차는 부왕의 유명을 한시도 잊지 않으려고 ‘섶 위에서 잠을 자고’ 자기 방을 드나드는 신하들에게 방문 아에서 부왕의 유명을 외치게 했다. 그리고 그때마다 부처는 복수를 다짐했다.</p>\n" +
                "<p>이처럼 이를 갈며 준비하기를 3년, 드디어 때가 왔다. 부차의 복수심을 안 월왕 구천치 참모 범려의 간을 뿌리치고 선제 공격을 해온 것이다. 월나라 군사는 복수심에 불타는 오나라 군사에 대패하여 회계산으로 도망쳐 들어갔다. 진퇴양난에 빠진 구천은 범려의 의견에 따라 우선 항복을 청했다. 이때 오나라의 중신 오자서가 이 기회에 구천을 죽이고 월을 멸망시켜 뒤탈이 없도록 할 것을 전언했으나, 부차는 월에서 뇌물을 받은 재상 백비의 말을 좇아 구천의 투항을 받아들이고 귀국까기 허락했다. 부차가 원수인 구천을 용서한 이 일에서 ‘기사회생’이라는 말이 탄생했다.</p>\n" +
                "<p>한편 목숨을 건진 월왕 구천은 이후 항상 곁에다 쓸개를 놓아두고 앉으나 서나 그 ‘쓴맛을 맛보며’ 회계산에서의 치욕을 상기했다. 그리고 은밀히 군사를 훈련하며 복수의 기회를 노렸다. 이것을 아는 오자서는 구천의 일을 잊은 채 중원으로의 진출에만 부심하는 부차에게 구천을 경계하도록 진언했지만 부차는 들은 체도 하지 않았다. 기원전 484년, 제로의 출병을 반대하던 오자서는 끝내 부처의ㅣ 명에 의해 자결을 하고 말았다. 오자서는 죽기 전 ‘내 눈을 빼어 오의 동문 위에 걸어두라. 내 그 눈으로 오가 월에 의해 멸망당하는 것을 보리라’라는 통한의 말을 씹으며 부차가 내려 준 검으로 자결하였다.</p>\n" +
                "<p>드디어 회예의 치욕의 날로부터 12년이 지난 해 봄, 부차가 천하의 패자가 되기 위해 기의 땅에서 제후들과 회맹하고 있는 사이에 구천은 군사를 이끌고 오나라로 쳐들어갔다. 그로부터 역전 7년 만에 오나라의 도읍 고소에가지 밀고 들어간 구천은 부차를 굴복시키고 마침내 회게의 치욕을 씻었다. 부차는 용동에서 여생을 보내라는 구천의 호의에도 불구 ‘오자서를 대할 면목이 없다’는 말을 남기고 자결했다. 그후 구천은 부차를 대신하여 천하의 패자가 되었다.</p>\n";
        o.datetime = DateUtil.from("2023-03-14 13:07:00");
        o.category = 202303;

        return o;
    }
    public static Integer toCategory(String url) {
        Uri uri = Uri.parse(url);
        File f = new File(uri.getPath());
        return Integer.valueOf(FileUtil.getOnlyName(f));
    }
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "url")
    @SerializedName("url")
    public String url;
    @ColumnInfo(name = "title")
    @SerializedName("title")
    public String title;
    @ColumnInfo(name = "pronunciation")
    @SerializedName("pronunciation")
    public String pronunciation;
    @ColumnInfo(name = "summary")
    @SerializedName("summary")
    public String summary;
    @ColumnInfo(name = "story")
    @SerializedName("story")
    public String story;
    @ColumnInfo(name = "datetime")
    @SerializedName("datetime")
    public Date datetime;
    @ColumnInfo(name = "category")
    @SerializedName("category")
    public Integer category;
}
