package logbook.gui.widgets;

import logbook.config.AppConfig;
import logbook.config.bean.AppConfigBean;
import logbook.data.context.GlobalContext;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

/**
 * 設定画面-通知
 *
 */
public final class NotifyConfigComposite extends Composite {

    /** 遠征の通知をリマインドする */
    private final Button remind;
    /** 遠征の通知間隔(秒) */
    private final Spinner intervalSpinner;
    /** 遠征・入渠をバルーンで通知する */
    private final Button balloon;
    /** 母港の空きをタスクバーで通知する */
    private final Button taskbar;
    /** 母港の空きがn以下で警告表示 */
    private final Spinner fullySpinner;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public NotifyConfigComposite(Composite parent, int style) {
        super(parent, style);

        this.setLayout(new GridLayout(3, false));

        this.remind = new Button(this, SWT.CHECK);
        this.remind.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
        this.remind.setText("遠征の通知をリマインドする");
        this.remind.setSelection(AppConfig.get().isMissionRemind());

        Label intervallabel = new Label(this, SWT.NONE);
        intervallabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        intervallabel.setText("間隔(秒)");

        this.intervalSpinner = new Spinner(this, SWT.BORDER);
        this.intervalSpinner.setMaximum(60 * 60);
        this.intervalSpinner.setMinimum(10);
        this.intervalSpinner.setSelection(AppConfig.get().getRemindInterbal());
        GridData gdIntervalSpinner = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdIntervalSpinner.widthHint = 55;
        this.intervalSpinner.setLayoutData(gdIntervalSpinner);

        new Label(this, SWT.NONE);

        this.balloon = new Button(this, SWT.CHECK);
        this.balloon.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
        this.balloon.setText("遠征・入渠をバルーンで通知する");
        this.balloon.setSelection(AppConfig.get().isUseBalloon());

        this.taskbar = new Button(this, SWT.CHECK);
        this.taskbar.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
        this.taskbar.setText("母港の空きをタスクバーで通知する");
        this.taskbar.setSelection(AppConfig.get().isUseTaskbarNotify());

        Label fullyLabel1 = new Label(this, SWT.NONE);
        fullyLabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        fullyLabel1.setText("母港の空きが");

        this.fullySpinner = new Spinner(this, SWT.BORDER);
        this.fullySpinner.setMaximum(Math.max(100, GlobalContext.maxChara()));
        this.fullySpinner.setMinimum(0);
        this.fullySpinner.setSelection(AppConfig.get().getNotifyFully());
        GridData gdFullySpinner = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdFullySpinner.widthHint = 55;
        this.fullySpinner.setLayoutData(gdFullySpinner);

        Label fullyLabel2 = new Label(this, SWT.NONE);
        fullyLabel2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        fullyLabel2.setText("以下で警告表示");
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    /**
     * 遠征の通知をリマインドするを取得します。
     * @return 遠征の通知をリマインドする
     */
    public boolean getRemind() {
        return this.remind.getSelection();
    }

    /**
     * 遠征の通知間隔(秒)を取得します。
     * @return 遠征の通知間隔(秒)
     */
    public int getInterval() {
        return this.intervalSpinner.getSelection();
    }

    /**
     * 遠征・入渠をバルーンで通知するを取得します。
     * @return 遠征・入渠をバルーンで通知する
     */
    public boolean getBalloon() {
        return this.balloon.getSelection();
    }

    /**
     * 母港の空きをタスクバーで通知するを取得します。
     * @return 母港の空きをタスクバーで通知する
     */
    public boolean getTaskbar() {
        return this.taskbar.getSelection();
    }

    /**
     * 母港の空きがn以下で警告表示を取得します。
     * @return 母港の空きがn以下で警告表示
     */
    public int getFully() {
        return this.fullySpinner.getSelection();
    }

    /**
     * 画面設定を登録します
     * @param config アプリケーション設定
     */
    public void setConfig(AppConfigBean config) {
        config.setMissionRemind(this.getRemind());
        config.setRemindInterbal(this.getInterval());
        config.setUseBalloon(this.getBalloon());
        config.setUseTaskbarNotify(this.getTaskbar());
        config.setNotifyFully(this.getFully());
    }
}
