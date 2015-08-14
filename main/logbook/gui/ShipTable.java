package logbook.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import logbook.config.ShipGroupConfig;
import logbook.config.bean.ShipGroupBean;
import logbook.constants.AppConstants;
import logbook.data.context.GlobalContext;
import logbook.data.context.ShipContext;
import logbook.dto.DeckMissionDto;
import logbook.dto.NdockDto;
import logbook.dto.ShipDto;
import logbook.dto.ShipFilterDto;
import logbook.gui.logic.CreateReportLogic;
import logbook.gui.logic.TableItemCreator;
import logbook.internal.ExpTable;
import logbook.util.SwtUtils;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * 所有艦娘一覧テーブル
 *
 */
public final class ShipTable extends AbstractTableDialog {

    private static final int GAUGE_WIDTH = 20;

    private static final int GAUGE_HEIGHT = 12;

    /** 成長余地 */
    private boolean specdiff = false;

    /** フィルター */
    private ShipFilterDto filter = new ShipFilterDto();

    /** テーブルの行を作成する */
    private final TableItemCreator creator = new ShipTableItemCreator();

    /** HPゲージのキャッシュ */
    private final Map<Integer, Image> cacheHpGauge = new HashMap<>();

    /** 経験値ゲージのキャッシュ */
    private final Map<Integer, Image> cacheExpGauge = new HashMap<>();

    /** 経験値ゲージのキャッシュ */
    private final Map<Integer, Image> cacheTotalExpGauge = new HashMap<>();

    /**
     * @param parent
     */
    public ShipTable(Shell parent) {
        super(parent);
    }

    /**
     * @param parent
     * @param filter
     */
    public ShipTable(Shell parent, ShipFilterDto filter) {
        super(parent);
        this.filter = filter;
    }

    /**
     * フィルターを設定する
     * @param filter フィルター
     */
    public void updateFilter(ShipFilterDto filter) {
        this.filter = filter;
        this.reloadTable();
        this.shell.setText(this.getTitle());
    }

    @Override
    protected void createContents() {
        // キャッシュの削除
        this.shell.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                for (Map.Entry<?, Image> entry : ShipTable.this.cacheHpGauge.entrySet()) {
                    entry.getValue().dispose();
                }
                for (Map.Entry<?, Image> entry : ShipTable.this.cacheExpGauge.entrySet()) {
                    entry.getValue().dispose();
                }
                for (Map.Entry<?, Image> entry : ShipTable.this.cacheTotalExpGauge.entrySet()) {
                    entry.getValue().dispose();
                }
            }
        });
        // メニューバーに追加する
        // フィルターメニュー
        final MenuItem filter = new MenuItem(this.opemenu, SWT.PUSH);
        filter.setText("フィルター(&F)\tCtrl+F");
        filter.setAccelerator(SWT.CTRL + 'F');
        filter.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                new ShipFilterDialog(ShipTable.this.shell, ShipTable.this, ShipTable.this.filter).open();
            }
        });
        // セパレータ
        new MenuItem(this.opemenu, SWT.SEPARATOR);
        // 成長の余地を表示メニュー
        final MenuItem switchdiff = new MenuItem(this.opemenu, SWT.CHECK);
        switchdiff.setText("成長の余地を表示");
        switchdiff.setSelection(this.specdiff);
        switchdiff.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                ShipTable.this.specdiff = switchdiff.getSelection();
                ShipTable.this.reloadTable();
            }
        });
        // セパレータ
        new MenuItem(this.tablemenu, SWT.SEPARATOR);
        // 右クリックメニューに追加する
        final MenuItem filtertable = new MenuItem(this.tablemenu, SWT.NONE);
        filtertable.setText("フィルター(&F)");
        filtertable.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                new ShipFilterDialog(ShipTable.this.shell, ShipTable.this, ShipTable.this.filter).open();
            }
        });

        List<ShipGroupBean> groups = ShipGroupConfig.get().getGroup();

        MenuItem groupFilterCascade = new MenuItem(this.tablemenu, SWT.CASCADE);
        groupFilterCascade.setText("グループフィルター(&G)");
        Menu groupFilterMenu = new Menu(groupFilterCascade);
        groupFilterCascade.setMenu(groupFilterMenu);
        for (ShipGroupBean groupBean : groups) {
            final MenuItem groupItem = new MenuItem(groupFilterMenu, SWT.NONE);
            groupItem.setText(groupBean.getName());
            groupItem.setData(groupBean);
            groupItem.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    ShipGroupBean bean = (ShipGroupBean) e.widget.getData();
                    ShipFilterDto filter = ShipTable.this.getFilter();
                    filter.group = bean;
                    ShipTable.this.updateFilter(filter);
                }
            });
        }
        // セパレータ
        new MenuItem(this.tablemenu, SWT.SEPARATOR);

        MenuItem addGroupCascade = new MenuItem(this.tablemenu, SWT.CASCADE);
        addGroupCascade.setText("選択した艦娘をグループに追加(&A)");
        Menu addGroupMenu = new Menu(addGroupCascade);
        addGroupCascade.setMenu(addGroupMenu);
        for (ShipGroupBean groupBean : groups) {
            final MenuItem groupItem = new MenuItem(addGroupMenu, SWT.NONE);
            groupItem.setText(groupBean.getName());
            groupItem.setData(groupBean);
            groupItem.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    TableItem[] tableItems = ShipTable.this.table.getSelection();
                    if (tableItems.length > 0) {
                        List<ShipDto> ships = new ArrayList<>();
                        List<String> name = new ArrayList<>();
                        for (int i = 0; i < tableItems.length; i++) {
                            long id = Long.parseLong(tableItems[i].getText(1));
                            ShipDto ship = ShipContext.get().get(id);
                            if (ship != null) {
                                ships.add(ship);
                                name.add(ship.getName());
                            }
                        }
                        MessageBox box = new MessageBox(ShipTable.this.shell, SWT.YES | SWT.NO
                                | SWT.ICON_QUESTION);
                        box.setText("選択した艦娘をグループに追加");
                        box.setMessage("「" + StringUtils.join(name, ",") + "」をグループに追加しますか？");

                        if (box.open() == SWT.YES) {
                            ShipGroupBean bean = (ShipGroupBean) e.widget.getData();
                            for (ShipDto ship : ships) {
                                bean.getShips().add(ship.getId());
                            }
                        }
                    }
                }
            });
        }

        MenuItem removeGroupCascade = new MenuItem(this.tablemenu, SWT.CASCADE);
        removeGroupCascade.setText("選択した艦娘をグループから除去(&D)");
        Menu removeGroupMenu = new Menu(removeGroupCascade);
        removeGroupCascade.setMenu(removeGroupMenu);
        for (ShipGroupBean groupBean : groups) {
            final MenuItem groupItem = new MenuItem(removeGroupMenu, SWT.NONE);
            groupItem.setText(groupBean.getName());
            groupItem.setData(groupBean);
            groupItem.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    TableItem[] tableItems = ShipTable.this.table.getSelection();
                    if (tableItems.length > 0) {
                        List<ShipDto> ships = new ArrayList<>();
                        List<String> name = new ArrayList<>();
                        for (int i = 0; i < tableItems.length; i++) {
                            long id = Long.parseLong(tableItems[i].getText(1));
                            ShipDto ship = ShipContext.get().get(id);
                            if (ship != null) {
                                ships.add(ship);
                                name.add(ship.getName());
                            }
                        }
                        MessageBox box = new MessageBox(ShipTable.this.shell, SWT.YES | SWT.NO
                                | SWT.ICON_QUESTION);
                        box.setText("選択した艦娘をグループから除去");
                        box.setMessage("「" + StringUtils.join(name, ",") + "」をグループから除去しますか？");

                        if (box.open() == SWT.YES) {
                            ShipGroupBean bean = (ShipGroupBean) e.widget.getData();
                            for (ShipDto ship : ships) {
                                bean.getShips().remove(ship.getId());
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    protected String getTitle() {
        if ((this.filter != null) && (this.filter.group != null)) {
            return "所有艦娘一覧 (" + this.filter.group.getName() + ")";
        }
        return "所有艦娘一覧";
    }

    @Override
    protected Point getSize() {
        return new Point(600, 350);
    }

    @Override
    protected String[] getTableHeader() {
        return CreateReportLogic.getShipListHeader();
    }

    @Override
    protected void updateTableBody() {
        this.body = CreateReportLogic.getShipListBody(this.specdiff, this.filter);
    }

    @Override
    protected TableItemCreator getTableItemCreator() {
        return this.creator;
    }

    @Override
    protected SelectionListener getHeaderSelectionListener() {
        return new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (e.getSource() instanceof TableColumn) {
                    ShipTable.this.sortTableItems((TableColumn) e.getSource());
                }
            }
        };
    }

    /**
     * フィルターを取得します。
     * @return フィルター
     */
    public ShipFilterDto getFilter() {
        return this.filter;
    }

    /**
     * HPのゲージイメージを作成します
     *
     * @param ship 艦娘
     * @return ゲージイメージ
     */
    private Image hpGauge(ShipDto ship) {
        // 割合
        float ratio = (float) ship.getNowhp() / (float) ship.getMaxhp();
        // キャッシュのキー (実線の幅)
        Integer key = (int) (ratio * GAUGE_WIDTH);
        Image gauge = this.cacheHpGauge.get(key);
        if (gauge == null) {
            RGB background = this.table.getBackground().getRGB();
            gauge = SwtUtils.gaugeImage(ratio, GAUGE_WIDTH, GAUGE_HEIGHT,
                    background,
                    AppConstants.HP_EMPTY_COLOR, AppConstants.HP_HALF_COLOR, AppConstants.HP_FULL_COLOR);

            this.cacheHpGauge.put(key, gauge);
        }
        return gauge;
    }

    /**
     * 次のLvまでの経験値のゲージイメージを作成します
     *
     * @param ship 艦娘
     * @return ゲージイメージ
     */
    private Image expGauge(ShipDto ship) {
        // 割合
        float ratio = ship.getExpraito();
        // キャッシュのキー (実線の幅)
        Integer key = (int) (ratio * GAUGE_WIDTH);
        Image gauge = this.cacheExpGauge.get(key);
        if (gauge == null) {
            RGB background = this.table.getBackground().getRGB();
            gauge = SwtUtils.gaugeImage(ratio, GAUGE_WIDTH, GAUGE_HEIGHT,
                    background,
                    AppConstants.EXP_COLOR);
            ShipTable.this.cacheExpGauge.put(key, gauge);
        }
        return gauge;
    }

    /**
     * 経験値のゲージイメージを作成します
     *
     * @param ship 艦娘
     * @return ゲージイメージ
     */
    private Image totalExpGauge(ShipDto ship) {
        // Max経験値の基準Lv
        int targetLv = ship.getLv() > 100 ? 150 : 100;
        long maxExp = ExpTable.get().get(targetLv);
        // 割合
        float ratio = (float) ship.getExp() / (float) maxExp;
        // キャッシュのキー (実線の幅) + Max経験値の基準Lv
        Integer key = (int) (ratio * GAUGE_WIDTH) + targetLv;
        Image gauge = this.cacheTotalExpGauge.get(key);
        if (gauge == null) {
            // ゲージの色を100以上なら緑、99以下なら青にする
            RGB color = ship.getLv() > 100 ? AppConstants.HP_FULL_COLOR : AppConstants.EXP_COLOR;
            RGB background = this.table.getBackground().getRGB();
            gauge = SwtUtils.gaugeImage(ratio, GAUGE_WIDTH, GAUGE_HEIGHT,
                    background,
                    color);
            ShipTable.this.cacheTotalExpGauge.put(key, gauge);
        }
        return gauge;
    }

    /**
     * テーブルの行を作成する
     *
     */
    private final class ShipTableItemCreator implements TableItemCreator {

        private Set<Long> deckmissions;

        private Set<Long> docks;

        @Override
        public void init() {
            // 遠征
            this.deckmissions = new HashSet<Long>();
            for (DeckMissionDto deckMission : GlobalContext.getDeckMissions()) {
                if ((deckMission.getMission() != null) && (deckMission.getShips() != null)) {
                    this.deckmissions.addAll(deckMission.getShips());
                }
            }
            // 入渠
            this.docks = new HashSet<Long>();
            for (NdockDto ndock : GlobalContext.getNdocks()) {
                if (ndock.getNdockid() != 0) {
                    this.docks.add(ndock.getNdockid());
                }
            }
        }

        @Override
        public TableItem create(Table table, String[] text, int count) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(text);
            this.update(item, text, count);
            return item;
        }

        /* (非 Javadoc)
         * @see logbook.gui.logic.TableItemCreator#update(org.eclipse.swt.widgets.TableItem, java.lang.String[], int)
         */
        @Override
        public TableItem update(TableItem item, String[] text, int count) {

            // 艦娘
            Long id = Long.valueOf(text[1]);
            ShipDto ship = ShipContext.get().get(id);

            // 偶数行に背景色を付ける
            if ((count % 2) != 0) {
                item.setBackground(SWTResourceManager.getColor(AppConstants.ROW_BACKGROUND));
            } else {
                item.setBackground(null);
            }
            if (ship != null) {
                // 疲労
                int cond = (int) ship.getCond();

                if (!ship.getLocked()) {
                    // 鍵付きでは無い艦娘をグレー色にする
                    item.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
                } else if (this.docks.contains(id)) {
                    // 入渠
                    item.setForeground(SWTResourceManager.getColor(AppConstants.NDOCK_COLOR));
                } else if (this.deckmissions.contains(id)) {
                    // 遠征
                    item.setForeground(SWTResourceManager.getColor(AppConstants.MISSION_COLOR));
                } else if (cond <= AppConstants.COND_RED) {
                    // 赤疲労
                    item.setForeground(SWTResourceManager.getColor(AppConstants.COND_RED_COLOR));
                } else if (cond <= AppConstants.COND_ORANGE) {
                    // 疲労
                    item.setForeground(SWTResourceManager.getColor(AppConstants.COND_ORANGE_COLOR));
                } else if ((cond >= AppConstants.COND_DARK_GREEN) && (cond < AppConstants.COND_GREEN)) {
                    // cond.50-52
                    item.setForeground(SWTResourceManager.getColor(AppConstants.COND_DARK_GREEN_COLOR));
                } else if (cond >= AppConstants.COND_GREEN) {
                    // cond.53-
                    item.setForeground(SWTResourceManager.getColor(AppConstants.COND_GREEN_COLOR));
                } else {
                    item.setForeground(null);
                }

                // HPのゲージイメージ
                item.setImage(17, ShipTable.this.hpGauge(ship));
                // 次のLvまでの経験値のゲージ
                item.setImage(8, ShipTable.this.expGauge(ship));
                // 経験値のゲージイメージ
                item.setImage(9, ShipTable.this.totalExpGauge(ship));
            }
            return item;
        }
    }
}
