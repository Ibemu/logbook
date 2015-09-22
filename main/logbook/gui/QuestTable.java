package logbook.gui;

import logbook.data.context.GlobalContext;
import logbook.gui.bean.QuestBean;
import logbook.gui.listener.SelectedListener;
import logbook.gui.logic.CreateReportLogic;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * 任務一覧テーブル
 *
 */
public final class QuestTable extends AbstractTableDialogEx<QuestBean> {

    /**
     * @param parent
     */
    public QuestTable(Shell parent) {
        super(parent, QuestBean.class);
    }

    @Override
    protected void createContents() {
        this.addTable(this.shell)
                .setContentSupplier(CreateReportLogic::getQuestContent)
                .reload()
                .update();

        // 任務をリセット
        final MenuItem reset = new MenuItem(this.opemenu, SWT.NONE);
        reset.setText("任務をリセット");
        reset.addSelectionListener((SelectedListener) e -> {
            GlobalContext.getQuest().clear();
            this.getSelectionTable().reload().update();
        });
    }

    @Override
    protected String getTitle() {
        return "任務一覧";
    }
}
