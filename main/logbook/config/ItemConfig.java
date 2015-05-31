package logbook.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import logbook.constants.AppConstants;
import logbook.data.context.ItemContext;
import logbook.dto.ItemDto;
import logbook.internal.Item;
import logbook.util.BeanUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 装備のIDと装備IDの紐付けを保存・復元します
 *
 */
public class ItemConfig {

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(ItemConfig.class);
    }

    /**
     * 設定ファイルに書き込みます
     */
    public static void store() throws IOException {
        storeItem();
        storeLevel();
    }

    /**
     * 装備の復元
     *
     * @param properties
     * @return
     */
    public static void load() {
        loadItem();
        loadLevel();
    }

    /**
     * @throws IOException
     */
    private static void storeItem() throws IOException {
        Map<Long, Integer> map = new HashMap<Long, Integer>();
        for (Entry<Long, ItemDto> entry : ItemContext.get().entrySet()) {
            map.put(entry.getKey(), entry.getValue().getId());
        }
        BeanUtils.writeObject(AppConstants.ITEM_CONFIG_FILE, map);
    }

    /**
     * @throws IOException
     */
    private static void storeLevel() throws IOException {
        Map<Long, Integer> map = new HashMap<Long, Integer>();
        map.putAll(ItemContext.level());
        BeanUtils.writeObject(AppConstants.ITEM_LEVEL_CONFIG_FILE, map);
    }

    /**
     * 装備の復元
     */
    private static void loadItem() {
        try {
            Map<Long, Integer> map = BeanUtils.readObject(AppConstants.ITEM_CONFIG_FILE, Map.class);
            if (map != null) {
                for (Entry<Long, Integer> entry : map.entrySet()) {
                    Integer id = entry.getValue();
                    ItemDto item = Item.get(id);
                    if (item != null) {
                        ItemContext.get().put(entry.getKey(), item);
                    }
                }
            }
        } catch (Exception e) {
            LoggerHolder.LOG.warn("装備の復元に失敗しました", e);
        }
    }

    /**
     * レベルの復元
     */
    private static void loadLevel() {
        try {
            Map<Long, Integer> map = BeanUtils.readObject(AppConstants.ITEM_LEVEL_CONFIG_FILE, Map.class);
            if (map != null) {
                ItemContext.level().putAll(map);
            }
        } catch (Exception e) {
            LoggerHolder.LOG.warn("レベルの復元に失敗しました", e);
        }
    }
}
