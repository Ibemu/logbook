package logbook.gui.bean;

import java.text.SimpleDateFormat;

import logbook.annotation.Name;
import logbook.constants.AppConstants;
import logbook.dto.MissionResultDto;

/**
 * 遠征報告書のBean
 *
 */
public class MissionResultBean {

    /** 日付 */
    @Name("日付")
    private String date;

    /** 結果 */
    @Name("結果")
    private String result;

    /** 遠征 */
    @Name("遠征")
    private String name;

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
     * 結果を取得します。
     * @return 結果
     */
    public String getResult() {
        return this.result;
    }

    /**
     * 結果を設定します。
     * @param result 結果
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * 遠征を取得します。
     * @return 遠征
     */
    public String getName() {
        return this.name;
    }

    /**
     * 遠征を設定します。
     * @param name 遠征
     */
    public void setName(String name) {
        this.name = name;
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
     * MissionResultDto -> MissionResultBean 変換
     *
     * @param e MissionResultDto
     * @return MissionResultBean
     */
    public static MissionResultBean toBean(MissionResultDto e) {
        MissionResultBean b = new MissionResultBean();
        b.setDate(new SimpleDateFormat(AppConstants.DATE_FORMAT).format(e.getDate()));
        b.setResult(e.getClearResult());
        b.setName(e.getQuestName());
        b.setFuel(e.getFuel());
        b.setAmmo(e.getAmmo());
        b.setMetal(e.getMetal());
        b.setBauxite(e.getBauxite());
        return b;
    }
}
