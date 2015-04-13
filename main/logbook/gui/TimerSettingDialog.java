package logbook.gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import logbook.thread.ThreadManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * タイマー設定ダイアログ
 *
 */
public final class TimerSettingDialog extends Dialog {

    private Shell shell;
    private Date time;
    private String message;
    private Text messageText;

    /**
     * Create the dialog.
     *
     * @param parent
     */
    public TimerSettingDialog(Shell parent) {
        super(parent, SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.RESIZE);
        this.setText("タイマー設定の追加");
    }

    /**
     * Open the dialog.
     *
     * @return the result
     */
    public void open() {
        this.createContents();
        this.shell.open();
        this.shell.layout();
        Display display = this.getParent().getDisplay();
        while (!this.shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        this.shell = new Shell(this.getParent(), this.getStyle());
        this.shell.setText(this.getText());
        this.shell.setLayout(new GridLayout(1, false));

        Composite composite = new Composite(this.shell, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        composite.setLayout(new GridLayout(2, false));

        Label label = new Label(composite, SWT.NONE);
        label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        label.setText("指定の時刻にメッセージを表示します、よろしいですか？");

        Label timelabel1 = new Label(composite, SWT.NONE);
        timelabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        timelabel1.setText("時刻 :");

        Label timelabel2 = new Label(composite, SWT.NONE);
        timelabel2.setText((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.time)));

        Label msglabel1 = new Label(composite, SWT.NONE);
        msglabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        msglabel1.setText("メッセージ :");

        this.messageText = new Text(composite, SWT.BORDER);
        this.messageText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        this.messageText.setText(this.message);

        Composite compositeButton = new Composite(this.shell, SWT.NONE);
        compositeButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
        compositeButton.setLayout(new GridLayout(2, false));

        Button okButton = new Button(compositeButton, SWT.NONE);
        okButton.setText("OK(&O)");
        okButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // Task
                Runnable task = new AlertTask(TimerSettingDialog.this.getParent(),
                        TimerSettingDialog.this.messageText.getText());
                // Delay
                long delay = TimerSettingDialog.this.time.getTime() - System.currentTimeMillis();
                ThreadManager.getExecutorService()
                        .schedule(task, delay, TimeUnit.MILLISECONDS);

                MessageBox box = new MessageBox(TimerSettingDialog.this.shell, SWT.YES | SWT.ICON_QUESTION);
                box.setText("成功");
                box.setMessage("タイマー設定を予約しました");
                box.open();

                TimerSettingDialog.this.shell.close();
            }
        });
        Button chancelButton = new Button(compositeButton, SWT.NONE);
        chancelButton.setText("キャンセル(&C)");
        chancelButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TimerSettingDialog.this.shell.close();
            }
        });

        this.shell.pack();
    }

    /**
     * timeを設定します。
     *
     * @param time time
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * messageを設定します。
     *
     * @param message message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * メッセージを表示するタスク
     */
    private static final class AlertTask implements Runnable {

        private final Shell shell;
        private final String message;

        public AlertTask(Shell shell, String message) {
            this.shell = shell;
            this.message = message;
        }

        @Override
        public void run() {
            Display.getDefault().asyncExec(new Runnable() {
                @Override
                public void run() {
                    if (!AlertTask.this.shell.isDisposed()) {
                        MessageBox box = new MessageBox(AlertTask.this.shell, SWT.YES | SWT.ICON_QUESTION);
                        box.setText("指定の時刻になりました");
                        box.setMessage(AlertTask.this.message);
                        box.open();
                    }
                }
            });
        }
    }
}