package logbook.internal;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import logbook.util.builder.Builders;

/**
 * 戦闘での評価
 *
 */
public class EvaluateExp {

    /**
     * 戦闘での評価プリセット値
     */
    private static final Map<String, Double> EVAL_EXP = Builders.newMapBuilder(LinkedHashMap<String, Double>::new)
            .put("S完全勝利", 1.2d)
            .put("S勝利", 1.2d)
            .put("A勝利", 1.0d)
            .put("B戦術的勝利", 1.0d)
            .put("C戦術的敗北", 0.8d)
            .put("D敗北", 0.7d)
            .build();

    /**
     * 戦闘での評価を取得します
     * @return 戦闘での評価
     */
    public static Map<String, Double> get() {
        return Collections.unmodifiableMap(EVAL_EXP);
    }
}
