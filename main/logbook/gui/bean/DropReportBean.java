package logbook.gui.bean;

import java.text.SimpleDateFormat;

import logbook.annotation.Name;
import logbook.constants.AppConstants;
import logbook.dto.BattleResultDto;

/**
 * ドロップ報告書のBean
 *
 */
public class DropReportBean {

    /** 日付 */
    @Name("日付")
    private String date;

    /** 海域 */
    @Name("海域")
    private String questName;

    /** マス */
    @Name("マス")
    private Integer mapCellNo;

    /** ボス */
    @Name("ボス")
    private String bossText;

    /** ランク */
    @Name("ランク")
    private String rank;

    /** 艦隊行動 */
    @Name("艦隊行動")
    private String intercept;

    /** 味方陣形 */
    @Name("味方陣形")
    private String friendFormation;

    /** 敵陣形 */
    @Name("敵陣形")
    private String enemyFormation;

    /** 敵艦隊 */
    @Name("敵艦隊")
    private String enemyName;

    /** ドロップ艦種 */
    @Name("ドロップ艦種")
    private String dropType;

    /** ドロップ艦娘 */
    @Name("ドロップ艦娘")
    private String dropName;

    /** 海戦とドロップした艦娘 */
    private BattleResultDto result;

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
     * 海域を取得します。
     * @return 海域
     */
    public String getQuestName() {
        return this.questName;
    }

    /**
     * 海域を設定します。
     * @param questName 海域
     */
    public void setQuestName(String questName) {
        this.questName = questName;
    }

    /**
     * マスを取得します。
     * @return マス
     */
    public Integer getMapCellNo() {
        return this.mapCellNo;
    }

    /**
     * マスを設定します。
     * @param mapCellNo マス
     */
    public void setMapCellNo(Integer mapCellNo) {
        this.mapCellNo = mapCellNo;
    }

    /**
     * ボスを取得します。
     * @return ボス
     */
    public String getBossText() {
        return this.bossText;
    }

    /**
     * ボスを設定します。
     * @param bossText ボス
     */
    public void setBossText(String bossText) {
        this.bossText = bossText;
    }

    /**
     * ランクを取得します。
     * @return ランク
     */
    public String getRank() {
        return this.rank;
    }

    /**
     * ランクを設定します。
     * @param rank ランク
     */
    public void setRank(String rank) {
        this.rank = rank;
    }

    /**
     * 艦隊行動を取得します。
     * @return 艦隊行動
     */
    public String getIntercept() {
        return this.intercept;
    }

    /**
     * 艦隊行動を設定します。
     * @param intercept 艦隊行動
     */
    public void setIntercept(String intercept) {
        this.intercept = intercept;
    }

    /**
     * 味方陣形を取得します。
     * @return 味方陣形
     */
    public String getFriendFormation() {
        return this.friendFormation;
    }

    /**
     * 味方陣形を設定します。
     * @param friendFormation 味方陣形
     */
    public void setFriendFormation(String friendFormation) {
        this.friendFormation = friendFormation;
    }

    /**
     * 敵陣形を取得します。
     * @return 敵陣形
     */
    public String getEnemyFormation() {
        return this.enemyFormation;
    }

    /**
     * 敵陣形を設定します。
     * @param enemyFormation 敵陣形
     */
    public void setEnemyFormation(String enemyFormation) {
        this.enemyFormation = enemyFormation;
    }

    /**
     * 敵艦隊を取得します。
     * @return 敵艦隊
     */
    public String getEnemyName() {
        return this.enemyName;
    }

    /**
     * 敵艦隊を設定します。
     * @param enemyName 敵艦隊
     */
    public void setEnemyName(String enemyName) {
        this.enemyName = enemyName;
    }

    /**
     * ドロップ艦種を取得します。
     * @return ドロップ艦種
     */
    public String getDropType() {
        return this.dropType;
    }

    /**
     * ドロップ艦種を設定します。
     * @param dropType ドロップ艦種
     */
    public void setDropType(String dropType) {
        this.dropType = dropType;
    }

    /**
     * ドロップ艦娘を取得します。
     * @return ドロップ艦娘
     */
    public String getDropName() {
        return this.dropName;
    }

    /**
     * ドロップ艦娘を設定します。
     * @param dropName ドロップ艦娘
     */
    public void setDropName(String dropName) {
        this.dropName = dropName;
    }

    /**
     * 海戦とドロップした艦娘を取得します。
     * @return 海戦とドロップした艦娘
     */
    public BattleResultDto getResult() {
        return this.result;
    }

    /**
     * 海戦とドロップした艦娘を設定します。
     * @param result 海戦とドロップした艦娘
     */
    public void setResult(BattleResultDto result) {
        this.result = result;
    }

    /**
     * BattleResultDto -> DropReportBean 変換
     *
     * @param e BattleResultDto
     * @return DropReportBean
     */
    public static DropReportBean toBean(BattleResultDto e) {
        DropReportBean b = new DropReportBean();
        b.setDate(new SimpleDateFormat(AppConstants.DATE_FORMAT).format(e.getBattleDate()));
        b.setQuestName(e.getQuestName());
        b.setMapCellNo(e.getMapCellNo());
        b.setBossText(e.getBossText());
        b.setRank(e.getRank());
        b.setIntercept(e.getBattleDto().getIntercept());
        b.setFriendFormation(e.getBattleDto().getFriendFormation());
        b.setEnemyFormation(e.getBattleDto().getEnemyFormation());
        b.setEnemyName(e.getEnemyName());
        b.setDropType(e.getDropType());
        b.setDropName(e.getDropName());
        b.setResult(e);
        return b;
    }
}
