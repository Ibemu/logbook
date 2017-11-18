package logbook.internal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import logbook.util.builder.Builders;

/**
 * 遠征
 *
 */
public final class Deck {

    /**
     * 遠征プリセット値
     */
    private static final Map<Integer, String> DECK = Builders.newMapBuilder(ConcurrentHashMap<Integer, String>::new)
            .put(1, "練習航海")
            .put(2, "長距離練習航海")
            .put(3, "警備任務")
            .put(4, "対潜警戒任務")
            .put(5, "海上護衛任務")
            .put(6, "防空射撃演習")
            .put(7, "観艦式予行")
            .put(8, "観艦式")
            .put(9, "タンカー護衛任務")
            .put(10, "強行偵察任務")
            .put(11, "ボーキサイト輸送任務")
            .put(12, "資源輸送任務")
            .put(13, "鼠輸送作戦")
            .put(14, "包囲陸戦隊撤収作戦")
            .put(15, "囮機動部隊支援作戦")
            .put(16, "艦隊決戦援護作戦")
            .put(17, "敵地偵察作戦")
            .put(18, "航空機輸送作戦")
            .put(19, "北号作戦")
            .put(20, "潜水艦哨戒任務")
            .put(21, "北方鼠輸送作戦")
            .put(22, "艦隊演習")
            .put(23, "航空戦艦運用演習")
            .put(24, "北方航路海上護衛")
            .put(25, "通商破壊作戦")
            .put(26, "敵母港空襲作戦")
            .put(27, "潜水艦通商破壊作戦")
            .put(28, "西方海域封鎖作戦")
            .put(29, "潜水艦派遣演習")
            .put(30, "潜水艦派遣作戦")
            .put(31, "海外艦との接触")
            .put(32, "遠洋練習航海")
            .put(33, "前衛支援任務")
            .put(34, "艦隊決戦支援任務")
            .put(35, "ＭＯ作戦")
            .put(36, "水上機基地建設")
            .put(37, "東京急行")
            .put(38, "東京急行(弐)")
            .put(39, "遠洋潜水艦作戦")
            .put(40, "水上機前線輸送")
            .put(100, "兵站強化任務")
            .put(101, "海峡警備行動")
            .put(102, "長時間対潜警戒")
            .put(110, "南西方面航空偵察作戦")
            .build();

    /**
     * 遠征を取得します
     *
     * @param id ID
     * @return 遠征
     */
    public static String get(int id) {
        return DECK.get(id);
    }
}
