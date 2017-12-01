package logbook.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import logbook.constants.AppConstants;
import logbook.data.context.GlobalContext;
import logbook.internal.Item;
import logbook.internal.Ship;
import logbook.util.JsonUtils;

/**
 * 会敵を表します
 *
 */
public final class BattleDto extends AbstractDto {
    /** 味方艦隊 */
    private final List<DockDto> friends = new ArrayList<>();

    /** 敵艦隊 */
    private final List<ShipInfoDto> enemy1 = new ArrayList<>();

    /** 敵艦隊 */
    private final List<ShipInfoDto> enemy2 = new ArrayList<>();

    /** 敵装備 */
    private final List<ItemDto[]> enemy1Slot = new ArrayList<>();

    /** 敵装備 */
    private final List<ItemDto[]> enemy2Slot = new ArrayList<>();

    /** 敵Lv */
    private final List<Long> enemy1Lv = new ArrayList<>();

    /** 敵Lv */
    private final List<Long> enemy2Lv = new ArrayList<>();

    /** 味方最終HP */
    private final int[] endFriend1Hp = new int[AppConstants.MAX_CHARA];

    /** 味方最終HP */
    private final int[] endFriend2Hp = new int[AppConstants.MAX_CHARA];

    /** 敵最終HP */
    private final int[] endEnemy1Hp = new int[AppConstants.MAX_CHARA];

    /** 敵最終HP */
    private final int[] endEnemy2Hp = new int[AppConstants.MAX_CHARA];

    /** 味方HP */
    private final int[] nowFriend1Hp = new int[AppConstants.MAX_CHARA];

    /** 味方HP */
    private final int[] nowFriend2Hp = new int[AppConstants.MAX_CHARA];

    /** 敵HP */
    private final int[] nowEnemy1Hp = new int[AppConstants.MAX_CHARA];

    /** 敵HP */
    private final int[] nowEnemy2Hp = new int[AppConstants.MAX_CHARA];

    /** 味方MaxHP */
    private final int[] maxFriend1Hp = new int[AppConstants.MAX_CHARA];

    /** 味方MaxHP */
    private final int[] maxFriend2Hp = new int[AppConstants.MAX_CHARA];

    /** 敵MaxHP */
    private final int[] maxEnemy1Hp = new int[AppConstants.MAX_CHARA];

    /** 敵MaxHP */
    private final int[] maxEnemy2Hp = new int[AppConstants.MAX_CHARA];

    /** 夜戦 */
    private final boolean night;

    /** 夜戦→昼戦 */
    private final boolean nightToDay;

    /** 連合艦隊 */
    private final int friendCombined;

    /** 連合艦隊 */
    private final boolean enemyCombined;

    /** 連合艦隊 */
    private final String combinedType;

    /** 味方陣形 */
    private final String friendFormation;

    /** 敵陣形 */
    private final String enemyFormation;

    /** 艦隊行動 */
    private final String intercept;

    /** 索敵 */
    private final String friendSearch;

    /** 索敵 */
    private final String enemySearch;

    /** 触接 */
    private final String friendTouch;

    /** 触接 */
    private final String enemyTouch;

    /** 制空 */
    private final String dispSeiku;

    /** 退避 */
    private final boolean[] friend1Escape = { false, false, false, false, false, false, false };

    /** 退避 */
    private final boolean[] friend2Escape = { false, false, false, false, false, false, false };

    /** api_active_deck */
    private final int friendActiveDeck;
    private final int enemyActiveDeck;

    /**
     * コンストラクター
     */
    public BattleDto(JsonObject object, boolean night, boolean nightToDay, int combined) {

        this.night = night;
        this.nightToDay = nightToDay;

        String dockId;

        if (object.containsKey("api_dock_id")) {
            dockId = object.get("api_dock_id").toString();
        } else {
            dockId = object.get("api_deck_id").toString();
        }
        this.friends.add(GlobalContext.getDock(dockId));

        if (object.containsKey("api_fParam_combined")) {
            this.friends.add(GlobalContext.getDock("2"));
            this.friendCombined = combined;
        } else {
            this.friendCombined = 0;
        }

        this.combinedType = toCombined(this.friendCombined);

        if (object.containsKey("api_escape_idx")) {
            JsonArray escIdx = object.getJsonArray("api_escape_idx");
            for (int i = 0; i < escIdx.size(); i++) {
                int idx = escIdx.getJsonNumber(i).intValue() - 1;
                this.friend1Escape[idx] = true;
            }
        }

        if (object.containsKey("api_escape_idx_combined")) {
            JsonArray escIdx = object.getJsonArray("api_escape_idx_combined");
            for (int i = 0; i < escIdx.size(); i++) {
                int idx = escIdx.getJsonNumber(i).intValue() - 1;
                this.friend2Escape[idx] = true;
            }
        }

        JsonArray shipKe = object.getJsonArray("api_ship_ke");
        for (int i = 0; i < shipKe.size(); i++) {
            int id = shipKe.getJsonNumber(i).intValue();
            ShipInfoDto dto = Ship.get(id);
            if (dto != null) {
                this.enemy1.add(dto);
            }
        }

        JsonArray shipLv = object.getJsonArray("api_ship_lv");
        for (int i = 0; i < shipLv.size(); i++) {
            long lv = shipLv.getJsonNumber(i).longValue();
            this.enemy1Lv.add(lv);
        }

        JsonArray eSlots = object.getJsonArray("api_eSlot");
        for (int i = 0; i < eSlots.size(); i++) {
            JsonArray eSlot = eSlots.getJsonArray(i);
            ItemDto[] slot = new ItemDto[5];
            for (int j = 0; j < eSlot.size(); j++) {
                slot[j] = Item.get(eSlot.getInt(j));
            }
            this.enemy1Slot.add(slot);
        }

        this.enemyCombined = object.containsKey("api_ship_ke_combined");
        if (this.enemyCombined) {
            JsonArray shipKeC = object.getJsonArray("api_ship_ke_combined");
            for (int i = 0; i < shipKeC.size(); i++) {
                int id = shipKeC.getJsonNumber(i).intValue();
                ShipInfoDto dto = Ship.get(id);
                if (dto != null) {
                    this.enemy2.add(dto);
                }
            }

            JsonArray shipLvC = object.getJsonArray("api_ship_lv_combined");
            for (int i = 0; i < shipLvC.size(); i++) {
                long lv = shipLvC.getJsonNumber(i).longValue();
                this.enemy2Lv.add(lv);
            }

            JsonArray eSlotsC = object.getJsonArray("api_eSlot_combined");
            for (int i = 0; i < eSlotsC.size(); i++) {
                JsonArray eSlot = eSlotsC.getJsonArray(i);
                ItemDto[] slot = new ItemDto[5];
                for (int j = 0; j < eSlot.size(); j++) {
                    slot[j] = Item.get(eSlot.getInt(j));
                }
                this.enemy2Slot.add(slot);
            }
        }

        JsonArray fnowhps = object.getJsonArray("api_f_nowhps");
        for (int i = 0; i < fnowhps.size(); i++) {
            //if (i < 6) {
            this.endFriend1Hp[i] = this.nowFriend1Hp[i] = fnowhps.getJsonNumber(i).intValue();
            //} else if (i < 12) {
            //    this.endFriend2Hp[i - 6] = this.nowFriend2Hp[i - 6] = fnowhps.getJsonNumber(i).intValue();
            //}
        }

        JsonArray enowhps = object.getJsonArray("api_e_nowhps");
        for (int i = 0; i < enowhps.size(); i++) {
            //if (i < 6) {
            this.endEnemy1Hp[i] = this.nowEnemy1Hp[i] = enowhps.getJsonNumber(i).intValue();
            //} else if (i < 12) {
            //    this.endEnemy2Hp[i - 6] = this.nowEnemy2Hp[i - 6] = enowhps.getJsonNumber(i).intValue();
            //}
        }

        JsonArray fmaxhps = object.getJsonArray("api_f_maxhps");
        for (int i = 0; i < fmaxhps.size(); i++) {
            //if (i < 6) {
            this.maxFriend1Hp[i] = fmaxhps.getJsonNumber(i).intValue();
            //} else if (i < 12) {
            //    this.maxFriend2Hp[i - 6] = fmaxhps.getJsonNumber(i).intValue();
            //}
        }

        JsonArray emaxhps = object.getJsonArray("api_e_maxhps");
        for (int i = 0; i < emaxhps.size(); i++) {
            //if (i < 6) {
            this.maxEnemy1Hp[i] = emaxhps.getJsonNumber(i).intValue();
            //} else if (i < 12) {
            //    this.maxEnemy2Hp[i - 6] = emaxhps.getJsonNumber(i).intValue();
            //}
        }

        if (object.containsKey("api_f_nowhps_combined")) {
            JsonArray fnowhpsC = object.getJsonArray("api_f_nowhps_combined");
            for (int i = 0; i < fnowhpsC.size(); i++) {
                this.endFriend2Hp[i] = this.nowFriend2Hp[i] = fnowhpsC.getJsonNumber(i).intValue();
            }

            JsonArray fmaxhpsC = object.getJsonArray("api_f_maxhps_combined");
            for (int i = 0; i < fmaxhpsC.size(); i++) {
                this.maxFriend2Hp[i] = fmaxhpsC.getJsonNumber(i).intValue();
            }
        }

        if (object.containsKey("api_e_nowhps_combined")) {
            JsonArray enowhpsC = object.getJsonArray("api_e_nowhps_combined");
            for (int i = 0; i < enowhpsC.size(); i++) {
                this.endEnemy2Hp[i] = this.nowEnemy2Hp[i] = enowhpsC.getJsonNumber(i).intValue();
            }

            JsonArray emaxhpsC = object.getJsonArray("api_e_maxhps_combined");
            for (int i = 0; i < emaxhpsC.size(); i++) {
                this.maxEnemy2Hp[i] = emaxhpsC.getJsonNumber(i).intValue();
            }
        }

        if (object.containsKey("api_active_deck")) {
            JsonArray activeDeck = object.getJsonArray("api_active_deck");
            this.friendActiveDeck = JsonUtils.getInt(activeDeck.get(0));
            this.enemyActiveDeck = JsonUtils.getInt(activeDeck.get(1));
        } else {
            this.friendActiveDeck = this.friendCombined != 0 ? 2 : 1;
            this.enemyActiveDeck = 1;
        }

        if (this.enemyCombined) {
            this.searchDamageEc(object, night && (this.friendCombined != 0) && (this.friendActiveDeck == 2),
                    !nightToDay && night && (this.enemyActiveDeck == 2));
        } else {
            this.searchDamage(object, night && (this.friendCombined != 0));
        }

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

        if (object.containsKey("api_search")) {
            JsonArray search = object.getJsonArray("api_search");
            // 味方
            switch (search.get(0).getValueType()) {
            case NUMBER:
                this.friendSearch = toSearch(search.getInt(0));
                break;
            default:
                this.friendSearch = toSearch(Integer.parseInt(search.getString(0)));
            }
            // 敵
            switch (search.get(1).getValueType()) {
            case NUMBER:
                this.enemySearch = toSearch(search.getInt(1));
                break;
            default:
                this.enemySearch = toSearch(Integer.parseInt(search.getString(1)));
            }
        } else {
            this.friendSearch = "不明";
            this.enemySearch = "不明";
        }

        String dispSeiku = "不明";
        String friendTouch = "なし";
        String enemyTouch = "なし";

        try {
            JsonObject stage1 = object.getJsonObject("api_kouku").getJsonObject("api_stage1");
            try {
                dispSeiku = toDispSeiku(stage1.getInt("api_disp_seiku"));
            } catch (Exception e) {
                dispSeiku = "不明";
            }
            JsonArray search = stage1.getJsonArray("api_touch_plane");
            try {
                int t = search.getInt(0);
                if (t < 0) {
                    friendTouch = "なし";
                } else {
                    friendTouch = Item.get(t).getName();
                }
            } catch (Exception e) {
                friendTouch = "不明";
            }
            try {
                int t = search.getInt(1);
                if (t < 0) {
                    enemyTouch = "なし";
                } else {
                    enemyTouch = Item.get(t).getName();
                }
            } catch (Exception e) {
                enemyTouch = "不明";
            }
        } catch (Exception e) {
        }
        this.dispSeiku = dispSeiku;
        this.friendTouch = friendTouch;
        this.enemyTouch = enemyTouch;
    }

    private static String toDispSeiku(int s) {
        String dispSeiku;
        switch (s) {
        case 0:
            dispSeiku = "制空均衡";
            break;
        case 1:
            dispSeiku = "制空権確保";
            break;
        case 2:
            dispSeiku = "航空優勢";
            break;
        case 3:
            dispSeiku = "航空劣勢";
            break;
        case 4:
            dispSeiku = "制空権喪失";
            break;
        default:
            dispSeiku = "不明";
            break;
        }
        return dispSeiku;
    }

    private static String toSearch(int s) {
        String search;
        switch (s) {
        case 1:
            search = "成功";
            break;
        case 2:
            search = "成功(未帰還有)";
            break;
        case 3:
            search = "未帰還";
            break;
        case 4:
            search = "失敗";
            break;
        case 5:
            search = "成功";
            break;
        case 6:
            search = "失敗";
            break;
        default:
            search = "不明";
            break;
        }
        return search;
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
        case 6:
            formation = "警戒陣";
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
            formation = "不明";
            break;
        }
        return formation;
    }

    enum DockType {
        FRIEND1, FRIEND2, ENEMY1, ENEMY2
    }

    private void damage(DockType t, int i, int dam) {
        int[] endHp;
        int[] maxHp;
        ShipDto ship;
        switch (t) {
        case FRIEND1:
            endHp = this.endFriend1Hp;
            maxHp = this.maxFriend1Hp;
            try {
                ship = this.friends.get(0).getShips().get(i);
            } catch (Exception e) {
                ship = null;
            }
            break;
        case FRIEND2:
            endHp = this.endFriend2Hp;
            maxHp = this.maxFriend2Hp;
            try {
                ship = this.friends.get(1).getShips().get(i);
            } catch (Exception e) {
                ship = null;
            }
            break;
        case ENEMY1:
            endHp = this.endEnemy1Hp;
            maxHp = this.maxEnemy1Hp;
            ship = null;
            break;
        case ENEMY2:
            endHp = this.endEnemy2Hp;
            maxHp = this.maxEnemy2Hp;
            ship = null;
            break;
        default:
            return;
        }

        endHp[i] -= dam;
        if ((endHp[i] <= 0) && (ship != null)) {
            for (ItemDto item : ship.getItem()) {
                if (item != null) {
                    if (item.getName().equals("応急修理要員")) {
                        endHp[i] = maxHp[i] / 5;
                    } else if (item.getName().equals("応急修理女神")) {
                        endHp[i] = maxHp[i];
                    }
                }
            }
        }
        if (endHp[i] < 0) {
            endHp[i] = 0;
        }
    }

    private void searchDamage(JsonObject object, boolean comb) {
        for (JsonObject.Entry<String, JsonValue> e : object.entrySet()) {
            if ("api_fdam".equals(e.getKey())) {
                JsonArray fdam = (JsonArray) e.getValue();
                for (int i = 0; i < fdam.size(); i++) {
                    if (comb) {
                        this.damage(DockType.FRIEND2, i < 6 ? i : i - 6, fdam.getJsonNumber(i).intValue());
                    } else if ((this.friends.get(0).size() < 7) && (i >= 6)) {
                        this.damage(DockType.FRIEND2, i - 6, fdam.getJsonNumber(i).intValue());
                    } else {
                        this.damage(DockType.FRIEND1, i, fdam.getJsonNumber(i).intValue());
                    }
                }
            } else if ("api_edam".equals(e.getKey())) {
                JsonArray edam = (JsonArray) e.getValue();
                for (int i = 0; i < edam.size(); i++) {
                    this.damage(DockType.ENEMY1, i, edam.getJsonNumber(i).intValue());
                }
            } else if ("api_damage".equals(e.getKey()) && !ValueType.NULL.equals(e.getValue().getValueType())) {
                JsonArray eflag = object.getJsonArray("api_at_eflag");
                JsonArray dflist = object.getJsonArray("api_df_list");
                JsonArray damage = (JsonArray) e.getValue();
                for (int i = 0; i < damage.size(); i++) {
                    JsonValue v = damage.get(i);
                    boolean eflg = (eflag != null) && (eflag.getJsonNumber(i).intValue() != 0);
                    switch (v.getValueType()) {
                    case NUMBER:
                        if (eflg) {
                            if (comb) {
                                this.damage(DockType.FRIEND2, i < 6 ? i : i - 6, ((JsonNumber) v).intValue());
                            } else if ((this.friends.get(0).size() < 7) && (i >= 6)) {
                                this.damage(DockType.FRIEND2, i - 6, ((JsonNumber) v).intValue());
                            } else {
                                this.damage(DockType.FRIEND1, i, ((JsonNumber) v).intValue());
                            }
                        } else {
                            this.damage(DockType.ENEMY1, i, ((JsonNumber) v).intValue());
                        }
                        //this.damage(DockType.ENEMY1, i, ((JsonNumber) v).intValue());
                        break;
                    case ARRAY:
                        JsonArray dm = (JsonArray) v;
                        JsonArray df = dflist.getJsonArray(i);
                        for (int j = 0; j < dm.size(); j++) {
                            int idx = df.getJsonNumber(j).intValue();
                            /*if (idx < 1) {
                                continue;
                            } else if (idx < 6) {
                                if (comb) {
                                    this.damage(DockType.FRIEND2, idx, dm.getJsonNumber(j).intValue());
                                } else {
                                    this.damage(DockType.FRIEND1, idx, dm.getJsonNumber(j).intValue());
                                }
                            } else {
                                this.damage(DockType.ENEMY1, idx - 6, dm.getJsonNumber(j).intValue());
                            }*/
                            if (eflg) {
                                if (comb) {
                                    this.damage(DockType.FRIEND2, idx < 6 ? idx : idx - 6,
                                            dm.getJsonNumber(j).intValue());
                                } else if ((this.friends.get(0).size() < 7) && (idx >= 6)) {
                                    this.damage(DockType.FRIEND2, idx - 6, dm.getJsonNumber(j).intValue());
                                } else {
                                    this.damage(DockType.FRIEND1, idx, dm.getJsonNumber(j).intValue());
                                }
                            } else {
                                this.damage(DockType.ENEMY1, idx, dm.getJsonNumber(j).intValue());
                            }
                        }
                        break;
                    default:
                        break;
                    }
                }
            } else {
                final List<String> conb1 = Arrays.asList("api_hougeki1", "api_raigeki", "api_opening_atack");
                final List<String> conb2 = Arrays.asList("api_hougeki3", "api_raigeki", "api_opening_atack");
                final List<String> conb3 = conb1;
                boolean c = comb ||
                        ((e.getKey() != null) && e.getKey().contains("_combined")) ||
                        (((this.friendCombined == 1) && conb1.contains(e.getKey())) ||
                                ((this.friendCombined == 2) && conb2.contains(e.getKey())) ||
                                ((this.friendCombined == 3) && conb3.contains(e.getKey())));
                if (e.getValue() instanceof JsonObject) {
                    this.searchDamage((JsonObject) e.getValue(), c);
                } else if (e.getValue() instanceof JsonArray) {
                    this.searchDamageArray((JsonArray) e.getValue(), c);
                }
            }
        }
    }

    private void searchDamageArray(JsonArray array, boolean comb) {
        for (JsonValue v : array) {
            if (v instanceof JsonObject) {
                this.searchDamage((JsonObject) v, comb);
            } else if (v instanceof JsonArray) {
                this.searchDamageArray((JsonArray) v, comb);
            }
        }
    }

    private void searchDamageEc(JsonObject object, boolean fcomb, boolean ecomb) {
        for (JsonObject.Entry<String, JsonValue> e : object.entrySet()) {
            if (ValueType.NULL.equals(e.getValue().getValueType()))
                continue;
            if ("api_fdam".equals(e.getKey())) {
                JsonArray fdam = (JsonArray) e.getValue();
                for (int i = 0; i < fdam.size(); i++) {
                    if (fcomb) {
                        this.damage(DockType.FRIEND2, i < 6 ? i : i - 6, fdam.getJsonNumber(i).intValue());
                    } else if ((this.friends.get(0).size() < 7) && (i >= 6)) {
                        this.damage(DockType.FRIEND2, i - 6, fdam.getJsonNumber(i).intValue());
                    } else {
                        this.damage(DockType.FRIEND1, i, fdam.getJsonNumber(i).intValue());
                    }
                }
            } else if ("api_edam".equals(e.getKey())) {
                JsonArray edam = (JsonArray) e.getValue();
                for (int i = 0; i < edam.size(); i++) {
                    if (ecomb) {
                        this.damage(DockType.ENEMY2, i < 6 ? i : i - 6, edam.getJsonNumber(i).intValue());
                    } else if ((this.enemy1.size() < 7) && (i >= 6)) {
                        this.damage(DockType.ENEMY2, i - 6, edam.getJsonNumber(i).intValue());
                    } else {
                        this.damage(DockType.ENEMY1, i, edam.getJsonNumber(i).intValue());
                    }
                }
            } else if ("api_damage".equals(e.getKey())) {
                JsonArray eflag = object.containsKey("api_at_eflag") ? object.getJsonArray("api_at_eflag") : null;
                JsonArray dflist = object.getJsonArray("api_df_list");
                JsonArray damage = (JsonArray) e.getValue();
                for (int i = 0; i < damage.size(); i++) {
                    JsonValue v = damage.get(i);
                    boolean eflg = (eflag != null) && (eflag.getJsonNumber(i).intValue() != 0);
                    switch (v.getValueType()) {
                    case NUMBER:
                        if (eflg) {
                            if (fcomb) {
                                this.damage(DockType.FRIEND2, i < 6 ? i : i - 6, ((JsonNumber) v).intValue());
                            } else if ((this.friends.get(0).size() < 7) && (i >= 6)) {
                                this.damage(DockType.FRIEND2, i - 6, ((JsonNumber) v).intValue());
                            } else {
                                this.damage(DockType.FRIEND1, i, ((JsonNumber) v).intValue());
                            }
                        } else {
                            if (ecomb) {
                                this.damage(DockType.ENEMY2, i < 6 ? i : i - 6, ((JsonNumber) v).intValue());
                            } else if ((this.enemy1.size() < 7) && (i >= 6)) {
                                this.damage(DockType.ENEMY2, i - 6, ((JsonNumber) v).intValue());
                            } else {
                                this.damage(DockType.ENEMY1, i, ((JsonNumber) v).intValue());
                            }
                        }
                        break;
                    case ARRAY:
                        JsonArray dm = (JsonArray) v;
                        JsonArray df = dflist.getJsonArray(i);
                        for (int j = 0; j < dm.size(); j++) {
                            int idx = df.getJsonNumber(j).intValue();
                            if (eflg) {
                                if (fcomb) {
                                    this.damage(DockType.FRIEND2, idx < 6 ? idx : idx - 6,
                                            dm.getJsonNumber(j).intValue());
                                } else if ((this.friends.get(0).size() < 7) && (idx >= 6)) {
                                    this.damage(DockType.FRIEND2, idx - 6, dm.getJsonNumber(j).intValue());
                                } else {
                                    this.damage(DockType.FRIEND1, idx, dm.getJsonNumber(j).intValue());
                                }
                            } else {
                                if (ecomb) {
                                    this.damage(DockType.ENEMY2, idx < 6 ? idx : idx - 6,
                                            dm.getJsonNumber(j).intValue());
                                } else if ((this.enemy1.size() < 7) && (idx >= 6)) {
                                    this.damage(DockType.ENEMY2, idx - 6, dm.getJsonNumber(j).intValue());
                                } else {
                                    this.damage(DockType.ENEMY1, idx, dm.getJsonNumber(j).intValue());
                                }
                            }
                        }
                        break;
                    default:
                        break;
                    }
                }
            } else {
                boolean c = ((e.getKey() != null) && e.getKey().contains("_combined"));
                if (e.getValue() instanceof JsonObject) {
                    this.searchDamageEc((JsonObject) e.getValue(), c || fcomb, c || ecomb);
                } else if (e.getValue() instanceof JsonArray) {
                    this.searchDamageArrayEc((JsonArray) e.getValue(), c || fcomb, c || ecomb);
                }
            }
        }
    }

    private void searchDamageArrayEc(JsonArray array, boolean fcomb, boolean ecomb) {
        for (JsonValue v : array) {
            if (v instanceof JsonObject) {
                this.searchDamageEc((JsonObject) v, fcomb, ecomb);
            } else if (v instanceof JsonArray) {
                this.searchDamageArrayEc((JsonArray) v, fcomb, ecomb);
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

    private static String toCombined(int i) {
        String combined;
        switch (i) {
        case 1:
            combined = "空母機動部隊";
            break;
        case 2:
            combined = "水上打撃部隊";
            break;
        case 3:
            combined = "輸送護衛部隊";
            break;
        default:
            combined = "";
        }
        return combined;
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
    public List<ShipInfoDto> getEnemy1() {
        return this.enemy1;
    }

    /**
     * 敵艦隊を取得します。
     * @return 敵艦隊
     */
    public List<ShipInfoDto> getEnemy2() {
        return this.enemy2;
    }

    /**
     * 敵Lvを取得します。
     * @return 敵Lv
     */
    public List<Long> getEnemy1Lv() {
        return this.enemy1Lv;
    }

    /**
     * 敵Lvを取得します。
     * @return 敵Lv
     */
    public List<Long> getEnemy2Lv() {
        return this.enemy2Lv;
    }

    /**
     * 敵装備を取得します。
     * @return 敵装備
     */
    public List<ItemDto[]> getEnemy1Slot() {
        return this.enemy1Slot;
    }

    /**
     * 敵装備を取得します。
     * @return 敵装備
     */
    public List<ItemDto[]> getEnemy2Slot() {
        return this.enemy2Slot;
    }

    /**
     * @return endFriendHp
     */
    public int[] getEndFriend1Hp() {
        return this.endFriend1Hp;
    }

    /**
     * @return endCombinedHp
     */
    public int[] getEndFriend2Hp() {
        return this.endFriend2Hp;
    }

    /**
     * @return endEnemyHp
     */
    public int[] getEndEnemy1Hp() {
        return this.endEnemy1Hp;
    }

    /**
     * @return endEnemyHp
     */
    public int[] getEndEnemy2Hp() {
        return this.endEnemy2Hp;
    }

    /**
     * 味方HPを取得します。
     * @return 味方HP
     */
    public int[] getNowFriend1Hp() {
        return this.nowFriend1Hp;
    }

    /**
     * @return nowCombinedHp
     */
    public int[] getNowFriend2Hp() {
        return this.nowFriend2Hp;
    }

    /**
     * 敵HPを取得します。
     * @return 敵HP
     */
    public int[] getNowEnemy1Hp() {
        return this.nowEnemy1Hp;
    }

    /**
     * 敵HPを取得します。
     * @return 敵HP
     */
    public int[] getNowEnemy2Hp() {
        return this.nowEnemy2Hp;
    }

    /**
     * 味方MaxHPを取得します。
     * @return 味方MaxHP
     */
    public int[] getMaxFriend1Hp() {
        return this.maxFriend1Hp;
    }

    /**
     * @return maxCombinedHp
     */
    public int[] getMaxFriend2Hp() {
        return this.maxFriend2Hp;
    }

    /**
     * 敵MaxHPを取得します。
     * @return 敵MaxHP
     */
    public int[] getMaxEnemy1Hp() {
        return this.maxEnemy1Hp;
    }

    /**
     * 敵MaxHPを取得します。
     * @return 敵MaxHP
     */
    public int[] getMaxEnemy2Hp() {
        return this.maxEnemy2Hp;
    }

    /**
     * @return night
     */
    public boolean isNight() {
        return this.night;
    }

    /**
     * @return nightToDay
     */
    public boolean isNightToDay() {
        return this.nightToDay;
    }

    /**
     * @return combined
     */
    public boolean isFriendCombined() {
        return this.friendCombined != 0;
    }

    /**
     * @return combined
     */
    public boolean isEnemyCombined() {
        return this.enemyCombined;
    }

    /**
     * @return combinedType
     */
    public String getCombinedType() {
        return this.combinedType;
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
     * 味方索敵を取得します。
     * @return 味方索敵
     */
    public String getFriendSearch() {
        return this.friendSearch;
    }

    /**
     * 敵索敵を取得します。
     * @return 敵索敵
     */
    public String getEnemySearch() {
        return this.enemySearch;
    }

    /**
     * 味方触接を取得します。
     * @return 味方触接
     */
    public String getFriendTouch() {
        return this.friendTouch;
    }

    /**
     * 敵触接を取得します。
     * @return 敵触接
     */
    public String getEnemyTouch() {
        return this.enemyTouch;
    }

    /**
     * 制空を取得します。
     * @return 制空
     */
    public String getDispSeiku() {
        return this.dispSeiku;
    }

    /**
     * @return friendEscape
     */
    public boolean[] getFriend1Escape() {
        return this.friend1Escape;
    }

    /**
     * @return combinedEscape
     */
    public boolean[] getFriend2Escape() {
        return this.friend2Escape;
    }
}
