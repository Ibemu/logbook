package logbook.gui;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import logbook.config.ShipGroupConfig;
import logbook.config.bean.ShipGroupBean;
import logbook.data.context.ItemContext;
import logbook.dto.ItemDto;
import logbook.dto.ShipFilterDto;
import logbook.internal.SallyArea;

/**
 * 所有艦娘一覧で使用するフィルターダイアログ
 *
 */
public final class ShipFilterDialog extends Dialog {

    private Shell shell;

    private final Consumer<ShipFilterDto> update;

    private final Supplier<ShipFilterDto> supplier;

    /** グループ一覧 */
    private final List<ShipGroupBean> groups = ShipGroupConfig.get().getGroup();

    /** 名前 */
    private Text nametext;
    /** 名前.正規表現を使用する */
    private Button regexp;

    /** 艦種.駆逐艦 */
    private Button destroyer;
    /** 艦種.軽巡洋艦 */
    private Button lightCruiser;
    /** 艦種.重雷装巡洋艦 */
    private Button torpedoCruiser;
    /** 艦種.重巡洋艦 */
    private Button heavyCruiser;
    /** 艦種.航空巡洋艦 */
    private Button flyingDeckCruiser;
    /** 艦種.水上機母艦 */
    private Button seaplaneTender;
    /** 艦種.軽空母 */
    private Button escortCarrier;
    /** 艦種.正規空母 */
    private Button carrier;
    /** 艦種.戦艦 */
    private Button battleship;
    /** 艦種.航空戦艦 */
    private Button flyingDeckBattleship;
    /** 艦種.潜水艦 */
    private Button submarine;
    /** 艦種.潜水空母 */
    private Button carrierSubmarine;
    /** 艦種.揚陸艦 */
    private Button landingship;
    /** 艦種.装甲空母 */
    private Button armoredcarrier;
    /** 艦種.工作艦 */
    private Button repairship;
    /** 艦種.潜水母艦 */
    private Button submarineTender;
    /** 艦種.練習巡洋艦 */
    private Button trainingShip;
    /** 艦種.補給艦 */
    private Button supply;
    /** 艦種.海防艦 */
    private Button escort;
    /** 全て選択 */
    private Button selectall;
    /** グループ */
    private Button group;
    /** グループ選択 */
    private Combo groupcombo;
    /** 出撃海域 */
    private Button area;
    /** 出撃海域選択 */
    private Combo areacombo;
    /** 装備 */
    private Button item;
    /** 装備 */
    private Combo itemcombo;
    /** 艦隊に所属 */
    private Button onfleet;
    /** 艦隊に非所属 */
    private Button notonfleet;
    /** 鍵付き */
    private Button locked;
    /** 鍵付きではない */
    private Button notlocked;

    /**
     * Create the dialog.
     *
     * @param parent シェル
     * @param updateFunction フィルター適用関数
     * @param supplier フィルター供給関数
     */
    public ShipFilterDialog(Shell parent, Consumer<ShipFilterDto> updateFunction, Supplier<ShipFilterDto> supplier) {
        super(parent, SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.RESIZE);
        this.update = updateFunction;
        this.supplier = supplier;
    }

    /**
     * Open the dialog.
     */
    public void open() {
        this.createContents();
        this.shell.open();
        this.shell.layout();
        Display display = this.getParent().getDisplay();
        while (!this.shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        this.shell = new Shell(this.getParent(), this.getStyle());
        this.shell.setText("フィルター");
        GridLayout glShell = new GridLayout(1, false);
        glShell.verticalSpacing = 2;
        glShell.horizontalSpacing = 2;
        glShell.marginHeight = 2;
        glShell.marginWidth = 2;
        this.shell.setLayout(glShell);

        SelectionListener listener = new ApplyFilterSelectionAdapter();

        Composite composite = new Composite(this.shell, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout glComposite = new GridLayout(1, false);
        glComposite.verticalSpacing = 2;
        glComposite.horizontalSpacing = 2;
        glComposite.marginHeight = 2;
        glComposite.marginWidth = 2;
        composite.setLayout(glComposite);

        Group namegroup = new Group(composite, SWT.NONE);
        namegroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        namegroup.setLayout(new RowLayout());
        namegroup.setText("フリーワード検索(半角SPでAND検索)");

        this.nametext = new Text(namegroup, SWT.BORDER);
        this.nametext.setLayoutData(new RowData(180, SWT.DEFAULT));

        this.regexp = new Button(namegroup, SWT.CHECK);
        this.regexp.setText("正規表現");
        this.regexp.addSelectionListener(listener);

        Group shiptypegroup = new Group(composite, SWT.NONE);
        shiptypegroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout glShiptypegroup = new GridLayout(3, false);
        glShiptypegroup.verticalSpacing = 2;
        glShiptypegroup.horizontalSpacing = 2;
        glShiptypegroup.marginHeight = 2;
        glShiptypegroup.marginWidth = 2;
        shiptypegroup.setLayout(glShiptypegroup);
        shiptypegroup.setText("艦種");

        this.destroyer = new Button(shiptypegroup, SWT.CHECK);
        this.destroyer.setText("駆逐艦");
        this.destroyer.setSelection(true);
        this.destroyer.addSelectionListener(listener);

        this.lightCruiser = new Button(shiptypegroup, SWT.CHECK);
        this.lightCruiser.setText("軽巡洋艦");
        this.lightCruiser.setSelection(true);
        this.lightCruiser.addSelectionListener(listener);

        this.torpedoCruiser = new Button(shiptypegroup, SWT.CHECK);
        this.torpedoCruiser.setText("重雷装巡洋艦");
        this.torpedoCruiser.setSelection(true);
        this.torpedoCruiser.addSelectionListener(listener);

        this.heavyCruiser = new Button(shiptypegroup, SWT.CHECK);
        this.heavyCruiser.setText("重巡洋艦");
        this.heavyCruiser.setSelection(true);
        this.heavyCruiser.addSelectionListener(listener);

        this.flyingDeckCruiser = new Button(shiptypegroup, SWT.CHECK);
        this.flyingDeckCruiser.setText("航空巡洋艦");
        this.flyingDeckCruiser.setSelection(true);
        this.flyingDeckCruiser.addSelectionListener(listener);

        this.seaplaneTender = new Button(shiptypegroup, SWT.CHECK);
        this.seaplaneTender.setText("水上機母艦");
        this.seaplaneTender.setSelection(true);
        this.seaplaneTender.addSelectionListener(listener);

        this.escortCarrier = new Button(shiptypegroup, SWT.CHECK);
        this.escortCarrier.setText("軽空母");
        this.escortCarrier.setSelection(true);
        this.escortCarrier.addSelectionListener(listener);

        this.carrier = new Button(shiptypegroup, SWT.CHECK);
        this.carrier.setText("正規空母");
        this.carrier.setSelection(true);
        this.carrier.addSelectionListener(listener);

        this.battleship = new Button(shiptypegroup, SWT.CHECK);
        this.battleship.setText("戦艦");
        this.battleship.setSelection(true);
        this.battleship.addSelectionListener(listener);

        this.flyingDeckBattleship = new Button(shiptypegroup, SWT.CHECK);
        this.flyingDeckBattleship.setText("航空戦艦");
        this.flyingDeckBattleship.setSelection(true);
        this.flyingDeckBattleship.addSelectionListener(listener);

        this.submarine = new Button(shiptypegroup, SWT.CHECK);
        this.submarine.setText("潜水艦");
        this.submarine.setSelection(true);
        this.submarine.addSelectionListener(listener);

        this.carrierSubmarine = new Button(shiptypegroup, SWT.CHECK);
        this.carrierSubmarine.setText("潜水空母");
        this.carrierSubmarine.setSelection(true);
        this.carrierSubmarine.addSelectionListener(listener);

        this.landingship = new Button(shiptypegroup, SWT.CHECK);
        this.landingship.setText("揚陸艦");
        this.landingship.setSelection(true);
        this.landingship.addSelectionListener(listener);

        this.armoredcarrier = new Button(shiptypegroup, SWT.CHECK);
        this.armoredcarrier.setText("装甲空母");
        this.armoredcarrier.setSelection(true);
        this.armoredcarrier.addSelectionListener(listener);

        this.repairship = new Button(shiptypegroup, SWT.CHECK);
        this.repairship.setText("工作艦");
        this.repairship.setSelection(true);
        this.repairship.addSelectionListener(listener);

        this.submarineTender = new Button(shiptypegroup, SWT.CHECK);
        this.submarineTender.setText("潜水母艦");
        this.submarineTender.setSelection(true);
        this.submarineTender.addSelectionListener(listener);

        this.trainingShip = new Button(shiptypegroup, SWT.CHECK);
        this.trainingShip.setText("練習巡洋艦");
        this.trainingShip.setSelection(true);
        this.trainingShip.addSelectionListener(listener);

        this.supply = new Button(shiptypegroup, SWT.CHECK);
        this.supply.setText("補給艦");
        this.supply.setSelection(true);
        this.supply.addSelectionListener(listener);

        this.escort = new Button(shiptypegroup, SWT.CHECK);
        this.escort.setText("海防艦");
        this.escort.setSelection(true);
        this.escort.addSelectionListener(listener);

        new Label(shiptypegroup, SWT.NONE);

        new Label(shiptypegroup, SWT.NONE);

        this.selectall = new Button(shiptypegroup, SWT.CHECK);
        this.selectall.setText("全て選択");
        this.selectall.setSelection(true);
        this.selectall.addSelectionListener(new SelectAllSelectionAdapter());

        Group etcgroup = new Group(composite, SWT.NONE);
        etcgroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout glEtcgroup = new GridLayout(2, false);
        glEtcgroup.horizontalSpacing = 2;
        glEtcgroup.marginHeight = 2;
        glEtcgroup.marginWidth = 2;
        etcgroup.setLayout(glEtcgroup);
        etcgroup.setText("その他");

        this.group = new Button(etcgroup, SWT.CHECK);
        this.group.setText("グループ");
        this.group.setSelection(false);
        this.group.addSelectionListener(listener);

        this.groupcombo = new Combo(etcgroup, SWT.READ_ONLY);
        this.groupcombo.setEnabled(false);
        this.groupcombo.addSelectionListener(listener);
        for (ShipGroupBean groupBean : this.groups) {
            this.groupcombo.add(groupBean.getName());
        }
        this.group.addSelectionListener(new CheckAdapter(this.group, this.groupcombo));

        this.area = new Button(etcgroup, SWT.CHECK);
        this.area.setText("出撃海域");
        this.area.setSelection(false);
        this.area.addSelectionListener(listener);

        this.areacombo = new Combo(etcgroup, SWT.READ_ONLY);
        this.areacombo.setEnabled(false);
        this.areacombo.addSelectionListener(listener);
        for (SallyArea entry : SallyArea.values()) {
            String n = entry.getName();
            if (StringUtils.isBlank(n))
                n = "(なし)";
            this.areacombo.add(n);
        }
        this.area.addSelectionListener(new CheckAdapter(this.area, this.areacombo));

        this.item = new Button(etcgroup, SWT.CHECK);
        this.item.setText("装備");
        this.item.setSelection(false);
        this.item.addSelectionListener(listener);

        this.itemcombo = new Combo(etcgroup, SWT.READ_ONLY);
        this.itemcombo.setEnabled(false);
        this.itemcombo.addSelectionListener(listener);
        Set<String> items = new TreeSet<String>();
        for (ItemDto entry : ItemContext.get().values()) {
            items.add(entry.getName());
        }
        for (String name : items) {
            this.itemcombo.add(name);
        }
        this.item.addSelectionListener(new CheckAdapter(this.item, this.itemcombo));

        this.onfleet = new Button(etcgroup, SWT.CHECK);
        this.onfleet.setText("艦隊に所属");
        this.onfleet.setSelection(true);
        this.onfleet.addSelectionListener(listener);

        this.notonfleet = new Button(etcgroup, SWT.CHECK);
        this.notonfleet.setText("艦隊に所属していない");
        this.notonfleet.setSelection(true);
        this.notonfleet.addSelectionListener(listener);

        this.locked = new Button(etcgroup, SWT.CHECK);
        this.locked.setText("鍵付き");
        this.locked.setSelection(true);
        this.locked.addSelectionListener(listener);

        this.notlocked = new Button(etcgroup, SWT.CHECK);
        this.notlocked.setText("鍵付きではない");
        this.notlocked.setSelection(true);
        this.notlocked.addSelectionListener(listener);

        // 初期値を復元する
        ShipFilterDto filter = this.supplier.get();
        if (filter != null) {
            // 名前
            if (!StringUtils.isEmpty(filter.nametext)) {
                this.nametext.setText(filter.nametext);
            }
            // 名前.正規表現を使用する
            this.regexp.setSelection(filter.regexp);

            // 艦種.駆逐艦
            setTypeSelection(filter, this.destroyer);
            // 艦種.軽巡洋艦
            setTypeSelection(filter, this.lightCruiser);
            // 艦種.重雷装巡洋艦
            setTypeSelection(filter, this.torpedoCruiser);
            // 艦種.重巡洋艦
            setTypeSelection(filter, this.heavyCruiser);
            // 艦種.航空巡洋艦
            setTypeSelection(filter, this.flyingDeckCruiser);
            // 艦種.水上機母艦
            setTypeSelection(filter, this.seaplaneTender);
            // 艦種.軽空母
            setTypeSelection(filter, this.escortCarrier);
            // 艦種.正規空母
            setTypeSelection(filter, this.carrier);
            // 艦種.戦艦
            setTypeSelection(filter, this.battleship);
            // 艦種.航空戦艦
            setTypeSelection(filter, this.flyingDeckBattleship);
            // 艦種.潜水艦
            setTypeSelection(filter, this.submarine);
            // 艦種.潜水空母
            setTypeSelection(filter, this.carrierSubmarine);
            // 艦種.揚陸艦
            setTypeSelection(filter, this.landingship);
            // 艦種.装甲空母
            setTypeSelection(filter, this.armoredcarrier);
            // 艦種.工作艦
            setTypeSelection(filter, this.repairship);
            // 艦種.潜水母艦
            setTypeSelection(filter, this.submarineTender);
            // 艦種.練習巡洋艦
            setTypeSelection(filter, this.trainingShip);
            // 艦種.補給艦
            setTypeSelection(filter, this.supply);
            // 艦種.海防艦
            setTypeSelection(filter, this.escort);

            if (filter.group != null) {
                // グループ
                int idx = this.groups.indexOf(filter.group);
                if (idx != -1) {
                    this.group.setSelection(true);
                    this.groupcombo.setEnabled(true);
                    this.groupcombo.select(idx);
                }
            }
            if (filter.area != null) {
                // 出撃海域
                String n = filter.area.getName();
                if (StringUtils.isBlank(n))
                    n = "(なし)";
                int idx = this.areacombo.indexOf(n);
                if (idx != -1) {
                    this.area.setSelection(true);
                    this.areacombo.setEnabled(true);
                    this.areacombo.select(idx);
                }
            }
            if (!StringUtils.isEmpty(filter.itemname)) {
                // 装備
                this.item.setSelection(true);
                this.itemcombo.setEnabled(true);
                int index = 0;
                for (String name : items) {
                    if (filter.itemname.equals(name)) {
                        this.itemcombo.select(index);
                        break;
                    }
                    index++;
                }
            }
            // 艦隊に所属
            this.onfleet.setSelection(filter.onfleet);
            // 艦隊に非所属
            this.notonfleet.setSelection(filter.notonfleet);
            // 鍵付き
            this.locked.setSelection(filter.locked);
            // 鍵付きではない
            this.notlocked.setSelection(filter.notlocked);
        }
        this.nametext.addModifyListener(new ApplyFilterModifyAdapter());

        this.shell.pack();
    }

    /**
     * フィルターを構成する
     *
     * @return フィルター
     */
    private ShipFilterDto createFilter() {
        ShipFilterDto filter = this.supplier.get();
        filter.nametext = this.nametext.getText();
        filter.regexp = this.regexp.getSelection();
        // 艦種.駆逐艦
        setTypeFilter(this.destroyer, filter);
        // 艦種.軽巡洋艦
        setTypeFilter(this.lightCruiser, filter);
        // 艦種.重雷装巡洋艦
        setTypeFilter(this.torpedoCruiser, filter);
        // 艦種.重巡洋艦
        setTypeFilter(this.heavyCruiser, filter);
        // 艦種.重巡洋艦
        setTypeFilter(this.flyingDeckCruiser, filter);
        // 艦種.水上機母艦
        setTypeFilter(this.seaplaneTender, filter);
        // 艦種.軽空母
        setTypeFilter(this.escortCarrier, filter);
        // 艦種.正規空母
        setTypeFilter(this.carrier, filter);
        // 艦種.戦艦
        setTypeFilter(this.battleship, filter);
        // 艦種.航空戦艦
        setTypeFilter(this.flyingDeckBattleship, filter);
        // 艦種.潜水艦
        setTypeFilter(this.submarine, filter);
        // 艦種.潜水空母
        setTypeFilter(this.carrierSubmarine, filter);
        // 艦種.揚陸艦
        setTypeFilter(this.landingship, filter);
        // 艦種.装甲空母
        setTypeFilter(this.armoredcarrier, filter);
        // 艦種.工作艦
        setTypeFilter(this.repairship, filter);
        // 艦種.潜水母艦
        setTypeFilter(this.submarineTender, filter);
        // 艦種.練習巡洋艦
        setTypeFilter(this.trainingShip, filter);
        // 艦種.補給艦
        setTypeFilter(this.supply, filter);
        // 艦種.海防艦
        setTypeFilter(this.escort, filter);
        filter.group = null;
        if (ShipFilterDialog.this.group.getSelection()) {
            int idx = ShipFilterDialog.this.groupcombo.getSelectionIndex();
            if ((idx >= 0) && (idx < this.groups.size())) {
                filter.group = this.groups.get(idx);
            }
        }
        filter.area = null;
        if (ShipFilterDialog.this.area.getSelection()) {
            int idx = ShipFilterDialog.this.areacombo.getSelectionIndex();
            if ((idx >= 0) && (idx < SallyArea.values().length)) {
                filter.area = SallyArea.values()[idx];
            }
        }
        if (ShipFilterDialog.this.item.getSelection()) {
            if (ShipFilterDialog.this.itemcombo.getSelectionIndex() >= 0) {
                filter.itemname = this.itemcombo.getItem(ShipFilterDialog.this.itemcombo
                        .getSelectionIndex());
            }
        } else {
            filter.itemname = null;
        }
        filter.onfleet = this.onfleet.getSelection();
        filter.notonfleet = this.notonfleet.getSelection();
        filter.locked = this.locked.getSelection();
        filter.notlocked = this.notlocked.getSelection();

        return filter;
    }

    /**
     * 選択した時にコンボボックスを制御する
     */
    private final class CheckAdapter extends SelectionAdapter {

        private final Button button;
        private final Composite composite;

        public CheckAdapter(Button button, Composite composite) {
            this.button = button;
            this.composite = composite;
        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            this.composite.setEnabled(this.button.getSelection());
        }
    }

    /**
     * フィルターを適用する
     */
    private final class SelectAllSelectionAdapter extends SelectionAdapter {
        @Override
        public void widgetSelected(SelectionEvent e) {
            boolean select = ShipFilterDialog.this.selectall.getSelection();

            ShipFilterDialog.this.destroyer.setSelection(select);
            ShipFilterDialog.this.lightCruiser.setSelection(select);
            ShipFilterDialog.this.torpedoCruiser.setSelection(select);
            ShipFilterDialog.this.heavyCruiser.setSelection(select);
            ShipFilterDialog.this.flyingDeckCruiser.setSelection(select);
            ShipFilterDialog.this.seaplaneTender.setSelection(select);
            ShipFilterDialog.this.escortCarrier.setSelection(select);
            ShipFilterDialog.this.carrier.setSelection(select);
            ShipFilterDialog.this.battleship.setSelection(select);
            ShipFilterDialog.this.flyingDeckBattleship.setSelection(select);
            ShipFilterDialog.this.submarine.setSelection(select);
            ShipFilterDialog.this.carrierSubmarine.setSelection(select);
            ShipFilterDialog.this.landingship.setSelection(select);
            ShipFilterDialog.this.armoredcarrier.setSelection(select);
            ShipFilterDialog.this.repairship.setSelection(select);
            ShipFilterDialog.this.submarineTender.setSelection(select);
            ShipFilterDialog.this.trainingShip.setSelection(select);
            ShipFilterDialog.this.supply.setSelection(select);
            ShipFilterDialog.this.escort.setSelection(select);

            ShipFilterDialog.this.update.accept(ShipFilterDialog.this.createFilter());
        }
    }

    /**
     * フィルターを適用する
     */
    private final class ApplyFilterModifyAdapter implements ModifyListener {
        @Override
        public void modifyText(ModifyEvent e) {
            ShipFilterDialog.this.update.accept(ShipFilterDialog.this.createFilter());
        }
    }

    /**
     * フィルターを適用する
     */
    private final class ApplyFilterSelectionAdapter extends SelectionAdapter {
        @Override
        public void widgetSelected(SelectionEvent e) {
            ShipFilterDialog.this.update.accept(ShipFilterDialog.this.createFilter());
        }
    }

    /**
     * フィルターから艦種のチェックボックスを設定する
     *
     * @param checkbox 艦種のチェックボックス
     * @param dto フィルター
     */
    private static void setTypeSelection(ShipFilterDto dto, Button checkbox) {
        String type = checkbox.getText();
        checkbox.setSelection(dto.shipType.contains(type));
    }

    /**
     * 艦種のチェックボックスからフィルターを設定する
     *
     * @param checkbox 艦種のチェックボックス
     * @param dto フィルター
     */
    private static void setTypeFilter(Button checkbox, ShipFilterDto dto) {
        String type = checkbox.getText();
        if (checkbox.getSelection()) {
            dto.shipType.add(type);
        } else {
            dto.shipType.remove(type);
        }
    }
}
