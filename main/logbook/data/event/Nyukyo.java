package logbook.data.event;

import logbook.data.Data;
import logbook.data.DataType;
import logbook.data.EventListener;
import logbook.data.EventTarget;
import logbook.data.context.GlobalContext;
import logbook.data.context.ShipContext;
import logbook.dto.DockDto;
import logbook.dto.ShipDto;

/**
 * 入渠
 *
 */
@EventTarget({ DataType.NYUKYO })
public class Nyukyo implements EventListener {

    @Override
    public void update(DataType type, Data data) {
        if ("1".equals(data.getField("api_highspeed"))) {
            Long id = Long.valueOf(data.getField("api_ship_id"));
            ShipDto ship = ShipContext.get().get(id);
            if (ship != null) {
                ship.setNowHp(ship.getMaxhp());
                ship.setDocktime(0);

                String fleetid = ship.getFleetid();
                DockDto dockdto = GlobalContext.getDock(fleetid);
                if (dockdto != null) {
                    dockdto.setUpdate(true);
                }
            }
        }
    }

}
