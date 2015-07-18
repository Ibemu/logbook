package logbook.internal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import logbook.util.builder.Builders;

/**
 * アイテム
 *
 */
public class UseItem {

    /**
     * アイテムプリセット値
     */
    private static final Map<Integer, String> USE_ITEM = Builders
            .newMapBuilder(ConcurrentHashMap<Integer, String>::new)
            .put(10, "家具箱（小）")
            .put(11, "家具箱（中）")
            .put(12, "家具箱（大）")
            .put(50, "応急修理要員")
            .put(51, "応急修理女神")
            .put(54, "給糧艦「間宮」")
            .put(56, "艦娘からのチョコ")
            .put(57, "勲章")
            .put(59, "給糧艦「伊良湖」")
            .put(62, "菱餅")
            .build();

    /**
     * アイテムを取得します
     *
     * @param type ID
     * @return アイテム
     */
    public static String get(int type) {
        return USE_ITEM.get(type);
    }
}
