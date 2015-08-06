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
            item.setText(text);
            update(item, text, count);
            return item;
        }

        @Override
        public TableItem update(TableItem item, String[] text, int count) {
            // 偶数行に背景色を付ける
            if ((count % 2) != 0) {
                item.setBackground(SWTResourceManager.getColor(AppConstants.ROW_BACKGROUND));
            } else {
                item.setBackground(null);
            }
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
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(text);
            update(item, text, count);
            return item;
        }

        /* (非 Javadoc)
         * @see logbook.gui.logic.TableItemCreator#update(org.eclipse.swt.widgets.TableItem, java.lang.String[], int)
         */
        @Override
        public TableItem update(TableItem item, String[] text, int count) {

            // 艦娘
            Long ship = Long.valueOf(text[1]);

            // 偶数行に背景色を付ける
            if ((count % 2) != 0) {
                item.setBackground(SWTResourceManager.getColor(AppConstants.ROW_BACKGROUND));
            } else {
                item.setBackground(null);
            }

            // 疲労
            int cond = Integer.parseInt(text[5]);

            if ("1".equals(text[7])) {
                // Lv1の艦娘をグレー色にする
                item.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
            } else if (this.docks.contains(ship)) {
                // 入渠
                item.setForeground(SWTResourceManager.getColor(AppConstants.NDOCK_COLOR));
            } else if (this.deckmissions.contains(ship)) {
                // 遠征
                item.setForeground(SWTResourceManager.getColor(AppConstants.MISSION_COLOR));
            } else if (cond <= AppConstants.COND_RED) {
                // 赤疲労
                item.setForeground(SWTResourceManager.getColor(AppConstants.COND_RED_COLOR));
            } else if (cond <= AppConstants.COND_ORANGE) {
                // 疲労
                item.setForeground(SWTResourceManager.getColor(AppConstants.COND_ORANGE_COLOR));
            } else if ((cond >= AppConstants.COND_DARK_GREEN) && (cond < AppConstants.COND_GREEN)) {
                // cond.50-52
                item.setForeground(SWTResourceManager.getColor(AppConstants.COND_DARK_GREEN_COLOR));
            } else if (cond >= AppConstants.COND_GREEN) {
                // cond.53-
                item.setForeground(SWTResourceManager.getColor(AppConstants.COND_GREEN_COLOR));
            } else {
                item.setForeground(null);
            }

            return item;
        }
    };

    void init();

    TableItem create(Table table, String[] text, int count);

    TableItem update(TableItem item, String[] text, int count);
}
