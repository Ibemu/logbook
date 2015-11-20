package logbook.data.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonValue;

import logbook.annotation.EventTarget;
import logbook.data.Data;
import logbook.data.DataType;
import logbook.data.EventListener;
import logbook.data.context.ShipContext;

/**
 * 装備入れ替え
 *
 */
@EventTarget({ DataType.SLOT_EXCHANGE_INDEX })
public class SlotExchangeIndex implements EventListener {

    @Override
    public void update(DataType type, final Data data) {
        Optional.ofNullable(ShipContext.get().get(Long.valueOf(data.getField("api_id")))).ifPresent(
                ship -> {
                    JsonObject apiData = data.getJsonObject().getJsonObject("api_data");
                    if (apiData != null) {
                        JsonArray apiSlot = apiData.getJsonArray("api_slot");
                        List<Long> newSlot = new ArrayList<>(apiSlot.size());
                        for (JsonValue jsonValue : apiSlot) {
                            JsonNumber itemid = (JsonNumber) jsonValue;
                            newSlot.add(Long.valueOf(itemid.longValue()));
                        }
                        ship.setSlot(newSlot);
                    }
                });
    }

}
