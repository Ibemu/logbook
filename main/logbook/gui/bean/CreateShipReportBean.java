package logbook.gui.bean;

import java.text.SimpleDateFormat;

import logbook.annotation.Name;
import logbook.constants.AppConstants;
import logbook.dto.GetShipDto;

/**
 * 建造報告書のBean
 *
 */
public class CreateShipReportBean {

    /** 日付 */
    @Name("日付")
    private String date;

    /** 種類 */
    @Name("種類")
    private String buildType;

    /** 名前 */
    @Name("名前")
    private String name;

    /** 艦種 */
    @Name("艦種")
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

    /** 開発資材 */
    @Name("開発資材")
    private String researchMaterials;

    /** 空きドック */
    @Name("空きドック")
    private String freeDock;

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
     * 種類を取得します。
     * @return 種類
     */
    public String getBuildType() {
        return this.buildType;
    }

    /**
     * 種類を設定します。
     * @param buildType 種類
     */
    public void setBuildType(String buildType) {
        this.buildType = buildType;
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
     * 開発資材を取得します。
     * @return 開発資材
     */
    public String getResearchMaterials() {
        return this.researchMaterials;
    }

    /**
     * 開発資材を設定します。
     * @param researchMaterials 開発資材
     */
    public void setResearchMaterials(String researchMaterials) {
        this.researchMaterials = researchMaterials;
    }

    /**
     * 空きドックを取得します。
     * @return 空きドック
     */
    public String getFreeDock() {
        return this.freeDock;
    }

    /**
     * 空きドックを設定します。
     * @param freeDock 空きドック
     */
    public void setFreeDock(String freeDock) {
        this.freeDock = freeDock;
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
     * GetShipDto -&gt; CreateShipReportBean 変換
     *
     * @param e GetShipDto
     * @return CreateShipReportBean
     */
    public static CreateShipReportBean toBean(GetShipDto e) {
        CreateShipReportBean b = new CreateShipReportBean();
        b.setDate(new SimpleDateFormat(AppConstants.DATE_FORMAT).format(e.getGetDate()));
        b.setBuildType(e.getBuildType());
        b.setName(e.getName());
        b.setType(e.getType());
        b.setFuel(e.getFuel());
        b.setAmmo(e.getAmmo());
        b.setMetal(e.getMetal());
        b.setBauxite(e.getBauxite());
        b.setResearchMaterials(e.getResearchMaterials());
        b.setFreeDock(e.getFreeDock());
        b.setSecretary(e.getSecretary());
        b.setHqLevel(e.getHqLevel());
        return b;
    }
}
