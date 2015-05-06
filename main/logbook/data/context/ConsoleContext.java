package logbook.data.context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import logbook.constants.AppConstants;

/**
 * 母港画面のログ出力
 */
public class ConsoleContext {

    /** ログキュー */
    private static Queue<String> consoleQueue = new ArrayBlockingQueue<String>(10);

    /**
     * ログキューからログメッセージを取り出します
     * @return ログメッセージ
     */
    public static String getConsoleMessage() {
        return consoleQueue.poll();
    }

    /**
     * 母港画面のログ出力
     *
     * @param message ログメッセージ
     */
    public static void log(Object message) {
        consoleQueue.offer(new SimpleDateFormat(AppConstants.DATE_SHORT_FORMAT)
                .format(Calendar.getInstance().getTime())
                + "  " + message.toString());
    }
}
