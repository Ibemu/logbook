package logbook.gui.logic;

import logbook.constants.AppConstants;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * テーブルの行を作成するインターフェイスです
 *
 */
public interface TableItemCreator {

    /** テーブルアイテム作成(デフォルト) */
    TableItemCreator DEFAULT_TABLE_ITEM_CREATOR = new TableItemCreator() {

        @Override
        public void init() {
        }

        @Override
        public TableItem create(Table table, String[] text, int count) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(text);
            update(item, text, count);
            return item;
        }

        @Override
        public TableItem update(TableItem item, String[] text, int count) {
            // 偶数行に背景色を付ける
            if ((count % 2) != 0) {
                item.setBackground(SWTResourceManager.getColor(AppConstants.ROW_BACKGROUND));
            } else {
                item.setBackground(null);
            }
            return item;
        }
    };

    void init();

    TableItem create(Table table, String[] text, int count);

    TableItem update(TableItem item, String[] text, int count);
}
