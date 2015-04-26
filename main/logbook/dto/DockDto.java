package logbook.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 艦隊のドックを表します
 *
 */
public final class DockDto extends AbstractDto {
    private static final int DOCK_MAX_SIZE = 6;
    private static final String EMPTY_FLEET_ID = "";

    /** ドックID */
    private final String id;

    /** 艦隊名 */
    private final String name;

    /** 艦娘達 */
    private final List<ShipDto> ships = new ArrayList<ShipDto>(DOCK_MAX_SIZE);

    /** 更新フラグ */
    private boolean update;

    /**
     * コンストラクター
     */
    public DockDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * ドックIDを取得します。
     * @return ドックID
     */
    public String getId() {
        return this.id;
    }

    /**
     * 艦娘を艦隊に追加します
     * 
     * @param ship
     */
    public void addShip(ShipDto ship) {
        if (this.ships.size() < DOCK_MAX_SIZE) {
            ship.setFleetid(this.id);
            this.ships.add(ship);

            this.setUpdate(true);
            return;
        }
        throw new IndexOutOfBoundsException();
    }

    public ShipDto displaceShip(int fleet_position, ShipDto ship) {
        return this.setShip(fleet_position, ship, false);
    }

    public ShipDto replaceShip(int fleet_position, ShipDto ship) {
        return this.setShip(fleet_position, ship, true);
    }

    private ShipDto setShip(int fleet_position, ShipDto ship, final boolean setEmptyFleet) {
        ShipDto oship = this.ships.set(fleet_position, ship);
        ship.setFleetid(this.id);

        if (setEmptyFleet)
            oship.setFleetid(EMPTY_FLEET_ID);

        this.setUpdate(true);
        return oship;
    }

    public void removeShip(int fleet_position) {
        ShipDto ship = this.ships.remove(fleet_position);
        ship.setFleetid(EMPTY_FLEET_ID);

        this.setUpdate(true);
    }

    public void removeOthers() {
        List<ShipDto> other_ships = this.ships.stream().skip(1L).map(ship -> {
            ship.setFleetid(EMPTY_FLEET_ID);
            return ship;
        }).collect(Collectors.toList());

        this.ships.removeAll(other_ships);

        this.setUpdate(true);
    }

    public int size() {
        return this.ships.size();
    }

    public int indexOf(ShipDto ship) {
        return this.ships.indexOf(ship);
    }

    /**
     * 艦隊名を取得します。
     * @return 艦隊名
     */
    public String getName() {
        return this.name;
    }

    /**
     * 艦娘達を取得します。
     * @return 艦娘達
     */
    public List<ShipDto> getShips() {
        return Collections.unmodifiableList(this.ships);
    }

    /**
     * 更新フラグを取得します。
     * @return 更新フラグ
     */
    public boolean isUpdate() {
        return this.update;
    }

    /**
     * 更新フラグを設定します。
     * @param update 更新フラグ
     */
    public void setUpdate(boolean update) {
        this.update = update;
    }
}
