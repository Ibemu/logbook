package logbook.data;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * イベントをリスナーに送信するクラスです
 *
 */
public final class EventSender {

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(EventSender.class);
    }

    private final Map<DataType, List<EventListener>> listenerMap = new EnumMap<>(DataType.class);

    /**
     * リスナーを追加します
     *
     * @param listener
     */
    public void addEventListener(EventListener listener) {
        DataType[] types = this.getTypes(listener);
        for (DataType key : types) {
            List<EventListener> listeners = this.listenerMap.get(key);

            if (listeners == null) {
                listeners = new ArrayList<>();
                this.listenerMap.put(key, listeners);
            }
            synchronized (listeners) {
                listeners.add(listener);
            }
        }
    }

    /**
     * リスナーを除去します
     *
     * @param listener
     */
    public void removeEventListener(EventListener listener) {
        DataType[] types = this.getTypes(listener);
        for (DataType key : types) {
            List<EventListener> listeners = this.listenerMap.get(key);

            if (listeners != null) {
                synchronized (listeners) {
                    while (!listeners.remove(listener)) {
                    }
                }
            }
        }
    }

    /**
     * イベントを送信します(同期)
     *
     * @param type
     * @param data
     */
    public void syncSendEvent(DataType type, Data data) {
        List<EventListener> listeners = this.listenerMap.get(type);
        if (listeners != null) {
            synchronized (listeners) {
                for (EventListener listener : listeners) {
                    try {
                        listener.update(type, data);
                    } catch (Exception e) {
                        this.handle(listener, data, e);
                    }
                }
            }
        }
    }

    /**
     * リスナーのターゲット注釈からデータの種類を取得します
     *
     * @param listener
     * @return
     */
    private DataType[] getTypes(EventListener listener) {
        EventTarget target = listener.getClass().getAnnotation(EventTarget.class);
        if (target == null) {
            // 注釈がない場合全て
            return DataType.values();
        }
        return target.value();
    }

    /**
     * エラーハンドラ
     *
     * @param listener
     * @param data
     * @param e
     */
    private void handle(EventListener listener, Data data, Exception e) {
        LoggerHolder.LOG.warn(listener.getClass() + " でキャッチされない例外が発生しました", e);
        LoggerHolder.LOG.warn(data);
    }
}
