package logbook.data.event;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonValue;

import logbook.data.Data;
import logbook.data.DataType;
import logbook.data.EventListener;
import logbook.data.EventTarget;
import logbook.data.context.ConsoleContext;
import logbook.data.context.ItemContext;
import logbook.dto.ItemDto;
import logbook.internal.Item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 装備改修
 *
 */
@EventTarget(DataType.REMODEL_SLOT)
public class RemodelSlot implements EventListener {

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(RemodelSlot.class);
    }

    @Override
    public void update(DataType type, Data data) {
        try {
            JsonObject apidata = data.getJsonObject().getJsonObject("api_data");

            if (apidata.containsKey("api_after_slot")) {
                // 装備改修
                JsonObject after = apidata.getJsonObject("api_after_slot");
                // 装備ID
                Long key = after.getJsonNumber("api_id").longValue();
                // 装備マスターID
                int id = after.getJsonNumber("api_slotitem_id").intValue();
                // 改修レベル
                int lv = after.getJsonNumber("api_level").intValue();

                ItemDto item = ItemContext.get().get(key);
                if (item.getId() != id) {
                    // 装備マスターのIDが異なる場合、装備更新
                    ItemContext.get().put(key, Item.get(id));
                }
                // 改修レベルをセット
                ItemContext.level().put(key, lv);
            }

            if (apidata.containsKey("api_use_slot_id")) {
                // 装備消費
                JsonArray ids = apidata.getJsonArray("api_use_slot_id");
                for (JsonValue id : ids) {
                    Long key = ((JsonNumber) id).longValue();
                    ItemContext.get().remove(key);
                }
            }
            ConsoleContext.log("装備改修を更新しました");
        } catch (Exception e) {
            LoggerHolder.LOG.warn("装備改修に失敗しました", e);
            LoggerHolder.LOG.warn(data);
        }
    }

}
