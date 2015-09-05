package logbook.dto;

import java.util.HashSet;
import java.util.Set;

import logbook.config.bean.ShipGroupBean;
import logbook.internal.SallyArea;
import logbook.internal.ShipStyle;

/**
 * 所有艦娘一覧で使用するフィルター
 */
public final class ShipFilterDto extends AbstractDto {

    /** 名前 */
    public String nametext;
    /** 名前.正規表現を使用する */
    public boolean regexp;

    /** 艦種 */
    public Set<String> shipType = new HashSet<>(ShipStyle.get().values());

    /** グループ */
    public ShipGroupBean group;
    /** 出撃海域 */
    public SallyArea area;
    /** 装備 */
    public String itemname;
    /** 艦隊に所属 */
    public boolean onfleet = true;
    /** 艦隊に非所属 */
    public boolean notonfleet = true;
    /** 鍵付き */
    public boolean locked = true;
    /** 鍵付きではない */
    public boolean notlocked = true;
}
