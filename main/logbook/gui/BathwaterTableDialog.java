package logbook.gui;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import logbook.data.context.GlobalContext;
import logbook.data.context.ShipContext;
import logbook.dto.ShipDto;
import logbook.gui.bean.BathwaterBean;

import org.eclipse.swt.widgets.Shell;

/**
 * お風呂に入りたい艦娘
 *
 */
public final class BathwaterTableDialog extends AbstractTableDialogEx<BathwaterBean> {

    /**
     * Create the dialog.
     * @param parent
     */
    public BathwaterTableDialog(Shell parent) {
        super(parent, BathwaterBean.class);
    }

    @Override
    protected void createContents() {
        this.addTable(this.shell)
                .setContentSupplier(BathwaterTableDialog::getBathwaterContent)
                .reload()
                .update();
    }

    @Override
    protected String getTitle() {
        return "お風呂に入りたい艦娘";
    }

    /**
     * お風呂に入りたい艦娘の内容
     *
     * @return 内容
     */
    private static Stream<BathwaterBean> getBathwaterContent() {
        // 入渠中
        Set<Long> docks = Stream.of(GlobalContext.getNdocks())
                .filter(e -> e.getNdockid() != 0)
                .map(e -> e.getNdockid())
                .collect(Collectors.toSet());

        return ShipContext.get().values()
                .stream()
                .filter(e -> e.getDocktime() > 0)
                .filter(e -> !docks.contains(e.getId()))
                // 時間でソート
                .sorted(Comparator.comparing(ShipDto::getDocktime).reversed())
                .map(BathwaterBean::toBean);
    }
}
