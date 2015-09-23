package logbook.gui;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import logbook.config.ShipGroupConfig;
import logbook.config.bean.ShipGroupBean;
import logbook.constants.AppConstants;
import logbook.data.context.GlobalContext;
import logbook.data.context.ShipContext;
import logbook.dto.DeckMissionDto;
import logbook.dto.NdockDto;
import logbook.dto.ShipDto;
import logbook.dto.ShipFilterDto;
import logbook.gui.bean.ShipBean;
import logbook.gui.listener.SelectedListener;
import logbook.gui.logic.ShipFilterLogic;
import logbook.gui.logic.TableItemDecorator;
import logbook.gui.logic.TableWrapper;
import logbook.internal.ExpTable;
import logbook.util.SwtUtils;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * 所有艦娘一覧テーブル
 *
 */
public final class ShipTable extends AbstractTableDialogEx<ShipBean> {

    private static final int GAUGE_WIDTH = 20;

    private static final int GAUGE_HEIGHT = 12;

    private CTabFolder tabFolder;

    /** フィルター */
    private final Map<Integer, ShipFilterDto> filters = new HashMap<>();

    /** グループ */
    private final List<ShipGroupBean> groups = ShipGroupConfig.get().getGroup();

    /** HPゲージのキャッシュ */
    private final Map<Integer, Image> cache = new HashMap<>();

    /**
     * @param parent
     * @param filter
     */
    public ShipTable(Shell parent, ShipFilterDto filter) {
        super(parent, ShipBean.class);
        this.filters.put(0, filter);
    }

    /**
     * @param parent
     */
    public ShipTable(Shell parent) {
        this(parent, new ShipFilterDto());
    }

    @Override
    protected String getTitle() {
        return "所有艦娘一覧";
    }

    @Override
    protected void createContents() {
        // メニューバーのセット
        this.setMenuBar();

        // イメージ破棄のリスナー
        this.shell.addDisposeListener(e -> this.cache.forEach((k, v) -> v.dispose()));

        // タブ
        this.tabFolder = new CTabFolder(this.shell, SWT.BORDER);
        this.tabFolder.setTabHeight(26);
        this.tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(
                SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

        // 全ての艦娘
        CTabItem tabItem = new CTabItem(this.tabFolder, SWT.NONE);
        tabItem.setFont(SWTResourceManager.getBoldFont(this.shell.getFont()));
        tabItem.setText("全ての艦娘");

        Composite composite = new Composite(this.tabFolder, SWT.NONE);
        tabItem.setControl(composite);
        composite.setLayout(new FillLayout(SWT.HORIZONTAL));

        TableWrapper<ShipBean> table = this.addTable(composite);
        table.setContentSupplier(ShipTable::getShipContent)
                .setFilter(new ShipFilterLogic(this.filters.get(0)))
                .setDecorator(new ShipTableItemCreator(table, this.cache))
                .reload()
                .update();
        // 右クリックメニューのセット
        this.setRightClickMenu(table, 0);

        // グループ毎のタブ
        for (int i = 0; i < this.groups.size(); i++) {
            ShipGroupBean group = this.groups.get(i);

            ShipFilterDto filter = new ShipFilterDto();
            filter.group = group;
            this.filters.put(i + 1, filter);

            CTabItem tabItemSub = new CTabItem(this.tabFolder, SWT.NONE);
            tabItemSub.setText(group.getName());

            Composite compositeSub = new Composite(this.tabFolder, SWT.NONE);
            tabItemSub.setControl(compositeSub);
            compositeSub.setLayout(new FillLayout(SWT.HORIZONTAL));

            table = this.addTable(compositeSub);
            table.setContentSupplier(ShipTable::getShipContent)
                    .setFilter(new ShipFilterLogic(filter))
                    .setDecorator(new ShipTableItemCreator(table, this.cache))
                    .reload()
                    .update();
            // 右クリックメニューのセット
            this.setRightClickMenu(table, i + 1);
        }
    }

    @Override
    protected TableWrapper<ShipBean> getSelectionTable() {
        return this.tables.get(this.tabFolder.getSelectionIndex());
    }

    Supplier<ShipFilterDto> getFilter(int index) {
        return () -> this.filters.get(index);
    }

    Consumer<ShipFilterDto> updateFilter(int index) {
        return filter -> {
            this.filters.put(index, filter);
            this.tables.get(index)
                    .setFilter(new ShipFilterLogic(filter))
                    .reload()
                    .update();
        };
    }

    /**
     * メニューバーにメニューを追加する
     */
    private void setMenuBar() {
        // メニューバーに追加する
        // フィルターメニュー
        final MenuItem filter = new MenuItem(this.opemenu, SWT.PUSH);
        filter.setText("フィルター(&F)\tCtrl+F");
        filter.setAccelerator(SWT.CTRL + 'F');
        filter.addSelectionListener((SelectedListener) (e) -> {
            int index = ShipTable.this.tabFolder.getSelectionIndex();
            new ShipFilterDialog(this.shell, this.updateFilter(index), this.getFilter(index)).open();
        });
    }

    /**
     * 右クリックメニューにメニューを追加する
     *
     * @param table テーブル
     * @param index テーブルのインデックス
     */
    private void setRightClickMenu(TableWrapper<ShipBean> table, int index) {
        // テーブルメニュー
        Menu menu = table.getTable().getMenu();

        // セパレータ
        new MenuItem(menu, SWT.SEPARATOR);
        // 右クリックメニューに追加する
        final MenuItem filtertable = new MenuItem(menu, SWT.NONE);
        filtertable.setText("フィルター(&F)");
        filtertable.addSelectionListener((SelectedListener) (e) -> {
            new ShipFilterDialog(this.shell, this.updateFilter(index), this.getFilter(index)).open();
        });
        // セパレータ
        new MenuItem(menu, SWT.SEPARATOR);

        MenuItem addGroupCascade = new MenuItem(menu, SWT.CASCADE);
        addGroupCascade.setText("選択した艦娘をグループに追加(&A)");
        Menu addGroupMenu = new Menu(addGroupCascade);
        addGroupCascade.setMenu(addGroupMenu);
        for (ShipGroupBean group : this.groups) {
            MenuItem groupItem = new MenuItem(addGroupMenu, SWT.NONE);
            groupItem.setText(group.getName());
            groupItem.addSelectionListener((SelectedListener) (e) -> {
                ShipBean[] ships = table.getSelection(ShipBean[]::new);
                if (ships.length > 0) {
                    List<String> name = Arrays.stream(ships).map(ShipBean::getName).collect(Collectors.toList());
                    MessageBox box = new MessageBox(this.shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
                    box.setText("選択した艦娘をグループに追加");
                    box.setMessage("「" + StringUtils.join(name, ",") + "」をグループ「" + group.getName() + "」に追加しますか？");
                    if (box.open() == SWT.YES) {
                        for (ShipBean ship : ships) {
                            group.getShips().add(ship.getId());
                        }
                    }
                }
            });
        }
        MenuItem removeGroupCascade = new MenuItem(menu, SWT.CASCADE);
        removeGroupCascade.setText("選択した艦娘をグループから除去(&D)");
        Menu removeGroupMenu = new Menu(removeGroupCascade);
        removeGroupCascade.setMenu(removeGroupMenu);
        for (ShipGroupBean group : this.groups) {
            MenuItem groupItem = new MenuItem(removeGroupMenu, SWT.NONE);
            groupItem.setText(group.getName());
            groupItem.addSelectionListener((SelectedListener) (e) -> {
                ShipBean[] ships = table.getSelection(ShipBean[]::new);
                if (ships.length > 0) {
                    List<String> name = Arrays.stream(ships).map(ShipBean::getName).collect(Collectors.toList());
                    MessageBox box = new MessageBox(this.shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
                    box.setText("選択した艦娘をグループから除去");
                    box.setMessage("「" + StringUtils.join(name, ",") + "」をグループ「" + group.getName() + "」から除去しますか？");
                    if (box.open() == SWT.YES) {
                        for (ShipBean ship : ships) {
                            group.getShips().remove(ship.getId());
                        }
                    }
                }
            });
        }
    }

    /**
     * HPのゲージイメージを作成します
     *
     * @param ship 艦娘
     * @return ゲージイメージ
     */
    private static Image hpGauge(ShipDto ship, Map<Integer, Image> cache) {
        // 割合
        float ratio = (float) ship.getNowhp() / (float) ship.getMaxhp();
        // キャッシュのキー (実線の幅)
        Integer key = cacheKey(0, (int) (ratio * GAUGE_WIDTH));
        Image gauge = cache.get(key);
        if (gauge == null) {
            RGB background = new RGB(255, 255, 255);
            gauge = SwtUtils.gaugeImage(ratio, GAUGE_WIDTH, GAUGE_HEIGHT,
                    background,
                    AppConstants.HP_EMPTY_COLOR, AppConstants.HP_HALF_COLOR, AppConstants.HP_FULL_COLOR);
            cache.put(key, gauge);
        }
        return gauge;
    }

    /**
     * 経験値のゲージイメージを作成します
     *
     * @param ship 艦娘
     * @return ゲージイメージ
     */
    private static Image totalExpGauge(ShipDto ship, Map<Integer, Image> cache) {
        // Max経験値の基準Lv
        int targetLv = ship.getLv() > 100 ? 150 : 100;
        long maxExp = ExpTable.get().get(targetLv);
        // 割合
        float ratio = (float) ship.getExp() / (float) maxExp;
        // キャッシュのキー
        Integer key = cacheKey(2, (int) (ratio * GAUGE_WIDTH), targetLv);
        Image gauge = cache.get(key);
        if (gauge == null) {
            // ゲージの色を100以上なら緑、99以下なら青にする
            RGB color = ship.getLv() > 100 ? AppConstants.HP_FULL_COLOR : AppConstants.EXP_COLOR;
            RGB background = new RGB(255, 255, 255);
            gauge = SwtUtils.gaugeImage(ratio, GAUGE_WIDTH, GAUGE_HEIGHT,
                    background,
                    color);
            cache.put(key, gauge);
        }
        return gauge;
    }

    /**
     * キャッシュのキー
     *
     * @param keys
     * @return
     */
    private static int cacheKey(int... keys) {
        return Arrays.hashCode(keys);
    }

    /**
     * 艦娘一覧の内容
     * @return 内容
     */
    private static Stream<ShipBean> getShipContent() {
        Function<ShipDto, ShipBean> mapper = d -> {
            ShipBean b = new ShipBean();
            b.setId(d.getId());
            b.setFleetid(d.getFleetid());
            b.setName(d.getName());
            b.setType(d.getType());
            b.setCond(d.getCond());
            b.setCondClearDate(d.getCondClearDateString());
            b.setLv(d.getLv());
            b.setNext(d.getNext());
            b.setExp(d.getExp());
            b.setSallyArea(d.getSallyArea().getName());
            b.setSeiku(d.getSeiku());
            b.setSlot1(d.getSlot().get(0));
            b.setSlot2(d.getSlot().get(1));
            b.setSlot3(d.getSlot().get(2));
            b.setSlot4(d.getSlot().get(3));
            b.setSlot6(d.getSlot().get(5));
            b.setHp(d.getMaxhp());
            b.setKaryoku(d.getKaryoku());
            b.setRaisou(d.getRaisou());
            b.setTaiku(d.getTaiku());
            b.setSoukou(d.getSoukou());
            b.setKaihi(d.getKaihi());
            b.setTaisen(d.getTaisen());
            b.setSakuteki(d.getSakuteki());
            b.setLucky(d.getLucky());
            b.setAccuracy(d.getAccuracy());
            b.setHougekiPower(d.getHougekiPower());
            b.setRaigekiPower(d.getRaigekiPower());
            b.setTaisenPower(d.getTaisenPower());
            b.setYasenPower(d.getYasenPower());
            b.setShip(d);
            return b;
        };
        return ShipContext.get().values()
                .stream()
                .map(mapper)
                .sorted(Comparator.comparing(ShipBean::getExp).reversed());
    }

    /**
     * テーブルの行を装飾する
     */
    private final class ShipTableItemCreator implements TableItemDecorator<ShipBean> {

        private final Set<Long> deckmissions;

        private final Set<Long> ndocks;

        private final Map<Integer, Image> cache;

        private final int indexHp;
        private final int indexExp;

        public ShipTableItemCreator(TableWrapper<ShipBean> table, Map<Integer, Image> cache) {
            // 遠征中の艦娘たち
            this.deckmissions = Stream.of(GlobalContext.getDeckMissions())
                    .filter(e -> (e.getMission() != null) && (e.getShips() != null))
                    .map(DeckMissionDto::getShips)
                    .collect(HashSet<Long>::new, Set<Long>::addAll, Set<Long>::addAll);
            // 入渠中の艦娘たち
            this.ndocks = Stream.of(GlobalContext.getNdocks())
                    .filter(e -> e.getNdockid() != 0)
                    .map(NdockDto::getNdockid)
                    .collect(Collectors.toSet());
            this.cache = cache;
            this.indexHp = table.getColumnIndex("HP") + 1;
            this.indexExp = table.getColumnIndex("経験値") + 1;
        }

        @Override
        public void update(TableItem item, ShipBean bean, int index) {
            // 偶数行に背景色を付ける
            if ((index % 2) != 0) {
                item.setBackground(SWTResourceManager.getColor(AppConstants.ROW_BACKGROUND));
            } else {
                item.setBackground(null);
            }
            ShipDto ship = bean.getShip();
            long cond = ship.getCond();

            if (!ship.getLocked()) {
                // 鍵付きでは無い艦娘をグレー色にする
                item.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
            } else if (this.ndocks.contains(ship.getId())) {
                // 入渠
                item.setForeground(SWTResourceManager.getColor(AppConstants.NDOCK_COLOR));
            } else if (this.deckmissions.contains(ship.getId())) {
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
            item.setImage(this.indexHp, ShipTable.hpGauge(ship, this.cache));
            // 経験値のゲージイメージ
            item.setImage(this.indexExp, ShipTable.totalExpGauge(ship, this.cache));
        }
    }
}
