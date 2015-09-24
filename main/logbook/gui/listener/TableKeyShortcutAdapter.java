package logbook.gui.listener;

import java.util.stream.Stream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Table;

/**
 * テーブルウィジェットのキー操作のアダプターです
 *
 */
public final class TableKeyShortcutAdapter extends KeyAdapter {

    /** テーブルヘッダー */
    private String[] header;

    /** テーブル */
    private final Table table;

    /**
     * コンストラクター
     */
    public TableKeyShortcutAdapter(String[] header, Table table) {
        this.header = header;
        this.table = table;
    }

    /**
     * コンストラクター
     */
    public TableKeyShortcutAdapter(Table table) {
        this.table = table;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        String[] header = this.header;

        if (this.header == null) {
            header = Stream.of(this.table.getColumns()).map(c -> c.getText()).toArray(String[]::new);
        }

        if ((e.stateMask == SWT.CTRL) && (e.keyCode == 'c')) {
            TableToClipboardAdapter.copyTable(header, this.table);
        }
        if ((e.stateMask == SWT.CTRL) && (e.keyCode == 'a')) {
            this.table.selectAll();
        }
    }
}
