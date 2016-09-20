package logbook.internal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 出撃海域
 *
 */
public class SallyArea {

    public static final SallyArea NOTHING = new SallyArea(0, "");
    public static final SallyArea UNKNOWN = new SallyArea(-1, "不明海域");

    private static final Map<Integer, SallyArea> AREAS = new ConcurrentHashMap<Integer, SallyArea>();

    static {
        AREAS.put(-1, UNKNOWN);
        AREAS.put(0, NOTHING);
    }

    private final int val;

    private final String name;

    /**
     * 出撃海域
     */
    private SallyArea(int val, String name) {
        this.val = val;
        this.name = name;
    }

    private SallyArea(int val) {
        this.val = val;
        this.name = "第" + val + "番札";
    }

    public int getValue() {
        return this.val;
    }

    public String getName() {
        return this.name;
    }

    public static SallyArea valueOf(int val) {
        if (val < 0)
            return UNKNOWN;
        if (!AREAS.containsKey(val))
            AREAS.put(val, new SallyArea(val));
        return AREAS.get(val);

    }

    public static SallyArea[] values() {
        return AREAS.values().toArray(new SallyArea[0]);
    }
}
