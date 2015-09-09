package logbook.gui.bean;

import logbook.annotation.Name;

/**
 * 任務一覧のBean
 *
 */
public class QuestBean {

    /** 状態 */
    @Name("状態")
    private String state;

    /** タイトル */
    @Name("タイトル")
    private String title;

    /** 内容 */
    @Name("内容")
    private String detail;

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
     * 状態を取得します。
     * @return 状態
     */
    public String getState() {
        return this.state;
    }

    /**
     * 状態を設定します。
     * @param state 状態
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * タイトルを取得します。
     * @return タイトル
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * タイトルを設定します。
     * @param title タイトル
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 内容を取得します。
     * @return 内容
     */
    public String getDetail() {
        return this.detail;
    }

    /**
     * 内容を設定します。
     * @param detail 内容
     */
    public void setDetail(String detail) {
        this.detail = detail;
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

}
