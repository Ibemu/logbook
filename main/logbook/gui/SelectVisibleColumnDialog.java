package logbook.gui;

import java.util.Arrays;
import java.util.stream.Stream;

import logbook.config.AppConfig;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * テーブルの列を表示・非表示選択するダイアログ
 *
 */
public final class SelectVisibleColumnDialog extends Dialog {

    /** テーブル */
    private final Table table;

    /** ダイアログクラス */
    private final Class<?> clazz;

    /** シェル */
    private Shell shell;

    /**
     * Create the dialog.
     * @param parent 親シェル
     * @param table テーブル
     * @param clazz ダイアログクラス
     */
    public SelectVisibleColumnDialog(Shell parent, Table table, Class<?> clazz) {
        super(parent, SWT.NONE);
        this.table = table;
        this.clazz = clazz;
    }

    /**
     * Open the dialog.
     */
    public void open() {
        this.createContents();
        this.shell.open();
        this.shell.layout();
        Display display = this.getParent().getDisplay();
        while (!this.shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        this.shell = new Shell(this.getParent(), SWT.SHELL_TRIM | SWT.PRIMARY_MODAL);
        this.shell.setSize(300, 275);
        this.shell.setText("列の表示非表示");
        this.shell.setLayout(new FillLayout(SWT.HORIZONTAL));

        // ヘッダー
        String[] header = Stream.of(this.table.getColumns()).map(c -> c.getText()).toArray(String[]::new);
        // カラム設定を取得
        boolean[] visibles = AppConfig.get().getVisibleColumnMap().get(this.clazz.getName());
        if ((visibles == null) || (visibles.length != header.length)) {
            visibles = new boolean[header.length];
            Arrays.fill(visibles, true);
            AppConfig.get().getVisibleColumnMap().put(this.clazz.getName(), visibles);
        }

        Tree tree = new Tree(this.shell, SWT.BORDER | SWT.CHECK);

        for (int i = 1; i < header.length; i++) {
            TreeItem column = new TreeItem(tree, SWT.CHECK);
            column.setText(header[i]);
            column.setChecked(visibles[i]);
            column.setExpanded(true);
        }
        this.shell.addShellListener(new TreeShellAdapter(tree, visibles));
    }

    /**
     * チェックされた内容をウインドウが閉じるタイミングで保存します
     *
     */
    private static final class TreeShellAdapter extends ShellAdapter {

        private final Tree tree;
        private final boolean[] visibles;

        public TreeShellAdapter(Tree tree, boolean[] visibles) {
            this.tree = tree;
            this.visibles = visibles;
        }

        @Override
        public void shellClosed(ShellEvent e) {
            TreeItem[] items = this.tree.getItems();
            for (int i = 0; i < items.length; i++) {
                this.visibles[i + 1] = items[i].getChecked();
            }
        }
    }
}
