package logbook.gui;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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

/**
 * 出撃詳細
 *
 */
public final class SortieDialog extends Dialog
{
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

    private CLabel lblMapId;
    private CLabel lblCellId;
    private CLabel lblBossArrive;
    private CLabel lblIntercept;
    private CLabel lblBattleCount;
    private CLabel lblDayNight;
    private CLabel lblRank;

    private Label lblSeparator1;
    private Label lblSeparatorH;
    private Label lblSeparatorV;

    private DockComposite friend;
    private DockComposite combined;
    private DockComposite enemy;

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
        mi.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
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
        land.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                if (land.getSelection()) {
                    SortieDialog.this.landscape();
                }
                else {
                    SortieDialog.this.portrait();
                }
            }
        });

        // マップ|マス|ボス|戦闘回数|対峙|昼夜|ランク
        CLabel lblText;
        this.summary = new Composite(this.shell, SWT.NONE);
        this.summary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
        this.summary.setLayout(getGridLayout(7, false, 2));
        lblText = new CLabel(this.summary, SWT.BORDER);
        lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
        lblText.setAlignment(SWT.CENTER);
        lblText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        lblText.setText("マップ");
        lblText = new CLabel(this.summary, SWT.BORDER);
        lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
        lblText.setAlignment(SWT.CENTER);
        lblText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        lblText.setText("マス");
        lblText = new CLabel(this.summary, SWT.BORDER);
        lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
        lblText.setAlignment(SWT.CENTER);
        lblText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        lblText.setText("ボス");
        lblText = new CLabel(this.summary, SWT.BORDER);
        lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
        lblText.setAlignment(SWT.CENTER);
        lblText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        lblText.setText("回数");
        lblText = new CLabel(this.summary, SWT.BORDER);
        lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
        lblText.setAlignment(SWT.CENTER);
        lblText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        lblText.setText("対峙");
        lblText = new CLabel(this.summary, SWT.BORDER);
        lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
        lblText.setAlignment(SWT.CENTER);
        lblText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        lblText.setText("昼夜");
        lblText = new CLabel(this.summary, SWT.BORDER);
        lblText.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
        lblText.setAlignment(SWT.CENTER);
        lblText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        lblText.setText("ランク");

        this.lblMapId = new CLabel(this.summary, SWT.BORDER);
        this.lblMapId.setAlignment(SWT.CENTER);
        this.lblMapId.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        this.lblCellId = new CLabel(this.summary, SWT.BORDER);
        this.lblCellId.setAlignment(SWT.CENTER);
        this.lblCellId.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        this.lblBossArrive = new CLabel(this.summary, SWT.BORDER);
        this.lblBossArrive.setAlignment(SWT.CENTER);
        this.lblBossArrive.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        this.lblBattleCount = new CLabel(this.summary, SWT.BORDER);
        this.lblBattleCount.setAlignment(SWT.CENTER);
        this.lblBattleCount.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        this.lblIntercept = new CLabel(this.summary, SWT.BORDER);
        this.lblIntercept.setAlignment(SWT.CENTER);
        this.lblIntercept.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        this.lblDayNight = new CLabel(this.summary, SWT.BORDER);
        this.lblDayNight.setAlignment(SWT.CENTER);
        this.lblDayNight.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        this.lblRank = new CLabel(this.summary, SWT.BORDER);
        this.lblRank.setAlignment(SWT.CENTER);
        this.lblRank.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        this.lblSeparator1 = new Label(this.shell, SWT.SEPARATOR | SWT.HORIZONTAL);
        this.lblSeparator1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

        this.friend = new DockComposite(this.shell, SWT.NONE, fontData);
        this.friend.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        this.combined = new DockComposite(this.shell, SWT.NONE, fontData);
        this.combined.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        this.lblSeparatorH = new Label(this.shell, SWT.SEPARATOR | SWT.HORIZONTAL);
        this.lblSeparatorH.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

        this.lblSeparatorV = new Label(this.shell, SWT.SEPARATOR | SWT.VERTICAL);
        setVisible(this.lblSeparatorV, false, new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

        this.enemy = new DockComposite(this.shell, SWT.NONE, fontData);
        this.enemy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

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
        }
        else {
            SortieDialog.this.portrait();
        }
    }

    private void portrait()
    {
        this.shell.setLayout(new GridLayout(2, false));

        this.summary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

        this.lblSeparator1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

        this.friend.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        this.combined.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        setVisible(this.lblSeparatorH, true, new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        setVisible(this.lblSeparatorV, false, new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

        this.enemy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

        this.reload();
    }

    private void landscape()
    {
        this.shell.setLayout(new GridLayout(5, false));

        this.summary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 1));

        this.lblSeparator1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));

        this.friend.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        this.combined.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        setVisible(this.lblSeparatorH, false, new GridData(SWT.CENTER, SWT.FILL, false, true, 1, 1));
        setVisible(this.lblSeparatorV, true, new GridData(SWT.CENTER, SWT.FILL, false, true, 1, 1));

        this.enemy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        this.reload();
    }

    private void reload()
    {
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

        if (GlobalContext.getSortiePhase() == SortiePhase.BATTLE)
        {
            if (GlobalContext.getBattleList().isEmpty())
            {
                this.summary.layout();
                this.summary.pack();
                this.shell.layout();
                this.shell.pack();
                return;
            }
            BattleDto battles[] = GlobalContext.getBattleList().toArray(new BattleDto[0]);
            battleFirst = battles[0];
            battleLast = battles[battles.length - 1];
            this.lblRank.setText(getRank(battleFirst, battleLast) + "?");
        }
        else
        {
            if (GlobalContext.getBattleResultList().isEmpty())
            {
                this.summary.layout();
                this.summary.pack();
                this.shell.layout();
                this.shell.pack();
                return;
            }
            BattleResultDto result = GlobalContext.getBattleResultList().get(
                    GlobalContext.getBattleResultList().size() - 1);
            this.lblRank.setText(result.getRank());
            enemyName = result.getEnemyName();
            if (result.getBattles().length == 0)
            {
                this.summary.layout();
                this.summary.pack();
                this.shell.layout();
                this.shell.pack();
                return;
            }
            battleFirst = result.getBattles()[0];
            battleLast = result.getBattles()[result.getBattles().length - 1];
        }
        this.lblIntercept.setText(battleFirst.getIntercept());
        this.lblDayNight.setText(battleLast.isNight() ? "夜戦" : "昼戦");

        //味方(第1)
        DockDto dock = battleFirst.getFriends().get(0);
        this.friend.getDockName().setText(dock.getName());
        this.friend.getFormation().setText(battleFirst.getFriendFormation());
        for (int i = 0; i < dock.getShips().size(); i++)
        {
            this.friend.getNames()[i].setText(dock.getShips().get(i).getName());
            this.friend.getLvs()[i].setText(dock.getShips().get(i).getLv() + "");
            int nhp = battleFirst.getNowFriendHp()[i];
            int ehp = battleLast.getEndFriendHp()[i];
            int mhp = battleFirst.getMaxFriendHp()[i];
            this.friend.getNowhps()[i].setText(nhp + "");
            this.friend.getEndhps()[i].setText(ehp + "");
            this.friend.getMaxhps()[i].setText(mhp + "");
            setStatus(this.friend.getNowstats()[i], nhp, mhp);
            setStatus(this.friend.getEndstats()[i], ehp, mhp);
            setCond(this.friend.getConds()[i], dock.getShips().get(i).getCond());
        }
        for (int i = dock.getShips().size(); i < MAXCHARA; i++)
        {
            resetText(this.friend.getNames()[i]);
            resetText(this.friend.getLvs()[i]);
            resetText(this.friend.getNowhps()[i]);
            resetText(this.friend.getEndhps()[i]);
            resetText(this.friend.getMaxhps()[i]);
            resetText(this.friend.getNowstats()[i]);
            resetText(this.friend.getEndstats()[i]);
            resetText(this.friend.getConds()[i]);
        }

        //味方(第2)
        if (battleFirst.getFriends().size() > 1)
        {
            setVisible(this.combined, true);
            dock = battleFirst.getFriends().get(1);
            this.combined.getDockName().setText(dock.getName());
            for (int i = 0; i < dock.getShips().size(); i++)
            {
                this.combined.getNames()[i].setText(dock.getShips().get(i).getName());
                this.combined.getLvs()[i].setText(dock.getShips().get(i).getLv() + "");
                int nhp = battleFirst.getNowCombinedHp()[i];
                int ehp = battleLast.getEndCombinedHp()[i];
                int mhp = battleFirst.getMaxCombinedHp()[i];
                this.combined.getNowhps()[i].setText(nhp + "");
                this.combined.getEndhps()[i].setText(ehp + "");
                this.combined.getMaxhps()[i].setText(mhp + "");
                setStatus(this.combined.getNowstats()[i], nhp, mhp);
                setStatus(this.combined.getEndstats()[i], ehp, mhp);
                setCond(this.combined.getConds()[i], dock.getShips().get(i).getCond());
            }
            for (int i = dock.getShips().size(); i < MAXCHARA; i++)
            {
                resetText(this.combined.getNames()[i]);
                resetText(this.combined.getLvs()[i]);
                resetText(this.combined.getNowhps()[i]);
                resetText(this.combined.getEndhps()[i]);
                resetText(this.combined.getMaxhps()[i]);
                resetText(this.combined.getNowstats()[i]);
                resetText(this.combined.getEndstats()[i]);
                resetText(this.combined.getConds()[i]);
            }
        }
        else
            setVisible(this.combined, false);

        //敵
        this.enemy.getDockName().setText(enemyName);
        this.enemy.getFormation().setText(battleFirst.getEnemyFormation());
        for (int i = 0; i < battleFirst.getEnemy().size(); i++)
        {
            ShipInfoDto ship = battleFirst.getEnemy().get(i);
            String name = ship.getName();
            if (!StringUtils.isEmpty(ship.getFlagship())) {
                name += " " + ship.getFlagship();
            }
            this.enemy.getNames()[i].setText(name);
            this.enemy.getLvs()[i]
                    .setText((i < battleFirst.getEnemyLv().size()) ? (battleFirst.getEnemyLv().get(i) + "")
                            : "");
            int nhp = battleFirst.getNowEnemyHp()[i];
            int ehp = battleLast.getEndEnemyHp()[i];
            int mhp = battleFirst.getMaxEnemyHp()[i];
            this.enemy.getNowhps()[i].setText(nhp + "");
            this.enemy.getEndhps()[i].setText(ehp + "");
            this.enemy.getMaxhps()[i].setText(mhp + "");
            setStatus(this.enemy.getNowstats()[i], nhp, mhp);
            setStatus(this.enemy.getEndstats()[i], ehp, mhp);
            this.enemy.getConds()[i].setText("");
        }
        for (int i = battleFirst.getEnemy().size(); i < MAXCHARA; i++)
        {
            resetText(this.enemy.getNames()[i]);
            resetText(this.enemy.getLvs()[i]);
            resetText(this.enemy.getNowhps()[i]);
            resetText(this.enemy.getEndhps()[i]);
            resetText(this.enemy.getMaxhps()[i]);
            resetText(this.enemy.getNowstats()[i]);
            resetText(this.enemy.getEndstats()[i]);
            resetText(this.enemy.getConds()[i]);
        }
        this.summary.layout();
        this.summary.pack();
        this.friend.layout();
        this.friend.pack();
        this.combined.layout();
        this.combined.pack();
        this.enemy.layout();
        this.enemy.pack();
        this.shell.layout();
        this.shell.pack();
    }

    private static String getRank(BattleDto battleFirst, BattleDto battleLast)
    {
        int countf = 0; //味方の数
        int counte = 0; //敵の数
        int destf = 0; //味方の撃沈数
        int deste = 0; //敵の撃沈数
        boolean desteflg = false; //敵の旗艦撃破
        int nowhpf = 0;
        int endhpf = 0;
        int nowhpe = 0;
        int endhpe = 0;

        for (int i = 0; i < MAXCHARA; i++)
        {
            if (battleLast.getMaxFriendHp()[i] > 0)
            {
                countf++;
                nowhpf += battleFirst.getNowFriendHp()[i];
                if (battleLast.getEndFriendHp()[i] <= 0)
                    destf++;
                else
                    endhpf += battleLast.getEndFriendHp()[i];
            }
            if (battleLast.getMaxCombinedHp()[i] > 0)
            {
                countf++;
                nowhpf += battleFirst.getNowCombinedHp()[i];
                if (battleLast.getEndCombinedHp()[i] <= 0)
                    destf++;
                else
                    endhpf += battleLast.getEndCombinedHp()[i];
            }
            if (battleLast.getMaxEnemyHp()[i] > 0)
            {
                counte++;
                nowhpe += battleFirst.getNowEnemyHp()[i];
                if (battleLast.getEndEnemyHp()[i] <= 0)
                {
                    deste++;
                    if (i == 0)
                        desteflg = true;
                }
                else
                    endhpe += battleLast.getEndEnemyHp()[i];
            }
        }

        double gaugef = (nowhpe - endhpe) / (double) nowhpe; //味方の戦果ゲージ
        double gaugee = (nowhpf - endhpf) / (double) nowhpf; //敵の戦果ゲージ

        if (destf > 0)
        {
            //轟沈あり
            if (((deste >= 4) || ((counte <= 5) && ((deste * 2) >= counte))) && ((gaugee * 2.5) < gaugef))
                return "B";
            else if (desteflg)
            {
                if (deste > destf)
                    return "B";
                else
                    return "C";
            }
            else if ((deste >= 4) || ((counte <= 5) && ((deste * 2) >= counte)))
                return "C";
            else if (gaugee < gaugef)
                return "C";
            else
                return "D";
        }
        else
        {
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

    private static void setStatus(CLabel label, int now, int max)
    {
        now *= 4;
        if (now > (max * 3))
        {
            label.setText("健在");
            label.setBackground((Color) null);
            label.setForeground(null);
        }
        else if (now > (max * 2))
        {
            label.setText("小破");
            label.setBackground((Color) null);
            label.setForeground(null);
        }
        else if (now > max)
        {
            label.setText("中破");
            label.setBackground(SWTResourceManager.getColor(AppConstants.COND_ORANGE_COLOR));
            label.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        }
        else if (now > 0)
        {
            label.setText("大破");
            label.setBackground(SWTResourceManager.getColor(AppConstants.COND_RED_COLOR));
            label.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        }
        else
        {
            label.setText("撃沈");
            label.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
            label.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        }
    }

    private static void setCond(CLabel label, long cond)
    {
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

    private static void resetText(CLabel label)
    {
        label.setText("");
        label.setBackground((Color) null);
        label.setForeground(null);
    }

    private static void setVisible(Control c, boolean visible)
    {
        if (c.getLayoutData() instanceof GridData)
            setVisible(c, visible, (GridData) c.getLayoutData());
        else
            c.setVisible(visible);
    }

    private static void setVisible(Control c, boolean visible, GridData gd)
    {
        gd.exclude = !visible;
        c.setLayoutData(gd);
        c.setVisible(visible);
    }

    private static GridLayout getGridLayout(int numColumns, boolean makeColumnsEqualWidth, int spacing)
    {
        GridLayout gl = new GridLayout(numColumns, makeColumnsEqualWidth);
        gl.horizontalSpacing = spacing;
        gl.verticalSpacing = spacing;
        return gl;
    }

    private class DockComposite extends Composite
    {
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

        public DockComposite(Composite parent, int style, FontData fontData)
        {
            super(parent, style);
            this.setLayout(getGridLayout(11, false, 2));
            //フォント取得
            String fontName = fontData.getName();
            int size = fontData.getHeight();

            this.dockName = new CLabel(this, SWT.NONE);
            this.dockName.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 11, 1));
            this.dockName.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
            this.dockName.setMargins(0, 0, 0, 0);

            this.formation = new CLabel(this, SWT.NONE);
            this.formation.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 11, 1));
            this.formation.setFont(SWTResourceManager.getFont(fontName, size, SWT.BOLD));
            this.formation.setMargins(0, 0, 0, 0);

            CLabel lblText;
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

            for (int i = 0; i < MAXCHARA; i++)
            {
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
