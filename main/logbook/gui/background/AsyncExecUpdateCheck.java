package logbook.gui.background;

import java.awt.Desktop;
import java.text.MessageFormat;

import logbook.config.AppConfig;
import logbook.constants.AppConstants;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * アップデートチェックを行います
 *
 */
public final class AsyncExecUpdateCheck implements Runnable {

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(AsyncExecUpdateCheck.class);
    }

    private static final String MESSAGE = "新しいバージョンがあります。ダウンロードサイトを開きますか？\r\n"
            + "現在のバージョン:" + AppConstants.VERSION + "\r\n"
            + "新しいバージョン:{0}\r\n"
            + "※自動アップデートチェックは[その他]-[設定]からOFFに出来ます";
    private static final String MESSAGE_PLUS = "新しいバージョンがあります。ダウンロードサイトを開きますか？\r\n"
            + "現在のバージョン:" + AppConstants.VERSION_FULL + "\r\n"
            + "新しいバージョン:{0}\r\n"
            + "※自動アップデートチェックは[その他]-[設定]からOFFに出来ます";

    private final Shell shell;

    /**
     * コンストラクター
     *
     * @param shell
     */
    public AsyncExecUpdateCheck(Shell shell) {
        this.shell = shell;
    }

    @Override
    public void run() {
        try {
            final String newversion = IOUtils.toString(AppConstants.UPDATE_CHECK_URI_PLUS);

            if (!AppConstants.VERSION_FULL.equals(newversion)) {
                Display.getDefault().asyncExec(() -> {
                    if (!this.shell.isDisposed()) {
                        MessageBox box = new MessageBox(this.shell, SWT.YES | SWT.NO
                                | SWT.ICON_QUESTION);
                        box.setText("新しいバージョン");
                        box.setMessage(MessageFormat.format(MESSAGE_PLUS, newversion));

                        // OKを押されたらダウンロードサイトへ移動する
                        if (box.open() == SWT.YES) {
                            try {
                                Desktop.getDesktop().browse(AppConstants.HOME_PAGE_URI_PLUS);
                            } catch (Exception e) {
                                LoggerHolder.LOG.warn("ダウンロードサイトに移動が失敗しました", e);
                            }
                        }
                    }
                });
            }
            else if (AppConfig.get().isCheckUpdateOriginal())
            {
                final String newversionorg = IOUtils.toString(AppConstants.UPDATE_CHECK_URI);

                if (!AppConstants.VERSION.equals(newversionorg)) {
                    Display.getDefault().asyncExec(new Runnable() {
                        @Override
                        public void run() {
                            Shell shell = AsyncExecUpdateCheck.this.shell;

                            if (shell.isDisposed()) {
                                // ウインドウが閉じられていたらなにもしない
                                return;
                            }

                            MessageBox box = new MessageBox(shell, SWT.YES | SWT.NO
                                    | SWT.ICON_QUESTION);
                            box.setText("新しいバージョン(本家)");
                            box.setMessage(MessageFormat.format(MESSAGE, newversionorg));

                            // OKを押されたらホームページへ移動する
                            if (box.open() == SWT.YES) {
                                try {
                                    Desktop.getDesktop().browse(AppConstants.HOME_PAGE_URI);
                                } catch (Exception e) {
                                    LoggerHolder.LOG.warn("ウェブサイトに移動が失敗しました", e);
                                }
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            LoggerHolder.LOG.info(e.toString() + "が原因でアップデートチェックに失敗しました");
        }
    }
}
