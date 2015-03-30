package logbook.config;

import java.io.File;
import java.lang.Exception;
import java.util.Map;
import java.util.Set;

import javax.annotation.CheckForNull;

import logbook.config.bean.QuestBean;
import logbook.config.bean.QuestMapBean;
import logbook.constants.AppConstants;
import logbook.data.context.GlobalContext;
import logbook.dto.ResourceDto;
import logbook.dto.ShipDto;
import logbook.util.BeanUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 任務の進行状況を保存・復元します
 *
 */
public class QuestConfig {

    /** ロガー */
    private static final Logger LOG = LogManager.getLogger(QuestConfig.class);
    
    /** 任務のBean */
    private static QuestMapBean mapBean;
    
    /** 最後の更新 */
    private static long last;

    /**
     * 任務の進行状況を保存します
     */
    public static void store() {
        load();
        if (mapBean != null) {
            try {
                BeanUtils.writeObject(AppConstants.QUEST_CONFIG_FILE, mapBean);
                last = AppConstants.QUEST_CONFIG_FILE.lastModified();
            } catch (Exception e) {
                LOG.warn("任務の進行状況を保存しますに失敗しました", e);
            }
        }
    }

    /**
     * 任務の進行状況を保存します
     * 
     * @param no 任務No
     * @param quest 任務
     */
    public static void put(int no, QuestBean quest) {
        load();
        if (mapBean == null) {
            mapBean = new QuestMapBean();
            last = -1;
        }
        
        mapBean.getQuestMap().put(no, quest);
        
        store();
    }

    /**
     * 任務の進行状況を復元します
     */
    public static void load() {
        try {
            if (mapBean == null) {
                mapBean = BeanUtils.readObject(AppConstants.QUEST_CONFIG_FILE, QuestMapBean.class);
                last = AppConstants.QUEST_CONFIG_FILE.lastModified();
            } else if(last != AppConstants.QUEST_CONFIG_FILE.lastModified()) {
                mapBean.marge(BeanUtils.readObject(AppConstants.QUEST_CONFIG_FILE, QuestMapBean.class));
                last = AppConstants.QUEST_CONFIG_FILE.lastModified();
            }
        } catch (Exception e) {
            LOG.warn("任務の進行状況を復元しますに失敗しました", e);
        }
    }

    /**
     * 任務の進行状況を取得します
     * 
     * @param no 任務No
     * @return 任務の進行状況
     */
    @CheckForNull
    public static QuestBean get(int no) {
        load();
        if (mapBean != null) {
            return mapBean.getQuestMap().get(no);
        }
        return null;
    }

    /**
     * 任務の進行状況を削除します
     * 
     * @param no 任務No
     */
    public static void remove(int no) {
        load();
        if (mapBean != null) {
            mapBean.getQuestMap().remove(no);
            store();
        }
    }

    /**
     * 任務Noを一覧します
     * 
     * @return 任務No一覧
     */
    public static Set<Integer> getNoList() {
        load();
        if (mapBean != null) {
            return mapBean.getQuestMap().keySet();
        }
        return null;
    }
}
