package logbook.data.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.json.JsonArray;
import javax.json.JsonObject;

import logbook.annotation.EventTarget;
import logbook.data.Data;
import logbook.data.DataType;
import logbook.data.EventListener;
import logbook.data.context.GlobalContext;
import logbook.data.context.ShipContext;
import logbook.dto.ShipDto;

/**
 * 編成展開
 *
 */
@EventTarget({ DataType.PRESET_SELECT })
public class PresetSelect implements EventListener {

    @Override
    public void update(DataType type, final Data data) {
        Optional.ofNullable(GlobalContext.getDock(data.getField("api_deck_id"))).ifPresent(
                dock -> {
                    JsonObject apiData = data.getJsonObject().getJsonObject("api_data");
                    if (apiData != null) {
                        JsonArray ships = apiData.getJsonArray("api_ship");
                        final List<ShipDto> newShips = new ArrayList<>();
                        for (int i = 0; i < ships.size(); i++) {
                            Optional.ofNullable(ShipContext.get().get(Long.valueOf(ships.getInt(i)))).ifPresent(
                                    newShips::add);
                        }
                        dock.replaceShips(apiData.getString("api_name"), newShips);
                        dock.setUpdate(true);
                    }
                });
    }

}
