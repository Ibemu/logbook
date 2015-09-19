package logbook.gui.listener;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

/**
 * {@link SelectionListener}の関数型インターフェース
 *
 */
@FunctionalInterface
public interface SelectedListener extends SelectionListener {

    @Override
    void widgetSelected(SelectionEvent e);

    @Override
    default void widgetDefaultSelected(SelectionEvent e) {
    }
}
