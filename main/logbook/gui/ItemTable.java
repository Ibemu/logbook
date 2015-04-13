package logbook.gui;

import logbook.dto.ShipFilterDto;
import logbook.gui.logic.CreateReportLogic;
import logbook.gui.logic.TableItemCreator;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * 所有装備一覧
 *
 */
public final class ItemTable extends AbstractTableDialog {

    /**
     * @param parent
     */
    public ItemTable(Shell parent) {
        super(parent);
    }

    @Override
    protected void createContents() {
        this.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                TableItem[] items = ItemTable.this.table.getSelection();
                for (TableItem tableItem : items) {
                    String itemName = tableItem.getText(1);
                    ShipFilterDto filter = new ShipFilterDto();
                    filter.itemname = itemName;
                    new ShipTable(ItemTable.this.shell, filter).open();
                }
            }
        });
    }

    @Override
    protected String getTitle() {
        return "所有装備一覧";
    }

    @Override
    protected Point getSize() {
        return new Point(600, 350);
    }

    @Override
    protected String[] getTableHeader() {
        return CreateReportLogic.getItemListHeader();
    }

    @Override
    protected void updateTableBody() {
        this.body = CreateReportLogic.getItemListBody();
    }

    @Override
    protected TableItemCreator getTableItemCreator() {
        return TableItemCreator.DEFAULT_TABLE_ITEM_CREATOR;
    }

    @Override
    protected SelectionListener getHeaderSelectionListener() {
        return new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (e.getSource() instanceof TableColumn) {
                    ItemTable.this.sortTableItems((TableColumn) e.getSource());
                }
            }
        };
    }
}
