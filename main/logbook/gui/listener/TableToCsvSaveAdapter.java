package logbook.gui.listener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import logbook.util.FileUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * テーブルをCSVファイルに保存するアダプターです
 *
 */
public final class TableToCsvSaveAdapter extends SelectionAdapter {

    /** シェル */
    private final Shell shell;

    /** ファイル名 */
    private final String name;

    /** ヘッダー */
    private String[] header;

    /** テーブル */
    private Table table;

    /** テーブル */
    private Supplier<Table> tableSupplier;

    /**
     * コンストラクター
     *
     * @param shell シェル
     * @param name ファイル名
     * @param header ヘッダー
     * @param table テーブル
     */
    public TableToCsvSaveAdapter(Shell shell, String name, String[] header, Table table) {
        this.shell = shell;
        this.name = name;
        this.header = header;
        this.table = table;
    }

    /**
     * コンストラクター
     *
     * @param shell シェル
     * @param name ファイル名
     * @param tableSupplier テーブル
     */
    public TableToCsvSaveAdapter(Shell shell, String name, Supplier<Table> tableSupplier) {
        this.shell = shell;
        this.name = name;
        this.tableSupplier = tableSupplier;
    }

    @Override
    public void widgetSelected(SelectionEvent arg) {
        Table table = this.table;
        String[] header = this.header;

        if (this.tableSupplier != null) {
            table = this.tableSupplier.get();
            header = Stream.of(table.getColumns()).map(c -> c.getText()).toArray(String[]::new);
        }

        FileDialog dialog = new FileDialog(this.shell, SWT.SAVE);
        dialog.setFileName(this.name + ".csv");
        dialog.setFilterExtensions(new String[] { "*.csv" });
        String filename = dialog.open();
        if (filename != null) {
            Path path = Paths.get(filename);
            if (Files.exists(path)) {
                MessageBox messageBox = new MessageBox(this.shell, SWT.YES | SWT.NO);
                messageBox.setText("確認");
                messageBox.setMessage("指定されたファイルは存在します。\n上書きしますか？");
                if (messageBox.open() == SWT.NO) {
                    return;
                }
            }
            try {
                List<String[]> body = new ArrayList<String[]>();
                TableItem[] items = table.getItems();
                for (TableItem item : items) {
                    String[] colums = new String[header.length];
                    for (int i = 0; i < colums.length; i++) {
                        colums[i] = item.getText(i);
                    }
                    body.add(colums);
                }

                FileUtils.writeCsv(path, header, body, false);
            } catch (IOException e) {
                MessageBox messageBox = new MessageBox(this.shell, SWT.ICON_ERROR);
                messageBox.setText("書き込めませんでした");
                messageBox.setMessage(e.toString());
                messageBox.open();
            }
        }
    }
}
