package logbook.gui.logic;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import logbook.dto.ItemDto;
import logbook.dto.ShipDto;
import logbook.dto.ShipFilterDto;
import logbook.gui.bean.ShipBean;

import org.apache.commons.lang3.StringUtils;

/**
 * 艦娘フィルタロジック
 *
 */
public final class ShipFilterLogic implements Predicate<ShipBean> {

    private final ShipFilterDto filter;

    /**
     * 艦娘フィルタ
     */
    public ShipFilterLogic(ShipFilterDto filter) {
        this.filter = filter;
    }

    @Override
    public boolean test(ShipBean t) {
        return this.shipFilter(t.getShip());
    }

    /**
     * 艦娘をフィルタします
     *
     * @param ship 艦娘
     * @param filter フィルターオブジェクト
     * @return フィルタ結果
     */
    private boolean shipFilter(ShipDto ship) {
        // テキストでフィルタ
        if (!StringUtils.isEmpty(this.filter.nametext)) {
            if (!textFilter(ship, this.filter)) {
                return false;
            }
        }
        // 艦種でフィルタ
        if (!typeFilter(ship, this.filter)) {
            return false;
        }
        // グループでフィルタ
        if (this.filter.group != null) {
            if (!this.filter.group.getShips().contains(ship.getId())) {
                return false;
            }
        }
        // 装備でフィルタ
        if (!StringUtils.isEmpty(this.filter.itemname)) {
            if (!itemFilter(ship, this.filter)) {
                return false;
            }
        }
        // 艦隊に所属
        if (!this.filter.onfleet) {
            if (!StringUtils.isEmpty(ship.getFleetid())) {
                return false;
            }
        }
        // 艦隊に非所属
        if (!this.filter.notonfleet) {
            if (StringUtils.isEmpty(ship.getFleetid())) {
                return false;
            }
        }
        // 鍵付き
        if (!this.filter.locked) {
            if (ship.getLocked()) {
                return false;
            }
        }
        // 鍵付きではない
        if (!this.filter.notlocked) {
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
        List<String> items = ship.getSlot();

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
                for (String item : items) {
                    find = find ? find : pattern.matcher(item).find();
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
                for (String item : items) {
                    find = find ? find : item.indexOf(words[i]) != -1;
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
        return filter.shipType.contains(ship.getType());
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
