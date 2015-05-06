package logbook.data.event;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

import logbook.config.AppConfig;
import logbook.constants.AppConstants;
import logbook.data.Data;
import logbook.data.DataType;
import logbook.data.EventListener;
import logbook.data.EventTarget;
import logbook.data.context.ConsoleContext;
import logbook.dto.MaterialDto;
import logbook.gui.logic.CreateReportLogic;

/**
 * 保有資源・資材
 *
 */
@EventTarget({ DataType.MATERIAL, DataType.PORT })
public class Material implements EventListener {

    /** 保有資源・資材 */
    private MaterialDto material;

    /** 最後に資源ログに追加した時間 */
    private Date materialLogLastUpdate;

    @Override
    public void update(DataType type, Data data) {
        switch (type) {
        case MATERIAL:
            JsonArray obj1 = data.getJsonObject().getJsonArray("api_data");
            this.doMaterialSub(obj1);
            break;
        case PORT:
            JsonObject obj2 = data.getJsonObject().getJsonObject("api_data");
            JsonArray apiMaterial = obj2.getJsonArray("api_material");
            this.doMaterialSub(apiMaterial);
            break;
        default:
            break;
        }
        ConsoleContext.log("保有資材を更新しました");
    }

    /**
     * 保有資材を更新する
     *
     * @param apidata
     */
    private void doMaterialSub(JsonArray apidata) {
        Date time = Calendar.getInstance().getTime();
        MaterialDto dto = new MaterialDto();
        dto.setTime(time);

        for (JsonValue value : apidata) {
            JsonObject entry = (JsonObject) value;

            switch (entry.getInt("api_id")) {
            case AppConstants.MATERIAL_FUEL:
                dto.setFuel(entry.getInt("api_value"));
                break;
            case AppConstants.MATERIAL_AMMO:
                dto.setAmmo(entry.getInt("api_value"));
                break;
            case AppConstants.MATERIAL_METAL:
                dto.setMetal(entry.getInt("api_value"));
                break;
            case AppConstants.MATERIAL_BAUXITE:
                dto.setBauxite(entry.getInt("api_value"));
                break;
            case AppConstants.MATERIAL_BURNER:
                dto.setBurner(entry.getInt("api_value"));
                break;
            case AppConstants.MATERIAL_BUCKET:
                dto.setBucket(entry.getInt("api_value"));
                break;
            case AppConstants.MATERIAL_RESEARCH:
                dto.setResearch(entry.getInt("api_value"));
                break;
            default:
                break;
            }
        }
        this.material = dto;

        // 資材ログに書き込む
        if ((this.materialLogLastUpdate == null)
                || (TimeUnit.MILLISECONDS.toSeconds(time.getTime() - this.materialLogLastUpdate.getTime()) >
                AppConfig.get().getMaterialLogInterval())) {
            CreateReportLogic.storeMaterialReport(this.material);

            this.materialLogLastUpdate = time;
        }
    }
}
