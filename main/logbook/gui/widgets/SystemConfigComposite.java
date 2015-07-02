package logbook.gui.widgets;

import java.util.Map;

import logbook.config.AppConfig;
import logbook.config.bean.AppConfigBean;
import logbook.config.bean.WindowLocationBean;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

/**
 * 設定画面-一般
 *
 */
public final class SystemConfigComposite extends Composite {

    /** ポート番号 */
    private final Text listenport;
    /** 音量 */
    private final Text soundlevel;
    /** 透明度 */
    private final Spinner alpha;
    /** 報告書の保存先 */
    private final Text reportDir;
    /** 資材ログ保存間隔(秒) */
    private final Spinner materialintervalSpinner;
    /** 最小化時にタスクトレイに格納 */
    private final Button hidewindow;
    /** 最前面に表示する */
    private final Button ontop;
    /** 起動時にアップデートチェック */
    private final Button checkUpdate;
    /** 終了時に確認する */
    private final Button checkDoit;
    /** ローカルループバックアドレスからの接続のみ受け入れる */
    private final Button onlyFromLocalhost;
    /** サブウインドウの位置とサイズをリセット */
    private final Button resetWindowLocation;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public SystemConfigComposite(Composite parent, int style) {
        super(parent, style);

        this.setLayout(new GridLayout(3, false));

        Label label = new Label(this, SWT.NONE);
        label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        label.setText("ポート番号*");

        this.listenport = new Text(this, SWT.BORDER);
        GridData gdListenport = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gdListenport.widthHint = 90;
        this.listenport.setLayoutData(gdListenport);
        this.listenport.setText(Integer.toString(AppConfig.get().getListenPort()));
        new Label(this, SWT.NONE);

        Label label3 = new Label(this, SWT.NONE);
        label3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        label3.setText("音量(%)");

        this.soundlevel = new Text(this, SWT.BORDER);
        GridData gdSoundlevel = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gdSoundlevel.widthHint = 90;
        this.soundlevel.setLayoutData(gdSoundlevel);
        this.soundlevel.setText(Integer.toString((int) (AppConfig.get().getSoundLevel() * 100)));
        new Label(this, SWT.NONE);

        Label label7 = new Label(this, SWT.NONE);
        label7.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        label7.setText("透明度*");

        this.alpha = new Spinner(this, SWT.BORDER);
        GridData gdAlpha = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gdAlpha.widthHint = 90;
        this.alpha.setLayoutData(gdAlpha);
        this.alpha.setMaximum(255);
        this.alpha.setMinimum(10);
        this.alpha.setSelection(AppConfig.get().getAlpha());
        new Label(this, SWT.NONE);

        Label label8 = new Label(this, SWT.NONE);
        label8.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        label8.setText("報告書の保存先");

        this.reportDir = new Text(this, SWT.BORDER);
        GridData gdReportDir = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gdReportDir.widthHint = 120;
        this.reportDir.setLayoutData(gdReportDir);
        this.reportDir.setText(AppConfig.get().getReportPath());

        Button reportSavedirBtn = new Button(this, SWT.NONE);
        reportSavedirBtn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dialog = new DirectoryDialog(SystemConfigComposite.this.getShell());
                dialog.setMessage("保存先を指定して下さい");
                String path = dialog.open();
                if (path != null) {
                    SystemConfigComposite.this.reportDir.setText(path);
                }
            }
        });
        reportSavedirBtn.setText("選択...");

        Label materialintervallabel = new Label(this, SWT.NONE);
        materialintervallabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        materialintervallabel.setText("資材ログ保存間隔(秒)");

        this.materialintervalSpinner = new Spinner(this, SWT.BORDER);
        this.materialintervalSpinner.setMaximum(60 * 60 * 24);
        this.materialintervalSpinner.setMinimum(10);
        this.materialintervalSpinner.setSelection(AppConfig.get().getMaterialLogInterval());
        GridData gdMaterialIntervalSpinner = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gdMaterialIntervalSpinner.widthHint = 55;
        this.materialintervalSpinner.setLayoutData(gdMaterialIntervalSpinner);

        new Label(this, SWT.NONE);

        this.hidewindow = new Button(this, SWT.CHECK);
        this.hidewindow.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
        this.hidewindow.setText("最小化時にタスクトレイに格納");
        this.hidewindow.setSelection(AppConfig.get().isHideWindow());

        this.ontop = new Button(this, SWT.CHECK);
        this.ontop.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
        this.ontop.setText("最前面に表示する*");
        this.ontop.setSelection(AppConfig.get().isOnTop());

        this.checkUpdate = new Button(this, SWT.CHECK);
        this.checkUpdate.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
        this.checkUpdate.setText("起動時にアップデートチェック*");
        this.checkUpdate.setSelection(AppConfig.get().isCheckUpdate());

        this.checkDoit = new Button(this, SWT.CHECK);
        this.checkDoit.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
        this.checkDoit.setText("終了時に確認する");
        this.checkDoit.setSelection(AppConfig.get().isCheckDoit());

        this.onlyFromLocalhost = new Button(this, SWT.CHECK);
        this.onlyFromLocalhost.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
        this.onlyFromLocalhost.setText("ローカルループバックアドレスからの接続のみ受け入れる*");
        this.onlyFromLocalhost.setSelection(AppConfig.get().isAllowOnlyFromLocalhost());

        this.resetWindowLocation = new Button(this, SWT.CHECK);
        this.resetWindowLocation.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
        this.resetWindowLocation.setText("サブウインドウの位置とサイズをリセット");
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    /**
     * ポート番号を取得します。
     * @return ポート番号
     */
    public String getListenport() {
        return this.listenport.getText();
    }

    /**
     * 音量を取得します。
     * @return 音量
     */
    public String getSoundlevel() {
        return this.soundlevel.getText();
    }

    /**
     * 透明度を取得します。
     * @return 透明度
     */
    public int getAlpha() {
        return this.alpha.getSelection();
    }

    /**
     * 報告書の保存先を取得します。
     * @return 報告書の保存先
     */
    public String getReportDir() {
        return this.reportDir.getText();
    }

    /**
     * 資材ログ保存間隔(秒)を取得します。
     * @return 資材ログ保存間隔(秒)
     */
    public int getMaterialinterval() {
        return this.materialintervalSpinner.getSelection();
    }

    /**
     * 最小化時にタスクトレイに格納を取得します。
     * @return 最小化時にタスクトレイに格納
     */
    public boolean getHidewindow() {
        return this.hidewindow.getSelection();
    }

    /**
     * 最前面に表示するを取得します。
     * @return 最前面に表示する
     */
    public boolean getOntop() {
        return this.ontop.getSelection();
    }

    /**
     * 起動時にアップデートチェックを取得します。
     * @return 起動時にアップデートチェック
     */
    public boolean getCheckUpdate() {
        return this.checkUpdate.getSelection();
    }

    /**
     * 終了時に確認するを取得します。
     * @return 終了時に確認する
     */
    public boolean getCheckDoit() {
        return this.checkDoit.getSelection();
    }

    /**
     * ローカルループバックアドレスからの接続のみ受け入れるを取得します。
     * @return ローカルループバックアドレスからの接続のみ受け入れる
     */
    public boolean getOnlyFromLocalhost() {
        return this.onlyFromLocalhost.getSelection();
    }

    /**
     * サブウインドウの位置とサイズをリセットを取得します。
     * @return サブウインドウの位置とサイズをリセット
     */
    public boolean getResetWindowLocation() {
        return this.resetWindowLocation.getSelection();
    }

    /**
     * 画面設定を登録します
     * @param config アプリケーション設定
     */
    public void setConfig(AppConfigBean config) {
        if (StringUtils.isNumeric(this.getListenport())) {
            config.setListenPort(Integer.parseInt(this.getListenport()));
        }
        config.setHideWindow(this.getHidewindow());
        config.setOnTop(this.getOntop());
        config.setCheckDoit(this.getCheckDoit());
        if (StringUtils.isNumeric(this.getSoundlevel())) {
            float level = (float) Integer.parseInt(this.getSoundlevel()) / 100;
            config.setSoundLevel(level);
        }
        config.setAlpha(this.getAlpha());
        config.setReportPath(this.getReportDir());
        config.setMaterialLogInterval(this.getMaterialinterval());
        config.setCheckUpdate(this.getCheckUpdate());
        config.setAllowOnlyFromLocalhost(this.getOnlyFromLocalhost());
        if (this.getResetWindowLocation()) {
            Map<String, WindowLocationBean> map = config.getWindowLocationMap();
            synchronized (map) {
                map.clear();
            }
            MessageBox box = new MessageBox(this.getShell(), SWT.ICON_INFORMATION | SWT.OK);
            box.setText("設定");
            box.setMessage("サブウインドウの位置とサイズがリセットされました");
            box.open();
        }
    }
}
