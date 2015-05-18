package logbook.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * データが何を示すのかを列挙する
 *
 */
public enum DataType {

    /** 補給 */
    CHARGE("/kcsapi/api_req_hokyu/charge"),
    /** 編成 */
    CHANGE("/kcsapi/api_req_hensei/change"),
    /** 母港 */
    PORT("/kcsapi/api_port/port"),
    /** 保有艦 */
    SHIP2("/kcsapi/api_get_member/ship2"),
    /** 保有艦 */
    SHIP3("/kcsapi/api_get_member/ship3"),
    /** 遠征 */
    MISSION_START("/kcsapi/api_req_mission/start"),
    /** 保有艦 */
    SHIP_DECK("/kcsapi/api_get_member/ship_deck"),
    /** 遠征(帰還) */
    MISSION_RESULT("/kcsapi/api_req_mission/result"),
    /** 基本 */
    BASIC("/kcsapi/api_get_member/basic"),
    /** 資材 */
    MATERIAL("/kcsapi/api_get_member/material"),
    /** 入渠ドック */
    NDOCK("/kcsapi/api_get_member/ndock"),
    /** アイテム一覧 */
    SLOTITEM_MEMBER("/kcsapi/api_get_member/slot_item"),
    /** 艦隊 */
    DECK("/kcsapi/api_get_member/deck"),
    /** 戦闘 */
    BATTLE("/kcsapi/api_req_sortie/battle"),
    /** 戦闘(夜戦) */
    BATTLE_MIDNIGHT("/kcsapi/api_req_battle_midnight/battle"),
    /** 戦闘(夜戦) */
    BATTLE_SP_MIDNIGHT("/kcsapi/api_req_battle_midnight/sp_midnight"),
    /** 戦闘(夜戦→昼戦) */
    BATTLE_NIGHT_TO_DAY("/kcsapi/api_req_sortie/night_to_day"),
    /** 戦闘(航空戦) */
    BATTLE_AIRBATTLE("/kcsapi/api_req_sortie/airbattle"),
    /** 戦闘(連合艦隊) */
    COMBINED_BATTLE("/kcsapi/api_req_combined_battle/battle"),
    /** 戦闘(連合艦隊・夜戦) */
    COMBINED_BATTLE_MIDNIGHT("/kcsapi/api_req_combined_battle/midnight_battle"),
    /** 戦闘(連合艦隊・夜戦) */
    COMBINED_BATTLE_SP_MIDNIGHT("/kcsapi/api_req_combined_battle/sp_midnight"),
    /** 戦闘(連合艦隊・航空戦) */
    COMBINED_BATTLE_AIRBATTLE("/kcsapi/api_req_combined_battle/airbattle"),
    /** 戦闘(連合艦隊) */
    COMBINED_BATTLE_WATER("/kcsapi/api_req_combined_battle/battle_water"),
    /** 戦闘(夜戦) */
    COMBINED_MIDNIGHT_BATTLE("/kcsapi/api_req_combined_battle/midnight_battle"),
    /** 戦闘(夜戦) */
    COMBINED_SP_MIDNIGHT("/kcsapi/api_req_combined_battle/sp_midnight"),
    /** 戦闘結果 */
    BATTLE_RESULT("/kcsapi/api_req_sortie/battleresult"),
    /** 戦闘結果(連合艦隊) */
    COMBINED_BATTLE_RESULT("/kcsapi/api_req_combined_battle/battleresult"),
    /** 開発 */
    CREATE_ITEM("/kcsapi/api_req_kousyou/createitem"),
    /** 建造 */
    CREATE_SHIP("/kcsapi/api_req_kousyou/createship"),
    /** 建造ドック */
    KDOCK("/kcsapi/api_get_member/kdock"),
    /** 建造(入手) */
    GET_SHIP("/kcsapi/api_req_kousyou/getship"),
    /** 解体 */
    DESTROY_SHIP("/kcsapi/api_req_kousyou/destroyship"),
    /** 廃棄 */
    DESTROY_ITEM2("/kcsapi/api_req_kousyou/destroyitem2"),
    /** 近代化改修 */
    POWERUP("/kcsapi/api_req_kaisou/powerup"),
    /** 出撃 */
    START("/kcsapi/api_req_map/start"),
    /** 進撃 */
    NEXT("/kcsapi/api_req_map/next"),
    /** 任務一覧 */
    QUEST_LIST("/kcsapi/api_get_member/questlist"),
    /** 任務消化 */
    QUEST_CLEAR("/kcsapi/api_req_quest/clearitemget"),
    /** 設定 */
    START2("/kcsapi/api_start2"),
    /** 演習結果 */
    PRACTICE_RESULT("/kcsapi/api_req_practice/battle_result"),
    /** 入渠 */
    NYUKYO("/kcsapi/api_req_nyukyo/start"),
    /** 装備改修一覧 */
    REMODEL_SLOTLIST("/kcsapi/api_req_kousyou/remodel_slotlist"),
    /** 装備改修 */
    REMODEL_SLOT("/kcsapi/api_req_kousyou/remodel_slot"),
    /** 図鑑表示 */
    PICTURE_BOOK("/kcsapi/api_get_member/picture_book"),
    /** 戦績表示 */
    RECORD("/kcsapi/api_get_member/record"),
    /** ランキング */
    RANKING("/kcsapi/api_req_ranking/getlist"),
    /** アイテム */
    PAYITEM("/kcsapi/api_get_member/payitem"),
    /** 模様替え */
    FURNITURE("/kcsapi/api_req_furniture/change"),
    /** フィルタ前のデータ */
    UNDEFINED(null);

    public static final Map<String, DataType> TYPEMAP;

    static {
        TYPEMAP = new ConcurrentHashMap<>();
        for (DataType type : values()) {
            if (type.getUrl() != null) {
                TYPEMAP.put(type.getUrl(), type);
            }
        }
    }

    private final String url;

    private DataType(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }
}
