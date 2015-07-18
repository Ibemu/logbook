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
            .put(41, "<UNKNOWN>")
            .put(42, "<UNKNOWN>")
            .put(43, "<UNKNOWN>")
            .put(44, "<UNKNOWN>")
            .put(45, "<UNKNOWN>")
            .put(46, "<UNKNOWN>")
            .put(47, "<UNKNOWN>")
            .put(48, "<UNKNOWN>")
            .put(49, "<UNKNOWN>")
            .put(50, "<UNKNOWN>")
            .put(51, "<UNKNOWN>")
            .put(52, "<UNKNOWN>")
            .put(53, "<UNKNOWN>")
            .put(54, "<UNKNOWN>")
            .put(55, "<UNKNOWN>")
            .put(56, "<UNKNOWN>")
            .put(57, "<UNKNOWN>")
            .put(58, "<UNKNOWN>")
            .put(59, "<UNKNOWN>")
            .put(60, "<UNKNOWN>")
            .put(61, "<UNKNOWN>")
            .put(62, "<UNKNOWN>")
            .put(63, "<UNKNOWN>")
            .put(64, "<UNKNOWN>")
            .put(65, "<UNKNOWN>")
            .put(66, "<UNKNOWN>")
            .put(67, "<UNKNOWN>")
            .put(68, "<UNKNOWN>")
            .put(69, "<UNKNOWN>")
            .put(70, "<UNKNOWN>")
            .put(71, "<UNKNOWN>")
            .put(72, "<UNKNOWN>")
            .put(73, "<UNKNOWN>")
            .put(74, "<UNKNOWN>")
            .put(75, "<UNKNOWN>")
            .put(76, "<UNKNOWN>")
            .put(77, "<UNKNOWN>")
            .put(78, "<UNKNOWN>")
            .put(79, "<UNKNOWN>")
            .put(80, "<UNKNOWN>")
            .put(81, "<UNKNOWN>")
            .put(82, "<UNKNOWN>")
            .put(83, "<UNKNOWN>")
            .put(84, "<UNKNOWN>")
            .put(85, "<UNKNOWN>")
            .put(86, "<UNKNOWN>")
            .put(87, "<UNKNOWN>")
            .put(88, "<UNKNOWN>")
            .put(89, "<UNKNOWN>")
            .put(90, "<UNKNOWN>")
            .put(91, "<UNKNOWN>")
            .put(92, "<UNKNOWN>")
            .put(93, "<UNKNOWN>")
            .put(94, "<UNKNOWN>")
            .put(95, "<UNKNOWN>")
            .put(96, "<UNKNOWN>")
            .put(97, "<UNKNOWN>")
            .put(98, "<UNKNOWN>")
            .put(99, "<UNKNOWN>")
            .put(100, "<UNKNOWN>")
            .put(101, "<UNKNOWN>")
            .put(102, "<UNKNOWN>")
            .put(103, "<UNKNOWN>")
            .put(104, "<UNKNOWN>")
            .put(105, "<UNKNOWN>")
            .put(106, "<UNKNOWN>")
            .put(107, "<UNKNOWN>")
            .put(108, "<UNKNOWN>")
            .put(109, "前衛支援任務")
            .put(110, "艦隊決戦支援任務")
            .put(141, "前衛支援任務")
            .put(142, "艦隊決戦支援任務")
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
