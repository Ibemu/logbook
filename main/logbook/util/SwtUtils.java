package logbook.util;

import javax.annotation.CheckForNull;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TaskBar;
import org.eclipse.swt.widgets.TaskItem;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * SWTのutilです
 *
 */
public final class SwtUtils {

    /**
     * TaskItemを取得します
     *
     * @param shell
     * @return
     */
    @CheckForNull
    public static TaskItem getTaskBarItem(Shell shell) {
        TaskBar bar = Display.getDefault().getSystemTaskBar();
        if (bar == null)
            return null;
        TaskItem item = bar.getItem(shell);
        if (item == null)
            item = bar.getItem(null);
        return item;
    }

    /**
     * HPゲージのイメージを取得します
     *
     * @param hpratio HP割合
     * @param expraito 経験値割合
     * @param width 幅
     * @param height 高さ
     * @param expHeight 経験値ゲージの高さ
     * @param emptyColor 大破色
     * @param halfColor 中波色
     * @param fullColor 健在色
     * @param expColor 経験値色
     * @return HPゲージのイメージ
     */
    public static Image getHpAndExpGaugeImage(float hpratio, float expraito,
            int width, int height, int expHeight,
            RGB emptyColor, RGB halfColor, RGB fullColor, RGB expColor) {
        Image image = new Image(Display.getDefault(), width, height);
        GC gc = new GC(image);
        gc.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        gc.fillRectangle(0, 0, width, height);
        gc.setBackground(SWTResourceManager.getColor(gradation(hpratio, emptyColor, halfColor, fullColor)));
        gc.fillRectangle(0, 0, (int) (width * hpratio), height);
        gc.setBackground(SWTResourceManager.getColor(expColor));
        gc.fillRectangle(0, height - expHeight, (int) (width * expraito), expHeight);
        gc.drawImage(image, 0, 0);
        gc.dispose();
        return image;
    }

    /**
     * ゲージのイメージを取得します
     *
     * @param ratio 割合
     * @param width 幅
     * @param height 高さ
     * @param background 背景色
     * @param colors 色たち
     * @return ゲージのイメージ
     */
    public static Image gaugeImage(float ratio, int width, int height, RGB background, RGB... colors) {
        Image image = new Image(Display.getDefault(), width, height);
        GC gc = new GC(image);
        gc.drawImage(image, 0, 0);
        gc.setAlpha(0);
        gc.setBackground(SWTResourceManager.getColor(background));
        gc.fillRectangle(0, 0, width, height);
        gc.setAlpha(255);
        gc.setBackground(SWTResourceManager.getColor(gradation(ratio, colors)));
        gc.fillRectangle(0, 0, (int) (width * ratio), height);
        gc.dispose();
        return image;
    }

    /**
     * 複数の色の中間色を取得する
     *
     * @param raito 割合
     * @param rgbs 色たち
     * @return 色
     */
    public static RGB gradation(float raito, RGB... rgbs) {
        if ((rgbs == null) || (rgbs.length == 0)) {
            throw new IllegalArgumentException("rgbs is empty.");
        }
        if (rgbs.length == 1) {
            return rgbs[0];
        }
        if (raito <= 0.0f) {
            return rgbs[0];
        }
        if (raito >= 1.0f) {
            return rgbs[rgbs.length - 1];
        }
        int length = rgbs.length - 1;

        // 開始色
        int start = (int) (length * raito);
        // 終了色
        int end = start + 1;
        // 開始色と終了色の割合を算出
        float startPer = (float) start / length;
        float endPer = (float) end / length;
        float subPer = (raito - startPer) / (endPer - startPer);
        return gradation(subPer, rgbs[start], rgbs[end]);
    }

    /**
     * 2つの色の中間色を取得する
     *
     * @param raito 割合
     * @param start 開始色
     * @param end 終了色
     * @return 色
     */
    public static RGB gradation(float raito, RGB start, RGB end) {
        int r = (int) (start.red + ((end.red - start.red) * raito));
        int g = (int) (start.green + ((end.green - start.green) * raito));
        int b = (int) (start.blue + ((end.blue - start.blue) * raito));
        return new RGB(r, g, b);
    }
}
