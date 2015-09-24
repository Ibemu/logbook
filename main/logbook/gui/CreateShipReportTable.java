package logbook.gui;

import logbook.gui.bean.CreateShipReportBean;
import logbook.gui.logic.CreateReportLogic;

import org.eclipse.swt.widgets.Shell;

/**
 * 建造報告書
 *
 */
public final class CreateShipReportTable extends AbstractTableDialogEx<CreateShipReportBean> {

    /**
     * @param parent
     */
    public CreateShipReportTable(Shell parent) {
        super(parent, CreateShipReportBean.class);
    }

    @Override
    protected void createContents() {
        this.addTable(this.shell)
                .setContentSupplier(CreateReportLogic::getCreateShipContent)
                .reload()
                .update();
    }

    @Override
    protected String getTitle() {
        return "建造報告書";
    }
}
