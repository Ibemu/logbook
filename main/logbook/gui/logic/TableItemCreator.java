package logbook.gui.logic;

import java.util.HashSet;
import java.util.Set;

import logbook.constants.AppConstants;
import logbook.data.context.GlobalContext;
import logbook.dto.DeckMissionDto;
import logbook.dto.NdockDto;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * テーブルの行を作成するインターフェイスです
 *
 */
public interface TableItemCreator {

    /** テーブルアイテム作成(デフォルト) */
    TableItemCreator DEFAULT_TABLE_ITEM_CREATOR = new TableItemCreator() {

        @Override
        public void init() {
        }

        @Override
        public TableItem create(Table table, String[] text, int count) {
            TableItem item = new TableItem(table, SWT.NONE);
            // 偶数行に背景色を付ける
            if ((count % 2) != 0) {
                item.setBackground(SWTResourceManager.getColor(AppConstants.ROW_BACKGROUND));
            }
            item.setText(text);
            return item;
        }
    };
    /** テーブルアイテム作成(所有艦娘一覧) */
    TableItemCreator SHIP_LIST_TABLE_ITEM_CREATOR = new TableItemCreator() {

        private Set<Long> deckmissions;

        private Set<Long> docks;

        @Override
        public void init() {
            // 遠征
            this.deckmissions = new HashSet<Long>();
            for (DeckMissionDto deckMission : GlobalContext.getDeckMissions()) {
                if ((deckMission.getMission() != null) && (deckMission.getShips() != null)) {
                    this.deckmissions.addAll(deckMission.getShips());
                }
            }
            // 入渠
            this.docks = new HashSet<Long>();
            for (NdockDto ndock : GlobalContext.getNdocks()) {
                if (ndock.getNdockid() != 0) {
                    this.docks.add(ndock.getNdockid());
                }
            }
        }

        @Override
        public TableItem create(Table table, String[] text, int count) {
            // 艦娘
            Long ship = Long.valueOf(text[1]);

            TableItem item = new TableItem(table, SWT.NONE);
            // 偶数行に背景色を付ける
            if ((count % 2) != 0) {
                item.setBackground(SWTResourceManager.getColor(AppConstants.ROW_BACKGROUND));
            }

            // 疲労
            int cond = Integer.parseInt(text[5]);
            if (cond <= AppConstants.COND_RED) {
                item.setForeground(SWTResourceManager.getColor(AppConstants.COND_RED_COLOR));
            } else if (cond <= AppConstants.COND_ORANGE) {
                item.setForeground(SWTResourceManager.getColor(AppConstants.COND_ORANGE_COLOR));
            } else if ((cond >= AppConstants.COND_DARK_GREEN) && (cond < AppConstants.COND_GREEN)) {
                item.setForeground(SWTResourceManager.getColor(AppConstants.COND_DARK_GREEN_COLOR));
            } else if (cond >= AppConstants.COND_GREEN) {
                item.setForeground(SWTResourceManager.getColor(AppConstants.COND_GREEN_COLOR));
            }

            // 遠征
            if (this.deckmissions.contains(ship)) {
                item.setForeground(SWTResourceManager.getColor(AppConstants.MISSION_COLOR));
            }
            // 入渠
            if (this.docks.contains(ship)) {
                item.setForeground(SWTResourceManager.getColor(AppConstants.NDOCK_COLOR));
            }
            // Lv1の艦娘をグレー色にする
            if ("1".equals(text[7])) {
                item.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
            }

            item.setText(text);
            return item;
        }
    };

    void init();

    TableItem create(Table table, String[] text, int count);
}
