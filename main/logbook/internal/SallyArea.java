package logbook.internal;

/**
 * 出撃海域
 *
 */
public enum SallyArea {

    NOTHING(0, ""),

    AREA1(1, "A.初動作戦"),

    AREA2(2, "B.機動部隊"),

    AREA3(3, "C.挺身部隊"),

    AREA4(4, "D.西部方面"),

    UNKNOWN(-1, "不明海域");

    private final int val;

    private final String name;

    /**
     * 出撃海域
     */
    private SallyArea(int val, String name) {
        this.val = val;
        this.name = name;
    }

    public int getValue() {
        return this.val;
    }

    public String getName() {
        return this.name;
    }

    public static SallyArea valueOf(int val) {
        SallyArea area;
        switch (val) {
        case 0:
            area = NOTHING;
            break;
        case 1:
            area = AREA1;
            break;
        case 2:
            area = AREA2;
            break;
        case 3:
            area = AREA3;
            break;
        case 4:
            area = AREA4;
            break;
        default:
            area = UNKNOWN;
            break;
        }
        return area;
    }

}
