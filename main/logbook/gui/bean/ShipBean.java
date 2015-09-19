package logbook.gui.bean;

import logbook.annotation.Name;
import logbook.dto.ShipDto;

/**
 * 所有艦娘一覧のBean
 *
 */
public class ShipBean {

    /** ID */
    @Name("ID")
    private Long id;

    /** 艦隊 */
    @Name("艦隊")
    private String fleetid;

    /** 名前 */
    @Name("名前")
    private String name;

    /** 艦種 */
    @Name("艦種")
    private String type;

    /** 疲労 */
    @Name("疲労")
    private Long cond;

    /** 回復 */
    @Name("回復")
    private String condClearDate;

    /** Lv */
    @Name("Lv")
    private Long lv;

    /** Next */
    @Name("Next")
    private String next;

    /** 経験値 */
    @Name("経験値")
    private Long exp;

    /** 出撃海域 */
    @Name("出撃海域")
    private String sallyArea;

    /** 制空 */
    @Name("制空")
    private Integer seiku;

    /** 装備1 */
    @Name("装備1")
    private String slot1;

    /** 装備2 */
    @Name("装備2")
    private String slot2;

    /** 装備3 */
    @Name("装備3")
    private String slot3;

    /** 装備4 */
    @Name("装備4")
    private String slot4;

    /** 補強増設 */
    @Name("補強増設")
    private String slot6;

    /** HP */
    @Name("HP")
    private Long hp;

    /** 火力 */
    @Name("火力")
    private Long karyoku;

    /** 雷装 */
    @Name("雷装")
    private Long raisou;

    /** 対空 */
    @Name("対空")
    private Long taiku;

    /** 装甲 */
    @Name("装甲")
    private Long soukou;

    /** 回避 */
    @Name("回避")
    private Long kaihi;

    /** 対潜 */
    @Name("対潜")
    private Long taisen;

    /** 索敵 */
    @Name("索敵")
    private Long sakuteki;

    /** 運 */
    @Name("運")
    private Long lucky;

    /** 装備命中 */
    @Name("装備命中")
    private Long accuracy;

    /** 砲撃戦火力 */
    @Name("砲撃戦火力")
    private Long hougekiPower;

    /** 雷撃戦火力 */
    @Name("雷撃戦火力")
    private Long raigekiPower;

    /** 対潜火力 */
    @Name("対潜火力")
    private Long taisenPower;

    /** 夜戦火力 */
    @Name("夜戦火力")
    private Long yasenPower;

    /** 艦娘 */
    private ShipDto ship;

    /**
     * IDを取得します。
     * @return ID
     */
    public Long getId() {
        return this.id;
    }

    /**
     * IDを設定します。
     * @param id ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 艦隊を取得します。
     * @return 艦隊
     */
    public String getFleetid() {
        return this.fleetid;
    }

    /**
     * 艦隊を設定します。
     * @param fleetid 艦隊
     */
    public void setFleetid(String fleetid) {
        this.fleetid = fleetid;
    }

    /**
     * 名前を取得します。
     * @return 名前
     */
    public String getName() {
        return this.name;
    }

    /**
     * 名前を設定します。
     * @param name 名前
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 艦種を取得します。
     * @return 艦種
     */
    public String getType() {
        return this.type;
    }

    /**
     * 艦種を設定します。
     * @param type 艦種
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 疲労を取得します。
     * @return 疲労
     */
    public Long getCond() {
        return this.cond;
    }

    /**
     * 疲労を設定します。
     * @param cond 疲労
     */
    public void setCond(Long cond) {
        this.cond = cond;
    }

    /**
     * 回復を取得します。
     * @return 回復
     */
    public String getCondClearDate() {
        return this.condClearDate;
    }

    /**
     * 回復を設定します。
     * @param condClearDate 回復
     */
    public void setCondClearDate(String condClearDate) {
        this.condClearDate = condClearDate;
    }

    /**
     * Lvを取得します。
     * @return Lv
     */
    public Long getLv() {
        return this.lv;
    }

    /**
     * Lvを設定します。
     * @param lv Lv
     */
    public void setLv(Long lv) {
        this.lv = lv;
    }

    /**
     * Nextを取得します。
     * @return Next
     */
    public String getNext() {
        return this.next;
    }

    /**
     * Nextを設定します。
     * @param next Next
     */
    public void setNext(String next) {
        this.next = next;
    }

    /**
     * 経験値を取得します。
     * @return 経験値
     */
    public Long getExp() {
        return this.exp;
    }

    /**
     * 経験値を設定します。
     * @param exp 経験値
     */
    public void setExp(Long exp) {
        this.exp = exp;
    }

    /**
     * 出撃海域を取得します。
     * @return 出撃海域
     */
    public String getSallyArea() {
        return this.sallyArea;
    }

    /**
     * 出撃海域を設定します。
     * @param sallyArea 出撃海域
     */
    public void setSallyArea(String sallyArea) {
        this.sallyArea = sallyArea;
    }

    /**
     * 制空を取得します。
     * @return 制空
     */
    public Integer getSeiku() {
        return this.seiku;
    }

    /**
     * 制空を設定します。
     * @param seiku 制空
     */
    public void setSeiku(Integer seiku) {
        this.seiku = seiku;
    }

    /**
     * 装備1を取得します。
     * @return 装備1
     */
    public String getSlot1() {
        return this.slot1;
    }

    /**
     * 装備1を設定します。
     * @param slot1 装備1
     */
    public void setSlot1(String slot1) {
        this.slot1 = slot1;
    }

    /**
     * 装備2を取得します。
     * @return 装備2
     */
    public String getSlot2() {
        return this.slot2;
    }

    /**
     * 装備2を設定します。
     * @param slot2 装備2
     */
    public void setSlot2(String slot2) {
        this.slot2 = slot2;
    }

    /**
     * 装備3を取得します。
     * @return 装備3
     */
    public String getSlot3() {
        return this.slot3;
    }

    /**
     * 装備3を設定します。
     * @param slot3 装備3
     */
    public void setSlot3(String slot3) {
        this.slot3 = slot3;
    }

    /**
     * 装備4を取得します。
     * @return 装備4
     */
    public String getSlot4() {
        return this.slot4;
    }

    /**
     * 装備4を設定します。
     * @param slot4 装備4
     */
    public void setSlot4(String slot4) {
        this.slot4 = slot4;
    }

    /**
     * 補強増設を取得します。
     * @return 補強増設
     */
    public String getSlot6() {
        return this.slot6;
    }

    /**
     * 補強増設を設定します。
     * @param slot6 補強増設
     */
    public void setSlot6(String slot6) {
        this.slot6 = slot6;
    }

    /**
     * HPを取得します。
     * @return HP
     */
    public Long getHp() {
        return this.hp;
    }

    /**
     * HPを設定します。
     * @param hp HP
     */
    public void setHp(Long hp) {
        this.hp = hp;
    }

    /**
     * 火力を取得します。
     * @return 火力
     */
    public Long getKaryoku() {
        return this.karyoku;
    }

    /**
     * 火力を設定します。
     * @param karyoku 火力
     */
    public void setKaryoku(Long karyoku) {
        this.karyoku = karyoku;
    }

    /**
     * 雷装を取得します。
     * @return 雷装
     */
    public Long getRaisou() {
        return this.raisou;
    }

    /**
     * 雷装を設定します。
     * @param raisou 雷装
     */
    public void setRaisou(Long raisou) {
        this.raisou = raisou;
    }

    /**
     * 対空を取得します。
     * @return 対空
     */
    public Long getTaiku() {
        return this.taiku;
    }

    /**
     * 対空を設定します。
     * @param taiku 対空
     */
    public void setTaiku(Long taiku) {
        this.taiku = taiku;
    }

    /**
     * 装甲を取得します。
     * @return 装甲
     */
    public Long getSoukou() {
        return this.soukou;
    }

    /**
     * 装甲を設定します。
     * @param soukou 装甲
     */
    public void setSoukou(Long soukou) {
        this.soukou = soukou;
    }

    /**
     * 回避を取得します。
     * @return 回避
     */
    public Long getKaihi() {
        return this.kaihi;
    }

    /**
     * 回避を設定します。
     * @param kaihi 回避
     */
    public void setKaihi(Long kaihi) {
        this.kaihi = kaihi;
    }

    /**
     * 対潜を取得します。
     * @return 対潜
     */
    public Long getTaisen() {
        return this.taisen;
    }

    /**
     * 対潜を設定します。
     * @param taisen 対潜
     */
    public void setTaisen(Long taisen) {
        this.taisen = taisen;
    }

    /**
     * 索敵を取得します。
     * @return 索敵
     */
    public Long getSakuteki() {
        return this.sakuteki;
    }

    /**
     * 索敵を設定します。
     * @param sakuteki 索敵
     */
    public void setSakuteki(Long sakuteki) {
        this.sakuteki = sakuteki;
    }

    /**
     * 運を取得します。
     * @return 運
     */
    public Long getLucky() {
        return this.lucky;
    }

    /**
     * 運を設定します。
     * @param lucky 運
     */
    public void setLucky(Long lucky) {
        this.lucky = lucky;
    }

    /**
     * 装備命中を取得します。
     * @return 装備命中
     */
    public Long getAccuracy() {
        return this.accuracy;
    }

    /**
     * 装備命中を設定します。
     * @param accuracy 装備命中
     */
    public void setAccuracy(Long accuracy) {
        this.accuracy = accuracy;
    }

    /**
     * 砲撃戦火力を取得します。
     * @return 砲撃戦火力
     */
    public Long getHougekiPower() {
        return this.hougekiPower;
    }

    /**
     * 砲撃戦火力を設定します。
     * @param hougekiPower 砲撃戦火力
     */
    public void setHougekiPower(Long hougekiPower) {
        this.hougekiPower = hougekiPower;
    }

    /**
     * 雷撃戦火力を取得します。
     * @return 雷撃戦火力
     */
    public Long getRaigekiPower() {
        return this.raigekiPower;
    }

    /**
     * 雷撃戦火力を設定します。
     * @param raigekiPower 雷撃戦火力
     */
    public void setRaigekiPower(Long raigekiPower) {
        this.raigekiPower = raigekiPower;
    }

    /**
     * 対潜火力を取得します。
     * @return 対潜火力
     */
    public Long getTaisenPower() {
        return this.taisenPower;
    }

    /**
     * 対潜火力を設定します。
     * @param taisenPower 対潜火力
     */
    public void setTaisenPower(Long taisenPower) {
        this.taisenPower = taisenPower;
    }

    /**
     * 夜戦火力を取得します。
     * @return 夜戦火力
     */
    public Long getYasenPower() {
        return this.yasenPower;
    }

    /**
     * 夜戦火力を設定します。
     * @param yasenPower 夜戦火力
     */
    public void setYasenPower(Long yasenPower) {
        this.yasenPower = yasenPower;
    }

    /**
     * 艦娘を取得します。
     * @return 艦娘
     */
    public ShipDto getShip() {
        return ship;
    }

    /**
     * 艦娘を設定します。
     * @param ship 艦娘
     */
    public void setShip(ShipDto ship) {
        this.ship = ship;
    }

}
