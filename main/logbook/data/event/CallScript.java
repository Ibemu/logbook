package logbook.data.event;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import logbook.config.AppConfig;
import logbook.data.Data;
import logbook.data.DataType;
import logbook.data.EventListener;
import logbook.data.ScriptEventAdapter;
import logbook.data.ScriptLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * スクリプトの呼び出しを行うリスナー
 *
 */
public final class CallScript implements EventListener {

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(CallScript.class);
    }

    /** ScriptLoader */
    private ScriptLoader loader;

    /** ユーザースクリプトによって実装されたリスナー */
    private List<EventListener> listeners;

    @Override
    public void update(DataType type, Data data) {
        if (AppConfig.get().isUseUserScript()) {
            if (this.loader == null) {
                this.loader = new ScriptLoader();
                this.listeners = new ArrayList<>();

                String[] scripts = AppConfig.get().getUserScripts();
                if (scripts != null) {
                    for (String script : scripts) {
                        try {
                            EventListener listener = this.loader.getEventListener(Paths.get(script));
                            if (listener != null) {
                                this.listeners.add(listener);
                            } else {
                                LoggerHolder.LOG.warn("ユーザースクリプト " + script + " にupdate(DataType, Data)関数が見つかりません");
                            }
                        } catch (Exception e) {
                            LoggerHolder.LOG.warn("ユーザースクリプト " + script + " の初期化で例外が発生しました", e);
                        }
                    }
                }
            }

            for (EventListener listener : this.listeners) {
                try {
                    listener.update(type, data);
                } catch (Exception e) {
                    LoggerHolder.LOG.warn(((ScriptEventAdapter) listener).getPath() + " でキャッチされない例外が発生しました", e);
                    LoggerHolder.LOG.warn(data);
                }
            }
        }
    }
}
