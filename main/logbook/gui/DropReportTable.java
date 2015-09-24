package logbook.gui;

import logbook.gui.bean.DropReportBean;
import logbook.gui.logic.CreateReportLogic;
import logbook.gui.logic.TableWrapper;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Shell;

/**
 * ドロップ報告書
 *
 */
public final class DropReportTable extends AbstractTableDialogEx<DropReportBean> {

    /**
     * @param parent
     */
    public DropReportTable(Shell parent) {
        super(parent, DropReportBean.class);
    }

    @Override
    protected void createContents() {
        TableWrapper<DropReportBean> table = this.addTable(this.shell)
                .setContentSupplier(CreateReportLogic::getBattleResultContent)
                .reload()
                .update();

        table.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                DropReportBean[] selection = table.getSelection(DropReportBean[]::new);
                for (DropReportBean item : selection) {
                    new BattleDialog(DropReportTable.this.shell, item.getResult()).open();
                }
            }
        });
    }

    @Override
    protected String getTitle() {
        return "ドロップ報告書";
    }
}
