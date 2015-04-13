package logbook.gui.logic;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import logbook.dto.ItemDto;
import logbook.dto.ShipDto;
import logbook.dto.ShipFilterDto;

import org.apache.commons.lang3.StringUtils;

/**
 * 艦娘フィルタロジック
 *
 */
public class ShipFilterLogic {

    /**
     * 艦娘をフィルタします
     *
     * @param ship 艦娘
     * @param filter フィルターオブジェクト
     * @return フィルタ結果
     */
    public static boolean shipFilter(ShipDto ship, ShipFilterDto filter) {
        // テキストでフィルタ
        if (!StringUtils.isEmpty(filter.nametext)) {
            if (!textFilter(ship, filter)) {
                return false;
            }
        }
        // 艦種でフィルタ
        if (!typeFilter(ship, filter)) {
            return false;
        }
        // グループでフィルタ
        if (filter.group != null) {
            if (!filter.group.getShips().contains(ship.getId())) {
                return false;
            }
        }
        // 装備でフィルタ
        if (!StringUtils.isEmpty(filter.itemname)) {
            if (!itemFilter(ship, filter)) {
                return false;
            }
        }
        // 艦隊に所属
        if (!filter.onfleet) {
            if (!StringUtils.isEmpty(ship.getFleetid())) {
                return false;
            }
        }
        // 艦隊に非所属
        if (!filter.notonfleet) {
            if (StringUtils.isEmpty(ship.getFleetid())) {
                return false;
            }
        }
        // 鍵付き
        if (!filter.locked) {
            if (ship.getLocked()) {
                return false;
            }
        }
        // 鍵付きではない
        if (!filter.notlocked) {
            if (!ship.getLocked()) {
                return false;
            }
        }
        return true;
    }

    /**
     * テキストでフィルタ
     *
     * @param ship
     * @param filter
     */
    private static boolean textFilter(ShipDto ship, ShipFilterDto filter) {
        // 検索ワード
        String[] words = StringUtils.split(filter.nametext, " ");
        // 検索対象
        // 名前
        String name = ship.getName();
        // 艦種
        String type = ship.getType();
        // 装備
        List<ItemDto> item = ship.getItem();

        // テキストが入力されている場合処理する
        if (filter.regexp) {
            // 正規表現で検索
            for (int i = 0; i < words.length; i++) {
                Pattern pattern;
                try {
                    pattern = Pattern.compile(words[i]);
                } catch (PatternSyntaxException e) {
                    // 無効な正規表現はfalseを返す
                    return false;
                }
                boolean find = false;

                // 名前で検索
                find = find ? find : pattern.matcher(name).find();
                // 艦種で検索
                find = find ? find : pattern.matcher(type).find();
                // 装備で検索
                for (ItemDto itemDto : item) {
                    if ((itemDto == null) || (itemDto.getName() == null)) {
                        find = find ? find : false;
                    } else {
                        find = find ? find : pattern.matcher(itemDto.getName()).find();
                    }
                }

                if (!find) {
                    // どれにもマッチしない場合
                    return false;
                }
            }
        } else {
            // 部分一致で検索する
            for (int i = 0; i < words.length; i++) {
                boolean find = false;

                // 名前で検索
                find = find ? find : name.indexOf(words[i]) != -1;
                // 艦種で検索
                find = find ? find : type.indexOf(words[i]) != -1;
                // 装備で検索
                for (ItemDto itemDto : item) {
                    if ((itemDto == null) || (itemDto.getName() == null)) {
                        find = find ? find : false;
                    } else {
                        find = find ? find : itemDto.getName().indexOf(words[i]) != -1;
                    }
                }

                if (!find) {
                    // どれにもマッチしない場合
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 艦種でフィルタ
     *
     * @param ship
     * @param filter
     */
    private static boolean typeFilter(ShipDto ship, ShipFilterDto filter) {
        if (ship.getType() == null) {
            return false;
        }
        if (!filter.destroyer) {
            if ("駆逐艦".equals(ship.getType())) {
                return false;
            }
        }
        if (!filter.lightCruiser) {
            if ("軽巡洋艦".equals(ship.getType())) {
                return false;
            }
        }
        if (!filter.torpedoCruiser) {
            if ("重雷装巡洋艦".equals(ship.getType())) {
                return false;
            }
        }
        if (!filter.heavyCruiser) {
            if ("重巡洋艦".equals(ship.getType())) {
                return false;
            }
        }
        if (!filter.flyingDeckCruiser) {
            if ("航空巡洋艦".equals(ship.getType())) {
                return false;
            }
        }
        if (!filter.seaplaneTender) {
            if ("水上機母艦".equals(ship.getType())) {
                return false;
            }
        }
        if (!filter.escortCarrier) {
            if ("軽空母".equals(ship.getType())) {
                return false;
            }
        }
        if (!filter.carrier) {
            if ("正規空母".equals(ship.getType())) {
                return false;
            }
        }
        if (!filter.battleship) {
            if ("戦艦".equals(ship.getType())) {
                return false;
            }
        }
        if (!filter.flyingDeckBattleship) {
            if ("航空戦艦".equals(ship.getType())) {
                return false;
            }
        }
        if (!filter.submarine) {
            if ("潜水艦".equals(ship.getType())) {
                return false;
            }
        }
        if (!filter.carrierSubmarine) {
            if ("潜水空母".equals(ship.getType())) {
                return false;
            }
        }
        if (!filter.landingship) {
            if ("揚陸艦".equals(ship.getType())) {
                return false;
            }
        }
        if (!filter.armoredcarrier) {
            if ("装甲空母".equals(ship.getType())) {
                return false;
            }
        }
        if (!filter.repairship) {
            if ("工作艦".equals(ship.getType())) {
                return false;
            }
        }
        if (!filter.submarineTender) {
            if ("潜水母艦".equals(ship.getType())) {
                return false;
            }
        }
        if (!filter.trainingShip) {
            if ("練習巡洋艦".equals(ship.getType())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 装備でフィルタ
     *
     * @param ship
     * @param filter
     */
    private static boolean itemFilter(ShipDto ship, ShipFilterDto filter) {
        List<ItemDto> item = ship.getItem();
        boolean hit = false;
        for (ItemDto itemDto : item) {
            if (itemDto != null) {
                if (filter.itemname.equals(itemDto.getName())) {
                    hit = true;
                    break;
                }
            }
        }
        if (!hit) {
            return false;
        }
        return true;
    }
}
