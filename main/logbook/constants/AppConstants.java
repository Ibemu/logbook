package logbook.constants;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TimeZone;

import org.eclipse.swt.graphics.RGB;

import logbook.internal.Version;

/**
 * アプリケーションで使用する共通の定数クラス
 *
 */
public class AppConstants {

    /** アプリケーション名 */
    public static final String NAME = "航海日誌";

    /** バージョン */
    public static final Version VERSION = new Version(0, 9, 11);

    /**　アプリケーション名 */
    public static final String NAME_PLUS = "航海日誌+";

    /** バージョン */
    public static final Version VERSION_PLUS = new Version(1, 6, 6);

    /** バージョン */
    public static final String VERSION_FULL = VERSION + "+" + VERSION_PLUS.toStringFull();

    /** ホームページ */
    public static final URI HOME_PAGE_URI = URI.create("https://kancolle.sanaechan.net/");

    /** ホームページ */
    public static final URI HOME_PAGE_URI_PLUS = URI.create("https://github.com/Ibemu/logbook");

    /** アップデートチェック先 */
    public static final URI UPDATE_CHECK_URI = URI.create("http://kancolle.sanaechan.net/checkversion.txt");

    /** アップデートチェック先 */
    public static final URI UPDATE_CHECK_URI_PLUS = URI
            .create("https://raw.githubusercontent.com/Ibemu/logbook/plus/checkversion.txt");

    /** 日付書式 */
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** 日付書式(時刻のみ) */
    public static final String DATE_SHORT_FORMAT = "HH:mm:ss";

    /** 日付書式(日付のみ) */
    public static final String DATE_DAYS_FORMAT = "yyyy-MM-dd";

    /** 日付書式(ミリ秒を含む) */
    public static final String DATE_LONG_FORMAT = "yyyy-MM-dd HH-mm-ss.SSS";

    /** タイムゾーン(任務が更新される05:00JSTに0:00になるタイムゾーン) */
    public static final TimeZone TIME_ZONE_MISSION = TimeZone.getTimeZone("GMT+04:00");

    /** 1艦隊に編成できる艦娘の数 */
    public static final int MAX_CHARA = 7;

    /** 疲労赤色 */
    public static final int COND_RED = 19;

    /** 疲労オレンジ色 */
    public static final int COND_ORANGE = 29;

    /** 疲労緑色(偽) */
    public static final int COND_DARK_GREEN = 50;

    /** 疲労緑色 */
    public static final int COND_GREEN = 53;

    /** 遠征色 */
    public static final RGB MISSION_COLOR = new RGB(102, 51, 255);

    /** 入渠色 */
    public static final RGB NDOCK_COLOR = new RGB(0, 102, 153);

    /** 疲労赤色 */
    public static final RGB COND_RED_COLOR = new RGB(255, 16, 0);

    /** 疲労オレンジ色 */
    public static final RGB COND_ORANGE_COLOR = new RGB(255, 140, 0);

    /** 疲労緑色(偽) */
    public static final RGB COND_DARK_GREEN_COLOR = new RGB(0, 60, 0);

    /** 疲労緑色 */
    public static final RGB COND_GREEN_COLOR = new RGB(0, 128, 0);

    /** 5分前 */
    public static final RGB TIME_IN_5_MIN = new RGB(255, 215, 0);

    /** 10分前 */
    public static final RGB TIME_IN_10_MIN = new RGB(255, 239, 153);

    /** 20分前 */
    public static final RGB TIME_IN_20_MIN = new RGB(255, 247, 203);

    /** テーブル行(偶数行)背景色 */
    public static final RGB ROW_BACKGROUND = new RGB(246, 246, 246);

    /** HPゲージ最小色 */
    public static final RGB HP_EMPTY_COLOR = new RGB(0xff, 0, 0);

    /** HPゲージ中間色 */
    public static final RGB HP_HALF_COLOR = new RGB(0xff, 0xd7, 0);

    /** HPゲージ最大色 */
    public static final RGB HP_FULL_COLOR = new RGB(0, 0xd7, 0);

    /** 経験値ゲージ色 */
    public static final RGB EXP_COLOR = new RGB(0, 0x80, 0xff);

    /** 小破(75%) */
    public static final float SLIGHT_DAMAGE = 0.75f;

    /** 中破(50%) */
    public static final float HALF_DAMAGE = 0.5f;

    /** 大破(25%) */
    public static final float BADLY_DAMAGE = 0.25f;

    /** 補給(少) */
    public static final float LOW_SUPPLY = 0.77f;

    /** 補給(空) */
    public static final float EMPTY_SUPPLY = 0.33f;

    /** 文字コード(Shift_JIS) */
    public static final Charset CHARSET = Charset.forName("MS932");

    /** アプリケーション設定ファイル  */
    public static final Path APP_CONFIG_FILE = Paths.get("./config/internal.xml");

    /** 艦娘設定ファイル  */
    public static final Path SHIP_CONFIG_FILE = Paths.get("./config/ship.xml");

    /** 装備一覧設定ファイル  */
    public static final Path ITEM_CONFIG_FILE = Paths.get("./config/item.xml");

    /** レベル付き装備設定ファイル  */
    public static final Path ITEM_LEVEL_CONFIG_FILE = Paths.get("./config/itemlevel.xml");

    /** 熟練度設定ファイル  */
    public static final Path ITEM_ALV_CONFIG_FILE = Paths.get("./config/itemalv.xml");

    /** 装備マスター設定ファイル  */
    public static final Path ITEM_MST_CONFIG_FILE = Paths.get("./config/itemmst.xml");

    /** 建造ドック設定ファイル  */
    public static final Path KDOCK_CONFIG_FILE = Paths.get("./config/kdock.xml");

    /** 任務設定ファイル  */
    public static final Path QUEST_CONFIG_FILE = Paths.get("./config/quest.xml");

    /** 所有艦娘グループ設定ファイル  */
    public static final Path GROUP_CONFIG_FILE = Paths.get("./config/group.xml");

    /** 資材チャートcss */
    public static final Path CHART_STYLESHEET_FILE = Paths.get("./config/chart.css");

    /** 保有資材:燃料 */
    public static final int MATERIAL_FUEL = 1;

    /** 保有資材:弾薬 */
    public static final int MATERIAL_AMMO = 2;

    /** 保有資材:鋼材 */
    public static final int MATERIAL_METAL = 3;

    /** 保有資材:ボーキサイト */
    public static final int MATERIAL_BAUXITE = 4;

    /** 保有資材:バーナー */
    public static final int MATERIAL_BURNER = 5;

    /** 保有資材:高速修復材 */
    public static final int MATERIAL_BUCKET = 6;

    /** 保有資材:開発資材 */
    public static final int MATERIAL_RESEARCH = 7;

    /** /resources/icon/add.png */
    public static final String R_ICON_ADD = "/resources/icon/add.png";

    /** /resources/icon/delete.png */
    public static final String R_ICON_DELETE = "/resources/icon/delete.png";

    /** /resources/icon/error.png */
    public static final String R_ICON_ERROR = "/resources/icon/error.png";

    /** /resources/icon/error_mono.png */
    public static final String R_ICON_ERROR_MONO = "/resources/icon/error_mono.png";

    /** /resources/icon/exclamation.png */
    public static final String R_ICON_EXCLAMATION = "/resources/icon/exclamation.png";

    /** /resources/icon/exclamation_mono.png */
    public static final String R_ICON_EXCLAMATION_MONO = "/resources/icon/exclamation_mono.png";

    /** /resources/icon/folder.png */
    public static final String R_ICON_FOLDER = "/resources/icon/folder.png";

    /** /resources/icon/star.png */
    public static final String R_ICON_STAR = "/resources/icon/star.png";

    /** 艦隊タブの艦娘ラベルに設定するツールチップテキスト */
    public static final String TOOLTIP_FLEETTAB_SHIP = "HP:{0}/{1} 燃料:{2}/{3} 弾:{4}/{5}\nNext:{6}exp\n装備\n{7}";

    /** メッセージ 出撃できます。 */
    public static final String MESSAGE_GOOD = "出撃できます。";

    /** メッセージ {0} 出撃はできません。 */
    public static final String MESSAGE_BAD = "{0} 出撃はできません。";

    /** メッセージ 大破している艦娘がいます  */
    public static final String MESSAGE_BADLY_DAMAGE = "大破艦あり";

    /** メッセージ 入渠中の艦娘がいます  */
    public static final String MESSAGE_BATHWATER = "入渠艦あり";

    /** メッセージ 遠征中です。  */
    public static final String MESSAGE_MISSION = "遠征中です。";

    /** メッセージ 疲労している艦娘がいます */
    public static final String MESSAGE_COND = "疲労艦あり {0}頃回復。";

    /** メッセージ 大破している艦娘がいます */
    public static final String MESSAGE_STOP_SORTIE = "大破している艦娘がいます、進撃はできません。";

    /** メッセージ 新しい艦娘が着任しました */
    public static final String MESSAGE_NEW_SHIP = "新しい艦娘が着任しました。";

    /** メッセージ 制空値:{0} */
    public static final String MESSAGE_SEIKU = "制空:{0}。";

    /** メッセージ  索敵値計:{0} */
    public static final String MESSAGE_SAKUTEKI = "索敵:{0} (";

    /** メッセージ  分岐点係数:{0} */
    public static final String MESSAGE_SAKUTEKI_NODE = "分岐点係数:{0}";

    /** メッセージ  )。 */
    public static final String MESSAGE_SAKUTEKI_AFTER = ")。";

    /** メッセージ  艦隊合計Lv:{0} */
    public static final String MESSAGE_TOTAL_LV = "合計Lv:{0}。";

    /** メッセージ ドラム缶:{0}隻{1}個 */
    public static final String MESSAGE_DRUM = "ドラム缶:{0}隻{1}個。";

    /** メッセージ TP:S{0}, A{1}。 */
    public static final String MESSAGE_TP = "TP:S{0}, A{1}。";

    /** メッセージ 泊地修理の準備中({0}分経過)*/
    public static final String BERTH_REPAIR_1 = "泊地修理の準備中({0}分経過)";

    /** メッセージ 泊地修理中({0}分経過)*/
    public static final String BERTH_REPAIR_2 = "泊地修理中({0}分経過)";

    /** 海戦・ドロップ報告書.csv */
    public static final String LOG_BATTLE_RESULT = "海戦・ドロップ報告書.csv";

    /** 海戦・ドロップ報告書_alternativefile.csv */
    public static final String LOG_BATTLE_RESULT_ALT = "海戦・ドロップ報告書_alternativefile.csv";

    /** 建造報告書.csv */
    public static final String LOG_CREATE_SHIP = "建造報告書.csv";

    /** 建造報告書_alternativefile.csv */
    public static final String LOG_CREATE_SHIP_ALT = "建造報告書_alternativefile.csv";

    /** 開発報告書.csv */
    public static final String LOG_CREATE_ITEM = "開発報告書.csv";

    /** 開発報告書_alternativefile.csv */
    public static final String LOG_CREATE_ITEM_ALT = "開発報告書_alternativefile.csv";

    /** 遠征報告書.csv */
    public static final String LOG_MISSION = "遠征報告書.csv";

    /** 遠征報告書.csv */
    public static final String LOG_MISSION_ALT = "遠征報告書_alternativefile.csv";

    /** 資材ログ.csv */
    public static final String LOG_RESOURCE = "資材ログ.csv";

    /** 資材ログ_alternativefile.csv */
    public static final String LOG_RESOURCE_ALT = "資材ログ_alternativefile.csv";
}
