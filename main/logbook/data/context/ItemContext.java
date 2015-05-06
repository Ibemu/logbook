package logbook.data.context;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import logbook.dto.ItemDto;

/**
 * 装備を管理します
 */
public class ItemContext {

    /** 装備Map */
    private static Map<Long, ItemDto> itemMap = new ConcurrentSkipListMap<Long, ItemDto>();

    /** 改修レベルMap */
    private static Map<Long, Integer> levelMap = new ConcurrentSkipListMap<Long, Integer>();

    /**
     * @return 装備Map
     */
    public static Map<Long, ItemDto> get() {
        return itemMap;
    }

    /**
     * @return 改修レベルMap
     */
    public static Map<Long, Integer> level() {
        return levelMap;
    }
}
