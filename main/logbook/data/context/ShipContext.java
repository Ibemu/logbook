package logbook.data.context;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.annotation.CheckForNull;

import logbook.dto.ShipDto;

/**
 * 艦娘を管理します
 */
public class ShipContext {

    /** 秘書艦 */
    private static ShipDto secretary;

    /** 秘書艦を変えた時刻 */
    private static Date modifySecretaryDate;

    /** 艦娘Map */
    private static Map<Long, ShipDto> shipMap = new ConcurrentSkipListMap<Long, ShipDto>();

    /**
     * @return 艦娘Map
     */
    public static Map<Long, ShipDto> get() {
        return shipMap;
    }

    /**
     * 秘書艦を取得します
     *
     * @return 秘書艦
     */
    @CheckForNull
    public static ShipDto getSecretary() {
        return secretary;
    }

    /**
     * 秘書艦を設定します
     *
     * @param ship
     */
    public static void setSecretary(ShipDto ship) {
        if ((secretary == null) || (ship.getId() != secretary.getId())) {
            modifySecretaryDate = Calendar.getInstance().getTime();
            ConsoleContext.log(ship.getName() + "(Lv" + ship.getLv() + ")" + " が秘書艦に任命されました");
        }
        // 秘書艦を設定
        secretary = ship;
    }

    /**
     * 秘書艦を変えた時刻を取得します
     *
     * @return 秘書艦を変えた時刻
     */
    @CheckForNull
    public static Date getModifySecretaryDate() {
        return modifySecretaryDate;
    }
}
