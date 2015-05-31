package logbook.config;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

import logbook.constants.AppConstants;

import org.eclipse.swt.graphics.RGB;

/**
 * 資材チャートのスタイルシートを保存します
 *
 */
public class ChartStylesheet {

    public static void store() throws IOException {
        StringBuilder sb = new StringBuilder();
        create(0, AppConfig.get().getFuelColor(), sb);
        create(1, AppConfig.get().getAmmoColor(), sb);
        create(2, AppConfig.get().getMetalColor(), sb);
        create(3, AppConfig.get().getBauxiteColor(), sb);

        try (Writer writer = Files.newBufferedWriter(AppConstants.CHART_STYLESHEET_FILE.toPath())) {
            writer.write(sb.toString());
        }
    }

    private static void create(int i, RGB color, StringBuilder sb) {
        String rgb = "rgb(" + color.red + "," + color.green + "," + color.blue + ")";

        sb.append(".default-color").append(i).append(".chart-line-symbol {").append("\n");
        sb.append("    -fx-background-color: ").append(rgb).append(", white;").append("\n");
        sb.append("}").append("\n");
        sb.append(".default-color").append(i).append(".chart-series-line {").append("\n");
        sb.append("    -fx-stroke: ").append(rgb).append(";").append("\n");
        sb.append("}").append("\n");
    }
}
