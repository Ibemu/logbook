package logbook.gui.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

import logbook.annotation.Name;
import logbook.constants.AppConstants;
import logbook.dto.ShipDto;
import logbook.gui.logic.TimeLogic;

/**
 * お風呂に入りたい艦娘のBean
 *
 */
public class BathwaterBean {

    /** 艦娘ID */
    @Name("艦娘ID")
    private Long id;

    /** 艦隊 */
    @Name("艦隊")
    private String fleet;

    /** 疲労 */
    @Name("疲労")
    private Long cond;

    /** 名前 */
    @Name("名前")
    private String name;

    /** Lv */
    @Name("Lv")
    private Long lv;

    /** HP */
    @Name("HP")
    private String hp;

    /** 時間 */
    @Name("時間")
    private String time;

    /** 今から */
    @Name("今から")
    private String fromNow;

    /** 燃料 */
    @Name("燃料")
    private Long fuel;

    /** 鋼材 */
    @Name("鋼材")
    private Long metal;

    /** 状態 */
    @Name("状態")
    private String status;

    /** HP1あたり */
    @Name("HP1あたり")
    private String oneHp;

    /**
     * 艦娘IDを取得します。
     * @return 艦娘ID
     */
    public Long getId() {
        return this.id;
    }

    /**
     * 艦娘IDを設定します。
     * @param id 艦娘ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 艦隊を取得します。
     * @return 艦隊
     */
    public String getFleet() {
        return this.fleet;
    }

    /**
     * 艦隊を設定します。
     * @param fleet 艦隊
     */
    public void setFleet(String fleet) {
        this.fleet = fleet;
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
     * HPを取得します。
     * @return HP
     */
    public String getHp() {
        return this.hp;
    }

    /**
     * HPを設定します。
     * @param hp HP
     */
    public void setHp(String hp) {
        this.hp = hp;
    }

    /**
     * 時間を取得します。
     * @return 時間
     */
    public String getTime() {
        return this.time;
    }

    /**
     * 時間を設定します。
     * @param time 時間
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 今からを取得します。
     * @return 今から
     */
    public String getFromNow() {
        return this.fromNow;
    }

    /**
     * 今からを設定します。
     * @param fromNow 今から
     */
    public void setFromNow(String fromNow) {
        this.fromNow = fromNow;
    }

    /**
     * 燃料を取得します。
     * @return 燃料
     */
    public Long getFuel() {
        return this.fuel;
    }

    /**
     * 燃料を設定します。
     * @param fuel 燃料
     */
    public void setFuel(Long fuel) {
        this.fuel = fuel;
    }

    /**
     * 鋼材を取得します。
     * @return 鋼材
     */
    public Long getMetal() {
        return this.metal;
    }

    /**
     * 鋼材を設定します。
     * @param metal 鋼材
     */
    public void setMetal(Long metal) {
        this.metal = metal;
    }

    /**
     * 状態を取得します。
     * @return 状態
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * 状態を設定します。
     * @param status 状態
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * HP1あたりを取得します。
     * @return HP1あたり
     */
    public String getOneHp() {
        return this.oneHp;
    }

    /**
     * HP1あたりを設定します。
     * @param oneHp HP1あたり
     */
    public void setOneHp(String oneHp) {
        this.oneHp = oneHp;
    }

    /**
     * ShipDto -&gt; BathwaterBean 変換
     *
     * @param e ShipDto
     * @return BathwaterBean
     */
    public static BathwaterBean toBean(ShipDto e) {
        BathwaterBean b = new BathwaterBean();
        // 状態
        if (e.isBadlyDamage()) {
            b.setStatus("大破");
        } else if (e.isHalfDamage()) {
            b.setStatus("中破");
        } else if (e.isSlightDamage()) {
            b.setStatus("小破");
        } else {
            b.setStatus("");
        }
        SimpleDateFormat format = new SimpleDateFormat(AppConstants.DATE_SHORT_FORMAT);
        // 整形
        b.setId(e.getId());
        b.setFleet(e.getFleetid());
        b.setCond(e.getCond());
        b.setName(e.getName());
        b.setLv(e.getLv());
        b.setHp(e.getNowhp() + "/" + e.getMaxhp());
        b.setTime(TimeLogic.toDateRestString(e.getDocktime() / 1000));
        b.setFromNow(format.format(new Date(System.currentTimeMillis() + e.getDocktime())));
        b.setFuel(e.getDockfuel());
        b.setMetal(e.getDockmetal());
        // HP1あたりの時間
        String oneHp = TimeLogic.toDateRestString((long) (e.getDocktime()
                / (float) (e.getMaxhp() - e.getNowhp()) / 1000));
        b.setOneHp(oneHp);
        return b;
    }
}
