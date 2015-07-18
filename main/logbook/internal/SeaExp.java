package logbook.internal;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import logbook.util.builder.Builders;

/**
 * 海域Exp
 *
 */
public class SeaExp {

    /**
     * 海域Expプリセット値
     */
    private static final Map<String, Integer> SEA_EXP = Builders.newMapBuilder(LinkedHashMap<String, Integer>::new)
            .put("1-1", 30)
            .put("1-2", 50)
            .put("1-3", 80)
            .put("1-4", 100)
            .put("1-5", 150)
            .put("2-1", 120)
            .put("2-2", 150)
            .put("2-3", 200)
            .put("2-4", 300)
            .put("2-5", 250)
            .put("3-1", 310)
            .put("3-2", 320)
            .put("3-3", 330)
            .put("3-4", 350)
            .put("3-5", 400)
            .put("4-1", 310)
            .put("4-2", 320)
            .put("4-3", 330)
            .put("4-4", 340)
            .put("5-1", 360)
            .put("5-2", 380)
            .put("5-3", 400)
            .put("5-4", 420)
            .put("5-5", 450)
            .put("6-1", 380)
            .put("6-2", 420)
            .build();

    /**
     * 海域Expを取得します
     *
     * @return
     */
    public static Map<String, Integer> get() {
        return Collections.unmodifiableMap(SEA_EXP);
    }
}
