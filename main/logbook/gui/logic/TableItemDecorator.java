package logbook.gui.logic;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.TableItem;

/**
 * TableItemの装飾を制御します
 *
 */
public interface TableItemDecorator<T> {

    /**
     * TableItemの装飾を設定します
     *
     * @param item TableItem
     * @param bean TableItemの元になったBean
     * @param index TableItemの行番号
     */
    void update(TableItem item, T bean, int index);

    /**
     * ストライプ模様のTableItemDecoratorを作成します
     *
     * @param color Color
     * @return ストライプ模様のTableItemDecorator
     */
    static <T> TableItemDecorator<T> stripe(Color color) {
        return (item, bean, index) -> item.setBackground((index % 2) != 0 ? color : null);
    }
}
