package logbook.dto;

import java.util.HashSet;
import java.util.Set;

import logbook.internal.Ship;

/**
 * 艦娘の名前と種別を表します
 *
 */
public final class ShipInfoDto extends AbstractDto {

    /** 空の艦種 */
    public static final ShipInfoDto EMPTY = new ShipInfoDto();

    /** 艦船ID */
    private int shipId;

    /** 名前 */
    private String name;

    /** 艦種 */
    private String type;

    /** 改レベル */
    private int afterlv;

    /** 改造後 */
    private int afterShipId;

    /** flagshipもしくはelite (敵艦のみ) */
    private String flagship;

    /** 弾 */
    private int maxBull;

    /** 燃料 */
    private int maxFuel;

    /**
     * コンストラクター
     */
    public ShipInfoDto() {
        this(0, "", "", "", 0, 0, 0, 0);
    }

    /**
     * コンストラクター
     *
     * @param shipId ID
     * @param name 名前
     * @param type 艦種
     * @param flagship flagshipもしくはelite (敵艦のみ)
     * @param afterlv 改レベル
     * @param afterShipId 改造後
     * @param maxBull 弾
     * @param maxFuel 燃料
     */
    public ShipInfoDto(int shipId, String name, String type, String flagship, int afterlv, int afterShipId, int maxBull,
            int maxFuel) {
        this.shipId = shipId;
        this.name = name;
        this.type = type;
        this.flagship = flagship;
        this.afterlv = afterlv;
        this.afterShipId = afterShipId;
        this.maxBull = maxBull;
        this.maxFuel = maxFuel;
    }

    /**
     * 名前を取得します。
     * @return 名前
     */
    public String getName() {
        return this.name;
    }

    /**
     * 名前を設定します。
     * @param name 名前
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 改レベルを設定します。
     * @param afterlv 改レベル
     */
    public void setAfterlv(int afterlv) {
        this.afterlv = afterlv;
    }

    /**
     * 艦種を取得します。
     * @return 艦種
     */
    public String getType() {
        return this.type;
    }

    /**
     * 艦種を設定します。
     * @param type 艦種
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return 改造レベル(改造ができない場合、0)
     */
    public int getAfterlv() {
        return this.afterlv;
    }

    /**
     * flagshipもしくはelite (敵艦のみ)を取得します。
     * @return flagshipもしくはelite (敵艦のみ)
     */
    public String getFlagship() {
        return this.flagship;
    }

    /**
     * flagshipもしくはelite (敵艦のみ)を設定します。
     * @param flagship flagshipもしくはelite (敵艦のみ)
     */
    public void setFlagship(String flagship) {
        this.flagship = flagship;
    }

    /**
     * 弾を取得します。
     * @return 弾
     */
    public int getMaxBull() {
        return this.maxBull;
    }

    /**
     * 弾を設定します。
     * @param maxBull 弾
     */
    public void setMaxBull(int maxBull) {
        this.maxBull = maxBull;
    }

    /**
     * 燃料を取得します。
     * @return 燃料
     */
    public int getMaxFuel() {
        return this.maxFuel;
    }

    /**
     * 燃料を設定します。
     * @param maxFuel 燃料
     */
    public void setMaxFuel(int maxFuel) {
        this.maxFuel = maxFuel;
    }

    /**
     * 改造後艦船を取得します。
     * @return 改造後艦船
     */
    public int getAfterShipId() {
        return this.afterShipId;
    }

    /**
     * 改造後艦船を設定します。
     * @param afterShipId 改造後艦船
     */
    public void setAfterShipId(int afterShipId) {
        this.afterShipId = afterShipId;
    }

    /**
     * 艦船IDを設定します。
     * @param afterShipId 艦船ID
     */
    public int getShipId() {
        return this.shipId;
    }

    /**
     * 艦船IDを設定します。
     * @param shipId 艦船ID
     */
    public void setShipId(int shipId) {
        this.shipId = shipId;
    }

    /**
     * 改造後艦船を含めて同一艦娘か判定します。
     * @param other
     * @return otherがthisと同じ艦娘あるいは改造前後の関係の場合true
     */
    public boolean compareShipIncludeAfter(ShipInfoDto other) {
        if (this.getShipId() == other.getShipId())
            return true;
        Set<Integer> afters = new HashSet<>();
        afters.add(other.getShipId());
        int after = other.getAfterShipId();
        while ((after != 0) && !afters.contains(after)) {
            if (this.getShipId() == after)
                return true;
            afters.add(after);
            after = Ship.get(Integer.toString(after)).getAfterShipId();
        }
        afters.clear();
        after = this.getAfterShipId();
        while ((after != 0) && !afters.contains(after)) {
            if (other.getShipId() == after)
                return true;
            afters.add(after);
            after = Ship.get(Integer.toString(after)).getAfterShipId();
        }
        return false;
    }
}
