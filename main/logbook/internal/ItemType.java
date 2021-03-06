package logbook.internal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import logbook.util.builder.Builders;

/**
 * アイテム種別
 *
 */
public class ItemType {

    /**
     * アイテム種別プリセット値
     */
    private static final Map<Integer, String> ITEMTYPE = Builders
            .newMapBuilder(ConcurrentHashMap<Integer, String>::new)
            .put(1, "小口径主砲")
            .put(2, "中口径主砲")
            .put(3, "大口径主砲")
            .put(4, "副砲")
            .put(5, "魚雷")
            .put(6, "艦上戦闘機")
            .put(7, "艦上爆撃機")
            .put(8, "艦上攻撃機")
            .put(9, "艦上偵察機")
            .put(10, "水上偵察機")
            .put(11, "電波探信儀")
            .put(12, "対空強化弾")
            .put(13, "徹甲弾")
            .put(14, "ダメコン")
            .put(15, "機銃")
            .put(16, "高角砲")
            .put(17, "爆雷投射機")
            .put(18, "ソナー")
            .put(19, "機関部強化")
            .put(20, "上陸用舟艇")
            .put(21, "オートジャイロ")
            .put(22, "対潜哨戒機")
            .put(23, "追加装甲")
            .put(24, "探照灯")
            .put(25, "簡易輸送部材")
            .put(26, "艦艇修理施設")
            .put(27, "照明弾")
            .put(28, "司令部施設")
            .put(29, "航空要員")
            .put(30, "高射装置")
            .put(31, "対地装備")
            .put(32, "航空要員")
            .put(33, "大型飛行艇")
            .put(34, "戦闘糧食")
            .put(35, "補給物資")
            .put(36, "特型内火艇")
            .put(37, "陸上攻撃機")
            .put(38, "局地戦闘機")
            .put(39, "噴式戦闘爆撃機(噴式景雲改)")
            .put(40, "噴式戦闘爆撃機(橘花改)")
            .put(41, "輸送機材")
            .put(42, "潜水艦装備")
            .put(43, "水上戦闘機")
            .put(44, "陸軍戦闘機")
            .put(45, "夜間戦闘機")
            .put(46, "夜間攻撃機")
            .put(47, "陸上攻撃機(対潜哨戒機)")
            .build();

    /**
     * アイテム種別を取得します
     *
     * @param type ID
     * @return アイテム種別
     */
    public static String get(Integer type) {
        return ITEMTYPE.get(type);
    }
}
