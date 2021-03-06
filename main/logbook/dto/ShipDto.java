package logbook.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.CheckForNull;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonValue;

import logbook.constants.AppConstants;
import logbook.data.context.ItemContext;
import logbook.internal.AntiAirCutInKind;
import logbook.internal.ExpTable;
import logbook.internal.SallyArea;
import logbook.internal.Ship;

/**
 * 艦娘を表します
 *
 */
public final class ShipDto extends AbstractDto {

    private static int skillLevel(int e) {
        switch (e) {
        case 1:
            return 10;
        case 2:
            return 25;
        case 3:
            return 40;
        case 4:
            return 55;
        case 5:
            return 70;
        case 6:
            return 85;
        case 7:
            return 100;
        }
        return 0;
    }

    // 制空ボーナス(艦戦)
    private static int bonusF(int e) {
        switch (e) {
        case 2:
            return 2;
        case 3:
            return 5;
        case 4:
            return 9;
        case 5:
            return 14;
        case 6:
            return 14;
        case 7:
            return 22;
        }
        return 0;
    }

    // 制空ボーナス(水爆)
    private static int bonusS(int e) {
        switch (e) {
        case 2:
            return 1;
        case 3:
            return 1;
        case 4:
            return 1;
        case 5:
            return 3;
        case 6:
            return 3;
        case 7:
            return 6;
        }
        return 0;
    }

    /** 日時 */
    private final Calendar time = Calendar.getInstance();

    /** 艦娘個人を識別するID */
    private final long id;

    /** 鍵付き */
    private final boolean locked;

    /** 艦隊ID */
    private String fleetid = "";

    /** 名前 */
    private final String name;

    /** 艦種 */
    private final String type;

    /** Lv */
    private final long lv;

    /** 疲労 */
    private final long cond;

    /** 入渠時間 */
    private long docktime;

    /** 修復資材 燃料 */
    private final long dockfuel;

    /** 修復資材 鋼材 */
    private final long dockmetal;

    /** 残弾 */
    private int bull;

    /** 弾Max */
    private final int bullmax;

    /** 残燃料 */
    private int fuel;

    /** 燃料Max */
    private final int fuelmax;

    /** 経験値 */
    private final long exp;

    /** 経験値ゲージの割合 */
    private final float expraito;

    /** HP */
    private long nowhp;

    /** MaxHP */
    private final long maxhp;

    /** 装備 */
    private final List<Long> slot;

    /** 装備の搭載数 */
    private final List<Integer> onslot;

    /** 火力 */
    private final long karyoku;

    /** 火力(最大) */
    private final long karyokuMax;

    /** 雷装 */
    private final long raisou;

    /** 雷装(最大) */
    private final long raisouMax;

    /** 対空 */
    private final long taiku;

    /** 対空(最大) */
    private final long taikuMax;

    /** 装甲 */
    private final long soukou;

    /** 装甲(最大) */
    private final long soukouMax;

    /** 回避 */
    private final long kaihi;

    /** 回避(最大) */
    private final long kaihiMax;

    /** 対潜 */
    private final long taisen;

    /** 対潜(最大) */
    private final long taisenMax;

    /** 索敵 */
    private final long sakuteki;

    /** 索敵(最大) */
    private final long sakutekiMax;

    /** 運 */
    private final long lucky;

    /** 運(最大) */
    private final long luckyMax;

    /** 艦娘 */
    private final ShipInfoDto shipInfo;

    /** 出撃海域 */
    private final SallyArea sallyArea;

    /** */
    private final int lockedEquip;

    /** 空母 */
    private final boolean isCarrier;

    /** 装備リスト */
    private List<String> itemNames;

    /** 装備リスト */
    private List<ItemDto> items;

    /** 補強増設 */
    private boolean hasExSlot;

    /** 対空CI */
    private Set<AntiAirCutInKind> aaci;

    /**
     * コンストラクター
     *
     * @param object JSON Object
     */
    public ShipDto(JsonObject object) {

        this.id = object.getJsonNumber("api_id").longValue();
        this.locked = object.getJsonNumber("api_locked").longValue() == 1;

        ShipInfoDto shipinfo = Ship.get(object.getJsonNumber("api_ship_id").intValue());
        this.shipInfo = shipinfo;
        this.name = shipinfo.getName();
        this.type = shipinfo.getType();

        this.lv = object.getJsonNumber("api_lv").longValue();
        this.cond = object.getJsonNumber("api_cond").longValue();

        this.docktime = object.getJsonNumber("api_ndock_time").longValue();
        this.dockfuel = object.getJsonArray("api_ndock_item").getJsonNumber(0).longValue();
        this.dockmetal = object.getJsonArray("api_ndock_item").getJsonNumber(1).longValue();

        this.bull = object.getJsonNumber("api_bull").intValue();
        this.fuel = object.getJsonNumber("api_fuel").intValue();
        this.bullmax = shipinfo.getMaxBull();
        this.fuelmax = shipinfo.getMaxFuel();

        this.exp = object.getJsonArray("api_exp").getJsonNumber(0).longValue();
        this.expraito = object.getJsonArray("api_exp").getJsonNumber(2).longValue() / 100f;
        this.nowhp = object.getJsonNumber("api_nowhp").longValue();
        this.maxhp = object.getJsonNumber("api_maxhp").longValue();
        this.slot = new ArrayList<Long>();
        JsonArray slot = object.getJsonArray("api_slot");
        for (JsonValue jsonValue : slot) {
            JsonNumber itemid = (JsonNumber) jsonValue;
            this.slot.add(Long.valueOf(itemid.longValue()));
        }
        if (object.containsKey("api_slot_ex")) {
            long itemid = object.getJsonNumber("api_slot_ex").longValue();
            this.hasExSlot = itemid != 0;
            this.slot.add(itemid);
        }
        this.onslot = new ArrayList<Integer>();
        JsonArray onslot = object.getJsonArray("api_onslot");
        for (JsonValue jsonValue : onslot) {
            JsonNumber itemid = (JsonNumber) jsonValue;
            this.onslot.add(Integer.valueOf(itemid.intValue()));
        }
        this.karyoku = ((JsonNumber) object.getJsonArray("api_karyoku").get(0)).longValue();
        this.karyokuMax = ((JsonNumber) object.getJsonArray("api_karyoku").get(1)).longValue();
        this.raisou = ((JsonNumber) object.getJsonArray("api_raisou").get(0)).longValue();
        this.raisouMax = ((JsonNumber) object.getJsonArray("api_raisou").get(1)).longValue();
        this.taiku = ((JsonNumber) object.getJsonArray("api_taiku").get(0)).longValue();
        this.taikuMax = ((JsonNumber) object.getJsonArray("api_taiku").get(1)).longValue();
        this.soukou = ((JsonNumber) object.getJsonArray("api_soukou").get(0)).longValue();
        this.soukouMax = ((JsonNumber) object.getJsonArray("api_soukou").get(1)).longValue();
        this.kaihi = ((JsonNumber) object.getJsonArray("api_kaihi").get(0)).longValue();
        this.kaihiMax = ((JsonNumber) object.getJsonArray("api_kaihi").get(1)).longValue();
        this.taisen = ((JsonNumber) object.getJsonArray("api_taisen").get(0)).longValue();
        this.taisenMax = ((JsonNumber) object.getJsonArray("api_taisen").get(1)).longValue();
        this.sakuteki = ((JsonNumber) object.getJsonArray("api_sakuteki").get(0)).longValue();
        this.sakutekiMax = ((JsonNumber) object.getJsonArray("api_sakuteki").get(1)).longValue();
        this.lucky = ((JsonNumber) object.getJsonArray("api_lucky").get(0)).longValue();
        this.luckyMax = ((JsonNumber) object.getJsonArray("api_lucky").get(1)).longValue();
        this.lockedEquip = object.getJsonNumber("api_locked_equip").intValue();
        if (object.containsKey("api_sally_area")) {
            this.sallyArea = SallyArea.valueOf(object.getJsonNumber("api_sally_area").intValue());
        } else {
            this.sallyArea = SallyArea.NOTHING;
        }
        // 疲労が抜ける時間を計算する
        if (this.cond < 49) {
            float compare = 49 - this.cond;
            int addMinutes = (int) (Math.ceil(compare / 3f) * 3);
            this.time.add(Calendar.MINUTE, addMinutes);
        }
        this.isCarrier = "水上機母艦".equals(this.type) || "軽空母".equals(this.type) || "正規空母".equals(this.type)
                || "装甲空母".equals(this.type);
        this.aaci = null;
    }

    /**
     * @return 艦娘個人を識別するID
     */
    public long getId() {
        return this.id;
    }

    /**
     * @return 鍵付き
     */
    public boolean getLocked() {
        return this.locked;
    }

    /**
     * @return 艦隊ID
     */
    public String getFleetid() {
        return this.fleetid;
    }

    /**
     * 艦隊IDを設定する
     *
     * @param fleetid 艦隊ID
     */
    public void setFleetid(String fleetid) {
        this.fleetid = fleetid;
    }

    /**
     * @return 名前
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return 艦種
     */
    public String getType() {
        return this.type;
    }

    /**
     * @return Lv
     */
    public long getLv() {
        return this.lv;
    }

    /**
     * @return 疲労
     */
    public long getCond() {
        return this.cond;
    }

    /**
     * @return 入渠時間
     */
    public long getDocktime() {
        return this.docktime;
    }

    /**
     * 入渠時間を設定します。
     * @param docktime 入渠時間
     */
    public void setDocktime(long docktime) {
        this.docktime = docktime;
    }

    /**
     * @return 修復資材 燃料
     */
    public long getDockfuel() {
        return this.dockfuel;
    }

    /**
     * @return 修復資材 鋼材
     */
    public long getDockmetal() {
        return this.dockmetal;
    }

    /**
     * @return 弾
     */
    public int getBull() {
        return this.bull;
    }

    /**
     * @return 弾Max
     */
    public int getBullMax() {
        return this.bullmax;
    }

    /**
     * @param bull 残弾
     */
    public void setBull(int bull) {
        this.bull = bull;
    }

    /**
     * @return 燃料
     */
    public int getFuel() {
        return this.fuel;
    }

    /**
     * @return 燃料Max
     */
    public int getFuelMax() {
        return this.fuelmax;
    }

    /**
     * @param fuel 残燃料
     */
    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    /**
     * @return 経験値
     */
    public long getExp() {
        return this.exp;
    }

    /**
     * @return 経験値ゲージの割合
     */
    public float getExpraito() {
        return this.expraito;
    }

    /**
     * @return HP
     */
    public long getNowhp() {
        return this.nowhp;
    }

    public void setNowHp(long nowhp) {
        this.nowhp = nowhp;
    }

    /**
     * @return MaxHP
     */
    public long getMaxhp() {
        return this.maxhp;
    }

    public void setSlot(List<Long> newSlot) {
        for (int i = 0; i < newSlot.size(); i++) {
            this.slot.set(i, newSlot.get(i));
        }
        this.itemNames = null;
        this.items = null;
        this.aaci = null;
    }

    /**
     * @return 装備
     */
    public List<String> getSlot() {
        if (this.itemNames == null) {
            this.itemNames = new ArrayList<String>();
            Map<Long, ItemDto> itemMap = ItemContext.get();
            Map<Long, Integer> levelMap = ItemContext.level();
            Map<Long, Integer> alvMap = ItemContext.alv();
            for (Long itemid : this.slot) {
                if (itemid > 0) {
                    ItemDto name = itemMap.get(itemid);
                    Integer level = levelMap.get(itemid);
                    Integer alv = alvMap.get(itemid);
                    boolean viewLevel = (level != null) && (level.intValue() > 0);
                    if (name != null) {
                        if (viewLevel || (alv != null)) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(name.getName());
                            if (alv != null) {
                                sb.append("☆+");
                                sb.append(alv);
                            }
                            if (viewLevel) {
                                sb.append("★+");
                                sb.append(level);
                            }
                            this.itemNames.add(sb.toString());
                        } else {
                            this.itemNames.add(name.getName());
                        }
                    } else {
                        this.itemNames.add("<UNKNOWN>");
                    }
                } else {
                    this.itemNames.add("");
                }
            }
        }
        return this.itemNames;
    }

    /**
     * @return 装備
     */
    public List<ItemDto> getItem() {
        if (this.items == null) {
            this.items = new ArrayList<ItemDto>();
            Map<Long, ItemDto> itemMap = ItemContext.get();
            for (Long itemid : this.slot) {
                if (-1 != itemid) {
                    ItemDto item = itemMap.get(itemid);
                    if (item != null) {
                        this.items.add(item);
                    } else {
                        this.items.add(null);
                    }
                } else {
                    this.items.add(null);
                }
            }
        }
        return this.items;
    }

    /**
     * @return 装備ID
     */
    public List<Long> getItemId() {
        return Collections.unmodifiableList(this.slot);
    }

    /**
     * @return 装備の搭載数
     */
    public List<Integer> getOnslot() {
        return this.onslot;
    }

    /**
     * @return 補強増設
     */
    public boolean HasExSlot() {
        return this.hasExSlot;
    }

    /**
     * @return 制空値
     */
    public int getSeiku() {
        List<ItemDto> items = this.getItem();
        Map<Long, Integer> alvMap = ItemContext.alv();
        Map<Long, Integer> levelMap = ItemContext.level();
        int seiku = 0;
        for (int i = 0; i < items.size(); i++) {
            ItemDto item = items.get(i);
            if (item != null) {
                if ((item.getType2() == 6)
                        || (item.getType2() == 7)
                        || (item.getType2() == 8)
                        || (item.getType2() == 11)
                        || (item.getType2() == 45)) {
                    //6:艦上戦闘機,7:艦上爆撃機,8:艦上攻撃機,11:水上爆撃機,45:水上戦闘機の場合は制空値を計算する
                    double bonus = 0;
                    Integer alv = alvMap.get(this.slot.get(i));
                    if (alv != null) {
                        bonus += Math.sqrt(skillLevel(alv) / 10.0);
                        if ((item.getType2() == 6) || (item.getType2() == 45))
                            bonus += bonusF(alv);
                        else if (item.getType2() == 11)
                            bonus += bonusS(alv);
                    }
                    double tyku = item.getTyku();
                    Integer level = levelMap.get(this.slot.get(i));
                    if (level != null) {
                        if ((item.getType2() == 6) || (item.getType2() == 45))
                            tyku += 0.2 * level;
                        else if (item.getType2() == 7)
                            tyku += 0.25 * level;
                    }
                    seiku += (int) Math.floor((tyku * Math.sqrt(this.onslot.get(i))) + bonus);
                }
            }
        }
        return seiku;
    }

    /**
     * @return 火力
     */
    public long getKaryoku() {
        return this.karyoku;
    }

    /**
     * @return 火力(最大)
     */
    public long getKaryokuMax() {
        return this.karyokuMax;
    }

    /**
     * @return 雷装
     */
    public long getRaisou() {
        return this.raisou;
    }

    /**
     * @return 雷装(最大)
     */
    public long getRaisouMax() {
        return this.raisouMax;
    }

    /**
     * @return 対空
     */
    public long getTaiku() {
        return this.taiku;
    }

    /**
     * @return 対空(最大)
     */
    public long getTaikuMax() {
        return this.taikuMax;
    }

    /**
     * @return 装甲
     */
    public long getSoukou() {
        return this.soukou;
    }

    /**
     * @return 装甲(最大)
     */
    public long getSoukouMax() {
        return this.soukouMax;
    }

    /**
     * @return 回避
     */
    public long getKaihi() {
        return this.kaihi;
    }

    /**
     * @return 回避(最大)
     */
    public long getKaihiMax() {
        return this.kaihiMax;
    }

    /**
     * @return 対潜
     */
    public long getTaisen() {
        return this.taisen;
    }

    /**
     * @return 対潜(最大)
     */
    public long getTaisenMax() {
        return this.taisenMax;
    }

    /**
     * @return 索敵
     */
    public long getSakuteki() {
        return this.sakuteki;
    }

    /**
     * @return 索敵(最大)
     */
    public long getSakutekiMax() {
        return this.sakutekiMax;
    }

    /**
     * @return 運
     */
    public long getLucky() {
        return this.lucky;
    }

    /**
     * @return 運(最大)
     */
    public long getLuckyMax() {
        return this.luckyMax;
    }

    /**
     * @return 艦娘
     */
    public ShipInfoDto getShipInfo() {
        return this.shipInfo;
    }

    /**
     * @return 次のレベルまでの経験値
     */
    public String getNext() {
        String next = "";
        Long nextLvExp = ExpTable.get().get((int) this.lv + 1);
        if (nextLvExp != null) {
            next = Long.toString(nextLvExp - this.exp);
        }
        return next;
    }

    /**
     * @return 疲労が抜けるまでの時間
     */
    public String getCondClearDateString() {
        if (this.cond < 49) {
            return new SimpleDateFormat("HH:mm").format(this.time.getTime());
        }
        return "";
    }

    /**
     * @return 疲労が抜けるまでの時間
     */
    @CheckForNull
    public Date getCondClearDate() {
        if (this.cond < 49) {
            return this.time.getTime();
        }
        return null;
    }

    /**
     * 艦娘が大破しているかを調べます
     * @return 大破以上の場合
     */
    public boolean isBadlyDamage() {
        return ((float) this.nowhp / (float) this.maxhp) <= AppConstants.BADLY_DAMAGE;
    }

    /**
     * 艦娘が中破しているかを調べます
     * @return 中破以上の場合
     */
    public boolean isHalfDamage() {
        return ((float) this.nowhp / (float) this.maxhp) <= AppConstants.HALF_DAMAGE;
    }

    /**
     * 艦娘が小破しているかを調べます
     * @return 小破以上の場合
     */
    public boolean isSlightDamage() {
        return ((float) this.nowhp / (float) this.maxhp) <= AppConstants.SLIGHT_DAMAGE;
    }

    /**
     * 出撃海域を取得します。
     * @return 出撃海域
     */
    public SallyArea getSallyArea() {
        return this.sallyArea;
    }

    /**
     * lockedEquipを取得します。
     * @return lockedEquip
     */
    public int getLockedEquip() {
        return this.lockedEquip;
    }

    /**
     * 装備で加算された命中
     *
     * @return 装備の命中
     */
    public long getAccuracy() {
        long accuracy = 0;
        for (Long itemid : this.slot) {
            if (-1 != itemid) {
                Map<Long, ItemDto> itemMap = ItemContext.get();
                ItemDto item = itemMap.get(itemid);
                if (item != null) {
                    accuracy += item.getHoum();
                }
            }
        }
        return accuracy;
    }

    /**
     * 砲撃戦火力
     *
     * @return 砲撃戦火力
     */
    public long getHougekiPower() {
        if (this.isCarrier) {
            // (火力 + 雷装) × 1.5 + 爆装 × 2 + 55
            long rai = 0;
            long baku = 0;
            Map<Long, ItemDto> itemMap = ItemContext.get();
            for (Long itemid : this.slot) {
                if (-1 != itemid) {
                    ItemDto item = itemMap.get(itemid);
                    if (item != null) {
                        rai += item.getRaig();
                        baku += item.getBaku();
                    }
                }
            }
            return Math.round(((this.getKaryoku() + rai) * 1.5d) + (baku * 2) + 55);
        } else {
            return this.getKaryoku() + 5;
        }
    }

    /**
     * 雷撃戦火力
     *
     * @return 雷撃戦火力
     */
    public long getRaigekiPower() {
        return this.getRaisou() + 5;
    }

    /**
     * 対潜火力
     *
     * @return 対潜火力
     */
    public long getTaisenPower() {
        // [ 艦船の対潜 ÷ 5 ] + 装備の対潜 × 2 + 25
        long taisenShip = this.getTaisen();
        long taisenItem = 0;
        Map<Long, ItemDto> itemMap = ItemContext.get();
        for (Long itemid : this.slot) {
            if (-1 != itemid) {
                ItemDto item = itemMap.get(itemid);
                if (item != null) {
                    int taisen = item.getTais();
                    taisenShip -= taisen;
                    taisenItem += taisen;
                }
            }
        }
        return Math.round(Math.floor(taisenShip / 5d) + (taisenItem * 2) + 25);
    }

    /**
     * 夜戦火力
     *
     * @return 夜戦火力
     */
    public long getYasenPower() {
        return this.getKaryoku() + this.getRaisou();
    }

    /**
     * 対潜先制爆雷攻撃可能
     *
     * @return 対潜先制爆雷攻撃
     */
    public boolean canTsbk() {
        if (this.getType() != null) {
            List<ItemDto> items = this.getItem();
            boolean sonar = false;
            long tais = this.getTaisen();
            switch (this.getName()) {
            case "五十鈴改二":
            case "龍田改二":
            case "Jervis改":
                return true;
            case "Gambier Bay":
            case "Gambier Bay改":
                if (tais >= 50)
                    for (ItemDto i : items) {
                        if (i == null)
                            continue;
                        switch (i.getType2()) {
                        case 40: //大型ソナー
                            return true;
                        }
                    }
                if (!"Gambier Bay改".equals(this.getName()))
                    break;
            case "大鷹":
            case "瑞鳳改二乙":
                if (tais >= 65)
                    for (ItemDto i : items) {
                        if (i == null)
                            continue;
                        switch (i.getType2()) {
                        case 7: //艦上爆撃機
                        case 8: //艦上攻撃機
                            if (i.getTais() >= 7)
                                return true;
                            break;
                        case 25: //オートジャイロ
                        case 26: //対潜哨戒機
                            return true;
                        }
                    }
                break;
            case "大鷹改":
            case "大鷹改二":
                for (ItemDto i : items) {
                    if (i == null)
                        continue;
                    switch (i.getType2()) {
                    case 7: //艦上爆撃機
                    case 8: //艦上攻撃機
                        if (i.getTais() >= 1)
                            return true;
                        break;
                    case 25: //オートジャイロ
                    case 26: //対潜哨戒機
                        return true;
                    }
                }
                break;
            }
            switch (this.getType()) {
            case "軽巡洋艦":
            case "駆逐艦":
            case "重雷装巡洋艦":
            case "練習巡洋艦":
                if (tais < 100)
                    return false;
                for (ItemDto item : items) {
                    if (item != null) {
                        tais -= item.getTais();
                        switch (item.getType2()) {
                        case 14: //ソナー
                        case 40: //大型ソナー
                            sonar = true;
                        }
                    }
                }
                return sonar && (tais > 0);

            case "海防艦":
                if (tais < 60)
                    return false;
                for (ItemDto item : items) {
                    if (item != null) {
                        tais -= item.getTais();
                        switch (item.getType2()) {
                        case 14: //ソナー
                        case 40: //大型ソナー
                            sonar = true;
                        }
                    }
                }
                return (tais > 0) && (sonar || ((this.getTaisen() >= 75) && ((this.getTaisen() - tais) >= 4)));
            }
        }
        return false;
    }

    /**
     * 発動できる対空カットイン種別
     *
     * @return 発動できる対空カットイン種別
     */
    public Set<AntiAirCutInKind> getAntiAirCI() {
        if (this.aaci == null)
            this.aaci = Arrays.stream(AntiAirCutInKind.values()).filter(k -> k.getCondition().test(this))
                    .collect(Collectors.toSet());
        return this.aaci;
    }

    /**
     * S勝利時のTP
     *
     * @return S勝利時のTP
     */
    public int getTP() {
        if ((this.getType() == null) || this.isBadlyDamage())
            return 0;
        int tp = 0;
        // 艦種
        switch (this.getType()) {
        case "駆逐艦":
            tp += 5;
            break;
        case "練習巡洋艦":
            tp += 6;
            break;
        case "軽巡洋艦":
            tp += 2;
            break;
        case "航空巡洋艦":
            tp += 4;
            break;
        case "航空戦艦":
            tp += 7;
            break;
        case "補給艦":
            tp += 15;
            break;
        case "水上機母艦":
            tp += 9;
            break;
        case "揚陸艦":
            tp += 12;
            break;
        case "潜水空母":
            tp += 1;
            break;
        case "潜水母艦":
            tp += 7;
            break;
        }
        if ("鬼怒改二".equals(this.getName()))
            tp += 8;
        // 装備
        for (ItemDto item : this.getItem()) {
            if (item != null) {
                switch (item.getName()) {
                case "ドラム缶(輸送用)":
                    tp += 5;
                    break;
                case "大発動艇":
                case "大発動艇(八九式中戦車&陸戦隊)":
                case "特大発動艇":
                case "特大発動艇+戦車第11連隊":
                    tp += 8;
                    break;
                case "特二式内火艇":
                    tp += 2;
                    break;
                case "戦闘糧食":
                case "戦闘糧食(特別なおにぎり)":
                case "秋刀魚の缶詰":
                    tp += 1;
                    break;
                }
            }
        }
        return tp;
    }
}
