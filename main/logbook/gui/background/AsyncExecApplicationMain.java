package logbook.gui.background;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import logbook.config.AppConfig;
import logbook.constants.AppConstants;
import logbook.data.context.GlobalContext;
import logbook.data.context.ItemContext;
import logbook.data.context.ShipContext;
import logbook.dto.DeckMissionDto;
import logbook.dto.DockDto;
import logbook.dto.NdockDto;
import logbook.dto.ShipDto;
import logbook.gui.ApplicationMain;
import logbook.gui.logic.TimeLogic;
import logbook.gui.widgets.FleetComposite;
import logbook.thread.PlayerThread;
import logbook.util.SwtUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TaskItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * 非同期にメイン画面を更新します
 */
public final class AsyncExecApplicationMain extends Thread {

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(AsyncExecApplicationMain.class);
    }

    private static final int ONE_MINUTES = 60;

    private final ApplicationMain main;

    /**
     * 非同期にメイン画面を更新するスレッドのコンストラクター
     *
     * @param main メイン画面
     */
    public AsyncExecApplicationMain(ApplicationMain main) {
        this.main = main;
        this.setName("logbook_async_exec_application_main");
    }

    /**
     * 現在のメイン画面を更新します
     */
    @Override
    public void run() {
        try {
            boolean update = GlobalContext.updateContext();
            if (update) {
                // 保有アイテム数を更新する
                Display.getDefault().asyncExec(new UpdateItemCountTask(this.main));
                // 保有艦娘数を更新する
                Display.getDefault().asyncExec(new UpdateShipCountTask(this.main));
                // 艦隊タブを更新する
                Display.getDefault().asyncExec(new UpdateFleetTabTask(this.main));
            }
            // 遠征と入渠を更新する
            Display.getDefault().asyncExec(new UpdateDeckNdockTask(this.main));
        } catch (Exception e) {
            LoggerHolder.LOG.fatal("スレッドが異常終了しました", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 2つの日付から残り時間を計算する
     *
     * @param date1
     * @param date2
     * @return
     */
    private static long getRest(Date date1, Date date2) {
        return TimeUnit.MILLISECONDS.toSeconds(date2.getTime() - date1.getTime());
    }

    /**
     * 保有アイテム数を更新する
     */
    private static final class UpdateItemCountTask implements Runnable {

        private final ApplicationMain main;

        /**
         * コンストラクター
         */
        public UpdateItemCountTask(ApplicationMain main) {
            this.main = main;
        }

        @Override
        public void run() {
            Button itemList = this.main.getItemList();
            String setText = "所有装備(" + ItemContext.get().size() + "/"
                    + GlobalContext.maxSlotitem() + ")";
            if (!setText.equals(itemList.getText())) {
                itemList.setText(setText);
                itemList.getParent().layout();
            }
        }
    }

    /**
     * 保有艦娘数を更新する
     */
    private static final class UpdateShipCountTask implements Runnable {

        private final ApplicationMain main;

        /**
         * コンストラクター
         */
        public UpdateShipCountTask(ApplicationMain main) {
            this.main = main;
        }

        @Override
        public void run() {
            Button shipList = this.main.getShipList();
            String setText = "所有艦娘(" + ShipContext.get().size() + "/" + GlobalContext.maxChara()
                    + ")";
            if (setText.equals(shipList.getText())) {
                return;
            }

            shipList.setText(setText);
            shipList.getParent().layout();

            if (AppConfig.get().isUseTaskbarNotify()) {
                TaskItem item = SwtUtils.getTaskBarItem(this.main.getShell());
                if (item != null) {
                    int max = GlobalContext.maxChara();
                    int size = ShipContext.get().size();
                    int locked = 0;
                    for (Entry<Long, ShipDto> entry : ShipContext.get().entrySet()) {
                        if (entry.getValue().getLocked()) {
                            locked++;
                        }
                    }
                    int r = Math.round(((float) (size - locked) / (float) (max - locked)) * 100);

                    item.setProgress(r);

                    if (max <= (size + AppConfig.get().getNotifyFully())) {
                        item.setProgressState(SWT.PAUSED);
                    } else {
                        item.setProgressState(SWT.NORMAL);
                    }
                }
            }
        }
    }

    /**
     * 遠征と入渠を更新する
     */
    private static final class UpdateDeckNdockTask implements Runnable {

        private static final boolean[] FLAG_NOTICE_DECK = { false, false, false };
        private static final boolean[] FLAG_NOTICE_NDOCK = { false, false, false, false };

        private final ApplicationMain main;

        private static ToolTip tip = null;

        private static Date last = null;

        /** 日付フォーマット */
        private final SimpleDateFormat format = new SimpleDateFormat(AppConstants.DATE_SHORT_FORMAT);

        /**
         * コンストラクター
         */
        public UpdateDeckNdockTask(ApplicationMain main) {
            this.main = main;
        }

        @Override
        public void run() {
            // 現在時刻
            Date now = Calendar.getInstance().getTime();
            boolean noticeflg = false;
            boolean visibleHome = false;
            // 遠征を更新する
            if (this.updateDeck(now)) {
                noticeflg = true;
                PlayerThread.randomExpeditionSoundPlay();
                visibleHome |= AppConfig.get().isVisibleOnReturnMission();
            }
            // 入渠を更新する
            if (this.updateNdock(now)) {
                noticeflg = true;
                PlayerThread.randomDockSoundPlay();
                visibleHome |= AppConfig.get().isVisibleOnReturnBathwater();
            }
            if (visibleHome) {
                this.main.getTabFolder().setSelection(0);
            }
            if (AppConfig.get().isUseBalloon()) {
                // バルーンツールチップを表示する
                try {
                    // 遠征・入渠のお知らせ
                    if (noticeflg) {
                        last = now;
                        List<String> notice = new ArrayList<String>();

                        DeckMissionDto[] deckMissions = GlobalContext.getDeckMissions();
                        for (int i = 0; i < deckMissions.length; i++) {
                            if (FLAG_NOTICE_DECK[i] && (deckMissions[i].getMission() != null)) {
                                notice.add(deckMissions[i].getName() + " (" + deckMissions[i].getMission() + ")"
                                        + " がまもなく帰投します");
                            }
                        }

                        Map<Long, ShipDto> shipMap = ShipContext.get();
                        NdockDto[] ndocks = GlobalContext.getNdocks();
                        for (int i = 0; i < ndocks.length; i++) {
                            if (FLAG_NOTICE_NDOCK[i] && (ndocks[i].getNdockid() != 0)) {
                                ShipDto ship = shipMap.get(Long.valueOf(ndocks[i].getNdockid()));
                                if (ship != null) {
                                    notice.add(ship.getName() + " (Lv" + ship.getLv() + ")" + " がまもなくお風呂からあがります");
                                }
                            }
                        }

                        if ((tip == null) || !tip.isVisible()) {
                            tip = new ToolTip(this.main.getShell(), SWT.BALLOON | SWT.ICON_INFORMATION);
                        }
                        tip.setText("遠征・入渠");
                        tip.setMessage(StringUtils.join(notice, "\r\n"));
                        this.main.getTrayItem().setToolTip(tip);
                        tip.setVisible(true);
                    }
                } catch (Exception e) {
                    LoggerHolder.LOG.warn("お知らせの表示に失敗しました", e);
                }
            }
        }

        /**
         * 遠征を更新する
         *
         * @param now
         * @param notice
         * @return
         */
        private boolean updateDeck(Date now) {
            boolean noticeflg = false;
            boolean remind = false;

            Label[] deckNameLabels = { this.main.getDeck1name(), this.main.getDeck2name(), this.main.getDeck3name() };
            Text[] deckTimeTexts = { this.main.getDeck1time(), this.main.getDeck2time(), this.main.getDeck3time() };

            DeckMissionDto[] deckMissions = GlobalContext.getDeckMissions();

            for (int i = 0; i < deckMissions.length; i++) {
                String time = "";
                String dispname = "";
                if (deckMissions[i].getMission() != null) {
                    dispname = deckMissions[i].getName() + " (" + deckMissions[i].getMission() + ")";

                    if (deckMissions[i].getTime() != null) {
                        long rest = getRest(now, deckMissions[i].getTime());

                        // ツールチップテキストで時刻を表示する
                        deckTimeTexts[i].setToolTipText(this.format.format(deckMissions[i].getTime()));

                        // 20分前、10分前、5分前になったら背景色を変更する
                        if (rest <= (ONE_MINUTES * 5)) {
                            deckTimeTexts[i].setBackground(SWTResourceManager
                                    .getColor(AppConstants.TIME_IN_5_MIN));
                        } else if (rest <= (ONE_MINUTES * 10)) {
                            deckTimeTexts[i].setBackground(SWTResourceManager
                                    .getColor(AppConstants.TIME_IN_10_MIN));
                        } else if (rest <= (ONE_MINUTES * 20)) {
                            deckTimeTexts[i].setBackground(SWTResourceManager
                                    .getColor(AppConstants.TIME_IN_20_MIN));
                        } else {
                            deckTimeTexts[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
                        }
                        if (this.main.getDeckNotice().getSelection()) {
                            if ((rest <= ONE_MINUTES) && !FLAG_NOTICE_DECK[i]) {
                                noticeflg = true;
                                FLAG_NOTICE_DECK[i] = true;
                            } else if (AppConfig.get().isMissionRemind() && (rest < -1)) {
                                // リマインドする
                                remind = true;
                            } else if (rest > ONE_MINUTES) {
                                FLAG_NOTICE_DECK[i] = false;
                            }
                        } else {
                            FLAG_NOTICE_DECK[i] = false;
                        }
                        time = TimeLogic.toDateRestString(rest);
                        if (time == null) {
                            time = "まもなく帰投します";
                        }
                    }
                } else {
                    FLAG_NOTICE_DECK[i] = false;
                    deckTimeTexts[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
                    deckTimeTexts[i].setToolTipText(null);
                }
                deckNameLabels[i].setText(dispname);
                deckTimeTexts[i].setText(time);
            }
            if (remind && (last != null) && AppConfig.get().isMissionRemind()
                    && ((getRest(now, last) % AppConfig.get().getRemindInterbal()) == 0)) {
                // 定期的にリマインドする
                noticeflg = true;
            }
            return noticeflg;
        }

        /**
         * 入渠を更新する
         *
         * @param now
         * @param notice
         * @return
         */
        private boolean updateNdock(Date now) {
            boolean noticeflg = false;

            Map<Long, ShipDto> shipMap = ShipContext.get();

            Label[] ndockNameLabels = { this.main.getNdock1name(), this.main.getNdock2name(),
                    this.main.getNdock3name(), this.main.getNdock4name() };
            Text[] ndockTimeTexts = { this.main.getNdock1time(), this.main.getNdock2time(), this.main.getNdock3time(),
                    this.main.getNdock4time() };

            NdockDto[] ndocks = GlobalContext.getNdocks();

            for (int i = 0; i < ndocks.length; i++) {
                String name = "";
                String time = "";

                if (ndocks[i].getNdockid() != 0) {
                    ShipDto ship = shipMap.get(Long.valueOf(ndocks[i].getNdockid()));
                    if (ship != null) {
                        name = ship.getName() + " (Lv" + ship.getLv() + ")";
                        long rest = getRest(now, ndocks[i].getNdocktime());

                        // ツールチップテキストで時刻を表示する
                        ndockTimeTexts[i].setToolTipText(this.format.format(ndocks[i].getNdocktime()));

                        // 20分前、10分前、5分前になったら背景色を変更する
                        if (rest <= (ONE_MINUTES * 5)) {
                            ndockTimeTexts[i].setBackground(SWTResourceManager
                                    .getColor(AppConstants.TIME_IN_5_MIN));
                        } else if (rest <= (ONE_MINUTES * 10)) {
                            ndockTimeTexts[i].setBackground(SWTResourceManager
                                    .getColor(AppConstants.TIME_IN_10_MIN));
                        } else if (rest <= (ONE_MINUTES * 20)) {
                            ndockTimeTexts[i].setBackground(SWTResourceManager
                                    .getColor(AppConstants.TIME_IN_20_MIN));
                        } else {
                            ndockTimeTexts[i].setBackground(SWTResourceManager
                                    .getColor(SWT.COLOR_WHITE));
                        }
                        if (this.main.getNdockNotice().getSelection()) {

                            if ((rest <= ONE_MINUTES) && !FLAG_NOTICE_NDOCK[i]) {
                                noticeflg = true;
                                FLAG_NOTICE_NDOCK[i] = true;
                            } else if (rest > ONE_MINUTES) {
                                FLAG_NOTICE_NDOCK[i] = false;
                            }
                        } else {
                            FLAG_NOTICE_NDOCK[i] = false;
                        }
                        time = TimeLogic.toDateRestString(rest);
                        if (time == null) {
                            time = "まもなくお風呂からあがります";
                        }
                    }
                } else {
                    FLAG_NOTICE_NDOCK[i] = false;
                    ndockTimeTexts[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
                    ndockTimeTexts[i].setToolTipText(null);
                }
                ndockNameLabels[i].setText(name);
                ndockTimeTexts[i].setText(time);
            }
            return noticeflg;
        }
    }

    /**
     * 艦隊タブを更新する
     */
    private static final class UpdateFleetTabTask implements Runnable {

        private static String[] dockname = new String[4];
        private static CTabItem[] tabItems = new CTabItem[4];
        private static FleetComposite[] dockComposites = new FleetComposite[4];

        private final ApplicationMain main;

        /**
         * コンストラクター
         */
        public UpdateFleetTabTask(ApplicationMain main) {
            this.main = main;
        }

        @Override
        public void run() {
            // タブを更新する
            CTabItem maintab = this.main.getTabFolder().getItem(0);
            maintab.setToolTipText(
                    "装備:" + ItemContext.get().size() + "/"
                            + GlobalContext.maxSlotitem()
                            + " 艦娘:" + ShipContext.get().size() + "/"
                            + GlobalContext.maxChara());

            for (int i = 0; i < 4; i++) {
                DockDto dock = GlobalContext.getDock(Integer.toString(i + 1));
                if (dock != null) {
                    FleetComposite tabComposite = dockComposites[i];
                    CTabItem tabItem = tabItems[i];

                    if (tabItem == null) {

                        tabItem = new CTabItem(this.main.getTabFolder(), SWT.NONE);
                        tabItem.setText(dock.getName());

                        // メインコンポジット
                        tabComposite = new FleetComposite(this.main.getTabFolder(), tabItem, this.main);
                        tabItem.setControl(tabComposite);

                        tabItems[i] = tabItem;
                        dockComposites[i] = tabComposite;
                    }
                    if (!dock.getName().equals(dockname[i])) {
                        dockname[i] = dock.getName();
                    }
                    tabComposite.updateFleet(dock);
                    tabItem.setText(dock.getName());
                }
            }
        }
    }
}
