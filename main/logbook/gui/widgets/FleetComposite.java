package logbook.gui.widgets;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.List;

import javax.annotation.CheckForNull;

import logbook.config.AppConfig;
import logbook.constants.AppConstants;
import logbook.data.context.GlobalContext;
import logbook.dto.DockDto;
import logbook.dto.ItemDto;
import logbook.dto.ShipDto;
import logbook.gui.ApplicationMain;
import logbook.gui.TimerSettingDialog;
import logbook.gui.logic.Sound;
import logbook.internal.EvaluateExp;
import logbook.internal.SeaExp;
import logbook.util.CalcExpUtils;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * 艦隊タブのウィジェットです
 *
 */
public class FleetComposite extends Composite {

    /** 警告 */
    private static final int WARN = 1;
    /** 致命的 */
    private static final int FATAL = 2;
    /** 1艦隊に編成できる艦娘の数 */
    private static final int MAXCHARA = 6;

    /** HPゲージ幅 */
    private static final int GAUGE_WIDTH = 50;
    /** HPゲージ高さ */
    private static final int GAUGE_HEIGHT = 10;
    /** 経験値ゲージ高さ */
    private static final int EXP_GAUGE_HEIGHT = 2;
    /** HPゲージ最小色 */
    private static final RGB GAUGE_EMPTY = new RGB(0xff, 0, 0);
    /** HPゲージ中間色 */
    private static final RGB GAUGE_HALF = new RGB(0xff, 0xd7, 0);
    /** HPゲージ最大色 */
    private static final RGB GAUGE_FULL = new RGB(0, 0xd7, 0);
    /** 経験値ゲージ色 */
    private static final RGB EXP_GAUGE = new RGB(0, 0x80, 0xff);

    /** タブ */
    private final CTabItem tab;
    /** メイン画面 */
    private final ApplicationMain main;
    /** フォント大きい */
    private final Font large;
    /** フォント小さい */
    private final Font small;
    /** 艦隊 */
    private DockDto dock;

    private final Composite fleetGroup;

    /** タブアイコン表示 */
    private final BitSet state = new BitSet();
    /** コンディション最小値(メッセージ表示用) */
    private long cond;
    /** 疲労回復時間(メッセージ表示用) */
    private String clearDateString;
    /** 疲労回復時間 */
    private Date clearDate;
    /** 大破している */
    private boolean badlyDamage;

    /** アイコンラベル */
    private final Label[] iconLabels = new Label[MAXCHARA];
    /** 名前ラベル */
    private final Label[] nameLabels = new Label[MAXCHARA];
    /** Lvラベル */
    private final Label[] lvLabels = new Label[MAXCHARA];
    /** HP */
    private final Label[] hpLabels = new Label[MAXCHARA];
    /** HPゲージ */
    private final Label[] hpgaugeLabels = new Label[MAXCHARA];
    /** HPゲージイメージ */
    private final Image[] hpgaugeImages = new Image[MAXCHARA];
    /** HPメッセージ */
    private final Label[] hpmsgLabels = new Label[MAXCHARA];
    /** コンディション */
    private final Label[] condLabels = new Label[MAXCHARA];
    /** コンディションステータス */
    private final Label[] condstLabels = new Label[MAXCHARA];
    /** 弾ステータス */
    private final Label[] bullstLabels = new Label[MAXCHARA];
    /** 燃料ステータス */
    private final Label[] fuelstLabels = new Label[MAXCHARA];
    /** ダメコンステータス(要員) */
    private final Label[] dmgcstyLabels = new Label[MAXCHARA];
    /** ダメコンステータス(女神) */
    private final Label[] dmgcstmLabels = new Label[MAXCHARA];
    /** レベリングステータス */
    private final Label[] nextLabels = new Label[MAXCHARA];
    /** メッセージ */
    private final StyledText message;

    /**
     * @param parent 艦隊タブの親
     * @param tabItem 艦隊タブ
     */
    public FleetComposite(CTabFolder parent, CTabItem tabItem, ApplicationMain main) {
        super(parent, SWT.NONE);
        this.tab = tabItem;
        this.main = main;
        this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        GridLayout glParent = new GridLayout(1, false);
        glParent.horizontalSpacing = 0;
        glParent.marginTop = 0;
        glParent.marginWidth = 0;
        glParent.marginHeight = 0;
        glParent.marginBottom = 0;
        glParent.verticalSpacing = 0;
        this.setLayout(glParent);

        FontData normalfd = parent.getShell().getFont().getFontData()[0];
        FontData largefd = new FontData(normalfd.getName(), normalfd.getHeight() + 2, normalfd.getStyle());
        FontData smallfd = new FontData(normalfd.getName(), normalfd.getHeight() - 1, normalfd.getStyle());

        this.large = new Font(Display.getCurrent(), largefd);
        this.small = new Font(Display.getCurrent(), smallfd);

        this.fleetGroup = new Composite(this, SWT.NONE);
        this.fleetGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout glShipGroup = new GridLayout(3, false);
        glShipGroup.horizontalSpacing = 0;
        glShipGroup.marginTop = 0;
        glShipGroup.marginWidth = 1;
        glShipGroup.marginHeight = 0;
        glShipGroup.marginBottom = 0;
        glShipGroup.verticalSpacing = 0;
        this.fleetGroup.setLayout(glShipGroup);
        this.init();

        // セパレーター
        Label separator = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
        separator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        // メッセージ
        this.message = new StyledText(this, SWT.READ_ONLY | SWT.WRAP);
        this.message.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        this.message.setWordWrap(true);
        this.message.setBackground(this.getBackground());
        this.message.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent event) {
                try {
                    int offset = FleetComposite.this.message.getOffsetAtLocation(new Point(event.x, event.y));
                    StyleRange style = FleetComposite.this.message.getStyleRangeAtOffset(offset);
                    if ((style != null) && (style.data instanceof Date)) {

                        TimerSettingDialog dialog = new TimerSettingDialog(FleetComposite.this.getShell());
                        dialog.setTime((Date) style.data);
                        dialog.setMessage(MessageFormat.format("「{0}」の疲労が回復しました", FleetComposite.this.dock.getName()));
                        dialog.open();
                    }
                } catch (IllegalArgumentException e) {
                    // no character under event.x, event.y
                }

            }
        });

        this.fleetGroup.layout();
    }

    /**
     * 初期化
     */
    private void init() {
        for (int i = 0; i < MAXCHARA; i++) {
            // アイコン
            Label iconlabel = new Label(this.fleetGroup, SWT.NONE);
            GridData gdIconlabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
            gdIconlabel.widthHint = 16;
            iconlabel.setLayoutData(gdIconlabel);
            // 名前
            Composite nameComposite = new Composite(this.fleetGroup, SWT.NONE);
            GridLayout glName = new GridLayout(2, false);
            glName.horizontalSpacing = 0;
            glName.marginTop = 0;
            glName.marginWidth = 1;
            glName.marginHeight = 0;
            glName.marginBottom = 0;
            glName.verticalSpacing = 0;
            nameComposite.setLayout(glName);
            nameComposite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

            Label namelabel = new Label(nameComposite, SWT.NONE);
            namelabel.setFont(this.large);
            namelabel.setText("名前");

            Label lvlabel = new Label(nameComposite, SWT.NONE);
            lvlabel.setFont(this.small);
            lvlabel.setText("Lv.0");
            // HP
            Composite hpComposite = new Composite(this.fleetGroup, SWT.NONE);
            GridLayout glHp = new GridLayout(3, false);
            glHp.horizontalSpacing = 0;
            glHp.marginTop = 0;
            glHp.marginWidth = 1;
            glHp.marginHeight = 0;
            glHp.marginBottom = 0;
            glHp.verticalSpacing = 0;
            hpComposite.setLayout(glHp);
            hpComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

            Label hp = new Label(hpComposite, SWT.NONE);
            hp.setFont(this.small);
            Label hpgauge = new Label(hpComposite, SWT.NONE);
            Label hpmsg = new Label(hpComposite, SWT.NONE);
            hpmsg.setText("健在");

            // ステータス
            new Label(this.fleetGroup, SWT.NONE);
            Composite stateComposite = new Composite(this.fleetGroup, SWT.NONE);
            GridLayout glState = new GridLayout(6, false);
            glState.horizontalSpacing = 0;
            glState.marginTop = 0;
            glState.marginWidth = 0;
            glState.marginHeight = 0;
            glState.marginBottom = 0;
            glState.verticalSpacing = 0;
            stateComposite.setLayout(glState);
            stateComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

            Label condst = new Label(stateComposite, SWT.NONE);
            condst.setText("疲");
            Label fuelst = new Label(stateComposite, SWT.NONE);
            fuelst.setText("燃");
            Label bullst = new Label(stateComposite, SWT.NONE);
            bullst.setText("弾");
            Label dmgcsty = new Label(stateComposite, SWT.NONE);
            dmgcsty.setText("ダ");
            Label dmgcstm = new Label(stateComposite, SWT.NONE);
            dmgcstm.setText("ダ");
            Label next = new Label(stateComposite, SWT.NONE);
            next.setFont(this.small);
            next.setText("");

            // 疲労
            Label cond = new Label(this.fleetGroup, SWT.NONE);
            cond.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
            cond.setText("49 cond.");

            this.iconLabels[i] = iconlabel;
            this.nameLabels[i] = namelabel;
            this.lvLabels[i] = lvlabel;
            this.hpLabels[i] = hp;
            this.hpgaugeLabels[i] = hpgauge;
            this.hpmsgLabels[i] = hpmsg;
            this.condLabels[i] = cond;
            this.condstLabels[i] = condst;
            this.bullstLabels[i] = bullst;
            this.dmgcstyLabels[i] = dmgcsty;
            this.dmgcstmLabels[i] = dmgcstm;
            this.fuelstLabels[i] = fuelst;
            this.nextLabels[i] = next;
        }
    }

    /**
     * 艦隊を更新します
     *
     * @param dock
     */
    public void updateFleet(DockDto dock) {
        if ((this.dock == dock) && !this.dock.isUpdate()) {
            return;
        }

        this.getShell().setRedraw(false);

        this.dock = dock;
        this.dock.setUpdate(false);
        this.state.set(WARN, false);
        this.state.set(FATAL, false);
        this.cond = 49;
        this.clearDateString = null;
        this.clearDate = null;
        this.badlyDamage = false;
        this.message.setText("");

        List<ShipDto> ships = dock.getShips();
        for (int i = ships.size(); i < MAXCHARA; i++) {
            this.iconLabels[i].setImage(null);
            this.nameLabels[i].setText("");
            this.lvLabels[i].setText("");
            this.hpLabels[i].setText("");
            this.hpgaugeLabels[i].setImage(null);
            this.hpmsgLabels[i].setText("");
            this.condLabels[i].setText("");
            this.condstLabels[i].setText("");
            this.bullstLabels[i].setText("");
            this.dmgcstyLabels[i].setText("");
            this.dmgcstmLabels[i].setText("");
            this.fuelstLabels[i].setText("");
            this.nextLabels[i].setText("");
        }
        // 艦隊合計Lv
        int totallv = 0;
        // 索敵値計(素)
        int totalSakuteki = 0;
        // 偵察機索敵値計
        int totalSakutekiSurvey = 0;
        // 電探索敵値計
        int totalSakutekiRader = 0;
        // 2-5式(秋)索敵値計
        double totalSakutekiAutumn = Math.ceil(GlobalContext.hqLevel() / 5.0) * 5 * -0.61;
        // ドラム缶計
        int totalDrum = 0;
        // ドラム缶所持艦娘計
        int totalDrumShip = 0;

        for (int i = 0; i < ships.size(); i++) {
            ShipDto ship = ships.get(i);
            // 艦娘のステータス
            BitSet shipstatus = new BitSet();
            // HP
            long nowhp = ship.getNowhp();
            // MaxHP
            long maxhp = ship.getMaxhp();
            // HP割合
            float hpratio = (float) nowhp / (float) maxhp;
            // 経験値ゲージの割合
            float expraito = ship.getExpraito();
            // 疲労
            long cond = ship.getCond();
            // 弾
            int bull = ship.getBull();
            // 弾Max
            int bullmax = ship.getBullMax();
            // 残弾比
            float bullraito = bullmax != 0 ? (float) bull / (float) bullmax : 1f;
            // 燃料
            int fuel = ship.getFuel();
            // 燃料Max
            int fuelmax = ship.getFuelMax();
            // 残燃料比
            float fuelraito = fuelmax != 0 ? (float) fuel / (float) fuelmax : 1f;
            // 艦隊合計Lv
            totallv += ship.getLv();
            // 索敵値計(素)
            totalSakuteki += ship.getSakuteki();
            // 偵察機索敵値計
            totalSakutekiSurvey += ship.getSakutekiSurvey();
            // 電探索敵値計
            totalSakutekiRader += ship.getSakutekiRader();

            // 疲労している艦娘がいる場合メッセージを表示
            if (this.cond > cond) {
                this.cond = cond;
                this.clearDateString = ship.getCondClearDateString();
                this.clearDate = ship.getCondClearDate();
            }

            // 体力メッセージ
            if (ship.isBadlyDamage()) {
                if (AppConfig.get().isFatalBybadlyDamage()) {
                    // 大破で致命的アイコン
                    this.state.set(FATAL);
                    shipstatus.set(FATAL);
                }
                // 大破している艦娘がいる場合メッセージを表示
                this.badlyDamage = true;
                this.hpmsgLabels[i].setText("大破");
                this.hpmsgLabels[i].setBackground(SWTResourceManager.getColor(AppConstants.COND_RED_COLOR));
                this.hpmsgLabels[i].setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
            } else if (ship.isHalfDamage()) {
                if (AppConfig.get().isWarnByHalfDamage()) {
                    // 中破で警告アイコン
                    this.state.set(WARN);
                    shipstatus.set(WARN);
                }

                this.hpmsgLabels[i].setText("中破");
                this.hpmsgLabels[i].setBackground(SWTResourceManager.getColor(AppConstants.COND_ORANGE_COLOR));
                this.hpmsgLabels[i].setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
            } else if (ship.isSlightDamage()) {
                this.hpmsgLabels[i].setText("小破");
                this.hpmsgLabels[i].setBackground(null);
                this.hpmsgLabels[i].setForeground(null);
            } else {
                this.hpmsgLabels[i].setText("健在");
                this.hpmsgLabels[i].setBackground(null);
                this.hpmsgLabels[i].setForeground(null);
            }

            // ステータス
            // ステータス.疲労
            this.condstLabels[i].setText("疲");
            if (cond >= 49) {
                this.condstLabels[i].setEnabled(false);
            } else {
                this.condstLabels[i].setEnabled(true);
            }
            // ステータス.燃料
            this.fuelstLabels[i].setText("燃");
            if (fuelraito >= 1f) {
                this.fuelstLabels[i].setEnabled(false);
                this.fuelstLabels[i].setForeground(null);
            } else {
                if (AppConfig.get().isWarnByNeedSupply()) {
                    // 補給不足で警告アイコン
                    this.state.set(WARN);
                    shipstatus.set(WARN);
                }
                this.fuelstLabels[i].setEnabled(true);
                if (fuelraito <= AppConstants.EMPTY_SUPPLY) {
                    // 補給赤
                    this.fuelstLabels[i].setForeground(SWTResourceManager.getColor(AppConstants.COND_RED_COLOR));
                } else if (fuelraito <= AppConstants.LOW_SUPPLY) {
                    // 補給橙
                    this.fuelstLabels[i].setForeground(SWTResourceManager.getColor(AppConstants.COND_ORANGE_COLOR));
                }
            }
            // ステータス.弾
            this.bullstLabels[i].setText("弾");
            if (bullraito >= 1f) {
                this.bullstLabels[i].setEnabled(false);
                this.bullstLabels[i].setBackground(null);
                this.bullstLabels[i].setForeground(null);
            } else {
                if (AppConfig.get().isWarnByNeedSupply()) {
                    // 補給不足で警告アイコン
                    this.state.set(WARN);
                    shipstatus.set(WARN);
                }
                this.bullstLabels[i].setEnabled(true);
                if (bullraito <= AppConstants.EMPTY_SUPPLY) {
                    this.bullstLabels[i].setForeground(SWTResourceManager.getColor(AppConstants.COND_RED_COLOR));
                } else if (bullraito <= AppConstants.LOW_SUPPLY) {
                    this.bullstLabels[i].setForeground(SWTResourceManager.getColor(AppConstants.COND_ORANGE_COLOR));
                }
            }
            // ステータス.ダメコン
            // 装備
            List<ItemDto> item = ship.getItem();
            List<String> names = new ArrayList<String>();
            int dmgcsty = 0;
            int dmgcstm = 0;
            boolean drum = false;
            // 索敵値計(秋)
            long saku = ship.getSakuteki();
            for (ItemDto itemDto : item) {
                if (itemDto != null) {
                    if (itemDto.getName().equals("応急修理要員")) {
                        dmgcsty++;
                    } else if (itemDto.getName().equals("応急修理女神")) {
                        dmgcstm++;
                    } else if (itemDto.getName().equals("ドラム缶(輸送用)")) {
                        totalDrum++;
                        drum = true;
                    }
                    // 索敵値計(秋)
                    switch (itemDto.getType2())
                    {
                    case 7:
                        //艦上爆撃機
                        totalSakutekiAutumn += itemDto.getSaku() * 1.04;
                        break;
                    case 8:
                        //艦上攻撃機
                        totalSakutekiAutumn += itemDto.getSaku() * 1.37;
                        break;
                    case 9:
                        //艦上偵察機
                        totalSakutekiAutumn += itemDto.getSaku() * 1.66;
                        break;
                    case 10:
                        //水上偵察機
                        totalSakutekiAutumn += itemDto.getSaku() * 2.00;
                        break;
                    case 11:
                        //水上爆撃機
                        totalSakutekiAutumn += itemDto.getSaku() * 1.78;
                        break;
                    case 12:
                        //小型電探
                        totalSakutekiAutumn += itemDto.getSaku() * 1.00;
                        break;
                    case 13:
                        //大型電探
                        totalSakutekiAutumn += itemDto.getSaku() * 0.99;
                        break;
                    case 29:
                        //探照灯
                        totalSakutekiAutumn += itemDto.getSaku() * 0.91;
                        break;
                    }
                    saku -= itemDto.getSaku();
                    names.add(itemDto.getName());
                }
            }
            totalSakutekiAutumn += Math.sqrt(saku) * 1.69;
            if (drum) {
                totalDrumShip++;
            }
            if (dmgcsty > 0) {
                this.dmgcstyLabels[i].setText("要員x" + dmgcsty);
                this.dmgcstyLabels[i].setEnabled(true);
                this.dmgcstyLabels[i].setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));

            } else {
                this.dmgcstyLabels[i].setText("");
                this.dmgcstyLabels[i].setEnabled(false);
                this.dmgcstyLabels[i].setForeground(null);
            }
            if (dmgcstm > 0) {
                this.dmgcstmLabels[i].setText("女神x" + dmgcstm);
                this.dmgcstmLabels[i].setEnabled(true);
                this.dmgcstmLabels[i].setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));

            } else {
                this.dmgcstmLabels[i].setText("");
                this.dmgcstmLabels[i].setEnabled(false);
                this.dmgcstmLabels[i].setForeground(null);
            }
            // ステータス.あと何回
            if (AppConfig.get().isDisplayCount()) {
                Integer nextcount = this.getNextCount(ship, i == 0);
                if (nextcount != null) {
                    this.nextLabels[i].setText(MessageFormat.format("あと{0}回", nextcount));
                } else {
                    this.nextLabels[i].setText("");
                }
            }

            // コンディション
            if (cond <= AppConstants.COND_RED) {
                // 疲労19以下
                if (AppConfig.get().isWarnByCondState()) {
                    // 疲労状態で警告アイコン
                    this.state.set(WARN);
                    shipstatus.set(WARN);
                }
                this.condLabels[i].setForeground(SWTResourceManager.getColor(AppConstants.COND_RED_COLOR));
                this.condstLabels[i].setForeground(SWTResourceManager.getColor(AppConstants.COND_RED_COLOR));
            } else if (cond <= AppConstants.COND_ORANGE) {
                // 疲労29以下
                if (AppConfig.get().isWarnByCondState()) {
                    // 疲労状態で警告アイコン
                    this.state.set(WARN);
                    shipstatus.set(WARN);
                }
                this.condLabels[i].setForeground(SWTResourceManager.getColor(AppConstants.COND_ORANGE_COLOR));
                this.condstLabels[i].setForeground(SWTResourceManager.getColor(AppConstants.COND_ORANGE_COLOR));
            } else if ((cond >= AppConstants.COND_DARK_GREEN) && (cond < AppConstants.COND_GREEN)) {
                // 疲労50以上
                this.condLabels[i].setForeground(SWTResourceManager.getColor(AppConstants.COND_DARK_GREEN_COLOR));
                this.condstLabels[i].setForeground(SWTResourceManager.getColor(AppConstants.COND_DARK_GREEN_COLOR));
            } else if (cond >= AppConstants.COND_GREEN) {
                // 疲労53以上
                this.condLabels[i].setForeground(SWTResourceManager.getColor(AppConstants.COND_GREEN_COLOR));
                this.condstLabels[i].setForeground(SWTResourceManager.getColor(AppConstants.COND_GREEN_COLOR));
            } else {
                this.condLabels[i].setForeground(null);
                this.condstLabels[i].setForeground(null);
            }

            // 艦娘の状態アイコンを更新
            if (shipstatus.get(FATAL)) {
                this.iconLabels[i].setImage(SWTResourceManager.getImage(FleetComposite.class,
                        AppConfig.get().isMonoIcon()
                                ? AppConstants.R_ICON_EXCLAMATION_MONO
                                : AppConstants.R_ICON_EXCLAMATION));
            } else if (shipstatus.get(WARN)) {
                this.iconLabels[i].setImage(SWTResourceManager.getImage(FleetComposite.class,
                        AppConfig.get().isMonoIcon()
                                ? AppConstants.R_ICON_ERROR_MONO
                                : AppConstants.R_ICON_ERROR));
            } else {
                this.iconLabels[i].setImage(null);
            }

            // ラベルを更新する
            // 名前
            this.nameLabels[i].setText(ship.getName());
            this.nameLabels[i].setToolTipText(MessageFormat.format(AppConstants.TOOLTIP_FLEETTAB_SHIP, nowhp, maxhp,
                    fuel, fuelmax, bull, bullmax, ship.getNext(), StringUtils.join(names, "\n  ")));
            this.lvLabels[i].setText(MessageFormat.format("(Lv.{0})", ship.getLv()));
            // HP
            this.hpLabels[i].setText(MessageFormat.format("{0}/{1} ", nowhp, maxhp));
            // HPゲージ
            Image gauge = this.getHpGaugeImage(hpratio, expraito);
            this.hpgaugeLabels[i].setImage(gauge);
            if (this.hpgaugeImages[i] != null) {
                // 古いイメージを破棄
                this.hpgaugeImages[i].dispose();
            }
            this.hpgaugeImages[i] = gauge;
            // コンディション
            this.condLabels[i].setText(MessageFormat.format("{0} cond.", cond));
            this.bullstLabels[i].getParent().layout();
        }
        // メッセージを更新する
        // 入渠中の艦娘を探す
        boolean isBathwater = false;
        for (ShipDto shipDto : ships) {
            if (GlobalContext.isNdock(shipDto)) {
                isBathwater = true;
                break;
            }
        }
        // 制空値を計算
        int seiku = 0;
        for (ShipDto shipDto : ships) {
            seiku += shipDto.getSeiku();
        }
        // 索敵値を計算
        // 偵察機索敵値×2 ＋ 電探索敵値 ＋ √(艦隊の装備込み索敵値合計 - 偵察機索敵値 - 電探索敵値)
        BigDecimal sakuteki = BigDecimal.valueOf((totalSakutekiSurvey * 2) + totalSakutekiRader
                + Math.sqrt(totalSakuteki - totalSakutekiSurvey - totalSakutekiRader));
        sakuteki = sakuteki.setScale(2, RoundingMode.HALF_UP);

        if (GlobalContext.isMission(this.dock.getId())) {
            // 遠征中
            StyleRange style = new StyleRange();
            style.fontStyle = SWT.BOLD;
            style.foreground = SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE);
            this.addStyledText(this.message, AppConstants.MESSAGE_MISSION, style);
        } else if (isBathwater) {
            // 入渠中
            StyleRange style = new StyleRange();
            style.fontStyle = SWT.BOLD;
            style.foreground = SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE);
            this.addStyledText(this.message,
                    MessageFormat.format(AppConstants.MESSAGE_BAD, AppConstants.MESSAGE_BATHWATER), style);
        } else if (this.badlyDamage) {
            // 大破
            StyleRange style = new StyleRange();
            style.fontStyle = SWT.BOLD;
            style.underline = true;
            style.underlineStyle = SWT.UNDERLINE_SQUIGGLE;
            style.underlineColor = SWTResourceManager.getColor(SWT.COLOR_RED);
            style.foreground = SWTResourceManager.getColor(SWT.COLOR_RED);
            this.addStyledText(this.message,
                    MessageFormat.format(AppConstants.MESSAGE_BAD, AppConstants.MESSAGE_BADLY_DAMAGE), style);
        } else {
            // 出撃可能
            StyleRange style = new StyleRange();
            style.fontStyle = SWT.BOLD;
            style.foreground = SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN);
            this.addStyledText(this.message, AppConstants.MESSAGE_GOOD, style);
        }
        if (this.clearDateString != null) {
            StyleRange style = new StyleRange();
            style.data = this.clearDate;
            style.underline = true;
            style.underlineStyle = SWT.UNDERLINE_LINK;
            this.addStyledText(this.message, MessageFormat.format(AppConstants.MESSAGE_COND, this.clearDateString),
                    style);
        }
        // 制空
        this.addStyledText(this.message, MessageFormat.format(AppConstants.MESSAGE_SEIKU, seiku), null);
        // 索敵
        this.addStyledText(this.message,
                MessageFormat.format(AppConstants.MESSAGE_SAKUTEKI, sakuteki, totalSakutekiAutumn), null);
        // 合計Lv
        this.addStyledText(this.message, MessageFormat.format(AppConstants.MESSAGE_TOTAL_LV, totallv), null);
        // ドラム缶
        this.addStyledText(this.message, MessageFormat.format(AppConstants.MESSAGE_DRUM, totalDrumShip, totalDrum),
                null);

        this.updateTabIcon();
        this.postFatal();

        this.fleetGroup.layout();

        this.getShell().setRedraw(true);
    }

    /**
     * 艦隊タブのアイコンを更新します
     */
    private void updateTabIcon() {
        if (this.state.get(FATAL)) {
            this.tab.setImage(SWTResourceManager.getImage(FleetComposite.class,
                    AppConfig.get().isMonoIcon()
                            ? AppConstants.R_ICON_EXCLAMATION_MONO
                            : AppConstants.R_ICON_EXCLAMATION));
        } else if (this.state.get(WARN)) {
            this.tab.setImage(SWTResourceManager.getImage(FleetComposite.class,
                    AppConfig.get().isMonoIcon()
                            ? AppConstants.R_ICON_ERROR_MONO
                            : AppConstants.R_ICON_ERROR));
        } else {
            this.tab.setImage(null);
        }
    }

    /**
     * 艦隊が出撃中で大破した場合に警告を行います
     */
    private void postFatal() {
        if (this.badlyDamage && GlobalContext.isSortie(this.dock.getId())) {
            if (AppConfig.get().isBalloonBybadlyDamage()) {
                List<ShipDto> ships = this.dock.getShips();
                StringBuilder sb = new StringBuilder();
                sb.append(AppConstants.MESSAGE_STOP_SORTIE);
                sb.append("\n");
                for (ShipDto shipDto : ships) {
                    if (shipDto.isBadlyDamage()) {
                        sb.append(shipDto.getName());
                        sb.append("(" + shipDto.getLv() + ")");
                        sb.append(" : ");
                        List<ItemDto> items = shipDto.getItem();
                        List<String> names = new ArrayList<String>();
                        for (ItemDto itemDto : items) {
                            if (itemDto != null) {
                                names.add(itemDto.getName());
                            }
                        }
                        sb.append(StringUtils.join(names, ","));
                        sb.append("\n");
                    }
                }
                ToolTip tip = new ToolTip(this.getShell(), SWT.BALLOON
                        | SWT.ICON_ERROR);
                tip.setText("大破警告");
                tip.setMessage(sb.toString());

                this.main.getTrayItem().setToolTip(tip);
                tip.setVisible(true);
            }
            // 大破時にサウンドを再生する
            Sound.randomBadlySoundPlay();
        }
    }

    /**
     * HPゲージのイメージを取得します
     * @param hpratio HP割合
     * @return HPゲージのイメージ
     */
    private Image getHpGaugeImage(float hpratio, float expraito) {
        Image image = new Image(Display.getDefault(), GAUGE_WIDTH, GAUGE_HEIGHT);
        GC gc = new GC(image);
        gc.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        gc.fillRectangle(0, 0, GAUGE_WIDTH, GAUGE_HEIGHT);
        gc.setBackground(SWTResourceManager.getColor(gradation(hpratio, GAUGE_EMPTY, GAUGE_HALF, GAUGE_FULL)));
        gc.fillRectangle(0, 0, (int) (GAUGE_WIDTH * hpratio), GAUGE_HEIGHT);
        gc.setBackground(SWTResourceManager.getColor(EXP_GAUGE));
        gc.fillRectangle(0, GAUGE_HEIGHT - EXP_GAUGE_HEIGHT, (int) (GAUGE_WIDTH * expraito), EXP_GAUGE_HEIGHT);
        gc.drawImage(image, 0, 0);
        gc.dispose();
        return image;
    }

    /**
     * スタイル付きテキストを設定します
     *
     * @param text StyledText
     * @param str 文字
     * @param style スタイル
     */
    private void addStyledText(StyledText text, String str, StyleRange style) {
        StyleRange[] oldranges = text.getStyleRanges();
        String beforeText = text.getText();
        StyleRange addStyle = style;
        if (addStyle == null) {
            addStyle = new StyleRange();
        }
        addStyle.start = beforeText.length();
        addStyle.length = str.length();

        StyleRange[] ranges = new StyleRange[oldranges.length + 1];
        for (int i = 0; i < oldranges.length; i++) {
            ranges[i] = oldranges[i];
        }
        ranges[oldranges.length] = addStyle;

        text.setText(beforeText + str);
        text.setStyleRanges(ranges);
    }

    /**
     * あと何回戦闘すればよいかを取得します
     *
     * @param ship 艦娘
     * @param isFlagship 旗艦
     * @return 回数
     */
    @CheckForNull
    private Integer getNextCount(ShipDto ship, boolean isFlagship) {
        // 次のレベルに必要な経験値
        Long nextexp = CalcExpUtils.getNextLvExp((int) ship.getLv());
        if (nextexp != null) {
            // 必要経験値
            long needexp = nextexp - ship.getExp();
            // 海域Exp
            Integer baseexp = SeaExp.get().get(AppConfig.get().getDefaultSea());
            // 評価倍率
            Double eval = EvaluateExp.get().get(AppConfig.get().getDefaultEvaluate());
            if ((baseexp != null) && (eval != null)) {
                // 得られる経験値
                long getexpd = CalcExpUtils.getExp(baseexp, eval, isFlagship, false);
                // 戦闘回数
                int count = CalcExpUtils.getCount(needexp, getexpd);
                return Integer.valueOf(count);
            }
        }
        return null;
    }

    @Override
    public void dispose() {
        super.dispose();
        this.large.dispose();
        this.small.dispose();
        for (Image image : this.hpgaugeImages) {
            if (image != null) {
                image.dispose();
            }
        }
    }

    /**
     * 複数の色の中間色を取得する
     *
     * @param raito 割合
     * @param rgbs 色たち
     * @return 色
     */
    private static RGB gradation(float raito, RGB... rgbs) {
        if (raito <= 0.0f) {
            return rgbs[0];
        }
        if (raito >= 1.0f) {
            return rgbs[rgbs.length - 1];
        }
        int length = rgbs.length - 1;

        // 開始色
        int start = (int) (length * raito);
        // 終了色
        int end = start + 1;
        // 開始色と終了色の割合を算出
        float startPer = (float) start / length;
        float endPer = (float) end / length;
        float subPer = (raito - startPer) / (endPer - startPer);
        return gradation(subPer, rgbs[start], rgbs[end]);
    }

    /**
     * 2つの色の中間色を取得する
     *
     * @param raito 割合
     * @param start 開始色
     * @param end 終了色
     * @return 色
     */
    private static RGB gradation(float raito, RGB start, RGB end) {
        int r = (int) (start.red + ((end.red - start.red) * raito));
        int g = (int) (start.green + ((end.green - start.green) * raito));
        int b = (int) (start.blue + ((end.blue - start.blue) * raito));
        return new RGB(r, g, b);
    }
}
