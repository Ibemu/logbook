package logbook.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.json.JsonObject;

import logbook.internal.UseItem;

import org.apache.commons.lang3.StringUtils;

/**
 * 海戦とドロップした艦娘を表します
 */
public final class BattleResultDto extends AbstractDto {

    /** 日付 */
    private final Date battleDate;

    /** 海域名 */
    private final String questName;

    /** ランク */
    private final String rank;

    /** マス */
    private final int mapCellNo;

    /** 出撃 */
    private final boolean start;

    /** ボスマス */
    private final boolean boss;

    /** 敵艦隊名 */
    private final String enemyName;

    /** ドロップフラグ */
    private final boolean dropShip;

    /** ドロップフラグ */
    private final boolean dropItem;

    /** 艦種 */
    private final String dropType;

    /** 艦名 */
    private final String dropName;

    /** 戦闘詳細 */
    private final BattleDto[] battles;

    /**
     * コンストラクター
     *
     * @param object JSON Object
     * @param mapCellNo マップ上のマス
     * @param mapBossCellNo　ボスマス
     * @param eventId EventId
     * @param isStart 出撃
     * @param battles 戦闘
     */
    public BattleResultDto(JsonObject object, int mapCellNo, int mapBossCellNo, boolean isBoss, boolean isStart,
            BattleDto[] battles) {

        this.battleDate = Calendar.getInstance().getTime();
        this.questName = object.getString("api_quest_name");
        this.rank = object.getString("api_win_rank");
        this.mapCellNo = mapCellNo;
        this.start = isStart;
        this.boss = isBoss;
        this.enemyName = object.getJsonObject("api_enemy_info").getString("api_deck_name");
        this.dropShip = object.containsKey("api_get_ship");
        this.dropItem = object.containsKey("api_get_useitem");
        if (this.dropShip || this.dropItem) {
            if (this.dropShip) {
                this.dropType = object.getJsonObject("api_get_ship").getString("api_ship_type");
                this.dropName = object.getJsonObject("api_get_ship").getString("api_ship_name");
            } else {
                String name = UseItem.get(object.getJsonObject("api_get_useitem").getInt("api_useitem_id"));
                this.dropType = "アイテム";
                this.dropName = StringUtils.defaultString(name);
            }
        } else {
            this.dropType = "";
            this.dropName = "";
        }

        this.battles = battles;
    }

    /**
     * 日付を取得します。
     * @return 日付
     */
    public Date getBattleDate() {
        return this.battleDate;
    }

    /**
     * 海域名を取得します。
     * @return 海域名
     */
    public String getQuestName() {
        return this.questName;
    }

    /**
     * ランクを取得します。
     * @return ランク
     */
    public String getRank() {
        return this.rank;
    }

    /**
     * マスを取得します。
     * @return マス
     */
    public int getMapCellNo() {
        return this.mapCellNo;
    }

    /**
     * 出撃を取得します
     * @return 出撃
     */
    public boolean isStart() {
        return this.start;
    }

    /**
     * ボスマスを取得します
     * @return ボスマス
     */
    public boolean isBoss() {
        return this.boss;
    }

    /**
     * 出撃・ボステキストを取得します
     * @return 出撃・ボステキスト
     */
    public String getBossText() {
        if (this.isStart() || this.isBoss()) {
            List<String> list = new ArrayList<>();
            if (this.isStart()) {
                list.add("出撃");
            }
            if (this.isBoss()) {
                list.add("ボス");
            }
            return StringUtils.join(list, "&");
        }
        return "";
    }

    /**
     * 敵艦隊名を取得します。
     * @return 敵艦隊名
     */
    public String getEnemyName() {
        return this.enemyName;
    }

    /**
     * ドロップフラグを取得します。
     * @return ドロップフラグ
     */
    public boolean isDropShip() {
        return this.dropShip;
    }

    /**
     * ドロップフラグを取得します。
     * @return ドロップフラグ
     */
    public boolean isDropItem() {
        return this.dropItem;
    }

    /**
     * 艦種を取得します。
     * @return 艦種
     */
    public String getDropType() {
        return this.dropType;
    }

    /**
     * @return 戦闘詳細
     */
    public BattleDto getBattleDto() {
        return this.battles[0];
    }

    /**
     * 艦名を取得します。
     * @return 艦名
     */
    public String getDropName() {
        return this.dropName;
    }

    /**
     * 戦闘詳細を取得します。
     * @return 戦闘詳細
     */
    public BattleDto getBattle() {
        return this.battles[0];
    }

    /**
     * 戦闘詳細を取得します。
     * @return 戦闘詳細
     */
    public BattleDto[] getBattles() {
        return this.battles;
    }
}
