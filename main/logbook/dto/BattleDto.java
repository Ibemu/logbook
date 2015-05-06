package logbook.dto;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonValue;

import logbook.data.context.GlobalContext;
import logbook.internal.Item;
import logbook.internal.Ship;

/**
 * 会敵を表します
 *
 */
public final class BattleDto extends AbstractDto {

    /** 味方艦隊 */
    private final List<DockDto> friends = new ArrayList<>();

    /** 敵艦隊 */
    private final List<ShipInfoDto> enemy = new ArrayList<>();

    /** 敵装備 */
    private final List<ItemDto[]> enemySlot = new ArrayList<>();

    /** 敵Lv */
    private final List<Long> enemyLv = new ArrayList<>();

    /** 味方最終HP */
    private final int[] endFriendHp = new int[6];

    /** 味方最終HP */
    private final int[] endCombinedHp = new int[6];

    /** 敵最終HP */
    private final int[] endEnemyHp = new int[6];

    /** 味方HP */
    private final int[] nowFriendHp = new int[6];

    /** 味方HP */
    private final int[] nowCombinedHp = new int[6];

    /** 敵HP */
    private final int[] nowEnemyHp = new int[6];

    /** 味方MaxHP */
    private final int[] maxFriendHp = new int[6];

    /** 味方MaxHP */
    private final int[] maxCombinedHp = new int[6];

    /** 敵MaxHP */
    private final int[] maxEnemyHp = new int[6];

    /** 夜戦 */
    private final boolean night;

    /** 連合艦隊 */
    private final int combined;

    /** 味方陣形 */
    private final String friendFormation;

    /** 敵陣形 */
    private final String enemyFormation;

    /** 艦隊行動 */
    private final String intercept;

    /** 退避 */
    private final boolean[] friendEscape = { false, false, false, false, false, false };

    /** 退避 */
    private final boolean[] combinedEscape = { false, false, false, false, false, false };

    /**
     * コンストラクター
     */
    public BattleDto(JsonObject object, boolean night, int combined) {

        this.night = night;
        this.combined = combined;

        String dockId;

        if (object.containsKey("api_dock_id")) {
            dockId = object.get("api_dock_id").toString();
        } else {
            dockId = object.get("api_deck_id").toString();
        }
        this.friends.add(GlobalContext.getDock(dockId));

        if (object.containsKey("api_fParam_combined")) {
            this.friends.add(GlobalContext.getDock("2"));
        }

        if (object.containsKey("api_escape_idx")) {
            JsonArray escIdx = object.getJsonArray("api_escape_idx");
            for (int i = 0; i < escIdx.size(); i++) {
                int idx = escIdx.getJsonNumber(i).intValue();
                this.friendEscape[idx - 1] = true;
            }
        }

        if (object.containsKey("api_escape_idx_combined")) {
            JsonArray escIdx = object.getJsonArray("api_escape_idx_combined");
            for (int i = 0; i < escIdx.size(); i++) {
                int idx = escIdx.getJsonNumber(i).intValue();
                this.combinedEscape[idx - 1] = true;
            }
        }

        JsonArray shipKe = object.getJsonArray("api_ship_ke");
        for (int i = 1; i < shipKe.size(); i++) {
            long id = shipKe.getJsonNumber(i).longValue();
            ShipInfoDto dto = Ship.get(Long.toString(id));
            if (dto != null) {
                this.enemy.add(dto);
            }
        }

        JsonArray shipLv = object.getJsonArray("api_ship_lv");
        for (int i = 1; i < shipLv.size(); i++) {
            long lv = shipLv.getJsonNumber(i).longValue();
            this.enemyLv.add(lv);
        }

        JsonArray eSlots = object.getJsonArray("api_eSlot");
        for (int i = 0; i < eSlots.size(); i++) {
            JsonArray eSlot = eSlots.getJsonArray(i);
            ItemDto[] slot = new ItemDto[5];
            for (int j = 0; j < eSlot.size(); j++) {
                slot[j] = Item.get(eSlot.getInt(j));
            }
            this.enemySlot.add(slot);
        }

        JsonArray nowhps = object.getJsonArray("api_nowhps");
        for (int i = 1; i < nowhps.size(); i++) {
            if (i <= 6) {
                this.endFriendHp[i - 1] = this.nowFriendHp[i - 1] = nowhps.getJsonNumber(i).intValue();
            } else {
                this.endEnemyHp[i - 1 - 6] = this.nowEnemyHp[i - 1 - 6] = nowhps.getJsonNumber(i).intValue();
            }
        }

        JsonArray maxhps = object.getJsonArray("api_maxhps");
        for (int i = 1; i < maxhps.size(); i++) {
            if (i <= 6) {
                this.maxFriendHp[i - 1] = maxhps.getJsonNumber(i).intValue();
            } else {
                this.maxEnemyHp[i - 1 - 6] = maxhps.getJsonNumber(i).intValue();
            }
        }

        if (object.containsKey("api_nowhps_combined")) {
            JsonArray nowhpsc = object.getJsonArray("api_nowhps_combined");
            for (int i = 1; i < nowhpsc.size(); i++) {
                this.endCombinedHp[i - 1] = this.nowCombinedHp[i - 1] = nowhpsc.getJsonNumber(i).intValue();
            }

            JsonArray maxhpsc = object.getJsonArray("api_maxhps_combined");
            for (int i = 1; i < maxhpsc.size(); i++) {
                this.maxCombinedHp[i - 1] = maxhpsc.getJsonNumber(i).intValue();
            }
        }

        this.searchDamage(object, night && (combined != 0));

        if (object.containsKey("api_formation")) {
            JsonArray formation = object.getJsonArray("api_formation");
            // 味方陣形
            switch (formation.get(0).getValueType()) {
            case NUMBER:
                this.friendFormation = toFormation(formation.getInt(0));
                break;
            default:
                this.friendFormation = toFormation(Integer.parseInt(formation.getString(0)));
            }
            // 敵陣形
            switch (formation.get(1).getValueType()) {
            case NUMBER:
                this.enemyFormation = toFormation(formation.getInt(1));
                break;
            default:
                this.enemyFormation = toFormation(Integer.parseInt(formation.getString(1)));
            }
            // 艦隊行動
            switch (formation.get(2).getValueType()) {
            case NUMBER:
                this.intercept = toIntercept(formation.getInt(2));
                break;
            default:
                this.intercept = toIntercept(Integer.parseInt(formation.getString(2)));
            }
        } else {
            this.friendFormation = "陣形不明";
            this.enemyFormation = "陣形不明";
            this.intercept = "不明";
        }
    }

    private static String toFormation(int f) {
        String formation;
        switch (f) {
        case 1:
            formation = "単縦陣";
            break;
        case 2:
            formation = "複縦陣";
            break;
        case 3:
            formation = "輪形陣";
            break;
        case 4:
            formation = "梯形陣";
            break;
        case 5:
            formation = "単横陣";
            break;
        case 11:
            formation = "第一警戒航行序列";
            break;
        case 12:
            formation = "第二警戒航行序列";
            break;
        case 13:
            formation = "第三警戒航行序列";
            break;
        case 14:
            formation = "第四警戒航行序列";
            break;
        default:
            formation = "単縦陣";
            break;
        }
        return formation;
    }

    private void searchDamage(JsonObject object, boolean comb) {
        for (JsonObject.Entry<String, JsonValue> e : object.entrySet()) {
            if ("api_fdam".equals(e.getKey())) {
                JsonArray fdam = (JsonArray) e.getValue();
                for (int i = 1; i < fdam.size(); i++) {
                    if (comb) {
                        this.endCombinedHp[i - 1] -= fdam.getJsonNumber(i).intValue();
                    } else {
                        this.endFriendHp[i - 1] -= fdam.getJsonNumber(i).intValue();
                    }
                }
            }
            else if ("api_edam".equals(e.getKey())) {
                JsonArray edam = (JsonArray) e.getValue();
                for (int i = 1; i < edam.size(); i++) {
                    this.endEnemyHp[i - 1] -= edam.getJsonNumber(i).intValue();
                }
            }
            else if ("api_damage".equals(e.getKey())) {
                JsonArray dflist = object.getJsonArray("api_df_list");
                JsonArray damage = (JsonArray) e.getValue();
                for (int i = 1; i < damage.size(); i++) {
                    JsonValue v = damage.get(i);
                    switch (v.getValueType()) {
                    case NUMBER:
                        this.endEnemyHp[i - 1] -= ((JsonNumber) v).intValue();
                        break;
                    case ARRAY:
                        JsonArray dm = (JsonArray) v;
                        JsonArray df = dflist.getJsonArray(i);
                        for (int j = 0; j < dm.size(); j++) {
                            int idx = df.getJsonNumber(j).intValue();
                            if (idx < 1) {
                                continue;
                            }
                            else if (idx <= 6) {
                                if (comb) {
                                    this.endCombinedHp[idx - 1] -= dm.getJsonNumber(j).intValue();
                                } else {
                                    this.endFriendHp[idx - 1] -= dm.getJsonNumber(j).intValue();
                                }
                            } else {
                                this.endEnemyHp[idx - 1 - 6] -= dm.getJsonNumber(j).intValue();
                            }
                        }
                        break;
                    default:
                        break;
                    }
                }
            }
            else {
                boolean c = comb
                        || ((e.getKey() != null) && e.getKey().contains("_combined"))
                        ||
                        (
                        ((this.combined == 1) && ("api_hougeki1".equals(e.getKey()) || "api_raigeki".equals(e.getKey()))) ||
                        ((this.combined == 2) && ("api_hougeki3".equals(e.getKey()) || "api_raigeki".equals(e.getKey())))
                        );
                if (e.getValue() instanceof JsonObject) {
                    this.searchDamage((JsonObject) e.getValue(), c);
                }
                else if (e.getValue() instanceof JsonArray) {
                    this.searchDamageArray((JsonArray) e.getValue(), c);
                }
            }
        }
    }

    private void searchDamageArray(JsonArray array, boolean comb) {
        for (JsonValue v : array) {
            if (v instanceof JsonObject) {
                this.searchDamage((JsonObject) v, comb);
            }
            else if (v instanceof JsonArray) {
                this.searchDamageArray((JsonArray) v, comb);
            }
        }
    }

    private static String toIntercept(int i) {
        String intercept;
        switch (i) {
        case 1:
            intercept = "同航戦";
            break;
        case 2:
            intercept = "反航戦";
            break;
        case 3:
            intercept = "Ｔ字戦(有利)";
            break;
        case 4:
            intercept = "Ｔ字戦(不利)";
            break;
        default:
            intercept = "同航戦";
        }
        return intercept;
    }

    /**
     * 味方艦隊を取得します。
     * @return 味方艦隊
     */
    public List<DockDto> getFriends() {
        return this.friends;
    }

    /**
     * 敵艦隊を取得します。
     * @return 敵艦隊
     */
    public List<ShipInfoDto> getEnemy() {
        return this.enemy;
    }

    /**
     * 敵Lvを取得します。
     * @return 敵Lv
     */
    public List<Long> getEnemyLv() {
        return this.enemyLv;
    }

    /**
     * 敵装備を取得します。
     * @return 敵装備
     */
    public List<ItemDto[]> getEnemySlot() {
        return this.enemySlot;
    }

    /**
     * @return endFriendHp
     */
    public int[] getEndFriendHp() {
        return this.endFriendHp;
    }

    /**
     * @return endCombinedHp
     */
    public int[] getEndCombinedHp() {
        return this.endCombinedHp;
    }

    /**
     * @return endEnemyHp
     */
    public int[] getEndEnemyHp() {
        return this.endEnemyHp;
    }

    /**
     * 味方HPを取得します。
     * @return 味方HP
     */
    public int[] getNowFriendHp() {
        return this.nowFriendHp;
    }

    /**
     * @return nowCombinedHp
     */
    public int[] getNowCombinedHp() {
        return this.nowCombinedHp;
    }

    /**
     * 敵HPを取得します。
     * @return 敵HP
     */
    public int[] getNowEnemyHp() {
        return this.nowEnemyHp;
    }

    /**
     * 味方MaxHPを取得します。
     * @return 味方MaxHP
     */
    public int[] getMaxFriendHp() {
        return this.maxFriendHp;
    }

    /**
     * @return maxCombinedHp
     */
    public int[] getMaxCombinedHp() {
        return this.maxCombinedHp;
    }

    /**
     * 敵MaxHPを取得します。
     * @return 敵MaxHP
     */
    public int[] getMaxEnemyHp() {
        return this.maxEnemyHp;
    }

    /**
     * @return night
     */
    public boolean isNight() {
        return this.night;
    }

    /**
     * @return combined
     */
    public boolean isCombined() {
        return this.combined != 0;
    }

    /**
     * 味方陣形を取得します。
     * @return 味方陣形
     */
    public String getFriendFormation() {
        return this.friendFormation;
    }

    /**
     * 敵陣形を取得します。
     * @return 敵陣形
     */
    public String getEnemyFormation() {
        return this.enemyFormation;
    }

    /**
     * 艦隊行動を取得します。
     * @return 艦隊行動
     */
    public String getIntercept() {
        return this.intercept;
    }

    /**
     * @return friendEscape
     */
    public boolean[] getFriendEscape() {
        return this.friendEscape;
    }

    /**
     * @return combinedEscape
     */
    public boolean[] getCombinedEscape() {
        return this.combinedEscape;
    }
}
