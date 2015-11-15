package logbook.gui.bean;

import java.text.SimpleDateFormat;

import logbook.annotation.Name;
import logbook.constants.AppConstants;
import logbook.dto.CreateItemDto;

/**
 * 開発報告書のBean
 *
 */
public class CreateItemReportBean {

    /** 日付 */
    @Name("日付")
    private String date;

    /** 開発装備 */
    @Name("開発装備")
    private String name;

    /** 種別 */
    @Name("種別")
    private String type;

    /** 燃料 */
    @Name("燃料")
    private String fuel;

    /** 弾薬 */
    @Name("弾薬")
    private String ammo;

    /** 鋼材 */
    @Name("鋼材")
    private String metal;

    /** ボーキ */
    @Name("ボーキ")
    private String bauxite;

    /** 秘書艦 */
    @Name("秘書艦")
    private String secretary;

    /** 司令部Lv */
    @Name("司令部Lv")
    private Integer hqLevel;

    /**
     * 日付を取得します。
     * @return 日付
     */
    public String getDate() {
        return this.date;
    }

    /**
     * 日付を設定します。
     * @param date 日付
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * 開発装備を取得します。
     * @return 開発装備
     */
    public String getName() {
        return this.name;
    }

    /**
     * 開発装備を設定します。
     * @param name 開発装備
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 種別を取得します。
     * @return 種別
     */
    public String getType() {
        return this.type;
    }

    /**
     * 種別を設定します。
     * @param type 種別
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 燃料を取得します。
     * @return 燃料
     */
    public String getFuel() {
        return this.fuel;
    }

    /**
     * 燃料を設定します。
     * @param fuel 燃料
     */
    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    /**
     * 弾薬を取得します。
     * @return 弾薬
     */
    public String getAmmo() {
        return this.ammo;
    }

    /**
     * 弾薬を設定します。
     * @param ammo 弾薬
     */
    public void setAmmo(String ammo) {
        this.ammo = ammo;
    }

    /**
     * 鋼材を取得します。
     * @return 鋼材
     */
    public String getMetal() {
        return this.metal;
    }

    /**
     * 鋼材を設定します。
     * @param metal 鋼材
     */
    public void setMetal(String metal) {
        this.metal = metal;
    }

    /**
     * ボーキを取得します。
     * @return ボーキ
     */
    public String getBauxite() {
        return this.bauxite;
    }

    /**
     * ボーキを設定します。
     * @param bauxite ボーキ
     */
    public void setBauxite(String bauxite) {
        this.bauxite = bauxite;
    }

    /**
     * 秘書艦を取得します。
     * @return 秘書艦
     */
    public String getSecretary() {
        return this.secretary;
    }

    /**
     * 秘書艦を設定します。
     * @param secretary 秘書艦
     */
    public void setSecretary(String secretary) {
        this.secretary = secretary;
    }

    /**
     * 司令部Lvを取得します。
     * @return 司令部Lv
     */
    public Integer getHqLevel() {
        return this.hqLevel;
    }

    /**
     * 司令部Lvを設定します。
     * @param hqLevel 司令部Lv
     */
    public void setHqLevel(Integer hqLevel) {
        this.hqLevel = hqLevel;
    }

    /**
     * CreateItemDto -&gt; CreateItemReportBean 変換
     *
     * @param e CreateItemDto
     * @return CreateItemReportBean
     */
    public static CreateItemReportBean toBean(CreateItemDto e) {
        CreateItemReportBean b = new CreateItemReportBean();
        if (e.isCreateFlag()) {
            b.setName(e.getName());
            b.setType(e.getType());
        } else {
            b.setName("失敗");
            b.setType("");
        }
        b.setDate(new SimpleDateFormat(AppConstants.DATE_FORMAT).format(e.getCreateDate()));
        b.setFuel(e.getFuel());
        b.setFuel(e.getFuel());
        b.setAmmo(e.getAmmo());
        b.setMetal(e.getMetal());
        b.setBauxite(e.getBauxite());
        b.setSecretary(e.getSecretary());
        b.setHqLevel(e.getHqLevel());
        return b;
    }
}
