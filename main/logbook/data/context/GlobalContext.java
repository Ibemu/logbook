package logbook.data.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonValue;

import logbook.config.AppConfig;
import logbook.config.KdockConfig;
import logbook.config.QuestConfig;
import logbook.config.bean.QuestBean;
import logbook.constants.AppConstants;
import logbook.data.Data;
import logbook.data.DataQueue;
import logbook.data.EventSender;
import logbook.data.event.CallScript;
import logbook.data.event.Material;
import logbook.data.event.RemodelSlot;
import logbook.dto.BattleDto;
import logbook.dto.BattleResultDto;
import logbook.dto.CreateItemDto;
import logbook.dto.DeckMissionDto;
import logbook.dto.DockDto;
import logbook.dto.GetShipDto;
import logbook.dto.ItemDto;
import logbook.dto.MissionResultDto;
import logbook.dto.NdockDto;
import logbook.dto.QuestDto;
import logbook.dto.ResourceDto;
import logbook.dto.ShipDto;
import logbook.dto.ShipInfoDto;
import logbook.gui.ApplicationMain;
import logbook.gui.logic.CreateReportLogic;
import logbook.internal.Deck;
import logbook.internal.Item;
import logbook.internal.QuestDue;
import logbook.internal.Ship;
import logbook.internal.ShipStyle;
import logbook.internal.SortiePhase;
import logbook.thread.PlayerThread;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolTip;

/**
 * 遠征・入渠などの情報を管理します
 *
 */
public final class GlobalContext {

    private GlobalContext() {
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(GlobalContext.class);
    }

    private static class GlobalContextHolder {

        /** イベント送信 */
        private static final EventSender SENDER = new EventSender();

        static {
            SENDER.addEventListener(new CallScript());
            SENDER.addEventListener(new RemodelSlot());
            SENDER.addEventListener(new Material());
        }
    }

    /** 建造 */
    private static List<GetShipDto> getShipList = new ArrayList<GetShipDto>();

    /** 建造(投入資源) */
    private static Map<String, ResourceDto> getShipResource = new HashMap<String, ResourceDto>();

    /** 開発 */
    private static List<CreateItemDto> createItemList = new ArrayList<CreateItemDto>();

    /** 海戦・ドロップ */
    private static List<BattleResultDto> battleResultList = new ArrayList<BattleResultDto>();

    /** 遠征結果 */
    private static List<MissionResultDto> missionResultList = new ArrayList<MissionResultDto>();

    /** 司令部Lv */
    private static int hqLevel;

    /** 最大保有可能 艦娘数 */
    private static int maxChara;

    /** 最大保有可能 装備数 */
    private static int maxSlotitem;

    /** 最後に建造を行った建造ドック */
    private static String lastBuildKdock;

    /** 戦闘詳細 */
    private static List<BattleDto> battleList = new ArrayList<BattleDto>();

    /** ボス戦 */
    private static boolean bossArrive = false;

    /** 出撃 */
    private static SortiePhase sortiePhase = SortiePhase.BATTLE_RESULT;

    /** 戦闘回数 */
    private static int battleCount = 0;

    /** 遠征リスト */
    private static DeckMissionDto[] deckMissions = new DeckMissionDto[] { DeckMissionDto.EMPTY, DeckMissionDto.EMPTY,
            DeckMissionDto.EMPTY };

    /** ドック */
    private static Map<String, DockDto> dock = new HashMap<String, DockDto>();

    /** 入渠リスト */
    private static NdockDto[] ndocks = new NdockDto[] { NdockDto.EMPTY, NdockDto.EMPTY, NdockDto.EMPTY,
            NdockDto.EMPTY };

    /** 任務Map */
    private static SortedMap<Integer, QuestDto> questMap = new ConcurrentSkipListMap<Integer, QuestDto>();

    /** 出撃中か */
    private static boolean[] isSortie = new boolean[4];

    /** 出撃(START)か */
    private static boolean isStart;

    /** 今いるマップ上のエリアNo */
    private static int mapAreaNo;

    /** 今いるマップ上のマップNo */
    private static int mapInfoNo;

    /** 今いるマップ上のマスNo */
    private static int mapCellNo;

    /** 今いるマップ上のボスNo */
    private static int mapBossCellNo;

    /** イベント ID */
    private static int eventId;

    /** 連合艦隊 */
    private static int combined;

    /** イベント送信 */
    private final EventSender sender = new EventSender();

    /**
     * @return エリアNo
     */
    public static int getMapAreaNo() {
        return mapAreaNo;
    }

    /**
     * @return マップNo
     */
    public static int getMapInfoNo() {
        return mapInfoNo;
    }

    /**
     * @return マスNo
     */
    public static int getMapCellNo() {
        return mapCellNo;
    }

    /**
     * @return ボス到着
     */
    public static boolean isBossArrive() {
        return bossArrive;
    }

    /**
     * @return 出撃
     */
    public static SortiePhase getSortiePhase() {
        return sortiePhase;
    }

    /**
     * @return 戦闘回数
     */
    public static int getBattleCount() {
        return battleCount;
    }

    /**
     * @return 司令部Lv
     */
    public static int hqLevel() {
        return hqLevel;
    }

    /**
     * @return 最大保有可能 艦娘数
     */
    public static int maxChara() {
        return maxChara;
    }

    /**
     * @return 最大保有可能 装備数
     */
    public static int maxSlotitem() {
        return maxSlotitem;
    }

    /**
     * @return 建造艦娘List
     */
    public static List<GetShipDto> getGetshipList() {
        return getShipList;
    }

    /**
     * @return 開発アイテムList
     */
    public static List<CreateItemDto> getCreateItemList() {
        return createItemList;
    }

    /**
     * @return 海戦List
     */
    public static List<BattleDto> getBattleList() {
        return battleList;
    }

    /**
     * @return 海戦・ドロップList
     */
    public static List<BattleResultDto> getBattleResultList() {
        return battleResultList;
    }

    /**
     * @return 遠征結果
     */
    public static List<MissionResultDto> getMissionResultList() {
        return missionResultList;
    }

    /**
     * @return 遠征リスト
     */
    public static DeckMissionDto[] getDeckMissions() {
        return deckMissions;
    }

    /**
     * @return 入渠リスト
     */
    public static NdockDto[] getNdocks() {
        return ndocks;
    }

    /**
     * @return イベント送信
     */
    public static EventSender getEventSender() {
        return GlobalContextHolder.SENDER;
    }

    /**
     * 艦娘が入渠しているかを調べます
     *
     * @param ship 艦娘
     * @return 入渠している場合true
     */
    public static boolean isNdock(ShipDto ship) {
        return isNdock(ship.getId());
    }

    /**
     * 艦娘が入渠しているかを調べます
     * @param ship 艦娘ID
     * @return 入渠している場合true
     */
    public static boolean isNdock(long ship) {
        for (NdockDto ndock : ndocks) {
            if (ship == ndock.getNdockid()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 艦隊が遠征中かを調べます
     *
     * @param
     */
    public static boolean isMission(String idstr) {
        int id = Integer.parseInt(idstr);
        for (int i = 0; i < deckMissions.length; i++) {
            if ((deckMissions[i].getMission() != null) && (deckMissions[i].getFleetid() == id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return ドック
     */
    public static DockDto getDock(String id) {
        return dock.get(id);
    }

    /**
     * 任務を取得します
     * @return 任務
     */
    public static Map<Integer, QuestDto> getQuest() {
        return questMap;
    }

    /**
     * 出撃中かを調べます
     * @return 出撃中
     */
    public static boolean isSortie(String idstr) {
        int id = Integer.parseInt(idstr);
        return isSortie[id - 1];
    }

    /**
     * 情報を更新します
     *
     * @return 更新する情報があった場合trueを返します
     */
    public static boolean updateContext() {
        boolean update = false;
        Data data;
        while ((data = DataQueue.poll()) != null) {
            update = true;

            getEventSender().syncSendEvent(data.getDataType(), data);

            switch (data.getDataType()) {
            // 補給
            case CHARGE:
                doCharge(data);
                break;
            // 編成
            case CHANGE:
                doChange(data);
                break;
            // 母港
            case PORT:
                doPort(data);
                break;
            // 保有装備
            case SLOTITEM_MEMBER:
                doSlotitemMember(data);
                break;
            // 保有艦
            case SHIP3:
                doShip3(data);
                break;
            // 保有艦
            case SHIP2:
                doShip2(data);
                break;
            // 保有艦
            case SHIP_DECK:
                doShipDeck(data);
                break;
            // 基本
            case BASIC:
                doBasic(data);
                break;
            // 遠征
            case MISSION_START:
                doMission(data);
                break;
            // 遠征(帰還)
            case MISSION_RESULT:
                doMissionResult(data);
                break;
            // 入渠
            case NDOCK:
                doNdock(data);
                break;
            // 建造
            case CREATE_SHIP:
                doCreateship(data);
                break;
            // 建造ドック
            case KDOCK:
                doKdock(data);
                break;
            // 建造(入手)
            case GET_SHIP:
                doGetship(data);
                break;
            // 装備開発
            case CREATE_ITEM:
                doCreateitem(data);
                break;
            // 解体
            case DESTROY_SHIP:
                doDestroyShip(data);
                break;
            // 廃棄
            case DESTROY_ITEM2:
                doDestroyItem2(data);
                break;
            // 近代化改修
            case POWERUP:
                doPowerup(data);
                break;
            // 海戦
            case BATTLE:
                doBattle(data);
                break;
            // 海戦
            case BATTLE_MIDNIGHT:
                doBattleNight(data);
                break;
            // 海戦
            case BATTLE_SP_MIDNIGHT:
                doBattleNight(data);
                break;
            // 海戦
            case BATTLE_NIGHT_TO_DAY:
                doBattle(data);
                break;
            // 海戦
            case BATTLE_AIRBATTLE:
                doBattle(data);
                break;
            // 海戦
            case COMBINED_BATTLE_AIRBATTLE:
                doBattle(data);
                break;
            // 海戦
            case COMBINED_BATTLE:
                doBattle(data);
                break;
            // 海戦
            case COMBINED_BATTLE_WATER:
                doBattle(data);
                break;
            // 海戦
            case COMBINED_BATTLE_MIDNIGHT:
                doBattleNight(data);
                break;
            // 海戦
            case COMBINED_BATTLE_SP_MIDNIGHT:
                doBattleNight(data);
                break;
            // 海戦結果
            case BATTLE_RESULT:
                doBattleresult(data);
                break;
            // 海戦結果
            case COMBINED_BATTLE_RESULT:
                doBattleresult(data);
                break;
            // 艦隊
            case DECK:
                doDeck(data);
                break;
            // 出撃
            case START:
                doStart(data);
                break;
            // 出撃
            case NEXT:
                doNext(data);
                break;
            // 任務
            case QUEST_LIST:
                doQuest(data);
                break;
            // 任務消化
            case QUEST_CLEAR:
                doQuestClear(data);
                break;
            // 設定
            case START2:
                doStart2(data);
                break;
            // 演習結果
            case PRACTICE_RESULT:
                doPracticeResult(data);
                break;
            // 入渠
            case NYUKYO:
                doNyukyo(data);
                break;
            default:
                break;
            }
        }
        return update;
    }

    /**
     * 補給を更新します
     * @param data
     */
    private static void doCharge(Data data) {
        try {
            JsonObject apidata = data.getJsonObject().getJsonObject("api_data");
            if (apidata != null) {
                JsonArray ships = apidata.getJsonArray("api_ship");
                for (JsonValue shipval : ships) {
                    JsonObject shipobj = (JsonObject) shipval;

                    Long shipid = shipobj.getJsonNumber("api_id").longValue();
                    int fuel = shipobj.getJsonNumber("api_fuel").intValue();
                    int bull = shipobj.getJsonNumber("api_bull").intValue();

                    ShipDto ship = ShipContext.get().get(shipid);
                    if (ship != null) {
                        ship.setFuel(fuel);
                        ship.setBull(bull);

                        String fleetid = ship.getFleetid();
                        if (fleetid != null) {
                            DockDto dockdto = dock.get(fleetid);
                            if (dockdto != null) {
                                dockdto.setUpdate(true);
                            }
                        }
                    }
                }
                Date now = new Date();
                for (Integer no : QuestConfig.getNoList()) {
                    QuestBean bean = QuestConfig.get(no);
                    if ((bean != null) && bean.getExecuting()) {
                        bean.countCharge(now);
                    }
                }
                QuestConfig.store();
                addConsole("補給を更新しました");
            }
        } catch (Exception e) {
            LoggerHolder.LOG.warn("補給を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 編成を更新します
     * @param data
     */
    private static void doChange(Data data) {
        try {
            String fleetid = data.getField("api_id");
            long shipid = Long.parseLong(data.getField("api_ship_id"));
            int shipidx = Integer.parseInt(data.getField("api_ship_idx"));

            DockDto dockdto = dock.get(fleetid);

            if (dockdto != null) {

                if ((shipid == -2L) && (shipidx == -1)) {
                    // 旗艦以外解除
                    dockdto.removeOthers();
                } else if (shipid == -1L) {
                    dockdto.removeShip(shipidx);
                } else {
                    ShipDto rship = ShipContext.get().get(shipid);

                    DockDto otherDock = dock.get(rship.getFleetid());

                    if (shipidx < dockdto.size()) {
                        if (otherDock != null)
                            otherDock.displaceShip(otherDock.indexOf(rship), dockdto.replaceShip(shipidx, rship));
                        else
                            dockdto.replaceShip(shipidx, rship);
                    } else {
                        if (otherDock != null)
                            otherDock.removeShip(otherDock.indexOf(rship));
                        dockdto.addShip(rship);
                    }
                }
                // 秘書艦を再設定
                ShipContext.setSecretary(dock.get("1").getShips().get(0));
            }
        } catch (Exception e) {
            LoggerHolder.LOG.warn("編成を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 母港を更新します
     * @param data
     */
    private static void doPort(Data data) {
        try {
            JsonObject apidata = data.getJsonObject().getJsonObject("api_data");
            if (apidata != null) {
                // 出撃中ではない
                Arrays.fill(isSortie, false);

                // 基本情報を更新する
                JsonObject apiBasic = apidata.getJsonObject("api_basic");
                doBasicSub(apiBasic);
                addConsole("司令部を更新しました");

                // 保有艦娘を更新する
                JsonArray apiShip = apidata.getJsonArray("api_ship");
                Set<Long> portShips = new HashSet<>();
                for (int i = 0; i < apiShip.size(); i++) {
                    ShipDto ship = new ShipDto((JsonObject) apiShip.get(i));
                    Long key = Long.valueOf(ship.getId());
                    ShipContext.get().put(key, ship);
                    portShips.add(key);
                }
                // portに無い艦娘を除く
                for (Entry<Long, ShipDto> entry : ShipContext.get().entrySet()) {
                    if (!portShips.contains(entry.getKey())) {
                        for (Long item : entry.getValue().getItemId()) {
                            ItemContext.get().remove(item);
                            ItemContext.level().remove(item);
                        }
                        ShipContext.get().remove(entry.getKey());
                    }
                }

                JsonArray apiDeckPort = apidata.getJsonArray("api_deck_port");
                doDeck(apiDeckPort);
                addConsole("保有艦娘情報を更新しました");

                // 入渠の状態を更新する
                JsonArray apiNdock = apidata.getJsonArray("api_ndock");
                doNdockSub(apiNdock);
                addConsole("入渠情報を更新しました");

                // 遠征の状態を更新する
                deckMissions = new DeckMissionDto[] { DeckMissionDto.EMPTY, DeckMissionDto.EMPTY, DeckMissionDto.EMPTY };
                for (int i = 1; i < apiDeckPort.size(); i++) {
                    JsonObject object = (JsonObject) apiDeckPort.get(i);
                    JsonArray jmission = object.getJsonArray("api_mission");
                    int section = ((JsonNumber) jmission.get(1)).intValue();
                    long milis = ((JsonNumber) jmission.get(2)).longValue();
                    long fleetid = object.getJsonNumber("api_id").longValue();
                    doMissionSub(fleetid, section, milis);
                }
                addConsole("遠征情報を更新しました");

                // 連合艦隊を更新する
                combined = 0;
                if (apidata.containsKey("api_combined_flag")) {
                    combined = apidata.getJsonNumber("api_combined_flag").intValue();
                    addConsole("連合艦隊を更新しました");
                }
            }
        } catch (Exception e) {
            LoggerHolder.LOG.warn("母港を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 海戦情報を更新します
     * @param data
     */
    private static void doBattle(Data data) {
        try {
            JsonObject apidata = data.getJsonObject().getJsonObject("api_data");
            battleList.add(new BattleDto(apidata, false, combined));
            if (sortiePhase != SortiePhase.BATTLE)
                battleCount++;
            sortiePhase = SortiePhase.BATTLE;
            addConsole("海戦情報を更新しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("海戦情報を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 海戦情報を更新します
     * @param data
     */
    private static void doBattleNight(Data data) {
        try {
            JsonObject apidata = data.getJsonObject().getJsonObject("api_data");
            battleList.add(new BattleDto(apidata, true, combined));
            if (sortiePhase != SortiePhase.BATTLE)
                battleCount++;
            sortiePhase = SortiePhase.BATTLE;
            addConsole("海戦情報を更新しました");

        } catch (Exception e) {
            LoggerHolder.LOG.warn("海戦情報を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 海戦情報を更新します
     * @param data
     */
    private static void doBattleresult(Data data) {
        try {
            if (battleList.size() != 0) {
                JsonObject apidata = data.getJsonObject().getJsonObject("api_data");
                BattleResultDto dto = new BattleResultDto(apidata, mapCellNo, mapBossCellNo, bossArrive, isStart,
                        battleList.toArray(new BattleDto[0]));
                battleResultList.add(dto);
                CreateReportLogic.storeBattleResultReport(dto);
                battleList.clear();
                BattleDto last = dto.getBattles()[dto.getBattles().length - 1];
                boolean s = "S".equals(dto.getRank());
                boolean a = s || "A".equals(dto.getRank());
                boolean win = a || "B".equals(dto.getRank());
                int ap = 0;
                int cv = 0;
                int ss = 0;

                for (int i = 0; i < last.getEnemy().size(); ++i) {
                    if (last.getEndEnemyHp()[i] <= 0) {
                        switch (last.getEnemy().get(i).getType()) {
                        case "補給艦":
                            ap++;
                            break;
                        case "軽空母":
                        case "正規空母":
                            cv++;
                            break;
                        case "潜水艦":
                            ss++;
                            break;
                        }
                    }
                }

                Date now = new Date();
                for (Integer no : QuestConfig.getNoList()) {
                    QuestBean bean = QuestConfig.get(no);
                    if ((bean != null) && bean.getExecuting()) {
                        if (win) {
                            bean.countBattleWin(now);
                        }
                        if (s) {
                            bean.countBattleWinS(now);
                        }
                        if (bossArrive) {
                            bean.countBossArrive(now);
                            if (win) {
                                bean.countBossWin(now);
                                switch (mapAreaNo) {
                                case 1:
                                    if ((mapInfoNo == 4) && s) {
                                        bean.countBoss1_4WinS(now);
                                    }
                                    if ((mapInfoNo == 5) && a) {
                                        bean.countBoss1_5WinA(now);
                                    }
                                    break;
                                case 2:
                                    bean.countBoss2Win(now);
                                    break;
                                case 3:
                                    if (mapInfoNo >= 3) {
                                        bean.countBoss3_3pWin(now);
                                    }
                                    break;
                                case 4:
                                    bean.countBoss4Win(now);
                                    if (mapInfoNo == 4) {
                                        bean.countBoss4_4Win(now);
                                    }
                                    break;
                                case 5:
                                    if ((mapInfoNo == 2) && s) {
                                        bean.countBoss5_2WinS(now);
                                    }
                                    break;
                                case 6:
                                    if ((mapInfoNo == 1) && s) {
                                        bean.countBoss6_1WinS(now);
                                    }
                                    break;
                                }
                            }
                        }
                        Calendar c = Calendar.getInstance();
                        c.setTime(now);
                        for (int i = 0; i < ap; ++i) {
                            bean.countDefeatAP(c.getTime());
                            c.add(Calendar.MILLISECOND, -1);
                        }
                        c.setTime(now);
                        for (int i = 0; i < cv; ++i) {
                            bean.countDefeatCV(c.getTime());
                            c.add(Calendar.MILLISECOND, -1);
                        }
                        c.setTime(now);
                        for (int i = 0; i < ss; ++i) {
                            bean.countDefeatSS(c.getTime());
                            c.add(Calendar.MILLISECOND, -1);
                        }
                    }
                }
                QuestConfig.store();

                List<ShipDto> sps = new ArrayList<ShipDto>();
                for (int i = 0; i < 6; i++) {
                    if ((last.getMaxFriendHp()[i] > 0) && ((last.getEndFriendHp()[i] * 4) <= last.getMaxFriendHp()[i])) {
                        sps.add(last.getFriends().get(0).getShips().get(i));
                    }
                    if ((last.getMaxCombinedHp()[i] > 0)
                            && ((last.getEndCombinedHp()[i] * 4) <= last.getMaxCombinedHp()[i])) {
                        sps.add(last.getFriends().get(1).getShips().get(i));
                    }
                }
                if (!sps.isEmpty()) {
                    Thread th = new Thread() {
                        private List<ShipDto> ships;

                        public Thread set(List<ShipDto> ships) {
                            this.ships = ships;
                            return this;
                        }

                        @Override
                        public void run() {
                            try {
                                sleep(8000);
                            }
                            catch (InterruptedException e) {
                            }
                            Display.getDefault().asyncExec(new PostFatal(this.ships));
                        }

                        class PostFatal implements Runnable {
                            private final List<ShipDto> ships;

                            public PostFatal(List<ShipDto> ships) {
                                super();
                                this.ships = ships;
                            }

                            @Override
                            public void run() {
                                if (AppConfig.get().isBalloonBybadlyDamage()) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(AppConstants.MESSAGE_STOP_SORTIE);
                                    sb.append("\n");
                                    for (ShipDto shipDto : this.ships) {
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
                                    ToolTip tip = new ToolTip(ApplicationMain.getWindow().getShell(), SWT.BALLOON
                                            | SWT.ICON_ERROR);
                                    tip.setText("大破警告");
                                    tip.setMessage(sb.toString());

                                    ApplicationMain.getWindow().getTrayItem().setToolTip(tip);
                                    tip.setVisible(true);
                                }
                                // 大破時にサウンドを再生する
                                PlayerThread.randomBadlySoundPlay();
                            }
                        }
                    }.set(sps);
                    th.start();
                }
            }
            // 出撃を更新
            sortiePhase = SortiePhase.BATTLE_RESULT;
            isStart = false;
            addConsole("海戦情報を更新しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("海戦情報を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 建造(投入資源)情報を更新します
     * @param data
     */
    private static void doCreateship(Data data) {
        try {
            String kdockid = data.getField("api_kdock_id");
            // 投入資源
            ResourceDto resource = new ResourceDto(
                    data.getField("api_large_flag"),
                    data.getField("api_item1"),
                    data.getField("api_item2"),
                    data.getField("api_item3"),
                    data.getField("api_item4"),
                    data.getField("api_item5"),
                    ShipContext.getSecretary(), hqLevel
                    );
            lastBuildKdock = kdockid;
            getShipResource.put(kdockid, resource);
            KdockConfig.store(kdockid, resource);

            Date now = new Date();
            for (Integer no : QuestConfig.getNoList()) {
                QuestBean bean = QuestConfig.get(no);
                if ((bean != null) && bean.getExecuting()) {
                    bean.countCreateShip(now);
                }
            }
            QuestConfig.store();

            addConsole("建造(投入資源)情報を更新しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("建造(投入資源)情報を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 建造を更新します
     * @param data
     */
    private static void doKdock(Data data) {
        try {
            // 建造ドックの空きをカウントします
            if (lastBuildKdock != null) {
                ResourceDto resource = getShipResource.get(lastBuildKdock);
                if (resource != null) {
                    int freecount = 0;
                    JsonArray apidata = data.getJsonObject().getJsonArray("api_data");
                    for (int i = 0; i < apidata.size(); i++) {
                        int state = ((JsonObject) apidata.get(i)).getJsonNumber("api_state").intValue();
                        if (state == 0) {
                            freecount++;
                        }
                    }
                    // 建造ドックの空きをセットします
                    resource.setFreeDock(Integer.toString(freecount));
                    KdockConfig.store(lastBuildKdock, resource);
                }
            }
            addConsole("建造を更新しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("建造を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 建造(入手)情報を更新します
     * @param data
     */
    private static void doGetship(Data data) {
        try {
            JsonObject apidata = data.getJsonObject().getJsonObject("api_data");
            String dock = data.getField("api_kdock_id");

            // 艦娘の装備を追加します
            if (!apidata.isNull("api_slotitem")) {
                JsonArray slotitem = apidata.getJsonArray("api_slotitem");
                for (int i = 0; i < slotitem.size(); i++) {
                    JsonObject object = (JsonObject) slotitem.get(i);
                    int typeid = object.getJsonNumber("api_slotitem_id").intValue();
                    Long id = object.getJsonNumber("api_id").longValue();
                    ItemDto item = Item.get(typeid);
                    if (item != null) {
                        ItemContext.get().put(id, item);
                    }
                }
            }
            // 艦娘を追加します
            JsonObject apiShip = apidata.getJsonObject("api_ship");
            ShipDto ship = new ShipDto(apiShip);
            ShipContext.get().put(Long.valueOf(ship.getId()), ship);
            // 投入資源を取得する
            ResourceDto resource = getShipResource.get(dock);
            if (resource == null) {
                resource = KdockConfig.load(dock);
            }
            GetShipDto dto = new GetShipDto(ship, resource);
            getShipList.add(dto);
            CreateReportLogic.storeCreateShipReport(dto);
            // 投入資源を除去する
            getShipResource.remove(dock);
            KdockConfig.remove(dock);

            addConsole("建造(入手)情報を更新しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("建造(入手)情報を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 装備開発情報を更新します
     *
     * @param data
     */
    private static void doCreateitem(Data data) {
        try {
            JsonObject apidata = data.getJsonObject().getJsonObject("api_data");

            // 投入資源
            ResourceDto resources = new ResourceDto(data.getField("api_item1"), data.getField("api_item2"),
                    data.getField("api_item3"), data.getField("api_item4"), ShipContext.getSecretary(), hqLevel);

            CreateItemDto createitem = new CreateItemDto(apidata, resources);
            if (createitem.isCreateFlag()) {
                JsonObject object = apidata.getJsonObject("api_slot_item");
                int typeid = object.getJsonNumber("api_slotitem_id").intValue();
                Long id = object.getJsonNumber("api_id").longValue();
                ItemDto item = Item.get(typeid);
                if (item != null) {
                    ItemContext.get().put(id, item);

                    createitem.setName(item.getName());
                    createitem.setType(item.getType());
                    createItemList.add(createitem);
                }
            } else {
                createItemList.add(createitem);
            }

            Date now = new Date();
            for (Integer no : QuestConfig.getNoList()) {
                QuestBean bean = QuestConfig.get(no);
                if ((bean != null) && bean.getExecuting()) {
                    bean.countCreateItem(now);
                }
            }
            QuestConfig.store();

            CreateReportLogic.storeCreateItemReport(createitem);

            addConsole("装備開発情報を更新しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("装備開発情報を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 保有装備を更新します
     *
     * @param data
     */
    private static void doSlotitemMember(Data data) {
        try {
            JsonArray apidata = data.getJsonObject().getJsonArray("api_data");
            // 破棄
            ItemContext.get().clear();
            ItemContext.level().clear();
            for (int i = 0; i < apidata.size(); i++) {
                JsonObject object = (JsonObject) apidata.get(i);
                int typeid = object.getJsonNumber("api_slotitem_id").intValue();
                int level = object.getJsonNumber("api_level").intValue();
                Long id = object.getJsonNumber("api_id").longValue();
                ItemDto item = Item.get(typeid);
                if (item != null) {
                    ItemContext.get().put(id, item);
                    ItemContext.level().put(id, level);
                }
            }

            addConsole("保有装備情報を更新しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("保有装備を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 保有艦娘を更新します
     *
     * @param data
     */
    private static void doShip3(Data data) {
        try {
            JsonObject apidata = data.getJsonObject().getJsonObject("api_data");

            String shipidstr = data.getField("api_shipid");
            JsonArray shipdata = apidata.getJsonArray("api_ship_data");

            if (shipidstr != null) {
                // 艦娘の指定がある場合は艦娘を差し替える
                Long shipid = Long.parseLong(shipidstr);
                for (int i = 0; i < shipdata.size(); i++) {
                    ShipDto ship = new ShipDto((JsonObject) shipdata.get(i));
                    ShipContext.get().put(shipid, ship);
                }
            } else {
                // 情報を破棄
                ShipContext.get().clear();
                for (int i = 0; i < shipdata.size(); i++) {
                    ShipDto ship = new ShipDto((JsonObject) shipdata.get(i));
                    ShipContext.get().put(Long.valueOf(ship.getId()), ship);
                }
            }
            // 艦隊を設定
            doDeck(apidata.getJsonArray("api_deck_data"));

            addConsole("保有艦娘情報を更新しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("保有艦娘を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 保有艦娘を更新します
     *
     * @param data
     */
    private static void doShip2(Data data) {
        try {
            JsonArray apidata = data.getJsonObject().getJsonArray("api_data");
            // 情報を破棄
            ShipContext.get().clear();
            for (int i = 0; i < apidata.size(); i++) {
                ShipDto ship = new ShipDto((JsonObject) apidata.get(i));
                ShipContext.get().put(Long.valueOf(ship.getId()), ship);
            }
            // 艦隊を設定
            doDeck(data.getJsonObject().getJsonArray("api_data_deck"));

            addConsole("保有艦娘情報を更新しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("保有艦娘を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 保有艦娘を更新します
     *
     * @param data
     */
    private static void doShipDeck(Data data) {
        try {
            JsonObject apidata = data.getJsonObject().getJsonObject("api_data");
            // 艦娘を差し替える
            JsonArray shipData = apidata.getJsonArray("api_ship_data");
            for (int i = 0; i < shipData.size(); i++) {
                ShipDto ship = new ShipDto((JsonObject) shipData.get(i));
                ShipContext.get().put(Long.valueOf(ship.getId()), ship);
            }
            // 艦隊を設定
            doDeck(apidata.getJsonArray("api_deck_data"));

        } catch (Exception e) {
            LoggerHolder.LOG.warn("保有艦娘を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 艦隊を更新します
     *
     * @param data
     */
    private static void doDeck(Data data) {
        try {
            JsonArray apidata = data.getJsonObject().getJsonArray("api_data");
            // 艦隊IDをクリアします
            for (DockDto dockdto : dock.values()) {
                for (ShipDto ship : dockdto.getShips()) {
                    ship.setFleetid("");
                }
            }
            doDeck(apidata);
            addConsole("艦隊を更新しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("艦隊を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 艦隊を設定します
     *
     * @param apidata
     */
    private static void doDeck(JsonArray apidata) {
        dock.clear();
        for (int i = 0; i < apidata.size(); i++) {
            JsonObject jsonObject = (JsonObject) apidata.get(i);
            String fleetid = Long.toString(jsonObject.getJsonNumber("api_id").longValue());
            String name = jsonObject.getString("api_name");
            JsonArray apiship = jsonObject.getJsonArray("api_ship");

            DockDto dockdto = new DockDto(fleetid, name);
            dock.put(fleetid, dockdto);

            for (int j = 0; j < apiship.size(); j++) {
                Long shipid = Long.valueOf(((JsonNumber) apiship.get(j)).longValue());
                ShipDto ship = ShipContext.get().get(shipid);

                if (ship != null) {
                    dockdto.addShip(ship);

                    if ((i == 0) && (j == 0)) {
                        ShipContext.setSecretary(ship);
                    }
                    // 艦隊IDを設定
                    ship.setFleetid(fleetid);
                }
            }
        }
    }

    /**
     * 艦娘を解体します
     * @param data
     */
    private static void doDestroyShip(Data data) {
        try {
            Long shipid = Long.parseLong(data.getField("api_ship_id"));
            ShipDto ship = ShipContext.get().get(shipid);
            if (ship != null) {
                // 持っている装備を廃棄する
                List<Long> items = ship.getItemId();
                for (Long item : items) {
                    ItemContext.get().remove(item);
                    ItemContext.level().remove(item);
                }
                // 艦娘を外す
                ShipContext.get().remove(ship.getId());

                DockDto dockdto = dock.get(ship.getFleetid());
                if (dockdto != null)
                    dockdto.removeShip(dockdto.indexOf(ship));
            }

            Date now = new Date();
            for (Integer no : QuestConfig.getNoList()) {
                QuestBean bean = QuestConfig.get(no);
                if ((bean != null) && bean.getExecuting()) {
                    bean.countDestroyShip(now);
                }
            }
            QuestConfig.store();

            addConsole("艦娘を解体しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("艦娘を解体しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 装備を廃棄します
     * @param data
     */
    private static void doDestroyItem2(Data data) {
        try {
            String itemids = data.getField("api_slotitem_ids");
            for (String itemid : itemids.split(",")) {
                Long item = Long.parseLong(itemid);
                ItemContext.get().remove(item);
                ItemContext.level().remove(item);
            }

            Date now = new Date();
            for (Integer no : QuestConfig.getNoList()) {
                QuestBean bean = QuestConfig.get(no);
                if ((bean != null) && bean.getExecuting()) {
                    bean.countDestroyItem(now);
                }
            }
            QuestConfig.store();

            addConsole("装備を廃棄しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("装備を廃棄しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 近代化改修します
     * @param data
     */
    private static void doPowerup(Data data) {
        try {
            String shipids = data.getField("api_id_items");
            for (String shipid : shipids.split(",")) {
                ShipDto ship = ShipContext.get().get(Long.parseLong(shipid));
                if (ship != null) {
                    // 持っている装備を廃棄する
                    List<Long> items = ship.getItemId();
                    for (Long item : items) {
                        ItemContext.get().remove(item);
                        ItemContext.level().remove(item);
                    }
                    // 艦娘を外す
                    ShipContext.get().remove(ship.getId());
                }
            }

            if (data.getJsonObject().getJsonObject("api_data").getJsonNumber("api_powerup_flag").intValue() != 0)
            {
                Date now = new Date();
                for (Integer no : QuestConfig.getNoList()) {
                    QuestBean bean = QuestConfig.get(no);
                    if ((bean != null) && bean.getExecuting()) {
                        bean.countPowerUp(now);
                    }
                }
                QuestConfig.store();
            }

            addConsole("近代化改修しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("近代化改修しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 司令部を更新する
     *
     * @param data
     */
    private static void doBasic(Data data) {
        try {
            JsonObject apidata = data.getJsonObject().getJsonObject("api_data");
            doBasicSub(apidata);

            addConsole("司令部を更新しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("司令部を更新するに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 司令部を更新する
     *
     * @param apidata
     */
    private static void doBasicSub(JsonObject apidata) {
        // 指令部Lv
        hqLevel = apidata.getJsonNumber("api_level").intValue();
        // 最大所有艦娘数
        maxChara = apidata.getJsonNumber("api_max_chara").intValue();
        // 最大所有装備数
        maxSlotitem = apidata.getJsonNumber("api_max_slotitem").intValue();
    }

    /**
     * 遠征を更新します
     *
     * @param data
     */
    private static void doMission(Data data) {
        try {
            JsonObject apidata = data.getJsonObject().getJsonObject("api_data");

            doMissionSub(Long.parseLong(data.getField("api_deck_id")),
                    Integer.parseInt(data.getField("api_mission_id")),
                    apidata.getJsonNumber("api_complatetime").longValue());

            addConsole("遠征情報を更新しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("遠征を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 遠征を更新します
     *
     * @param data
     */
    private static void doMissionSub(long fleetId, int missionId, long milis) {
        String mission = null;
        if (missionId != 0) {
            mission = Deck.get(missionId);
            if (mission == null) {
                mission = "<UNKNOWN>";
            }
        }
        DockDto dockdto = dock.get(Long.toString(fleetId));

        Set<Long> ships = new LinkedHashSet<Long>();
        for (ShipDto s : dockdto.getShips()) {
            long shipid = s.getId();
            if (shipid != -1) {
                ships.add(shipid);
            }
        }

        Date time = null;
        if (milis > 0) {
            time = new Date(milis);
        }
        deckMissions[(int) (fleetId - 2)] = new DeckMissionDto(dockdto.getName(), mission, time, fleetId, ships);
    }

    /**
     * 遠征(帰還)を更新します
     *
     * @param data
     */
    private static void doMissionResult(Data data) {
        try {
            JsonObject apidata = data.getJsonObject().getJsonObject("api_data");

            MissionResultDto result = new MissionResultDto();

            int clearResult = apidata.getJsonNumber("api_clear_result").intValue();
            result.setClearResult(clearResult);
            result.setQuestName(apidata.getString("api_quest_name"));

            if (clearResult != 0) {
                JsonArray material = apidata.getJsonArray("api_get_material");
                result.setFuel(material.getJsonNumber(0).toString());
                result.setAmmo(material.getJsonNumber(1).toString());
                result.setMetal(material.getJsonNumber(2).toString());
                result.setBauxite(material.getJsonNumber(3).toString());

                Date now = new Date();
                for (Integer no : QuestConfig.getNoList()) {
                    QuestBean bean = QuestConfig.get(no);
                    if ((bean != null) && bean.getExecuting()) {
                        bean.countMissionSuccess(now);
                    }
                }
                QuestConfig.store();
            }

            CreateReportLogic.storeCreateMissionReport(result);
            missionResultList.add(result);

            addConsole("遠征(帰還)情報を更新しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("遠征(帰還)を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 入渠を更新します
     * @param data
     */
    private static void doNdock(Data data) {
        try {
            JsonArray apidata = data.getJsonObject().getJsonArray("api_data");

            doNdockSub(apidata);

            addConsole("入渠情報を更新しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("入渠を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 入渠を更新します
     * @param apidata
     */
    private static void doNdockSub(JsonArray apidata) {
        ndocks = new NdockDto[] { NdockDto.EMPTY, NdockDto.EMPTY, NdockDto.EMPTY, NdockDto.EMPTY };

        for (int i = 0; i < apidata.size(); i++) {
            JsonObject object = (JsonObject) apidata.get(i);
            long id = object.getJsonNumber("api_ship_id").longValue();
            long milis = object.getJsonNumber("api_complete_time").longValue();

            Date time = null;
            if (milis > 0) {
                time = new Date(milis);
            }
            ndocks[i] = new NdockDto(id, time);
        }
    }

    /**
     * 任務を更新します
     *
     * @param data
     */
    private static void doQuest(Data data) {
        try {
            JsonObject apidata = data.getJsonObject().getJsonObject("api_data");
            if (!apidata.isNull("api_list")) {
                JsonArray apilist = apidata.getJsonArray("api_list");
                for (JsonValue value : apilist) {
                    if (value instanceof JsonObject) {
                        JsonObject questobject = (JsonObject) value;
                        // 任務を作成
                        QuestDto quest = new QuestDto();
                        quest.setNo(questobject.getInt("api_no"));
                        quest.setCategory(questobject.getInt("api_category"));
                        quest.setType(questobject.getInt("api_type"));
                        quest.setState(questobject.getInt("api_state"));
                        quest.setTitle(questobject.getString("api_title"));
                        quest.setDetail(questobject.getString("api_detail"));
                        JsonArray material = questobject.getJsonArray("api_get_material");
                        quest.setFuel(material.getJsonNumber(0).toString());
                        quest.setAmmo(material.getJsonNumber(1).toString());
                        quest.setMetal(material.getJsonNumber(2).toString());
                        quest.setBauxite(material.getJsonNumber(3).toString());
                        quest.setBonusFlag(questobject.getInt("api_bonus_flag"));
                        quest.setProgressFlag(questobject.getInt("api_progress_flag"));

                        QuestBean bean = QuestConfig.get(quest.getNo());
                        if (bean == null) {
                            bean = new QuestBean();
                        }
                        switch (quest.getType())
                        {
                        case 2:
                        case 4:
                        case 5:
                            bean.setDue(QuestDue.getDaily());
                            break;
                        case 3:
                            bean.setDue(QuestDue.getWeekly());
                            break;
                        case 6:
                            bean.setDue(QuestDue.getMonthly());
                            break;
                        default:
                            break;
                        }
                        bean.setExecuting(quest.getState() == 2);
                        QuestConfig.put(quest.getNo(), bean);
                        questMap.put(quest.getNo(), quest);
                    }
                }
                QuestConfig.store();
            }
            addConsole("任務を更新しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("任務を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 消化した任務を除去します
     *
     * @param data
     */
    private static void doQuestClear(Data data) {
        try {
            String idstr = data.getField("api_quest_id");
            if (idstr != null) {
                Integer id = Integer.valueOf(idstr);
                questMap.remove(id);
                QuestConfig.remove(id);
            }
        } catch (Exception e) {
            LoggerHolder.LOG.warn("消化した任務を除去しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 出撃を更新します
     *
     * @param data
     */
    private static void doStart(Data data) {
        try {
            String idstr = data.getField("api_deck_id");
            if (idstr != null) {
                int id = Integer.parseInt(idstr);
                isSortie[id - 1] = true;
            }
            // 連合艦隊第2艦隊の出撃
            if (combined != 0) {
                isSortie[1] = true;
            }
            // 出撃を更新
            isStart = true;

            JsonObject obj = data.getJsonObject().getJsonObject("api_data");

            mapAreaNo = obj.getJsonNumber("api_maparea_id").intValue();
            mapInfoNo = obj.getJsonNumber("api_mapinfo_no").intValue();
            mapCellNo = obj.getJsonNumber("api_no").intValue();
            mapBossCellNo = obj.getJsonNumber("api_bosscell_no").intValue();
            eventId = obj.getJsonNumber("api_event_id").intValue();

            int color = obj.getJsonNumber("api_color_no").intValue();
            bossArrive = (color == 5) || (color == 8);

            Date now = new Date();
            for (Integer no : QuestConfig.getNoList()) {
                QuestBean bean = QuestConfig.get(no);
                if ((bean != null) && bean.getExecuting()) {
                    bean.countSortie(now);
                }
            }
            QuestConfig.store();

            battleCount = 0;
            sortiePhase = SortiePhase.MAP;

            addConsole("出撃を更新しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("出撃を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 進撃を更新します
     *
     * @param data
     */
    private static void doNext(Data data) {
        try {
            JsonObject obj = data.getJsonObject().getJsonObject("api_data");

            mapAreaNo = obj.getJsonNumber("api_maparea_id").intValue();
            mapInfoNo = obj.getJsonNumber("api_mapinfo_no").intValue();
            mapCellNo = obj.getJsonNumber("api_no").intValue();
            mapBossCellNo = obj.getJsonNumber("api_bosscell_no").intValue();
            eventId = obj.getJsonNumber("api_event_id").intValue();

            int color = obj.getJsonNumber("api_color_no").intValue();
            bossArrive = (color == 5) || (color == 8);
            sortiePhase = SortiePhase.MAP;

            addConsole("進撃を更新しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("進撃を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 設定を更新します
     *
     * @param data
     */
    private static void doStart2(Data data) {
        try {
            JsonObject obj = data.getJsonObject().getJsonObject("api_data");
            if (obj != null) {
                // 艦娘一覧
                JsonArray apiMstShip = obj.getJsonArray("api_mst_ship");
                for (int i = 0; i < apiMstShip.size(); i++) {
                    JsonObject object = (JsonObject) apiMstShip.get(i);
                    String id = object.getJsonNumber("api_id").toString();
                    Ship.set(id, toShipInfoDto(object));
                }
                addConsole("艦娘一覧を更新しました");

                // 装備一覧
                JsonArray apiMstSlotitem = obj.getJsonArray("api_mst_slotitem");
                for (int i = 0; i < apiMstSlotitem.size(); i++) {
                    JsonObject object = (JsonObject) apiMstSlotitem.get(i);
                    ItemDto item = new ItemDto(object);
                    int id = object.getJsonNumber("api_id").intValue();
                    Item.set(id, item);
                }
                addConsole("装備一覧を更新しました");
            }

            addConsole("設定を更新しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("設定を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 演習情報を更新します
     * @param data
     */
    private static void doPracticeResult(Data data) {
        try {
            JsonObject obj = data.getJsonObject().getJsonObject("api_data");
            if (obj != null) {
                String rank = obj.getString("api_win_rank");
                boolean win = "B".equals(rank) || "A".equals(rank) || "S".equals(rank);

                Date now = new Date();
                for (Integer no : QuestConfig.getNoList()) {
                    QuestBean bean = QuestConfig.get(no);
                    if ((bean != null) && bean.getExecuting()) {
                        bean.countPractice(now);
                        if (win) {
                            bean.countPracticeWin(now);
                        }
                    }
                }
                QuestConfig.store();

                addConsole("演習情報を更新しました");
            }
        } catch (Exception e) {
            LoggerHolder.LOG.warn("演習情報を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 入渠します
     * @param data
     */
    private static void doNyukyo(Data data) {
        try {
            Date now = new Date();
            for (Integer no : QuestConfig.getNoList()) {
                QuestBean bean = QuestConfig.get(no);
                if ((bean != null) && bean.getExecuting()) {
                    bean.countRepair(now);
                }
            }
            QuestConfig.store();

            addConsole("入渠情報を更新しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("入渠を更新しますに失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

    /**
     * 艦娘を作成します
     *
     * @param object
     * @return
     */
    private static ShipInfoDto toShipInfoDto(JsonObject object) {
        String name = object.getString("api_name");

        if ("なし".equals(name)) {
            return ShipInfoDto.EMPTY;
        }

        String type = ShipStyle.get(object.getJsonNumber("api_stype").toString());
        String flagship = object.getString("api_yomi");
        if ("-".equals(flagship)) {
            flagship = "";
        }
        int afterlv = object.getJsonNumber("api_afterlv").intValue();
        int maxBull = 0;
        if (object.containsKey("api_bull_max")) {
            maxBull = object.getJsonNumber("api_bull_max").intValue();
        }
        int maxFuel = 0;
        if (object.containsKey("api_fuel_max")) {
            maxFuel = object.getJsonNumber("api_fuel_max").intValue();
        }
        return new ShipInfoDto(name, type, flagship, afterlv, maxBull, maxFuel);
    }

    /**
     * 母港画面のログ出力
     *
     * @param message ログメッセージ
     */
    private static void addConsole(Object message) {
        ConsoleContext.log(message);
    }
}
