package logbook.gui;

import logbook.dto.ShipFilterDto;
import logbook.gui.bean.ItemBean;
import logbook.gui.logic.CreateReportLogic;
import logbook.gui.logic.TableWrapper;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Shell;

/**
 * 所有装備一覧
 *
 */
public final class ItemTable extends AbstractTableDialogEx<ItemBean> {

    /**
     * @param parent
     */
    public ItemTable(Shell parent) {
        super(parent, ItemBean.class);
    }

    @Override
    protected void createContents() {
        TableWrapper<ItemBean> table = this.addTable(this.shell)
                .setContentSupplier(CreateReportLogic::getItemTablecontent)
                .reload()
                .update();

        table.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                ItemBean[] selection = table.getSelection(ItemBean[]::new);
                for (ItemBean item : selection) {
                    ShipFilterDto filter = new ShipFilterDto();
                    filter.itemname = item.getName();
                    new ShipTable(ItemTable.this.getParent(), filter).open();
                }
            }
        });
    }

    @Override
    protected String getTitle() {
        return "所有装備一覧";
    }
}
