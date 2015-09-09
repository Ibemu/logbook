package logbook.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import logbook.config.AppConfig;
import logbook.constants.AppConstants;
import logbook.gui.listener.SaveWindowLocationAdapter;
import logbook.gui.listener.TableKeyShortcutAdapter;
import logbook.gui.listener.TableToClipboardAdapter;
import logbook.gui.listener.TableToCsvSaveAdapter;
import logbook.gui.logic.LayoutLogic;
import logbook.gui.logic.TableItemDecorator;
import logbook.gui.logic.TableWrapper;
import logbook.thread.ThreadManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * 1つ以上の複数のテーブルで構成されるダイアログの基底クラス
 *
 */
public abstract class AbstractTableDialogEx<T> extends Dialog {

    /** Beanクラス */
    protected Class<T> clazz;

    /** スケジューリングされた再読み込みタスク */
    protected ScheduledFuture<?> future;

    /** シェル */
    protected Shell shell;

    /** メニューバー */
    protected Menu menubar;

    /** [ファイル]メニュー */
    protected Menu filemenu;

    /** [操作]メニュー */
    protected Menu opemenu;

    /** テーブル */
    protected List<TableWrapper<T>> tables = new ArrayList<>();

    /**
     * コンストラクター
     */
    public AbstractTableDialogEx(Shell parent, Class<T> clazz) {
        super(parent, SWT.SHELL_TRIM | SWT.MODELESS);
        this.clazz = clazz;
    }

    /**
     * Open the dialog.
     */
    public void open() {
        // シェルを作成
        this.shell = new Shell(this.getParent(), this.getStyle());
        this.shell.setSize(this.getSize());
        // ウインドウ位置を復元
        LayoutLogic.applyWindowLocation(this.getClass(), this.shell);
        // 閉じた時にウインドウ位置を保存
        this.shell.addShellListener(new SaveWindowLocationAdapter(this.getClass()));

        this.shell.setText(this.getTitle());
        this.shell.setLayout(new FillLayout());
        // メニューバー
        this.menubar = new Menu(this.shell, SWT.BAR);
        this.shell.setMenuBar(this.menubar);
        // メニューバーのメニュー
        MenuItem fileroot = new MenuItem(this.menubar, SWT.CASCADE);
        fileroot.setText("ファイル");
        this.filemenu = new Menu(fileroot);
        fileroot.setMenu(this.filemenu);

        MenuItem savecsv = new MenuItem(this.filemenu, SWT.NONE);
        savecsv.setText("CSVファイルに保存(&S)\tCtrl+S");
        savecsv.setAccelerator(SWT.CTRL + 'S');
        savecsv.addSelectionListener(new TableToCsvSaveAdapter(this.shell, this.getTitle(),
                () -> this.getSelectionTable().getTable()));

        MenuItem operoot = new MenuItem(this.menubar, SWT.CASCADE);
        operoot.setText("操作");
        this.opemenu = new Menu(operoot);
        operoot.setMenu(this.opemenu);

        MenuItem reload = new MenuItem(this.opemenu, SWT.NONE);
        reload.setText("再読み込み(&R)\tF5");
        reload.setAccelerator(SWT.F5);
        reload.addSelectionListener(new TableReloadAdapter());

        Boolean isCyclicReload = AppConfig.get().getCyclicReloadMap().get(this.getClass().getName());
        MenuItem cyclicReload = new MenuItem(this.opemenu, SWT.CHECK);
        cyclicReload.setText("定期的に再読み込み(&A)\tCtrl+F5");
        cyclicReload.setAccelerator(SWT.CTRL + SWT.F5);
        if ((isCyclicReload != null) && isCyclicReload.booleanValue()) {
            cyclicReload.setSelection(true);
        }
        CyclicReloadAdapter adapter = new CyclicReloadAdapter(cyclicReload);
        cyclicReload.addSelectionListener(adapter);
        adapter.setCyclicReload(cyclicReload);

        MenuItem selectVisible = new MenuItem(this.opemenu, SWT.NONE);
        selectVisible.setText("列の表示・非表示(&V)");
        selectVisible.addSelectionListener(new SelectVisibleColumnAdapter());

        new MenuItem(this.opemenu, SWT.SEPARATOR);

        // 閉じた時に設定を保存
        this.shell.addShellListener(new ShellAdapter() {
            @Override
            public void shellClosed(ShellEvent e) {
                AppConfig.get().getCyclicReloadMap()
                        .put(AbstractTableDialogEx.this.getClass().getName(), cyclicReload.getSelection());
            }
        });

        this.createContents();
        this.shell.open();
        this.shell.layout();
        Display display = this.getParent().getDisplay();
        while (!this.shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        // タスクがある場合キャンセル
        if (this.future != null) {
            this.future.cancel(false);
        }
    }

    /**
     * テーブルを追加します
     *
     * @param parent テーブルの親コンポジット
     * @return TableWrapper
     */
    protected TableWrapper<T> addTable(Composite parent) {
        // テーブル
        Table table = new Table(parent, SWT.FULL_SELECTION | SWT.MULTI);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.addKeyListener(new TableKeyShortcutAdapter(table));

        // テーブル右クリックメニュー
        Menu tablemenu = new Menu(table);
        table.setMenu(tablemenu);
        MenuItem sendclipbord = new MenuItem(tablemenu, SWT.NONE);
        sendclipbord.addSelectionListener(new TableToClipboardAdapter(table));
        sendclipbord.setText("クリップボードにコピー(&C)");
        MenuItem reloadtable = new MenuItem(tablemenu, SWT.NONE);
        reloadtable.setText("再読み込み(&R)");
        reloadtable.addSelectionListener(new TableReloadAdapter());

        TableWrapper<T> wrapper = new TableWrapper<T>(table, this.clazz)
                .setDialogClass(this.getClass())
                .setDecorator(new DefaultDecorator<T>());
        this.tables.add(wrapper);
        return wrapper;
    }

    /**
     * 選択されているテーブルを取得する
     */
    protected TableWrapper<T> getSelectionTable() {
        return this.tables.get(0);
    }

    /**
     * テーブルをリロードする
     */
    protected void reload() {
        this.getSelectionTable()
                .reload()
                .update();
    }

    /**
     * ウインドウサイズを返します
     * @return Point
     */
    protected Point getSize() {
        return new Point(600, 350);
    }

    /**
     * タイトルを返します
     * @return String
     */
    protected abstract String getTitle();

    /**
     * Create contents of the dialog.<br>
     * 1つの空のテーブルをダイアログに追加するにはcreateContents()に次のように記述します
     * <pre>{@code
     *  this.addTable(this.shell)
     *          .setContentSupplier(() -> Stream.empty()) // 空のStreamを返すサプライヤーをセット
     *          .reload()
     *          .update();
     * }</pre>
     */
    protected abstract void createContents();

    /**
     * テーブルを再読み込みするリスナーです
     */
    protected class TableReloadAdapter extends SelectionAdapter {
        @Override
        public void widgetSelected(SelectionEvent e) {
            AbstractTableDialogEx.this.reload();
        }
    }

    /**
     * テーブルの列を表示・非表示選択するダイアログを表示する
     */
    protected class SelectVisibleColumnAdapter extends SelectionAdapter {
        @Override
        public void widgetSelected(SelectionEvent e) {
            AbstractTableDialogEx<T> thiz = AbstractTableDialogEx.this;
            Shell shell = thiz.shell;
            Table table = thiz.getSelectionTable().getTable();
            Class<?> clazz = thiz.getClass();
            // ダイアログ表示
            new SelectVisibleColumnDialog(shell, table, clazz).open();
            // テーブル再読み込み
            thiz.reload();
        }
    }

    /**
     * テーブルを定期的に再読み込みする
     */
    protected class CyclicReloadAdapter extends SelectionAdapter {

        private final MenuItem menuitem;

        public CyclicReloadAdapter(MenuItem menuitem) {
            this.menuitem = menuitem;
        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            this.setCyclicReload(this.menuitem);
        }

        private void setCyclicReload(MenuItem menuitem) {
            if (menuitem.getSelection()) {
                Runnable command = () -> {
                    if (!AbstractTableDialogEx.this.shell.isDisposed()) {
                        Display.getDefault().asyncExec(() -> {
                            if (!AbstractTableDialogEx.this.shell.isDisposed()) {
                                AbstractTableDialogEx.this.reload();
                            }
                        });
                    } else {
                        // ウインドウが消えていたらタスクをキャンセルする
                        throw new ThreadDeath();
                    }
                };
                // タスクがある場合キャンセル
                if (AbstractTableDialogEx.this.future != null) {
                    AbstractTableDialogEx.this.future.cancel(false);
                }
                // 再読み込みするようにスケジュールする
                AbstractTableDialogEx.this.future = ThreadManager.getExecutorService()
                        .scheduleWithFixedDelay(command, 1, 1, TimeUnit.SECONDS);
            } else {
                // タスクがある場合キャンセル
                if (AbstractTableDialogEx.this.future != null) {
                    AbstractTableDialogEx.this.future.cancel(false);
                }
            }
        }
    }

    /**
     * TableItemの装飾
     *
     * @param <T>
     */
    private static class DefaultDecorator<T> implements TableItemDecorator<T> {
        @Override
        public void update(TableItem item, T bean, int index) {
            // 偶数行に背景色を付ける
            if ((index % 2) != 0) {
                item.setBackground(SWTResourceManager.getColor(AppConstants.ROW_BACKGROUND));
            } else {
                item.setBackground(null);
            }
        }
    }
}
