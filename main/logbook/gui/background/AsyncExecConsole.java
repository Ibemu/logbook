package logbook.gui.background;

import logbook.data.context.ConsoleContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;

/**
 * 非同期にコンソールを更新します
 *
 */
public final class AsyncExecConsole extends Thread {

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(AsyncExecConsole.class);
    }

    private static final int MAX_LOG_LINES = 200;

    private final List console;

    /**
     * 非同期にコンソールを更新します
     *
     * @param display
     * @param console
     */
    public AsyncExecConsole(List console) {
        this.console = console;
        this.setName("logbook_async_exec_application_main_console");
    }

    /**
     * 現在のメイン画面を更新します
     */
    @Override
    public void run() {
        try {
            // ログメッセージを取り出す
            String message;
            while ((message = ConsoleContext.getConsoleMessage()) != null) {
                Display.getDefault().syncExec(new UpdateConsoleTask(this.console, message));
            }
        } catch (Exception e) {
            LoggerHolder.LOG.fatal("スレッドが異常終了しました", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 非同期にコンソールを更新します
     */
    private static final class UpdateConsoleTask implements Runnable {

        private final List console;
        private final String message;

        /**
         * 非同期にコンソールを更新します
         */
        public UpdateConsoleTask(List console, String message) {
            this.console = console;
            this.message = message;
        }

        @Override
        public void run() {
            int size = this.console.getItemCount();
            if (size >= MAX_LOG_LINES) {
                this.console.remove(0);
            }
            this.console.add(this.message);
            this.console.setSelection(this.console.getItemCount() - 1);
        }
    }
}
