package logbook.gui.listener;

import logbook.gui.ApplicationMain;
import logbook.gui.FleetCompositeDialog;

import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

/**
 * タブをダブルクリックでダイアログを表示するリスナー
 *
 */
public final class TabFolderMouseListener implements MouseListener {
    /**　Shell */
    private final Shell shell;
    /** タブ */
    private final ApplicationMain main;

    /**
     * コンストラクター
     */
    public TabFolderMouseListener(Shell shell, ApplicationMain main) {
        this.shell = shell;
        this.main = main;
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
        Point point = new Point(e.x, e.y);
        CTabItem[] items = this.main.getTabFolder().getItems();
        for (int i = 1; i < items.length; i++) {
            if (items[i].getBounds().contains(point)) {
                new FleetCompositeDialog(this.shell, this.main, i).open();
            }
        }
    }

    @Override
    public void mouseDown(MouseEvent e) {

    }

    @Override
    public void mouseUp(MouseEvent e) {

    }
}