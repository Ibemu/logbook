package logbook.gui;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import logbook.data.context.GlobalContext;
import logbook.dto.DockDto;
import logbook.gui.listener.SaveWindowLocationAdapter;
import logbook.gui.logic.LayoutLogic;
import logbook.gui.widgets.FleetComposite;
import logbook.thread.ThreadManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 出撃詳細
 *
 */
public final class FleetCompositeDialog extends Dialog
{
    private Shell shell;
    private Display display;

    /** 本体 */
    private final ApplicationMain main;

    /** タブ */
    private CTabFolder tabFolder;

    /** タブ */
    private CTabItem tabItem;

    /** タブ */
    private FleetComposite tabComposite;

    /** 艦隊番号 */
    private final int index;

    /** スケジューリングされた再読み込みタスク */
    protected ScheduledFuture<?> future;

    /**
     * Create the dialog.
     * @param parent
     */
    public FleetCompositeDialog(Shell parent, ApplicationMain main, int index) {
        super(parent, SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.RESIZE);
        this.main = main;
        this.index = index;
    }

    /**
     * Open the dialog.
     */
    public void open() {
        this.createContents();
        this.reload();

        this.shell.open();
        this.display = this.getParent().getDisplay();

        Runnable command = () -> {
            if (!this.shell.isDisposed()) {
                this.display.asyncExec(() -> {
                    if (!this.shell.isDisposed()) {
                        this.reload();
                    }
                });
            } else {
                // ウインドウが消えていたらタスクをキャンセルする
                throw new ThreadDeath();
            }
        };
        // 再読み込みするようにスケジュールする
        this.future = ThreadManager.getExecutorService()
                .scheduleWithFixedDelay(command, 1, 1, TimeUnit.SECONDS);

        while (!this.shell.isDisposed()) {
            if (!this.display.readAndDispatch()) {
                this.display.sleep();
            }
        }
        // タスクがある場合キャンセル
        if (this.future != null) {
            this.future.cancel(false);
        }
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        // シェルを作成
        this.shell = new Shell(this.getParent(), this.getStyle());
        this.shell.setText("艦隊タブ");
        GridLayout glShell = new GridLayout(1, false);
        glShell.marginTop = 0;
        glShell.marginWidth = 0;
        glShell.marginHeight = 0;
        glShell.marginBottom = 0;
        this.shell.setLayout(glShell);
        // ウインドウ位置を復元
        LayoutLogic.applyWindowLocation(this.getClass(), this.shell);
        // 閉じた時にウインドウ位置を保存
        this.shell.addShellListener(new SaveWindowLocationAdapter(this.getClass()));

        // タブフォルダー
        this.tabFolder = new CTabFolder(this.shell, SWT.NONE);
        this.tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(
                SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
        this.tabFolder.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
        this.tabFolder.setTabHeight(26);
        this.tabFolder.marginWidth = 0;
        this.tabFolder.setMinimumCharacters(2);
    }

    private void reload()
    {
        DockDto dock = GlobalContext.getDock(Integer.toString(this.index));
        if (dock != null) {
            if (this.tabItem == null) {

                this.tabItem = new CTabItem(this.tabFolder, SWT.NONE);
                this.tabItem.setText(dock.getName());

                // メインコンポジット
                this.tabComposite = new FleetComposite(this.tabFolder, this.tabItem, this.main);
                this.tabItem.setControl(this.tabComposite);
            }
            this.tabComposite.updateFleet(dock);
            this.tabItem.setText(dock.getName());
        }
    }
}
