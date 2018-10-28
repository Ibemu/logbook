/**
 *
 */
package logbook.internal;

import java.util.function.Predicate;

import logbook.dto.ShipDto;

/**
 * 対空CI種別
 *
 */
public enum AntiAirCutInKind {
    AKIZUKI_HHR(1, "秋月型／高角砲・高角砲・電探", "秋7", 7, 1.7,
            s -> (s.getShipInfo().getClassType() == 54) // 秋月型
                    && (s.getItem().stream().filter(i -> (i != null) && (i.getType3() == 16)).count() >= 2) // 高角砲x2
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 11)) // 電探
    ),

    AKIZUKI_HR(2, "秋月型／高角砲・電探", "秋6", 6, 1.7,
            s -> (s.getShipInfo().getClassType() == 54) // 秋月型
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 16)) // 高角砲
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 11)) // 電探
    ),

    AKIZUKI_HH(3, "秋月型／高角砲・高角砲", "秋4", 4, 1.6,
            s -> (s.getShipInfo().getClassType() == 54) // 秋月型
                    && (s.getItem().stream().filter(i -> (i != null) && (i.getType3() == 16)).count() >= 2) // 高角砲x2
    ),

    BATTLESHIP_MSAR(4, "戦艦／大口径主砲・三式弾・高射装置・対空電探", "戦6", 6, 1.5,
            s -> (s.getShipInfo().getType().contains("戦艦")) // 戦艦, 航空戦艦
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 3)) // 大口径主砲
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 12)) // 三式弾
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 30)) // 高射装置
                    && s.getItem().stream().anyMatch(i -> (i != null) && i.isAntiAirRadar()) // 対空電探
    ),

    ALL_BBR(5, "水上艦／高性能高角砲・高性能高角砲・対空電探", "砲砲電", 4, 1.5,
            s -> (s.getItem().stream().filter(i -> (i != null) && i.isBuiltInHAMount()).count() >= 2) // 高性能高角砲x2
                    && s.getItem().stream().anyMatch(i -> (i != null) && i.isAntiAirRadar()) // 対空電探
    ),

    BATTLESHIP_MSA(6, "戦艦／大口径主砲・三式弾・高射装置", "戦4", 4, 1.45,
            s -> (s.getShipInfo().getType().contains("戦艦")) // 戦艦, 航空戦艦
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 3)) // 大口径主砲
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 12)) // 三式弾
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 30)) // 高射装置
    ),

    ALL_HAR(7, "水上艦／高角砲・高射装置・対空電探", "砲装電", 3, 1.35,
            s -> s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 16)) // 高角砲
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 30)) // 高射装置
                    && s.getItem().stream().anyMatch(i -> (i != null) && i.isAntiAirRadar()) // 対空電探
    ),

    ALL_BR(8, "水上艦／高性能高角砲・対空電探", "砲電", 4, 1.4,
            s -> s.getItem().stream().anyMatch(i -> (i != null) && i.isBuiltInHAMount()) // 高性能高角砲
                    && s.getItem().stream().anyMatch(i -> (i != null) && i.isAntiAirRadar()) // 対空電探
    ),

    ALL_HA(9, "水上艦／高角砲・高射装置", "砲装", 4, 1.4,
            s -> s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 16)) // 高角砲
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 30)) // 高射装置
    ),

    MAYA_HCR(10, "摩耶改二／高角砲・高性能機銃・対空電探", "摩8", 8, 1.65,
            s -> (s.getShipInfo().getShipId() == 428) // 摩耶改二
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 16)) // 高角砲
                    && s.getItem().stream().anyMatch(i -> (i != null) && i.isCDMG()) // 高性能機銃
                    && s.getItem().stream().anyMatch(i -> (i != null) && i.isAntiAirRadar()) // 対空電探
    ),

    MAYA_HC(11, "摩耶改二／高角砲・高性能機銃", "摩6", 6, 1.5,
            s -> (s.getShipInfo().getShipId() == 428) // 摩耶改二
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 16)) // 高角砲
                    && s.getItem().stream().anyMatch(i -> (i != null) && i.isCDMG()) // 高性能機銃
    ),

    ALL_CGR(12, "水上艦／高性能機銃・機銃・対空電探", "銃銃電", 3, 1.25,
            s -> s.getItem().stream().anyMatch(i -> (i != null) && i.isCDMG()) // 高性能機銃
                    && (s.getItem().stream().filter(i -> (i != null) && (i.getType3() == 15)).count() >= 2) // 機銃x2
                    && s.getItem().stream().anyMatch(i -> (i != null) && i.isAntiAirRadar()) // 対空電探
    ),

    NONE_13(13, "なし", "なし", 0, 0, s -> false),

    ISUZU_HGR(14, "五十鈴改二／高角砲・機銃・対空電探", "五4", 4, 1.45,
            s -> (s.getShipInfo().getShipId() == 141) // 五十鈴改二
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 16)) // 高角砲
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 15)) // 機銃
                    && s.getItem().stream().anyMatch(i -> (i != null) && i.isAntiAirRadar()) // 対空電探
    ),

    ISUZU_HG(15, "五十鈴改二／高角砲・機銃", "五3", 3, 1.3,
            s -> (s.getShipInfo().getShipId() == 141) // 五十鈴改二
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 16)) // 高角砲
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 15)) // 機銃
    ),

    KASUMI_HGR(16, "霞改二乙／高角砲・機銃・対空電探", "霞4", 4, 1.4,
            s -> (s.getShipInfo().getShipId() == 470) // 霞改二乙
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 16)) // 高角砲
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 15)) // 機銃
                    && s.getItem().stream().anyMatch(i -> (i != null) && i.isAntiAirRadar()) // 対空電探
    ),

    KASUMI_HG(17, "霞改二乙／高角砲・機銃", "霞2", 2, 1.25,
            s -> (s.getShipInfo().getShipId() == 470) // 霞改二乙
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 16)) // 高角砲
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 15)) // 機銃
    ),

    SATSUKI_C(18, "皐月改二／高性能機銃", "皐2", 2, 1.2,
            s -> (s.getShipInfo().getShipId() == 428) // 皐月改二
                    && s.getItem().stream().anyMatch(i -> (i != null) && i.isCDMG()) // 高性能機銃
    ),

    KINU_HC(19, "鬼怒改二／高角砲・高性能機銃", "鬼5", 5, 1.45,
            s -> (s.getShipInfo().getShipId() == 487) // 鬼怒改二
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 16) && !i.isBuiltInHAMount()) // 高性能高角砲以外の高角砲
                    && s.getItem().stream().anyMatch(i -> (i != null) && i.isCDMG()) // 高性能機銃
    ),

    KINU_C(20, "鬼怒改二／高性能機銃", "鬼3", 3, 1.25,
            s -> (s.getShipInfo().getShipId() == 487) // 鬼怒改二
                    && s.getItem().stream().anyMatch(i -> (i != null) && i.isCDMG()) // 高性能機銃
    ),

    YURA_HR(21, "由良改二／高角砲・対空電探", "由5", 5, 1.45,
            s -> (s.getShipInfo().getShipId() == 488) // 由良改二
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 16)) // 高角砲
                    && s.getItem().stream().anyMatch(i -> (i != null) && i.isAntiAirRadar()) // 対空電探
    ),

    FUMIZUKI_C(22, "文月改二／高性能機銃", "文2", 2, 1.2,
            s -> (s.getShipInfo().getShipId() == 548) // 文月改二
                    && s.getItem().stream().anyMatch(i -> (i != null) && i.isCDMG()) // 高性能機銃
    ),

    UIT25_G(23, "UIT-25・伊504／機銃", "U1", 1, 1.05,
            s -> ((s.getShipInfo().getShipId() == 539) || (s.getShipInfo().getShipId() == 530)) // UIT-25 || 伊504
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 15) && !i.isCDMG()) // 高性能機銃以外の機銃
    ),

    TATSUTA_HG(24, "龍田改二／高角砲・機銃", "龍3", 3, 1.25,
            s -> (s.getShipInfo().getShipId() == 478) // 龍田改二
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 16)) // 高角砲
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 15) && !i.isCDMG()) // 高性能機銃以外の機銃
    ),

    ISE_GRS(25, "伊勢型改／12cm30連装噴進砲改二・対空電探・三式弾", "伊7", 7, 1.55,
            s -> ((s.getShipInfo().getShipId() == 82) || (s.getShipInfo().getShipId() == 88)) // 伊勢改 || 日向改
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getId() == 274)) // 12cm30連装噴進砲改二
                    && s.getItem().stream().anyMatch(i -> (i != null) && i.isAntiAirRadar()) // 対空電探
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 12)) // 三式弾
    ),

    MUSASHI_HR(26, "武蔵改二／10cm連装高角砲改＋増設機銃・対空電探", "武6", 6, 1.4,
            s -> (s.getShipInfo().getShipId() == 546) // 武蔵改二
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getId() == 275)) // 10cm連装高角砲改＋増設機銃
                    && s.getItem().stream().anyMatch(i -> (i != null) && i.isAntiAirRadar()) // 対空電探
    ),

    NONE_27(27, "なし", "なし", 0, 0, s -> false),

    ISE_GR(28, "伊勢型改・武蔵改／12cm30連装噴進砲改二・対空電探", "伊4", 4, 1.4,
            s -> ((s.getShipInfo().getShipId() == 82) || (s.getShipInfo().getShipId() == 88) // 伊勢改 || 日向改
                    || (s.getShipInfo().getShipId() == 148) || (s.getShipInfo().getShipId() == 546)) // 武蔵改 || 武蔵改二
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getId() == 274)) // 12cm30連装噴進砲改二
                    && s.getItem().stream().anyMatch(i -> (i != null) && i.isAntiAirRadar()) // 対空電探
    ),

    ISOKAZE_HR(29, "磯風乙改・浜風乙改／高角砲・対空電探", "磯5", 5, 1.55,
            s -> ((s.getShipInfo().getShipId() == 557) || (s.getShipInfo().getShipId() == 558)) // 磯風乙改 || 浜風乙改
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 16)) // 高角砲
                    && s.getItem().stream().anyMatch(i -> (i != null) && i.isAntiAirRadar()) // 対空電探
    ),

    TENRYU_HHH(30, "天龍改二／高角砲・高角砲・高角砲", "天3", 3, 1.3,
            s -> (s.getShipInfo().getShipId() == 477) // 天龍改二
                    && (s.getItem().stream().filter(i -> (i != null) && (i.getType3() == 16)).count() >= 3) // 高角砲x3
    ),

    TENRYU_HH(31, "天龍改二／高角砲・高角砲", "天2", 2, 1.2,
            s -> (s.getShipInfo().getShipId() == 477) // 天龍改二
                    && (s.getItem().stream().filter(i -> (i != null) && (i.getType3() == 16)).count() >= 2) // 高角砲x2
    ),

    HMS_CG_CM(32, "HMS・金剛型改二／QF 2ポンド8連装ポンポン砲・（16inch Mk.I三連装砲改+FCR type284｜20連装7inch UP Rocket Launchers）", "H3", 3, 1.2,
            s -> ((s.getShipInfo().getClassType() == 67) || (s.getShipInfo().getClassType() == 78) // Queen Elizabeth級 || Ark Royal級
                    || (s.getShipInfo().getClassType() == 82) || (s.getShipInfo().getClassType() == 88) // J級 || Nelson級
                    || (s.getShipInfo().getShipId() == 149) || (s.getShipInfo().getShipId() == 150) // 金剛改二 || 比叡改二
                    || (s.getShipInfo().getShipId() == 151) || (s.getShipInfo().getShipId() == 152)) // 榛名改二 || 霧島改二
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getId() == 191)) // QF 2ポンド8連装ポンポン砲
                    && (s.getItem().stream().anyMatch(i -> (i != null) && (i.getId() == 300)) // 16inch Mk.I三連装砲改+FCR type284
                        || s.getItem().stream().anyMatch(i -> (i != null) && (i.getId() == 301))) // 20連装7inch UP Rocket Launchers
    ),

    GOTLAND_HG(33, "Gotland改／高角砲・機銃", "G3", 3, 1.35,
            s -> (s.getShipInfo().getShipId() == 579) // Gotland改
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 16)) // 高角砲
                    && s.getItem().stream().anyMatch(i -> (i != null) && (i.getType3() == 15)) // 機銃
    ),

    ;

    private int kind;
    private String name;
    private String shortName;
    private int basicBonus;
    private double additionalBonusRatio;
    private Predicate<ShipDto> condition;

    AntiAirCutInKind(int kind, String name, String shortName, int basic, double ratio, Predicate<ShipDto> condition) {
        this.kind = kind;
        this.name = name;
        this.shortName = shortName;
        this.basicBonus = basic;
        this.additionalBonusRatio = ratio;
        this.condition = condition;
    }

    /**
     * @return kind
     */
    public int getKind() {
        return this.kind;
    }

    /**
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return shortName
     */
    public String getShortName() {
        return this.shortName;
    }

    /**
     * @return basicBonus
     */
    public int getBasicBonus() {
        return this.basicBonus;
    }

    /**
     * @return additionalBonusRatio
     */
    public double getAdditionalBonusRatio() {
        return this.additionalBonusRatio;
    }

    /**
     * @return condition
     */
    public Predicate<ShipDto> getCondition() {
        return this.condition;
    }
}
