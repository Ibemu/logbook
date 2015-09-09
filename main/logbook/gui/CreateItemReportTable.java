package logbook.gui;

import logbook.gui.bean.CreateItemReportBean;
import logbook.gui.logic.CreateReportLogic;

import org.eclipse.swt.widgets.Shell;

/**
 * 開発報告書
 *
 */
public final class CreateItemReportTable extends AbstractTableDialogEx<CreateItemReportBean> {

    /**
     * @param parent
     */
    public CreateItemReportTable(Shell parent) {
        super(parent, CreateItemReportBean.class);
    }

    @Override
    protected void createContents() {
        this.addTable(this.shell)
                .setContentSupplier(CreateReportLogic::getCreateItemContent)
                .reload()
                .update();
    }

    @Override
    protected String getTitle() {
        return "開発報告書";
    }
}
