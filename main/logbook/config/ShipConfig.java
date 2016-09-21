package logbook.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import logbook.constants.AppConstants;
import logbook.dto.ShipInfoDto;
import logbook.internal.Ship;
import logbook.util.BeanUtils;

/**
 * 艦娘のIDと名前の紐付けを保存・復元します
 *
 */
public class ShipConfig {

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(ShipConfig.class);
    }

    /**
     * 設定ファイルに書き込みます
     */
    public static void store() throws IOException {
        Set<Integer> shipids = Ship.keySet();
        Map<Integer, ShipInfoDto> map = new HashMap<Integer, ShipInfoDto>();
        for (int key : shipids) {
            ShipInfoDto ship = Ship.get(key);
            map.put(key, ship);
        }
        BeanUtils.writeObject(AppConstants.SHIP_CONFIG_FILE, map);
    }

    /**
     * 艦娘のIDと名前の紐付けを設定ファイルから読み込みます
     */
    public static void load() {
        try {
            Map<Integer, ShipInfoDto> map = BeanUtils.readObject(AppConstants.SHIP_CONFIG_FILE, Map.class);
            if (map != null) {
                for (Entry<Integer, ShipInfoDto> entry : map.entrySet()) {
                    Ship.set(entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            LoggerHolder.LOG.warn("艦娘のIDと名前の紐付けを設定ファイルから読み込みますに失敗しました", e);
        }
    }
}
