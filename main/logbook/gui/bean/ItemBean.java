package logbook.gui.bean;

import logbook.annotation.Name;

/**
 * 所有装備一覧のBean
 *
 */
public class ItemBean {

    /** 名称 */
    @Name("名称")
    private String name;

    /** 種別 */
    @Name("種別")
    private String type;

    /** 個数 */
    @Name("個数")
    private Integer count;

    /** 火力 */
    @Name("火力")
    private Integer houg;

    /** 命中 */
    @Name("命中")
    private Integer houm;

    /** 射程 */
    @Name("射程")
    private Integer leng;

    /** 運 */
    @Name("運")
    private Integer luck;

    /** 回避 */
    @Name("回避")
    private Integer houk;

    /** 爆装 */
    @Name("爆装")
    private Integer baku;

    /** 雷装 */
    @Name("雷装")
    private Integer raig;

    /** 索敵 */
    @Name("索敵")
    private Integer saku;

    /** 対潜 */
    @Name("対潜")
    private Integer tais;

    /** 対空 */
    @Name("対空")
    private Integer tyku;

    /** 装甲 */
    @Name("装甲")
    private Integer souk;

    /**
     * 名称を取得します。
     * @return 名称
     */
    public String getName() {
        return this.name;
    }

    /**
     * 名称を設定します。
     * @param name 名称
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
     * 個数を取得します。
     * @return 個数
     */
    public Integer getCount() {
        return this.count;
    }

    /**
     * 個数を設定します。
     * @param count 個数
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 火力を取得します。
     * @return 火力
     */
    public Integer getHoug() {
        return this.houg;
    }

    /**
     * 火力を設定します。
     * @param houg 火力
     */
    public void setHoug(Integer houg) {
        this.houg = houg;
    }

    /**
     * 命中を取得します。
     * @return 命中
     */
    public Integer getHoum() {
        return this.houm;
    }

    /**
     * 命中を設定します。
     * @param houm 命中
     */
    public void setHoum(Integer houm) {
        this.houm = houm;
    }

    /**
     * 射程を取得します。
     * @return 射程
     */
    public Integer getLeng() {
        return this.leng;
    }

    /**
     * 射程を設定します。
     * @param leng 射程
     */
    public void setLeng(Integer leng) {
        this.leng = leng;
    }

    /**
     * 運を取得します。
     * @return 運
     */
    public Integer getLuck() {
        return this.luck;
    }

    /**
     * 運を設定します。
     * @param luck 運
     */
    public void setLuck(Integer luck) {
        this.luck = luck;
    }

    /**
     * 回避を取得します。
     * @return 回避
     */
    public Integer getHouk() {
        return this.houk;
    }

    /**
     * 回避を設定します。
     * @param houk 回避
     */
    public void setHouk(Integer houk) {
        this.houk = houk;
    }

    /**
     * 爆装を取得します。
     * @return 爆装
     */
    public Integer getBaku() {
        return this.baku;
    }

    /**
     * 爆装を設定します。
     * @param baku 爆装
     */
    public void setBaku(Integer baku) {
        this.baku = baku;
    }

    /**
     * 雷装を取得します。
     * @return 雷装
     */
    public Integer getRaig() {
        return this.raig;
    }

    /**
     * 雷装を設定します。
     * @param raig 雷装
     */
    public void setRaig(Integer raig) {
        this.raig = raig;
    }

    /**
     * 索敵を取得します。
     * @return 索敵
     */
    public Integer getSaku() {
        return this.saku;
    }

    /**
     * 索敵を設定します。
     * @param saku 索敵
     */
    public void setSaku(Integer saku) {
        this.saku = saku;
    }

    /**
     * 対潜を取得します。
     * @return 対潜
     */
    public Integer getTais() {
        return this.tais;
    }

    /**
     * 対潜を設定します。
     * @param tais 対潜
     */
    public void setTais(Integer tais) {
        this.tais = tais;
    }

    /**
     * 対空を取得します。
     * @return 対空
     */
    public Integer getTyku() {
        return this.tyku;
    }

    /**
     * 対空を設定します。
     * @param tyku 対空
     */
    public void setTyku(Integer tyku) {
        this.tyku = tyku;
    }

    /**
     * 装甲を取得します。
     * @return 装甲
     */
    public Integer getSouk() {
        return this.souk;
    }

    /**
     * 装甲を設定します。
     * @param souk 装甲
     */
    public void setSouk(Integer souk) {
        this.souk = souk;
    }
}
