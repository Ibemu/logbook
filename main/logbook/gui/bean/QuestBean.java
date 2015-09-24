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

    /** 期限 */
    @Name("期限")
    private String due;

    /** 出撃 */
    @Name("出撃")
    private Integer sortie;

    /** 戦闘勝利 */
    @Name("戦闘勝利")
    private Integer battleWin;

    /** 戦闘S */
    @Name("戦闘S")
    private Integer battleWinS;

    /** ボス到達 */
    @Name("ボス到達")
    private Integer bossArrive;

    /** ボス勝利 */
    @Name("ボス勝利")
    private Integer bossWin;

    /** 1-4S */
    @Name("1-4S")
    private Integer boss1_4WinS;

    /** 1-5A */
    @Name("1-5A")
    private Integer boss1_5WinA;

    /** 南西 */
    @Name("南西")
    private Integer boss2Win;

    /** 3-3+ */
    @Name("3-3+")
    private Integer boss3_3pWin;

    /** 西方 */
    @Name("西方")
    private Integer boss4Win;

    /** 4-4 */
    @Name("4-4")
    private Integer boss4_4Win;

    /** 5-2S */
    @Name("5-2S")
    private Integer boss5_2WinS;

    /** 6-1S */
    @Name("6-1S")
    private Integer boss6_1WinS;

    /** 補給艦 */
    @Name("補給艦")
    private Integer defeatAP;

    /** 空母 */
    @Name("空母")
    private Integer defeatCV;

    /** 潜水艦 */
    @Name("潜水艦")
    private Integer defeatSS;

    /** 演習 */
    @Name("演習")
    private Integer practice;

    /** 演習勝利 */
    @Name("演習勝利")
    private Integer practiceWin;

    /** 遠征 */
    @Name("遠征")
    private Integer missionSuccess;

    /** 建造 */
    @Name("建造")
    private Integer createShip;

    /** 開発 */
    @Name("開発")
    private Integer createItem;

    /** 解体 */
    @Name("解体")
    private Integer destroyShip;

    /** 廃棄 */
    @Name("廃棄")
    private Integer destroyItem;

    /** 補給 */
    @Name("補給")
    private Integer charge;

    /** 入渠 */
    @Name("入渠")
    private Integer repair;

    /** 改修 */
    @Name("改修")
    private Integer powerUp;

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

    /**
     * @return due
     */
    public String getDue() {
        return this.due;
    }

    /**
     * @param due セットする due
     */
    public void setDue(String due) {
        this.due = due;
    }

    /**
     * @return sortie
     */
    public Integer getSortie() {
        return this.sortie;
    }

    /**
     * @param sortie セットする sortie
     */
    public void setSortie(Integer sortie) {
        this.sortie = sortie;
    }

    /**
     * @return battleWin
     */
    public Integer getBattleWin() {
        return this.battleWin;
    }

    /**
     * @param battleWin セットする battleWin
     */
    public void setBattleWin(Integer battleWin) {
        this.battleWin = battleWin;
    }

    /**
     * @return battleWinS
     */
    public Integer getBattleWinS() {
        return this.battleWinS;
    }

    /**
     * @param battleWinS セットする battleWinS
     */
    public void setBattleWinS(Integer battleWinS) {
        this.battleWinS = battleWinS;
    }

    /**
     * @return bossArrive
     */
    public Integer getBossArrive() {
        return this.bossArrive;
    }

    /**
     * @param bossArrive セットする bossArrive
     */
    public void setBossArrive(Integer bossArrive) {
        this.bossArrive = bossArrive;
    }

    /**
     * @return bossWin
     */
    public Integer getBossWin() {
        return this.bossWin;
    }

    /**
     * @param bossWin セットする bossWin
     */
    public void setBossWin(Integer bossWin) {
        this.bossWin = bossWin;
    }

    /**
     * @return boss1_4WinS
     */
    public Integer getBoss1_4WinS() {
        return this.boss1_4WinS;
    }

    /**
     * @param boss1_4WinS セットする boss1_4WinS
     */
    public void setBoss1_4WinS(Integer boss1_4WinS) {
        this.boss1_4WinS = boss1_4WinS;
    }

    /**
     * @return boss1_5WinA
     */
    public Integer getBoss1_5WinA() {
        return this.boss1_5WinA;
    }

    /**
     * @param boss1_5WinA セットする boss1_5WinA
     */
    public void setBoss1_5WinA(Integer boss1_5WinA) {
        this.boss1_5WinA = boss1_5WinA;
    }

    /**
     * @return boss2Win
     */
    public Integer getBoss2Win() {
        return this.boss2Win;
    }

    /**
     * @param boss2Win セットする boss2Win
     */
    public void setBoss2Win(Integer boss2Win) {
        this.boss2Win = boss2Win;
    }

    /**
     * @return boss3_3pWin
     */
    public Integer getBoss3_3pWin() {
        return this.boss3_3pWin;
    }

    /**
     * @param boss3_3pWin セットする boss3_3pWin
     */
    public void setBoss3_3pWin(Integer boss3_3pWin) {
        this.boss3_3pWin = boss3_3pWin;
    }

    /**
     * @return boss4Win
     */
    public Integer getBoss4Win() {
        return this.boss4Win;
    }

    /**
     * @param boss4Win セットする boss4Win
     */
    public void setBoss4Win(Integer boss4Win) {
        this.boss4Win = boss4Win;
    }

    /**
     * @return boss4_4Win
     */
    public Integer getBoss4_4Win() {
        return this.boss4_4Win;
    }

    /**
     * @param boss4_4Win セットする boss4_4Win
     */
    public void setBoss4_4Win(Integer boss4_4Win) {
        this.boss4_4Win = boss4_4Win;
    }

    /**
     * @return boss5_2WinS
     */
    public Integer getBoss5_2WinS() {
        return this.boss5_2WinS;
    }

    /**
     * @param boss5_2WinS セットする boss5_2WinS
     */
    public void setBoss5_2WinS(Integer boss5_2WinS) {
        this.boss5_2WinS = boss5_2WinS;
    }

    /**
     * @return boss6_1WinS
     */
    public Integer getBoss6_1WinS() {
        return this.boss6_1WinS;
    }

    /**
     * @param boss6_1WinS セットする boss6_1WinS
     */
    public void setBoss6_1WinS(Integer boss6_1WinS) {
        this.boss6_1WinS = boss6_1WinS;
    }

    /**
     * @return defeatAP
     */
    public Integer getDefeatAP() {
        return this.defeatAP;
    }

    /**
     * @param defeatAP セットする defeatAP
     */
    public void setDefeatAP(Integer defeatAP) {
        this.defeatAP = defeatAP;
    }

    /**
     * @return defeatCV
     */
    public Integer getDefeatCV() {
        return this.defeatCV;
    }

    /**
     * @param defeatCV セットする defeatCV
     */
    public void setDefeatCV(Integer defeatCV) {
        this.defeatCV = defeatCV;
    }

    /**
     * @return defeatSS
     */
    public Integer getDefeatSS() {
        return this.defeatSS;
    }

    /**
     * @param defeatSS セットする defeatSS
     */
    public void setDefeatSS(Integer defeatSS) {
        this.defeatSS = defeatSS;
    }

    /**
     * @return practice
     */
    public Integer getPractice() {
        return this.practice;
    }

    /**
     * @param practice セットする practice
     */
    public void setPractice(Integer practice) {
        this.practice = practice;
    }

    /**
     * @return practiceWin
     */
    public Integer getPracticeWin() {
        return this.practiceWin;
    }

    /**
     * @param practiceWin セットする practiceWin
     */
    public void setPracticeWin(Integer practiceWin) {
        this.practiceWin = practiceWin;
    }

    /**
     * @return missionSuccess
     */
    public Integer getMissionSuccess() {
        return this.missionSuccess;
    }

    /**
     * @param missionSuccess セットする missionSuccess
     */
    public void setMissionSuccess(Integer missionSuccess) {
        this.missionSuccess = missionSuccess;
    }

    /**
     * @return createShip
     */
    public Integer getCreateShip() {
        return this.createShip;
    }

    /**
     * @param createShip セットする createShip
     */
    public void setCreateShip(Integer createShip) {
        this.createShip = createShip;
    }

    /**
     * @return createItem
     */
    public Integer getCreateItem() {
        return this.createItem;
    }

    /**
     * @param createItem セットする createItem
     */
    public void setCreateItem(Integer createItem) {
        this.createItem = createItem;
    }

    /**
     * @return destroyShip
     */
    public Integer getDestroyShip() {
        return this.destroyShip;
    }

    /**
     * @param destroyShip セットする destroyShip
     */
    public void setDestroyShip(Integer destroyShip) {
        this.destroyShip = destroyShip;
    }

    /**
     * @return destroyItem
     */
    public Integer getDestroyItem() {
        return this.destroyItem;
    }

    /**
     * @param destroyItem セットする destroyItem
     */
    public void setDestroyItem(Integer destroyItem) {
        this.destroyItem = destroyItem;
    }

    /**
     * @return charge
     */
    public Integer getCharge() {
        return this.charge;
    }

    /**
     * @param charge セットする charge
     */
    public void setCharge(Integer charge) {
        this.charge = charge;
    }

    /**
     * @return repair
     */
    public Integer getRepair() {
        return this.repair;
    }

    /**
     * @param repair セットする repair
     */
    public void setRepair(Integer repair) {
        this.repair = repair;
    }

    /**
     * @return powerUp
     */
    public Integer getPowerUp() {
        return this.powerUp;
    }

    /**
     * @param powerUp セットする powerUp
     */
    public void setPowerUp(Integer powerUp) {
        this.powerUp = powerUp;
    }

}
