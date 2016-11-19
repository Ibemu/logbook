package logbook.gui;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import logbook.config.AppConfig;
import logbook.constants.AppConstants;
import logbook.data.context.GlobalContext;
import logbook.dto.BattleDto;
import logbook.dto.BattleResultDto;
import logbook.dto.DockDto;
import logbook.dto.ShipInfoDto;
import logbook.gui.listener.SaveWindowLocationAdapter;
import logbook.gui.logic.LayoutLogic;
import logbook.internal.SortiePhase;
import logbook.thread.ThreadManager;

/**
 * 出撃詳細
 *
 */
public final class SortieDialog extends Dialog {
    private static final int MAXCHARA = 6;

    private Shell shell;
    private Display display;

    /** スケジューリングされた再読み込みタスク */
    protected ScheduledFuture<?> future;

    /** メニューバー */
    private Menu menubar;

    /** [操作]メニュー */
    private Menu opemenu;

    private Composite summary;

    private Composite summaryMap;
    private Composite summaryBattle;

    private CLabel lblMapId;
    private CLabel lblCellId;
    private CLabel lblBossArrive;
    private CLabel lblIntercept;
    private CLabel lblBattleCount;
    private CLabel lblDayNight;
    private CLabel lblRank;
    private CLabel lblDispSeiku;
    private CLabel lblFriendSearch;
    private CLabel lblEnemySearch;
    private CLabel lblFriendTouch;
    private CLabel lblEnemyTouch;
    private CLabel lblBossDamaged;

    private Label lblSeparator1;
    private Label lblSeparatorH;
    private Label lblSeparatorV;

    private DockComposite friend1;
    private DockComposite friend2;
    private DockComposite enemy1;
    private DockComposite enemy2;

    /**
     * Create the dialog.
     * @param parent
     */
    public SortieDialog(Shell parent) {
        super(parent, SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.RESIZE);
    }

    /**
     * Open the dialog.
     */
    public void open() {
        this.createContents();
        this.reload();
        this.shell.open();
        this.display = this.getParent().getDisplay();
        while (!this.shell.isDisposed()) {
            if (!this.display.readAndDispatch()) {
                this.display.sleep();
            }
        }
        // タスクがある場合キャンセル
        if (this.future != null) {
            this.future.cancel(false);
        }
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        // シェルを作成
        this.shell = new Shell(this.getParent(), this.getStyle());
        this.shell.setText("出撃報告");
        this.shell.setLayout(new GridLayout(2, false));
        // ウインドウ位置を復元
        LayoutLogic.applyWindowLocation(this.getClass(), this.shell);
        // 閉じた時にウインドウ位置を保存
        this.shell.addShellListener(new SaveWindowLocationAdapter(this.getClass()));

        //フォント取得
        FontData fontData = this.shell.getFont().getFontData()[0];
        String fontName = fontData.getName();
        int size = fontData.getHeight();

        // メニューバー
        this.menubar = new Menu(this.shell, SWT.BAR);
        this.shell.setMenuBar(this.menubar);
        MenuItem mi;
        // 操作
        MenuItem operoot = new MenuItem(this.menubar, SWT.CASCADE);
        operoot.setText("操作");
        this.opemenu = new Menu(operoot);
        operoot.setMenu(this.opemenu);
        // 操作-再読み込み
        mi = new MenuItem(this.opemenu, SWT.NONE);
        mi.setText("再読み込み(&R)\tF5");
        mi.setAccelerator(SWT.F5);
        mi.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                SortieDialog.this.reload();
            }
        });
        // 操作-定期的に再読み込み
        Boolean isCyclicReload = AppConfig.get().getCyclicReloadMap().get(this.getClass().getName());
        MenuItem cyclicReload = new MenuItem(this.opemenu, SWT.CHECK);
        cyclicReload.setText("定期的に再読み込み(&A)\tCtrl+F5");
        cyclicReload.setAccelerator(SWT.CTRL + SWT.F5);
        if ((isCyclicReload != null) && isCyclicReload.booleanValue()) {
            cyclicReload.setSelection(true);
        }
        CyclicReloadAdapter adapter = new CyclicReloadAdapter(cyclicReload);
        cyclicReload.addSelectionListener(adapter);
        adapter.setCyclicReload(cyclicReload);
        // セパレータ
        new MenuItem(this.opemenu, SWT.SEPARATOR);
        // 操作-横長表示
        MenuItem land = new MenuItem(this.opemenu, SWT.CHECK);
        land.setText("横長表示(&L)\tCtrl+L");
        land.setAccelerator(SWT.CTRL + 'L');
        if (AppConfig.get().isLandscapeLayout()) {
            land.setSelection(true);
        }
        land.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (land.getSelection()) {
                    SortieDialog.this.landscape();
                } else {
                    SortieDialog.this.portrait();
                }
            }
        });

        CLabel lblText;
        this.summary = new Composite(this.shell, SWT.NONE);
        this.summary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
        this.summary.setLayout(getGridLayout(1, false, 2));
        // マップ|マス|ボス|戦闘回数
        this.summaryMap = new Composite(this.summary, SWT.NONE);
        this.summaryMap.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
        this.summaryMap.setLayout(getGridLayout(4, false, 2, 0));

        lblText = new CLabel(this.summaryMap, SWT.BORDER);
        lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
        lblText.setAlignment(SWT.CENTER);
        lblText.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 2));
        lblText.setText("マップ");

        lblText = new CLabel(this.summaryMap, SWT.BORDER);
        lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
        lblText.setAlignment(SWT.CENTER);
        lblText.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 2));
        lblText.setText("マス");

        lblText = new CLabel(this.summaryMap, SWT.BORDER);
        lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
        lblText.setAlignment(SWT.CENTER);
        lblText.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 2));
        lblText.setText("ボス");

        lblText = new CLabel(this.summaryMap, SWT.BORDER);
        lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
        lblText.setAlignment(SWT.CENTER);
        lblText.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 2));
        lblText.setText("回数");

        this.lblMapId = new CLabel(this.summaryMap, SWT.BORDER);
        this.lblMapId.setAlignment(SWT.CENTER);
        this.lblMapId.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        this.lblCellId = new CLabel(this.summaryMap, SWT.BORDER);
        this.lblCellId.setAlignment(SWT.CENTER);
        this.lblCellId.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        this.lblBossArrive = new CLabel(this.summaryMap, SWT.BORDER);
        this.lblBossArrive.setAlignment(SWT.CENTER);
        this.lblBossArrive.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        this.lblBattleCount = new CLabel(this.summaryMap, SWT.BORDER);
        this.lblBattleCount.setAlignment(SWT.CENTER);
        this.lblBattleCount.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        //     |    |      |    |  索敵 |  触接 |
        // 対峙|昼夜|ランク|制空|味方|敵|味方|敵|ギミック
        this.summaryBattle = new Composite(this.summary, SWT.NONE);
        this.summaryBattle.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
        this.summaryBattle.setLayout(getGridLayout(9, false, 2, 0));

        lblText = new CLabel(this.summaryBattle, SWT.BORDER);
        lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
        lblText.setAlignment(SWT.CENTER);
        lblText.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 2));
        lblText.setText("対峙");

        lblText = new CLabel(this.summaryBattle, SWT.BORDER);
        lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
        lblText.setAlignment(SWT.CENTER);
        lblText.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 2));
        lblText.setText("昼夜");

        lblText = new CLabel(this.summaryBattle, SWT.BORDER);
        lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
        lblText.setAlignment(SWT.CENTER);
        lblText.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 2));
        lblText.setText("ランク");

        lblText = new CLabel(this.summaryBattle, SWT.BORDER);
        lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
        lblText.setAlignment(SWT.CENTER);
        lblText.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 2));
        lblText.setText("制空");

        lblText = new CLabel(this.summaryBattle, SWT.BORDER);
        lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
        lblText.setAlignment(SWT.CENTER);
        lblText.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 2, 1));
        lblText.setText("索敵");

        lblText = new CLabel(this.summaryBattle, SWT.BORDER);
        lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
        lblText.setAlignment(SWT.CENTER);
        lblText.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 2, 1));
        lblText.setText("触接");

        lblText = new CLabel(this.summaryBattle, SWT.BORDER);
        lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
        lblText.setAlignment(SWT.CENTER);
        lblText.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 2));
        lblText.setText("ギミック");

        lblText = new CLabel(this.summaryBattle, SWT.BORDER);
        lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
        lblText.setAlignment(SWT.CENTER);
        lblText.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
        lblText.setText("味方");

        lblText = new CLabel(this.summaryBattle, SWT.BORDER);
        lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
        lblText.setAlignment(SWT.CENTER);
        lblText.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
        lblText.setText("敵");

        lblText = new CLabel(this.summaryBattle, SWT.BORDER);
        lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
        lblText.setAlignment(SWT.CENTER);
        lblText.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
        lblText.setText("味方");

        lblText = new CLabel(this.summaryBattle, SWT.BORDER);
        lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
        lblText.setAlignment(SWT.CENTER);
        lblText.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
        lblText.setText("敵");

        this.lblIntercept = new CLabel(this.summaryBattle, SWT.BORDER);
        this.lblIntercept.setAlignment(SWT.CENTER);
        this.lblIntercept.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        this.lblDayNight = new CLabel(this.summaryBattle, SWT.BORDER);
        this.lblDayNight.setAlignment(SWT.CENTER);
        this.lblDayNight.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        this.lblRank = new CLabel(this.summaryBattle, SWT.BORDER);
        this.lblRank.setAlignment(SWT.CENTER);
        this.lblRank.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        this.lblDispSeiku = new CLabel(this.summaryBattle, SWT.BORDER);
        this.lblDispSeiku.setAlignment(SWT.CENTER);
        this.lblDispSeiku.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        this.lblFriendSearch = new CLabel(this.summaryBattle, SWT.BORDER);
        this.lblFriendSearch.setAlignment(SWT.CENTER);
        this.lblFriendSearch.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        this.lblEnemySearch = new CLabel(this.summaryBattle, SWT.BORDER);
        this.lblEnemySearch.setAlignment(SWT.CENTER);
        this.lblEnemySearch.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        this.lblFriendTouch = new CLabel(this.summaryBattle, SWT.BORDER);
        this.lblFriendTouch.setAlignment(SWT.CENTER);
        this.lblFriendTouch.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        this.lblEnemyTouch = new CLabel(this.summaryBattle, SWT.BORDER);
        this.lblEnemyTouch.setAlignment(SWT.CENTER);
        this.lblEnemyTouch.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        this.lblBossDamaged = new CLabel(this.summaryBattle, SWT.BORDER);
        this.lblBossDamaged.setAlignment(SWT.CENTER);
        this.lblBossDamaged.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        this.lblSeparator1 = new Label(this.shell, SWT.SEPARATOR | SWT.HORIZONTAL);
        this.lblSeparator1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

        this.friend1 = new DockComposite(this.shell, SWT.NONE, fontData);
        this.friend1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        this.friend2 = new DockComposite(this.shell, SWT.NONE, fontData);
        this.friend2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        this.lblSeparatorH = new Label(this.shell, SWT.SEPARATOR | SWT.HORIZONTAL);
        this.lblSeparatorH.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

        this.lblSeparatorV = new Label(this.shell, SWT.SEPARATOR | SWT.VERTICAL);
        setVisible(this.lblSeparatorV, false, new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

        this.enemy1 = new DockComposite(this.shell, SWT.NONE, fontData);
        this.enemy1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        this.enemy2 = new DockComposite(this.shell, SWT.NONE, fontData);
        this.enemy2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        // 閉じた時に設定を保存
        this.shell.addShellListener(new ShellAdapter() {
            @Override
            public void shellClosed(ShellEvent e) {
                AppConfig.get().getCyclicReloadMap()
                        .put(SortieDialog.this.getClass().getName(), cyclicReload.getSelection());
                AppConfig.get().setLandscapeLayout(land.getSelection());
            }
        });

        // 縦長・横長表示
        if (land.getSelection()) {
            SortieDialog.this.landscape();
        } else {
            SortieDialog.this.portrait();
        }
    }

    private void portrait() {
        this.shell.setLayout(new GridLayout(2, false));

        this.summary.setLayout(getGridLayout(1, false, 2));
        this.summary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

        this.lblSeparator1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

        this.friend1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        this.friend2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        setVisible(this.lblSeparatorH, true, new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        setVisible(this.lblSeparatorV, false, new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

        this.enemy1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        this.enemy1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        this.reload();
    }

    private void landscape() {
        this.shell.setLayout(new GridLayout(6, false));

        this.summary.setLayout(getGridLayout(2, false, 2));
        this.summary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 6, 1));

        this.lblSeparator1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));

        this.friend1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        this.friend2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        setVisible(this.lblSeparatorH, false, new GridData(SWT.CENTER, SWT.FILL, false, true, 1, 1));
        setVisible(this.lblSeparatorV, true, new GridData(SWT.CENTER, SWT.FILL, false, true, 1, 1));

        this.enemy1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        this.enemy1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        this.reload();
    }

    private void layoutPack() {
        this.summaryMap.layout();
        this.summaryMap.pack();
        this.summaryBattle.layout();
        this.summaryBattle.pack();
        this.summary.layout();
        this.summary.pack();
        this.friend1.layout();
        this.friend1.pack();
        this.friend2.layout();
        this.friend2.pack();
        this.enemy1.layout();
        this.enemy1.pack();
        this.enemy2.layout();
        this.enemy2.pack();
        this.shell.layout();
        this.shell.pack();
    }

    private void reload() {
        //ヘッダ
        this.lblMapId.setText(GlobalContext.getMapAreaNo() + "-" + GlobalContext.getMapInfoNo());
        this.lblCellId.setText(GlobalContext.getMapCellNo() + "");
        this.lblBossArrive
                .setText(GlobalContext.isBossArrive() ? (GlobalContext.getSortiePhase() == SortiePhase.MAP ? "次" : "○")
                        : "×");
        this.lblBattleCount.setText(GlobalContext.getBattleCount()
                + (GlobalContext.getSortiePhase() == SortiePhase.BATTLE ? "戦目" : "戦終了"));

        BattleDto battleFirst = null;
        BattleDto battleLast = null;
        String enemyName = "(敵艦隊)";

        if (GlobalContext.getSortiePhase() == SortiePhase.BATTLE) {
            if (GlobalContext.getBattleList().isEmpty()) {
                this.layoutPack();
                return;
            }
            BattleDto battles[] = GlobalContext.getBattleList().toArray(new BattleDto[0]);
            battleFirst = battles[0];
            battleLast = battles[battles.length - 1];
            this.lblRank.setText(getRank(battleFirst, battleLast) + "?");
        } else {
            if (GlobalContext.getBattleResultList().isEmpty()) {
                this.layoutPack();
                return;
            }
            BattleResultDto result = GlobalContext.getBattleResultList().get(
                    GlobalContext.getBattleResultList().size() - 1);
            this.lblRank.setText(result.getRank());
            enemyName = result.getEnemyName();
            if (result.getBattles().length == 0) {
                this.layoutPack();
                return;
            }
            battleFirst = result.getBattles()[0];
            battleLast = result.getBattles()[result.getBattles().length - 1];
        }
        this.lblIntercept.setText(battleFirst.getIntercept());
        this.lblDayNight.setText(battleLast.isNight() ? "夜戦" : "昼戦");
        this.lblDispSeiku.setText(battleFirst.getDispSeiku());
        this.lblFriendSearch.setText(battleFirst.getFriendSearch());
        this.lblEnemySearch.setText(battleFirst.getEnemySearch());
        this.lblFriendTouch.setText(battleFirst.getFriendTouch());
        this.lblEnemyTouch.setText(battleFirst.getEnemyTouch());
        this.lblBossDamaged.setText(Integer.toString(battleFirst.getBossDamaged()));

        //味方(第1)
        DockDto dock = battleFirst.getFriends().get(0);
        this.friend1.getDockName().setText(dock.getName());
        this.friend1.getFormation().setText(battleFirst.getFriendFormation());
        for (int i = 0; i < dock.getShips().size(); i++) {
            this.friend1.getNames()[i].setText(dock.getShips().get(i).getName());
            this.friend1.getLvs()[i].setText(dock.getShips().get(i).getLv() + "");
            int nhp = battleFirst.getNowFriendHp()[i];
            int ehp = battleLast.getEndFriendHp()[i];
            int mhp = battleFirst.getMaxFriendHp()[i];
            this.friend1.getNowhps()[i].setText(nhp + "");
            this.friend1.getEndhps()[i].setText(ehp + "");
            this.friend1.getMaxhps()[i].setText(mhp + "");
            setStatus(this.friend1.getNowstats()[i], nhp, mhp, battleFirst.getFriendEscape()[i]);
            setStatus(this.friend1.getEndstats()[i], ehp, mhp, battleFirst.getFriendEscape()[i]);
            setCond(this.friend1.getConds()[i], dock.getShips().get(i).getCond());
        }
        for (int i = dock.getShips().size(); i < MAXCHARA; i++) {
            resetText(this.friend1.getNames()[i]);
            resetText(this.friend1.getLvs()[i]);
            resetText(this.friend1.getNowhps()[i]);
            resetText(this.friend1.getEndhps()[i]);
            resetText(this.friend1.getMaxhps()[i]);
            resetText(this.friend1.getNowstats()[i]);
            resetText(this.friend1.getEndstats()[i]);
            resetText(this.friend1.getConds()[i]);
        }

        //味方(第2)
        if (battleFirst.getFriends().size() > 1) {
            setVisible(this.friend2, true);
            dock = battleFirst.getFriends().get(1);
            this.friend2.getDockName().setText(dock.getName());
            this.friend2.getFormation().setText(battleFirst.getCombinedType());
            for (int i = 0; i < dock.getShips().size(); i++) {
                this.friend2.getNames()[i].setText(dock.getShips().get(i).getName());
                this.friend2.getLvs()[i].setText(dock.getShips().get(i).getLv() + "");
                int nhp = battleFirst.getNowCombinedHp()[i];
                int ehp = battleLast.getEndCombinedHp()[i];
                int mhp = battleFirst.getMaxCombinedHp()[i];
                this.friend2.getNowhps()[i].setText(nhp + "");
                this.friend2.getEndhps()[i].setText(ehp + "");
                this.friend2.getMaxhps()[i].setText(mhp + "");
                setStatus(this.friend2.getNowstats()[i], nhp, mhp, battleFirst.getCombinedEscape()[i]);
                setStatus(this.friend2.getEndstats()[i], ehp, mhp, battleFirst.getCombinedEscape()[i]);
                setCond(this.friend2.getConds()[i], dock.getShips().get(i).getCond());
            }
            for (int i = dock.getShips().size(); i < MAXCHARA; i++) {
                resetText(this.friend2.getNames()[i]);
                resetText(this.friend2.getLvs()[i]);
                resetText(this.friend2.getNowhps()[i]);
                resetText(this.friend2.getEndhps()[i]);
                resetText(this.friend2.getMaxhps()[i]);
                resetText(this.friend2.getNowstats()[i]);
                resetText(this.friend2.getEndstats()[i]);
                resetText(this.friend2.getConds()[i]);
            }
        } else
            setVisible(this.friend2, false);

        //敵
        this.enemy1.getDockName().setText(enemyName);
        this.enemy1.getFormation().setText(battleFirst.getEnemyFormation());
        for (int i = 0; i < battleFirst.getEnemy().size(); i++) {
            ShipInfoDto ship = battleFirst.getEnemy().get(i);
            String name = ship.getName();
            if (!StringUtils.isEmpty(ship.getFlagship())) {
                name += " " + ship.getFlagship();
            }
            this.enemy1.getNames()[i].setText(name);
            this.enemy1.getLvs()[i]
                    .setText((i < battleFirst.getEnemyLv().size()) ? (battleFirst.getEnemyLv().get(i) + "")
                            : "");
            int nhp = battleFirst.getNowEnemyHp()[i];
            int ehp = battleLast.getEndEnemyHp()[i];
            int mhp = battleFirst.getMaxEnemyHp()[i];
            this.enemy1.getNowhps()[i].setText(nhp + "");
            this.enemy1.getEndhps()[i].setText(ehp + "");
            this.enemy1.getMaxhps()[i].setText(mhp + "");
            setStatus(this.enemy1.getNowstats()[i], nhp, mhp, false);
            setStatus(this.enemy1.getEndstats()[i], ehp, mhp, false);
            this.enemy1.getConds()[i].setText("");
        }
        for (int i = battleFirst.getEnemy().size(); i < MAXCHARA; i++) {
            resetText(this.enemy1.getNames()[i]);
            resetText(this.enemy1.getLvs()[i]);
            resetText(this.enemy1.getNowhps()[i]);
            resetText(this.enemy1.getEndhps()[i]);
            resetText(this.enemy1.getMaxhps()[i]);
            resetText(this.enemy1.getNowstats()[i]);
            resetText(this.enemy1.getEndstats()[i]);
            resetText(this.enemy1.getConds()[i]);
        }
        this.layoutPack();
    }

    private static String getRank(BattleDto battleFirst, BattleDto battleLast) {
        int countf = 0; //味方の数
        int counte = 0; //敵の数
        int destf = 0; //味方の撃沈数
        int deste = 0; //敵の撃沈数
        boolean desteflg = false; //敵の旗艦撃破
        int nowhpf = 0;
        int endhpf = 0;
        int nowhpe = 0;
        int endhpe = 0;

        for (int i = 0; i < MAXCHARA; i++) {
            if (battleLast.getMaxFriendHp()[i] > 0) {
                countf++;
                nowhpf += battleFirst.getNowFriendHp()[i];
                if (battleLast.getEndFriendHp()[i] <= 0)
                    destf++;
                else
                    endhpf += battleLast.getEndFriendHp()[i];
            }
            if (battleLast.getMaxCombinedHp()[i] > 0) {
                countf++;
                nowhpf += battleFirst.getNowCombinedHp()[i];
                if (battleLast.getEndCombinedHp()[i] <= 0)
                    destf++;
                else
                    endhpf += battleLast.getEndCombinedHp()[i];
            }
            if (battleLast.getMaxEnemyHp()[i] > 0) {
                counte++;
                nowhpe += battleFirst.getNowEnemyHp()[i];
                if (battleLast.getEndEnemyHp()[i] <= 0) {
                    deste++;
                    if (i == 0)
                        desteflg = true;
                } else
                    endhpe += battleLast.getEndEnemyHp()[i];
            }
        }

        double gaugef = (nowhpe - endhpe) / (double) nowhpe; //味方の戦果ゲージ
        double gaugee = (nowhpf - endhpf) / (double) nowhpf; //敵の戦果ゲージ

        if (destf > 0) {
            //轟沈あり
            if (((deste >= 4) || ((counte <= 5) && ((deste * 2) >= counte))) && ((gaugee * 2.5) < gaugef))
                return "B";
            else if (desteflg) {
                if (deste > destf)
                    return "B";
                else
                    return "C";
            } else if ((deste >= 4) || ((counte <= 5) && ((deste * 2) >= counte)))
                return "C";
            else if (gaugee < gaugef)
                return "C";
            else
                return "D";
        } else {
            if (deste == counte)
                return "S";
            else if ((deste >= 4) || ((counte <= 5) && ((deste * 2) >= counte)))
                return "A";
            else if (desteflg || ((gaugee * 2.5) < gaugef))
                return "B";
            else if ((gaugef <= 0.001) || (gaugef < gaugee))
                return "D";
            else
                return "C";
        }
    }

    private static void setStatus(CLabel label, int now, int max, boolean escape) {
        if (escape) {
            label.setText("退避");
            label.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
            label.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        } else {
            now *= 4;
            if (now > (max * 3)) {
                label.setText("健在");
                label.setBackground((Color) null);
                label.setForeground(null);
            } else if (now > (max * 2)) {
                label.setText("小破");
                label.setBackground((Color) null);
                label.setForeground(null);
            } else if (now > max) {
                label.setText("中破");
                label.setBackground(SWTResourceManager.getColor(AppConstants.COND_ORANGE_COLOR));
                label.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
            } else if (now > 0) {
                label.setText("大破");
                label.setBackground(SWTResourceManager.getColor(AppConstants.COND_RED_COLOR));
                label.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
            } else {
                label.setText("撃沈");
                label.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
                label.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
            }
        }
    }

    private static void setCond(CLabel label, long cond) {
        label.setText(cond + "");
        if (cond <= AppConstants.COND_RED)
            label.setForeground(SWTResourceManager.getColor(AppConstants.COND_RED_COLOR));
        else if (cond <= AppConstants.COND_ORANGE)
            label.setForeground(SWTResourceManager.getColor(AppConstants.COND_ORANGE_COLOR));
        else if (cond >= AppConstants.COND_GREEN)
            label.setForeground(SWTResourceManager.getColor(AppConstants.COND_GREEN_COLOR));
        else if (cond >= AppConstants.COND_DARK_GREEN)
            label.setForeground(SWTResourceManager.getColor(AppConstants.COND_DARK_GREEN_COLOR));
        else
            label.setForeground(null);
    }

    private static void resetText(CLabel label) {
        label.setText("");
        label.setBackground((Color) null);
        label.setForeground(null);
    }

    private static void setVisible(Control c, boolean visible) {
        if (c.getLayoutData() instanceof GridData)
            setVisible(c, visible, (GridData) c.getLayoutData());
        else
            c.setVisible(visible);
    }

    private static void setVisible(Control c, boolean visible, GridData gd) {
        gd.exclude = !visible;
        c.setLayoutData(gd);
        c.setVisible(visible);
    }

    private static GridLayout getGridLayout(int numColumns, boolean makeColumnsEqualWidth, int spacing) {
        GridLayout gl = new GridLayout(numColumns, makeColumnsEqualWidth);
        gl.horizontalSpacing = spacing;
        gl.verticalSpacing = spacing;
        return gl;
    }

    private static GridLayout getGridLayout(int numColumns, boolean makeColumnsEqualWidth, int spacing, int margin) {
        return getGridLayout(numColumns, makeColumnsEqualWidth, spacing, margin, margin);
    }

    private static GridLayout getGridLayout(int numColumns, boolean makeColumnsEqualWidth, int spacing,
            int marginWidth, int marginHeight) {
        GridLayout gl = getGridLayout(numColumns, makeColumnsEqualWidth, spacing);
        gl.marginTop = 0;
        gl.marginLeft = 0;
        gl.marginBottom = 0;
        gl.marginRight = 0;
        gl.marginWidth = marginWidth;
        gl.marginHeight = marginHeight;
        return gl;
    }

    private class DockComposite extends Composite {
        private final CLabel dockName;
        private final CLabel formation;
        private final CLabel names[] = new CLabel[MAXCHARA];
        private final CLabel lvs[] = new CLabel[MAXCHARA];
        private final CLabel nowhps[] = new CLabel[MAXCHARA];
        private final CLabel endhps[] = new CLabel[MAXCHARA];
        private final CLabel maxhps[] = new CLabel[MAXCHARA];
        private final CLabel nowstats[] = new CLabel[MAXCHARA];
        private final CLabel endstats[] = new CLabel[MAXCHARA];
        private final CLabel conds[] = new CLabel[MAXCHARA];

        public DockComposite(Composite parent, int style, FontData fontData) {
            super(parent, style);
            this.setLayout(getGridLayout(11, false, 2));
            //フォント取得
            String fontName = fontData.getName();
            int size = fontData.getHeight();

            CLabel lblText;
            this.dockName = new CLabel(this, SWT.NONE);
            this.dockName.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 11, 1));
            this.dockName.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
            this.dockName.setMargins(0, 0, 0, 0);

            this.formation = new CLabel(this, SWT.NONE);
            this.formation.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 11, 1));
            this.formation.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
            this.formation.setMargins(0, 0, 0, 0);

            lblText = new CLabel(this, SWT.BORDER);
            lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
            lblText.setAlignment(SWT.CENTER);
            lblText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
            lblText.setText("名前");
            lblText = new CLabel(this, SWT.BORDER);
            lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
            lblText.setAlignment(SWT.CENTER);
            lblText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
            lblText.setText("Lv.");
            lblText = new CLabel(this, SWT.BORDER);
            lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
            lblText.setAlignment(SWT.CENTER);
            lblText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 5, 1));
            lblText.setText("耐久");
            lblText = new CLabel(this, SWT.BORDER);
            lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
            lblText.setAlignment(SWT.CENTER);
            lblText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
            lblText.setText("状態");
            lblText = new CLabel(this, SWT.BORDER);
            lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
            lblText.setAlignment(SWT.CENTER);
            lblText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
            lblText.setText("cond.");

            for (int i = 0; i < MAXCHARA; i++) {
                this.names[i] = new CLabel(this, SWT.BORDER);
                this.names[i].setAlignment(SWT.LEFT);
                this.names[i].setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

                this.lvs[i] = new CLabel(this, SWT.BORDER);
                this.lvs[i].setAlignment(SWT.CENTER);
                this.lvs[i].setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

                this.nowhps[i] = new CLabel(this, SWT.BORDER);
                this.nowhps[i].setAlignment(SWT.RIGHT);
                this.nowhps[i].setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
                lblText = new CLabel(this, SWT.NONE);
                lblText.setAlignment(SWT.CENTER);
                lblText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
                lblText.setText("→");
                this.endhps[i] = new CLabel(this, SWT.BORDER);
                this.endhps[i].setAlignment(SWT.RIGHT);
                this.endhps[i].setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
                lblText = new CLabel(this, SWT.NONE);
                lblText.setAlignment(SWT.CENTER);
                lblText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
                lblText.setText("/");
                this.maxhps[i] = new CLabel(this, SWT.BORDER);
                this.maxhps[i].setAlignment(SWT.RIGHT);
                this.maxhps[i].setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

                this.nowstats[i] = new CLabel(this, SWT.BORDER);
                this.nowstats[i].setAlignment(SWT.CENTER);
                this.nowstats[i].setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
                lblText = new CLabel(this, SWT.NONE);
                lblText.setAlignment(SWT.CENTER);
                lblText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
                lblText.setText("→");
                this.endstats[i] = new CLabel(this, SWT.BORDER);
                this.endstats[i].setAlignment(SWT.CENTER);
                this.endstats[i].setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

                this.conds[i] = new CLabel(this, SWT.BORDER);
                this.conds[i].setAlignment(SWT.CENTER);
                this.conds[i].setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
            }
        }

        public CLabel getDockName() {
            return this.dockName;
        }

        public CLabel getFormation() {
            return this.formation;
        }

        public CLabel[] getNames() {
            return this.names;
        }

        public CLabel[] getLvs() {
            return this.lvs;
        }

        public CLabel[] getNowhps() {
            return this.nowhps;
        }

        public CLabel[] getEndhps() {
            return this.endhps;
        }

        public CLabel[] getMaxhps() {
            return this.maxhps;
        }

        public CLabel[] getNowstats() {
            return this.nowstats;
        }

        public CLabel[] getEndstats() {
            return this.endstats;
        }

        public CLabel[] getConds() {
            return this.conds;
        }
    }

    /**
     * テーブルを定期的に再読み込みする
     */
    protected class CyclicReloadAdapter extends SelectionAdapter {

        private final MenuItem menuitem;

        public CyclicReloadAdapter(MenuItem menuitem) {
            this.menuitem = menuitem;
        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            this.setCyclicReload(this.menuitem);
        }

        private void setCyclicReload(MenuItem menuitem) {
            if (menuitem.getSelection()) {
                Runnable command = () -> {
                    if (!SortieDialog.this.shell.isDisposed()) {
                        SortieDialog.this.display.asyncExec(() -> {
                            if (!SortieDialog.this.shell.isDisposed()) {
                                SortieDialog.this.reload();
                            }
                        });
                    } else {
                        // ウインドウが消えていたらタスクをキャンセルする
                        throw new ThreadDeath();
                    }
                };
                // タスクがある場合キャンセル
                if (SortieDialog.this.future != null) {
                    SortieDialog.this.future.cancel(false);
                }
                // 再読み込みするようにスケジュールする
                SortieDialog.this.future = ThreadManager.getExecutorService()
                        .scheduleWithFixedDelay(command, 1, 1, TimeUnit.SECONDS);
            } else {
                // タスクがある場合キャンセル
                if (SortieDialog.this.future != null) {
                    SortieDialog.this.future.cancel(false);
                }
            }
        }
    }
}
