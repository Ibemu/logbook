package logbook.gui.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import logbook.config.QuestConfig;
import logbook.constants.AppConstants;
import logbook.data.context.GlobalContext;
import logbook.data.context.ItemContext;
import logbook.dto.BattleDto;
import logbook.dto.BattleResultDto;
import logbook.dto.CreateItemDto;
import logbook.dto.DockDto;
import logbook.dto.GetShipDto;
import logbook.dto.ItemDto;
import logbook.dto.MaterialDto;
import logbook.dto.MissionResultDto;
import logbook.dto.QuestDto;
import logbook.dto.ShipDto;
import logbook.dto.ShipInfoDto;
import logbook.gui.bean.CreateItemReportBean;
import logbook.gui.bean.CreateShipReportBean;
import logbook.gui.bean.DropReportBean;
import logbook.gui.bean.ItemBean;
import logbook.gui.bean.MissionResultBean;
import logbook.gui.bean.QuestBean;
import logbook.util.BeanProperty;
import logbook.util.FileUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 各種報告書を作成します
 *
 */
public final class CreateReportLogic {

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(CreateReportLogic.class);
    }

    /**
     * ドロップ報告書の内容
     * @return 内容
     */
    public static Stream<DropReportBean> getBattleResultContent() {
        return GlobalContext.getBattleResultList()
                .stream()
                .map(DropReportBean::toBean);
    }

    /**
     * ドロップ報告書のヘッダー(保存用)
     *
     * @return ヘッダー
     */
    public static String[] getBattleResultStoreHeader() {
        return new String[] { "", "日付", "海域", "マス", "ボス", "ランク",
                "艦隊行動", "味方陣形", "敵陣形",
                "敵艦隊",
                "ドロップ艦種", "ドロップ艦娘",
                "味方艦1", "味方艦1HP",
                "味方艦2", "味方艦2HP",
                "味方艦3", "味方艦3HP",
                "味方艦4", "味方艦4HP",
                "味方艦5", "味方艦5HP",
                "味方艦6", "味方艦6HP",
                "敵艦1", "敵艦1HP",
                "敵艦2", "敵艦2HP",
                "敵艦3", "敵艦3HP",
                "敵艦4", "敵艦4HP",
                "敵艦5", "敵艦5HP",
                "敵艦6", "敵艦6HP" };
    }

    /**
     * ドロップ報告書の内容(保存用)
     * @param results ドロップ報告書
     *
     * @return 内容
     */
    public static List<String[]> getBattleResultStoreBody(List<BattleResultDto> results) {
        List<Object[]> body = new ArrayList<Object[]>();

        SimpleDateFormat format = new SimpleDateFormat(AppConstants.DATE_FORMAT);

        for (int i = 0; i < results.size(); i++) {
            BattleResultDto item = results.get(i);
            BattleDto battle = item.getBattleDto();
            if (battle == null) {
                continue;
            }
            String[] friend = new String[6];
            String[] friendHp = new String[6];
            String[] enemy = new String[6];
            String[] enemyHp = new String[6];

            Arrays.fill(friend, "");
            Arrays.fill(friendHp, "");
            Arrays.fill(enemy, "");
            Arrays.fill(enemyHp, "");

            List<DockDto> docks = battle.getFriends();
            if (docks != null) {
                DockDto dock = docks.get(0);
                List<ShipDto> friendships = dock.getShips();
                int[] fnowhps = battle.getNowFriend1Hp();
                int[] fmaxhps = battle.getMaxFriend1Hp();
                for (int j = 0; j < friendships.size(); j++) {
                    ShipDto ship = friendships.get(j);
                    friend[j] = ship.getName() + "(Lv" + ship.getLv() + ")";
                    friendHp[j] = fnowhps[j] + "/" + fmaxhps[j];
                }
                List<ShipInfoDto> enemyships = battle.getEnemy1();
                int[] enowhps = battle.getNowEnemy1Hp();
                int[] emaxhps = battle.getMaxEnemy1Hp();
                for (int j = 0; j < enemyships.size(); j++) {
                    ShipInfoDto ship = enemyships.get(j);
                    if (!StringUtils.isEmpty(ship.getYomi())) {
                        enemy[j] = ship.getName() + "(" + ship.getYomi() + ")";
                    } else {
                        enemy[j] = ship.getName();
                    }
                    enemyHp[j] = enowhps[j] + "/" + emaxhps[j];
                }
            }

            body.add(new Object[] {
                    Integer.toString(i + 1),
                    format.format(item.getBattleDate()),
                    item.getQuestName(),
                    item.getMapCellNo(),
                    item.getBossText(),
                    item.getRank(),
                    battle.getIntercept(),
                    battle.getFriendFormation(),
                    battle.getEnemyFormation(),
                    item.getEnemyName(),
                    item.getDropType(),
                    item.getDropName(),
                    friend[0], friendHp[0],
                    friend[1], friendHp[1],
                    friend[2], friendHp[2],
                    friend[3], friendHp[3],
                    friend[4], friendHp[4],
                    friend[5], friendHp[5],
                    enemy[0], enemyHp[0],
                    enemy[1], enemyHp[1],
                    enemy[2], enemyHp[2],
                    enemy[3], enemyHp[3],
                    enemy[4], enemyHp[4],
                    enemy[5], enemyHp[5] });
        }
        return toListStringArray(body);
    }

    /**
     * 建造報告書の内容
     *
     * @return 内容
     */
    public static Stream<CreateShipReportBean> getCreateShipContent() {
        return GlobalContext.getGetshipList()
                .stream()
                .map(CreateShipReportBean::toBean);
    }

    /**
     * 開発報告書の内容
     *
     * @return 内容
     */
    public static Stream<CreateItemReportBean> getCreateItemContent() {
        return GlobalContext.getCreateItemList()
                .stream()
                .map(CreateItemReportBean::toBean);
    }

    /**
     * 所有装備一覧の内容
     *
     * @return 内容
     */
    public static Stream<ItemBean> getItemTablecontent() {
        // 集計関数
        BiConsumer<HashMap<ItemDto, Integer>, ItemDto> accumulator = (m, b) ->
                m.put(b, m.getOrDefault(b, 0) + 1);
        // マージ関数
        BiConsumer<HashMap<ItemDto, Integer>, HashMap<ItemDto, Integer>> combiner = (t, u) ->
                u.forEach((b, i) -> t.put(b, t.getOrDefault(b, 0) + i));
        // ItemDto -> ItemBean 変換
        Function<Entry<ItemDto, Integer>, ItemBean> mapper = (e) -> {
            ItemDto d = e.getKey();
            ItemBean b = new ItemBean();
            b.setBaku(d.getBaku());
            b.setCount(e.getValue());
            b.setHoug(d.getHoug());
            b.setHouk(d.getHouk());
            b.setHoum(d.getHoum());
            b.setLeng(d.getLeng());
            b.setLuck(d.getLuck());
            b.setName(StringUtils.defaultString(d.getName()));
            b.setRaig(d.getRaig());
            b.setSaku(d.getSaku());
            b.setSouk(d.getSouk());
            b.setTais(d.getTais());
            b.setTyku(d.getTyku());
            b.setType(StringUtils.defaultString(d.getType()));
            return b;
        };

        Map<ItemDto, Integer> map = ItemContext.get()
                .values()
                .stream()
                .parallel()
                .collect(HashMap<ItemDto, Integer>::new, accumulator, combiner);
        return map.entrySet()
                .stream()
                .map(mapper)
                .sorted(Comparator.comparing(ItemBean::getName))
                .sorted(Comparator.comparing(ItemBean::getType));
    }

    /**
     * 遠征結果一覧の内容
     *
     * @return 遠征結果
     */
    public static Stream<MissionResultBean> getMissionResultContent() {
        return GlobalContext.getMissionResultList()
                .stream()
                .map(MissionResultBean::toBean);
    }

    /**
     * 任務一覧の内容
     *
     * @return
     */
    public static Stream<QuestBean> getQuestContent() {
        String[] states = { "", "", "遂行中", "達成", "" };
        Function<QuestDto, QuestBean> mapper = e -> {
            QuestBean b = new QuestBean();
            b.setState(states[e.getState()]);
            b.setTitle(e.getTitle());
            b.setDetail(e.getDetail());
            b.setFuel(e.getFuel());
            b.setAmmo(e.getAmmo());
            b.setMetal(e.getMetal());
            b.setBauxite(e.getBauxite());
            logbook.config.bean.QuestBean bean = QuestConfig.get(e.getNo());
            if (bean == null)
                bean = new logbook.config.bean.QuestBean();
            String due = "";
            if (bean.getDue() != null)
                due = new SimpleDateFormat(AppConstants.DATE_FORMAT).format(bean.getDue());
            b.setDue(due);
            b.setSortie(bean.getSortie().size());
            b.setBattleWin(bean.getBattleWin().size());
            b.setBattleWinS(bean.getBattleWinS().size());
            b.setBossArrive(bean.getBossArrive().size());
            b.setBossWin(bean.getBossWin().size());
            b.setBoss1_4WinS(bean.getBoss1_4WinS().size());
            b.setBoss1_5WinA(bean.getBoss1_5WinA().size());
            b.setBoss2Win(bean.getBoss2Win().size());
            b.setBoss3_3pWin(bean.getBoss3_3pWin().size());
            b.setBoss4Win(bean.getBoss4Win().size());
            b.setBoss4_4Win(bean.getBoss4_4Win().size());
            b.setBoss5_2WinS(bean.getBoss5_2WinS().size());
            b.setBoss6_1WinS(bean.getBoss6_1WinS().size());
            b.setDefeatAP(bean.getDefeatAP().size());
            b.setDefeatCV(bean.getDefeatCV().size());
            b.setDefeatSS(bean.getDefeatSS().size());
            b.setPractice(bean.getPractice().size());
            b.setPracticeWin(bean.getPracticeWin().size());
            b.setMissionSuccess(bean.getMissionSuccess().size());
            b.setCreateShip(bean.getCreateShip().size());
            b.setCreateItem(bean.getCreateItem().size());
            b.setDestroyShip(bean.getDestroyShip().size());
            b.setDestroyItem(bean.getDestroyItem().size());
            b.setCharge(bean.getCharge().size());
            b.setRepair(bean.getRepair().size());
            b.setPowerUp(bean.getPowerUp().size());
            return b;
        };
        return GlobalContext.getQuest().values()
                .stream()
                .map(mapper)
                .sorted(Comparator.comparing(QuestBean::getState).reversed());
    }

    /**
     * 資材のヘッダー
     *
     * @return ヘッダー
     */
    public static String[] getMaterialHeader() {
        return new String[] { "", "日付", "燃料", "弾薬", "鋼材", "ボーキ", "高速修復材", "高速建造材", "開発資材" };
    }

    /**
     * 資材の内容
     *
     * @param materials 資材
     * @return
     */
    public static List<String[]> getMaterialStoreBody(List<MaterialDto> materials) {
        List<String[]> body = new ArrayList<String[]>();

        for (int i = 0; i < materials.size(); i++) {
            MaterialDto material = materials.get(i);
            body.add(new String[] {
                    Integer.toString(i + 1),
                    new SimpleDateFormat(AppConstants.DATE_FORMAT).format(material.getTime()),
                    Integer.toString(material.getFuel()),
                    Integer.toString(material.getAmmo()),
                    Integer.toString(material.getMetal()),
                    Integer.toString(material.getBauxite()),
                    Integer.toString(material.getBucket()),
                    Integer.toString(material.getBurner()),
                    Integer.toString(material.getResearch())
            });
        }

        return body;
    }

    /**
     * オブジェクト配列をテーブルウィジェットに表示できるように文字列に変換します
     *
     * @param from テーブルに表示する内容
     * @return テーブルに表示する内容
     */
    private static List<String[]> toListStringArray(List<Object[]> from) {
        List<String[]> body = new ArrayList<String[]>();
        for (Object[] objects : from) {
            String[] values = new String[objects.length];
            for (int i = 0; i < objects.length; i++) {
                if (objects[i] != null) {
                    values[i] = String.valueOf(objects[i]);
                } else {
                    values[i] = "";
                }
            }
            body.add(values);
        }
        return body;
    }

    /**
     * 海戦・ドロップ報告書を書き込む
     *
     * @param dto 海戦・ドロップ報告
     */
    public static void storeBattleResultReport(BattleResultDto dto) {
        try {
            List<BattleResultDto> dtoList = Collections.singletonList(dto);

            Path report = FileUtils.getStoreFile(AppConstants.LOG_BATTLE_RESULT, AppConstants.LOG_BATTLE_RESULT_ALT);

            FileUtils.writeCsvStripFirstColumn(report,
                    CreateReportLogic.getBattleResultStoreHeader(),
                    CreateReportLogic.getBattleResultStoreBody(dtoList), true);
        } catch (IOException e) {
            LoggerHolder.LOG.warn("報告書の保存に失敗しました", e);
        }
    }

    /**
     * 建造報告書を書き込む
     *
     * @param dto 建造報告
     */
    public static void storeCreateShipReport(GetShipDto dto) {
        try {
            Path report = FileUtils.getStoreFile(AppConstants.LOG_CREATE_SHIP, AppConstants.LOG_CREATE_SHIP_ALT);

            BeanProperty<CreateShipReportBean> property = BeanProperty.getInstance(CreateShipReportBean.class);
            // header
            List<String> names = property.getNames();
            String[] header = names.toArray(new String[names.size()]);
            // content
            CreateShipReportBean bean = CreateShipReportBean.toBean(dto);
            List<String[]> list = Collections.singletonList(property.getStringValues(bean));

            FileUtils.writeCsv(report, header, list, true);
        } catch (IOException e) {
            LoggerHolder.LOG.warn("報告書の保存に失敗しました", e);
        }
    }

    /**
     * 開発報告書を書き込む
     *
     * @param dto 開発報告
     */
    public static void storeCreateItemReport(CreateItemDto dto) {
        try {
            Path report = FileUtils.getStoreFile(AppConstants.LOG_CREATE_ITEM, AppConstants.LOG_CREATE_ITEM_ALT);

            BeanProperty<CreateItemReportBean> property = BeanProperty.getInstance(CreateItemReportBean.class);
            // header
            List<String> names = property.getNames();
            String[] header = names.toArray(new String[names.size()]);
            // content
            CreateItemReportBean bean = CreateItemReportBean.toBean(dto);
            List<String[]> list = Collections.singletonList(property.getStringValues(bean));

            FileUtils.writeCsv(report, header, list, true);
        } catch (IOException e) {
            LoggerHolder.LOG.warn("報告書の保存に失敗しました", e);
        }
    }

    /**
     * 遠征報告書を書き込む
     *
     * @param dto 遠征結果
     */
    public static void storeCreateMissionReport(MissionResultDto dto) {
        try {
            Path report = FileUtils.getStoreFile(AppConstants.LOG_MISSION, AppConstants.LOG_MISSION_ALT);

            BeanProperty<MissionResultBean> property = BeanProperty.getInstance(MissionResultBean.class);
            // header
            List<String> names = property.getNames();
            String[] header = names.toArray(new String[names.size()]);
            // content
            MissionResultBean bean = MissionResultBean.toBean(dto);
            List<String[]> list = Collections.singletonList(property.getStringValues(bean));

            FileUtils.writeCsv(report, header, list, true);
        } catch (IOException e) {
            LoggerHolder.LOG.warn("報告書の保存に失敗しました", e);
        }
    }

    /**
     * 資材ログを書き込む
     *
     * @param material 資材
     */
    public static void storeMaterialReport(MaterialDto material) {
        try {
            List<MaterialDto> dtoList = Collections.singletonList(material);

            Path report = FileUtils.getStoreFile(AppConstants.LOG_RESOURCE, AppConstants.LOG_RESOURCE_ALT);

            FileUtils.writeCsvStripFirstColumn(report,
                    CreateReportLogic.getMaterialHeader(),
                    CreateReportLogic.getMaterialStoreBody(dtoList), true);
        } catch (IOException e) {
            LoggerHolder.LOG.warn("報告書の保存に失敗しました", e);
        }
    }
}
