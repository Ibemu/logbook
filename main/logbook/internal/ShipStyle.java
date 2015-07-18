package logbook.internal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import logbook.util.builder.Builders;

/**
 * 艦種
 *
 */
public class ShipStyle {

    /**
     * 艦種プリセット値
     */
    private static final Map<String, String> SHIPSTYLE = Builders.newMapBuilder(ConcurrentHashMap<String, String>::new)
            .put("1", "海防艦")
            .put("2", "駆逐艦")
            .put("3", "軽巡洋艦")
            .put("4", "重雷装巡洋艦")
            .put("5", "重巡洋艦")
            .put("6", "航空巡洋艦")
            .put("7", "軽空母")
            .put("8", "戦艦")
            .put("9", "戦艦")
            .put("10", "航空戦艦")
            .put("11", "正規空母")
            .put("12", "超弩級戦艦")
            .put("13", "潜水艦")
            .put("14", "潜水空母")
            .put("15", "補給艦")
            .put("16", "水上機母艦")
            .put("17", "揚陸艦")
            .put("18", "装甲空母")
            .put("19", "工作艦")
            .put("20", "潜水母艦")
            .put("21", "練習巡洋艦")
            .build();

    /**
     * 艦種を取得します
     *
     * @param id
     * @return 艦種
     */
    public static String get(String id) {
        return SHIPSTYLE.get(id);
    }
}
