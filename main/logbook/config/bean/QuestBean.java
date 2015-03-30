package logbook.config.bean;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;

/**
 * 任務の進行状況
 *
 */
public final class QuestBean {
    /** 遂行中 */
    private boolean executing;

    /**
     * 遂行中を取得します
     * @return 遂行中
     */
    public boolean getExecuting() {
        return this.executing;
    }

    /**
     * 遂行中を設定します
     * @param executing 遂行中
     */
    public void setExecuting(boolean executing) {
        this.executing=executing;
    }

    /** 出撃 */
    private Set<Date> sortie = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 出撃を取得します
     * @return 出撃
     */
    public Set<Date> getSortie() {
        return this.sortie;
    }

    /**
     * 出撃を設定します
     * @param sortie 出撃
     */
    public void setSortie(Set<Date> sortie) {
        this.sortie=sortie;
    }

    /**
     * 出撃をカウントします
     */
    public void countSortie(Date d) {
        if(this.sortie.size()<50) this.sortie.add(d);
    }

    /** ボス到達 */
    private Set<Date> bossArrive = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * ボス到達を取得します
     * @return ボス到達
     */
    public Set<Date> getBossArrive() {
        return this.bossArrive;
    }

    /**
     * ボス到達を設定します
     * @param bossArrive ボス到達
     */
    public void setBossArrive(Set<Date> bossArrive) {
        this.bossArrive=bossArrive;
    }

    /**
     * ボス到達をカウントします
     */
    public void countBossArrive(Date d) {
        if(this.bossArrive.size()<50) this.bossArrive.add(d);
    }

    /** ボス勝利 */
    private Set<Date> bossWin = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * ボス勝利を取得します
     * @return ボス勝利
     */
    public Set<Date> getBossWin() {
        return this.bossWin;
    }

    /**
     * ボス勝利を設定します
     * @param bossWin ボス勝利
     */
    public void setBossWin(Set<Date> bossWin) {
        this.bossWin=bossWin;
    }

    /**
     * ボス勝利をカウントします
     */
    public void countBossWin(Date d) {
        if(this.bossWin.size()<50) this.bossWin.add(d);
    }

    /** 1-4ボスS */
    private Set<Date> boss1_4WinS = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 1-4ボスSを取得します
     * @return 1-4ボスS
     */
    public Set<Date> getBoss1_4WinS() {
        return this.boss1_4WinS;
    }

    /**
     * 1-4ボスSを設定します
     * @param boss1_4WinS 1-4ボスS
     */
    public void setBoss1_4WinS(Set<Date> boss1_4WinS) {
        this.boss1_4WinS=boss1_4WinS;
    }

    /**
     * 1-4ボスSをカウントします
     */
    public void countBoss1_4WinS(Date d) {
        if(this.boss1_4WinS.size()<50) this.boss1_4WinS.add(d);
    }

    /** 1-5ボスA */
    private Set<Date> boss1_5WinA = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 1-5ボスAを取得します
     * @return 1-5ボスA
     */
    public Set<Date> getBoss1_5WinA() {
        return this.boss1_5WinA;
    }

    /**
     * 1-5ボスAを設定します
     * @param boss1_5WinA 1-5ボスA
     */
    public void setBoss1_5WinA(Set<Date> boss1_5WinA) {
        this.boss1_5WinA=boss1_5WinA;
    }

    /**
     * 1-5ボスAをカウントします
     */
    public void countBoss1_5WinA(Date d) {
        if(this.boss1_5WinA.size()<50) this.boss1_5WinA.add(d);
    }

    /** 南西ボス */
    private Set<Date> boss2Win = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 南西ボスを取得します
     * @return 南西ボス
     */
    public Set<Date> getBoss2Win() {
        return this.boss2Win;
    }

    /**
     * 南西ボスを設定します
     * @param boss2Win 南西ボス
     */
    public void setBoss2Win(Set<Date> boss2Win) {
        this.boss2Win=boss2Win;
    }

    /**
     * 南西ボスをカウントします
     */
    public void countBoss2Win(Date d) {
        if(this.boss2Win.size()<50) this.boss2Win.add(d);
    }

    /** 3-3+ボス */
    private Set<Date> boss3_3pWin = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 3-3+ボスを取得します
     * @return 3-3+ボス
     */
    public Set<Date> getBoss3_3pWin() {
        return this.boss3_3pWin;
    }

    /**
     * 3-3+ボスを設定します
     * @param boss3_3pWin 3-3+ボス
     */
    public void setBoss3_3pWin(Set<Date> boss3_3pWin) {
        this.boss3_3pWin=boss3_3pWin;
    }

    /**
     * 3-3+ボスをカウントします
     */
    public void countBoss3_3pWin(Date d) {
        if(this.boss3_3pWin.size()<50) this.boss3_3pWin.add(d);
    }

    /** 西方ボス */
    private Set<Date> boss4Win = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 西方ボスを取得します
     * @return 西方ボス
     */
    public Set<Date> getBoss4Win() {
        return this.boss4Win;
    }

    /**
     * 西方ボスを設定します
     * @param boss4Win 西方ボス
     */
    public void setBoss4Win(Set<Date> boss4Win) {
        this.boss4Win=boss4Win;
    }

    /**
     * 西方ボスをカウントします
     */
    public void countBoss4Win(Date d) {
        if(this.boss4Win.size()<50) this.boss4Win.add(d);
    }

    /** 4-4ボス */
    private Set<Date> boss4_4Win = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 4-4ボスを取得します
     * @return 4-4ボス
     */
    public Set<Date> getBoss4_4Win() {
        return this.boss4_4Win;
    }

    /**
     * 4-4ボスを設定します
     * @param boss4_4Win 4-4ボス
     */
    public void setBoss4_4Win(Set<Date> boss4_4Win) {
        this.boss4_4Win=boss4_4Win;
    }

    /**
     * 4-4ボスをカウントします
     */
    public void countBoss4_4Win(Date d) {
        if(this.boss4_4Win.size()<50) this.boss4_4Win.add(d);
    }

    /** 5-2ボスS */
    private Set<Date> boss5_2WinS = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 5-2ボスSを取得します
     * @return 5-2ボスS
     */
    public Set<Date> getBoss5_2WinS() {
        return this.boss5_2WinS;
    }

    /**
     * 5-2ボスSを設定します
     * @param boss5_2WinS 5-2ボスS
     */
    public void setBoss5_2WinS(Set<Date> boss5_2WinS) {
        this.boss5_2WinS=boss5_2WinS;
    }

    /**
     * 5-2ボスSをカウントします
     */
    public void countBoss5_2WinS(Date d) {
        if(this.boss5_2WinS.size()<50) this.boss5_2WinS.add(d);
    }

    /** 6-1ボスS */
    private Set<Date> boss6_1WinS = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 6-1ボスSを取得します
     * @return 6-1ボスS
     */
    public Set<Date> getBoss6_1WinS() {
        return this.boss6_1WinS;
    }

    /**
     * 6-1ボスSを設定します
     * @param boss6_1WinS 6-1ボスS
     */
    public void setBoss6_1WinS(Set<Date> boss6_1WinS) {
        this.boss6_1WinS=boss6_1WinS;
    }

    /**
     * 6-1ボスSをカウントします
     */
    public void countBoss6_1WinS(Date d) {
        if(this.boss6_1WinS.size()<50) this.boss6_1WinS.add(d);
    }

    /** 戦闘勝利 */
    private Set<Date> battleWin = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 戦闘勝利を取得します
     * @return 戦闘勝利
     */
    public Set<Date> getBattleWin() {
        return this.battleWin;
    }

    /**
     * 戦闘勝利を設定します
     * @param battleWin 戦闘勝利
     */
    public void setBattleWin(Set<Date> battleWin) {
        this.battleWin=battleWin;
    }

    /**
     * 戦闘勝利をカウントします
     */
    public void countBattleWin(Date d) {
        if(this.battleWin.size()<50) this.battleWin.add(d);
    }

    /** 戦闘Sランク */
    private Set<Date> battleWinS = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 戦闘Sランクを取得します
     * @return 戦闘Sランク
     */
    public Set<Date> getBattleWinS() {
        return this.battleWinS;
    }

    /**
     * 戦闘Sランクを設定します
     * @param battleWinS 戦闘Sランク
     */
    public void setBattleWinS(Set<Date> battleWinS) {
        this.battleWinS=battleWinS;
    }

    /**
     * 戦闘Sランクをカウントします
     */
    public void countBattleWinS(Date d) {
        if(this.battleWinS.size()<50) this.battleWinS.add(d);
    }

    /** 補給艦撃破 */
    private Set<Date> defeatAP = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 補給艦撃破を取得します
     * @return 補給艦撃破
     */
    public Set<Date> getDefeatAP() {
        return this.defeatAP;
    }

    /**
     * 補給艦撃破を設定します
     * @param defeatAP 補給艦撃破
     */
    public void setDefeatAP(Set<Date> defeatAP) {
        this.defeatAP=defeatAP;
    }

    /**
     * 補給艦撃破をカウントします
     */
    public void countDefeatAP(Date d) {
        if(this.defeatAP.size()<50) this.defeatAP.add(d);
    }

    /** 空母撃破 */
    private Set<Date> defeatCV = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 空母撃破を取得します
     * @return 空母撃破
     */
    public Set<Date> getDefeatCV() {
        return this.defeatCV;
    }

    /**
     * 空母撃破を設定します
     * @param defeatCV 空母撃破
     */
    public void setDefeatCV(Set<Date> defeatCV) {
        this.defeatCV=defeatCV;
    }

    /**
     * 空母撃破をカウントします
     */
    public void countDefeatCV(Date d) {
        if(this.defeatCV.size()<50) this.defeatCV.add(d);
    }

    /** 潜水艦撃破 */
    private Set<Date> defeatSS = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 潜水艦撃破を取得します
     * @return 潜水艦撃破
     */
    public Set<Date> getDefeatSS() {
        return this.defeatSS;
    }

    /**
     * 潜水艦撃破を設定します
     * @param defeatSS 潜水艦撃破
     */
    public void setDefeatSS(Set<Date> defeatSS) {
        this.defeatSS=defeatSS;
    }

    /**
     * 潜水艦撃破をカウントします
     */
    public void countDefeatSS(Date d) {
        if(this.defeatSS.size()<50) this.defeatSS.add(d);
    }

    /** 演習 */
    private Set<Date> practice = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 演習を取得します
     * @return 演習
     */
    public Set<Date> getPractice() {
        return this.practice;
    }

    /**
     * 演習を設定します
     * @param practice 演習
     */
    public void setPractice(Set<Date> practice) {
        this.practice=practice;
    }

    /**
     * 演習をカウントします
     */
    public void countPractice(Date d) {
        if(this.practice.size()<50) this.practice.add(d);
    }

    /** 演習勝利 */
    private Set<Date> practiceWin = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 演習勝利を取得します
     * @return 演習勝利
     */
    public Set<Date> getPracticeWin() {
        return this.practiceWin;
    }

    /**
     * 演習勝利を設定します
     * @param practiceWin 演習勝利
     */
    public void setPracticeWin(Set<Date> practiceWin) {
        this.practiceWin=practiceWin;
    }

    /**
     * 演習勝利をカウントします
     */
    public void countPracticeWin(Date d) {
        if(this.practiceWin.size()<50) this.practiceWin.add(d);
    }

    /** 遠征成功 */
    private Set<Date> missionSuccess = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 遠征成功を取得します
     * @return 遠征成功
     */
    public Set<Date> getMissionSuccess() {
        return this.missionSuccess;
    }

    /**
     * 遠征成功を設定します
     * @param missionSuccess 遠征成功
     */
    public void setMissionSuccess(Set<Date> missionSuccess) {
        this.missionSuccess=missionSuccess;
    }

    /**
     * 遠征成功をカウントします
     */
    public void countMissionSuccess(Date d) {
        if(this.missionSuccess.size()<50) this.missionSuccess.add(d);
    }

    /** 建造 */
    private Set<Date> createShip = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 建造を取得します
     * @return 建造
     */
    public Set<Date> getCreateShip() {
        return this.createShip;
    }

    /**
     * 建造を設定します
     * @param createShip 建造
     */
    public void setCreateShip(Set<Date> createShip) {
        this.createShip=createShip;
    }

    /**
     * 建造をカウントします
     */
    public void countCreateShip(Date d) {
        if(this.createShip.size()<50) this.createShip.add(d);
    }

    /** 開発 */
    private Set<Date> createItem = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 開発を取得します
     * @return 開発
     */
    public Set<Date> getCreateItem() {
        return this.createItem;
    }

    /**
     * 開発を設定します
     * @param createItem 開発
     */
    public void setCreateItem(Set<Date> createItem) {
        this.createItem=createItem;
    }

    /**
     * 開発をカウントします
     */
    public void countCreateItem(Date d) {
        if(this.createItem.size()<50) this.createItem.add(d);
    }

    /** 解体 */
    private Set<Date> destroyShip = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 解体を取得します
     * @return 解体
     */
    public Set<Date> getDestroyShip() {
        return this.destroyShip;
    }

    /**
     * 解体を設定します
     * @param destroyShip 解体
     */
    public void setDestroyShip(Set<Date> destroyShip) {
        this.destroyShip=destroyShip;
    }

    /**
     * 解体をカウントします
     */
    public void countDestroyShip(Date d) {
        if(this.destroyShip.size()<50) this.destroyShip.add(d);
    }

    /** 廃棄 */
    private Set<Date> destroyItem = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 廃棄を取得します
     * @return 廃棄
     */
    public Set<Date> getDestroyItem() {
        return this.destroyItem;
    }

    /**
     * 廃棄を設定します
     * @param destroyItem 廃棄
     */
    public void setDestroyItem(Set<Date> destroyItem) {
        this.destroyItem=destroyItem;
    }

    /**
     * 廃棄をカウントします
     */
    public void countDestroyItem(Date d) {
        if(this.destroyItem.size()<50) this.destroyItem.add(d);
    }

    /** 補給 */
    private Set<Date> charge = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 補給を取得します
     * @return 補給
     */
    public Set<Date> getCharge() {
        return this.charge;
    }

    /**
     * 補給を設定します
     * @param charge 補給
     */
    public void setCharge(Set<Date> charge) {
        this.charge=charge;
    }

    /**
     * 補給をカウントします
     */
    public void countCharge(Date d) {
        if(this.charge.size()<50) this.charge.add(d);
    }

    /** 入渠 */
    private Set<Date> repair = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 入渠を取得します
     * @return 入渠
     */
    public Set<Date> getRepair() {
        return this.repair;
    }

    /**
     * 入渠を設定します
     * @param repair 入渠
     */
    public void setRepair(Set<Date> repair) {
        this.repair=repair;
    }

    /**
     * 入渠をカウントします
     */
    public void countRepair(Date d) {
        if(this.repair.size()<50) this.repair.add(d);
    }

    /** 近代化改修 */
    private Set<Date> powerUp = Collections.synchronizedSet(new HashSet<Date>());

    /**
     * 近代化改修を取得します
     * @return 近代化改修
     */
    public Set<Date> getPowerUp() {
        return this.powerUp;
    }

    /**
     * 近代化改修を設定します
     * @param powerUp 近代化改修
     */
    public void setPowerUp(Set<Date> powerUp) {
        this.powerUp=powerUp;
    }

    /**
     * 近代化改修をカウントします
     */
    public void countPowerUp(Date d) {
        if(this.powerUp.size()<50) this.powerUp.add(d);
    }

    public void marge(QuestBean o) {
        if (o == null) return;
        for(Field f : QuestBean.class.getDeclaredFields()) {
            try {
                if (!Set.class.isAssignableFrom(f.getType())) continue;
                f.setAccessible(true);
                Set<Date> s = (Set<Date>)f.get(this);
                s.addAll((Set<Date>)f.get(o));
            } catch(Exception e) {
            }
        }
    }
}
