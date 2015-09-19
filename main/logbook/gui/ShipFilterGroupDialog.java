package logbook.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import logbook.config.ShipGroupConfig;
import logbook.config.bean.ShipGroupBean;
import logbook.constants.AppConstants;
import logbook.data.context.ShipContext;
import logbook.dto.ShipDto;
import logbook.gui.bean.ShipFilterGroupBean;
import logbook.gui.listener.SelectedListener;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * グループエディター
 *
 */
public final class ShipFilterGroupDialog extends AbstractTableDialogEx<ShipFilterGroupBean> {

    private Text text;
    private Combo shipcombo;
    private Tree tree;
    private Button btnAddShip;
    private Button btnRemoveShip;

    private final Map<String, ShipDto> shipmap = new HashMap<String, ShipDto>();

    /** 現在表示しているグループ */
    private GroupProperty property;

    /**
     * Create the dialog.
     * @param parent
     */
    public ShipFilterGroupDialog(Shell parent) {
        super(parent, ShipFilterGroupBean.class);
    }

    /**
     * Create contents of the dialog.
     */
    @Override
    protected void createContents() {
        GridLayout shellLayout = new GridLayout(1, false);
        shellLayout.verticalSpacing = 1;
        shellLayout.marginWidth = 1;
        shellLayout.marginHeight = 1;
        shellLayout.marginBottom = 1;
        shellLayout.horizontalSpacing = 1;
        this.shell.setLayout(shellLayout);

        SashForm sashForm = new SashForm(this.shell, SWT.SMOOTH);
        sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        Composite sideComposite = new Composite(sashForm, SWT.NONE);
        GridLayout sideLayout = new GridLayout(2, false);
        sideLayout.verticalSpacing = 1;
        sideLayout.marginWidth = 1;
        sideLayout.marginHeight = 1;
        sideLayout.marginBottom = 1;
        sideLayout.horizontalSpacing = 1;
        sideComposite.setLayout(sideLayout);

        Button btnAdd = new Button(sideComposite, SWT.NONE);
        btnAdd.setImage(SWTResourceManager.getImage(ShipFilterGroupDialog.class, AppConstants.R_ICON_ADD));

        Button btnRemove = new Button(sideComposite, SWT.NONE);
        btnRemove.addSelectionListener((SelectedListener) e -> {
            if (this.getProperty() != null) {
                ShipGroupBean target = this.getProperty().getShipGroupBean();

                MessageBox box = new MessageBox(this.shell, SWT.YES | SWT.NO
                        | SWT.ICON_QUESTION);
                box.setText("グループを除去");
                box.setMessage("「" + target.getName() + "」を除去しますか？");
                if (box.open() == SWT.YES) {
                    ShipGroupConfig.get().getGroup().removeIf(b -> b == target);
                    this.text.setText("");
                    this.getProperty().getTreeItem().dispose();
                    this.setProperty(null);
                }
            }
        });
        btnRemove.setImage(SWTResourceManager.getImage(ShipFilterGroupDialog.class, AppConstants.R_ICON_DELETE));

        this.tree = new Tree(sideComposite, SWT.BORDER);
        this.tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        this.tree.addSelectionListener((SelectedListener) e -> {
            if (e.item != null) {
                Object data = e.item.getData();
                if (data instanceof GroupProperty) {
                    this.setProperty((GroupProperty) data);
                } else {
                    this.setProperty(null);
                }
                this.reload();
            }
        });

        TreeItem treeItem;
        treeItem = new TreeItem(this.tree, SWT.NONE);
        treeItem.setImage(SWTResourceManager.getImage(ShipFilterGroupDialog.class, AppConstants.R_ICON_STAR));
        treeItem.setText("グループ");

        for (ShipGroupBean bean : ShipGroupConfig.get().getGroup()) {
            TreeItem groupItem = new TreeItem(treeItem, SWT.NONE);
            groupItem.setImage(SWTResourceManager
                    .getImage(ShipFilterGroupDialog.class, AppConstants.R_ICON_FOLDER));
            groupItem.setText(bean.getName());
            groupItem.setData(new GroupProperty(bean, groupItem));
        }
        treeItem.setExpanded(true);

        Composite mainComposite = new Composite(sashForm, SWT.NONE);
        GridLayout mainLayout = new GridLayout(3, false);
        mainLayout.verticalSpacing = 1;
        mainLayout.marginWidth = 1;
        mainLayout.marginHeight = 1;
        mainLayout.marginBottom = 1;
        mainLayout.horizontalSpacing = 1;
        mainComposite.setLayout(mainLayout);

        this.text = new Text(mainComposite, SWT.BORDER);
        this.text.addModifyListener(e -> {
            if (this.getProperty() != null) {
                String text = this.text.getText();
                this.getProperty().getTreeItem().setText(text);
                ShipGroupBean group = this.getProperty().getShipGroupBean();
                group.setName(text);
            }
        });
        this.text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        this.text.setEnabled(false);

        Table table = this.addTable(mainComposite)
                .setContentSupplier(Stream::empty)
                .reload()
                .update()
                .getTable();

        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
        this.shipcombo = new Combo(mainComposite, SWT.READ_ONLY);
        this.setShipComboData();
        this.shipcombo.setEnabled(false);

        this.btnAddShip = new Button(mainComposite, SWT.NONE);
        this.btnAddShip.setText("追加");
        this.btnAddShip.addSelectionListener((SelectedListener) e -> {
            if (this.getProperty() != null) {
                if (this.shipcombo.getSelectionIndex() >= 0) {
                    ShipGroupBean target = this.getProperty().getShipGroupBean();
                    ShipDto ship = this.shipmap.get(this.shipcombo.getItem(this.shipcombo.getSelectionIndex()));
                    target.getShips().add(ship.getId());
                    this.reload();
                }
            }
        });
        this.btnAddShip.setEnabled(false);

        this.btnRemoveShip = new Button(mainComposite, SWT.NONE);
        this.btnRemoveShip.setText("除去");
        this.btnRemoveShip.addSelectionListener((SelectedListener) e -> {
            if (this.getProperty() != null) {
                ShipGroupBean target = this.getProperty().getShipGroupBean();
                TableItem[] items = table.getSelection();
                for (TableItem tableItem : items) {
                    String text = tableItem.getText(1);
                    if (StringUtils.isNumeric(text)) {
                        Long id = Long.valueOf(text);
                        target.getShips().remove(id);
                    }
                }
                this.reload();
            }
        });
        this.btnRemoveShip.setEnabled(false);
        sashForm.setWeights(new int[] { 2, 5 });

        btnAdd.addSelectionListener((SelectedListener) e -> {
            List<ShipGroupBean> shipGroupList = ShipGroupConfig.get().getGroup();

            ShipGroupBean bean = new ShipGroupBean();
            bean.setName("新規グループ");
            shipGroupList.add(bean);

            TreeItem item = new TreeItem(treeItem, SWT.NONE);
            item.setImage(SWTResourceManager.getImage(ShipFilterGroupDialog.class, AppConstants.R_ICON_FOLDER));
            item.setText(bean.getName());
            item.setData(new GroupProperty(bean, item));
            treeItem.setExpanded(true);
        });
    }

    @Override
    protected String getTitle() {
        return "グループエディター";
    }

    private void setProperty(GroupProperty property) {
        this.property = property;
        if (property != null) {
            ShipGroupBean group = this.property.getShipGroupBean();
            this.text.setText(group.getName());
            this.text.setEnabled(true);
            this.shipcombo.setEnabled(true);
            this.btnAddShip.setEnabled(true);
            this.btnRemoveShip.setEnabled(true);

            this.getSelectionTable().setContentSupplier(property::getShip);
        } else {
            this.text.setText("");
            this.text.setEnabled(false);
            this.shipcombo.setEnabled(false);
            this.btnAddShip.setEnabled(false);
            this.btnRemoveShip.setEnabled(false);

            this.getSelectionTable().setContentSupplier(Stream::empty);
        }
        this.reload();
    }

    private GroupProperty getProperty() {
        return this.property;
    }

    /**
     * コンボボックスに艦娘をセットします
     *
     * @param combo
     */
    private void setShipComboData() {
        // コンボボックスから全ての艦娘を削除
        this.shipcombo.removeAll();
        // 表示用文字列と艦娘の紐付けを削除
        this.shipmap.clear();
        // 艦娘IDの最大を取得してゼロ埋め長さを算出
        long maxshipid = 0;
        for (ShipDto ship : ShipContext.get().values()) {
            maxshipid = Math.max(ship.getId(), maxshipid);
        }
        int padlength = Long.toString(maxshipid).length();
        // 表示用文字列と艦娘の紐付けを追加
        for (ShipDto ship : ShipContext.get().values()) {
            this.shipmap.put(this.getShipLabel(ship, padlength), ship);
        }
        // 艦娘を経験値順でソート
        List<ShipDto> ships = new ArrayList<ShipDto>(this.shipmap.values());
        Collections.sort(ships, new Comparator<ShipDto>() {
            @Override
            public int compare(ShipDto o1, ShipDto o2) {
                return Long.compare(o2.getExp(), o1.getExp());
            }
        });
        // コンボボックスに追加
        for (int i = 0; i < ships.size(); i++) {
            String key = this.getShipLabel(ships.get(i), padlength);
            this.shipcombo.add(key);
        }
        // コントロールを再配置
        this.shipcombo.pack();
        this.shipcombo.getParent().pack();
    }

    /**
     * 艦娘のプルダウン表示用文字列を作成します
     *
     * @param ship
     * @param padlength
     * @return
     */
    private String getShipLabel(ShipDto ship, int padlength) {
        return new StringBuilder().append(StringUtils.leftPad(Long.toString(ship.getId()), padlength, '0'))
                .append(": ").append(ship.getName()).append(" (Lv").append(ship.getLv() + ")").toString();
    }

    /**
     * テーブルに表示する所有艦娘のグループ
     *
     */
    private static final class GroupProperty {

        /** 所有艦娘のグループ */
        private final ShipGroupBean shipGroup;

        /** TreeItem */
        private final TreeItem item;

        /**
         * コンストラクター
         *
         * @param shipGroup 所有艦娘のグループ
         * @param item TreeItem
         */
        public GroupProperty(ShipGroupBean shipGroup, TreeItem item) {
            this.shipGroup = shipGroup;
            this.item = item;
        }

        /**
         * 所有艦娘のグループを取得します
         *
         * @return 所有艦娘のグループ
         */
        public ShipGroupBean getShipGroupBean() {
            return this.shipGroup;
        }

        /**
         * TreeItemを取得します
         * @return TreeItem
         */
        public TreeItem getTreeItem() {
            return this.item;
        }

        /**
         * 艦娘を取得します
         *
         * @return 艦娘
         */
        public Stream<ShipFilterGroupBean> getShip() {
            Function<ShipDto, ShipFilterGroupBean> mapper = e -> {
                ShipFilterGroupBean b = new ShipFilterGroupBean();
                b.setId(e.getId());
                b.setFleetid(e.getFleetid());
                b.setName(e.getName());
                b.setType(e.getType());
                b.setLv(e.getLv());
                b.setCond(e.getCond());
                b.setSallyArea(e.getSallyArea().getName());
                return b;
            };
            return ShipContext.get().values()
                    .stream()
                    .filter(e -> this.shipGroup.getShips().contains(e.getId()))
                    .map(mapper);
        }
    }
}
