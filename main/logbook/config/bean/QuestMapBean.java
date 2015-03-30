package logbook.config.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任務の進行状況を保存します
 *
 */
public final class QuestMapBean {

    /** 任務Map */
    private Map<Integer, QuestBean> questMap = new ConcurrentHashMap<Integer, QuestBean>();

    /**
     * 任務Mapを取得します。
     * @return 任務Map
     */
    public Map<Integer, QuestBean> getQuestMap() {
        return this.questMap;
    }

    /**
     * 任務Mapを設定します。
     * @param questMap 任務Map
     */
    public void setQuestMap(Map<Integer, QuestBean> questMap) {
        this.questMap = questMap;
    }
    
    public void marge(QuestMapBean o) {
        if (o == null) return;
        for (Map.Entry<Integer, QuestBean> e : o.questMap.entrySet()) {
            if (questMap.containsKey(e.getKey())) {
                questMap.get(e.getKey()).marge(e.getValue());
            } else {
                questMap.put(e.getKey(), e.getValue());
            }
        }
    }
}
