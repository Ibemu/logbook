package logbook.gui;

import logbook.gui.bean.MissionResultBean;
import logbook.gui.logic.CreateReportLogic;

import org.eclipse.swt.widgets.Shell;

/**
 * 遠征報告書
 *
 */
public final class MissionResultTable extends AbstractTableDialogEx<MissionResultBean> {

    /**
     * @param parent
     */
    public MissionResultTable(Shell parent) {
        super(parent, MissionResultBean.class);
    }

    @Override
    protected void createContents() {
        this.addTable(this.shell)
                .setContentSupplier(CreateReportLogic::getMissionResultContent)
                .reload()
                .update();
    }

    @Override
    protected String getTitle() {
        return "遠征報告書";
    }
}
