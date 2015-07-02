package logbook.gui.widgets;

import logbook.config.AppConfig;
import logbook.config.bean.AppConfigBean;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

/**
 * 設定画面-プロキシ
 *
 */
public final class ProxyConfigComposite extends Composite {

    /** 接続にプロキシを使用する */
    private final Button useProxyButton;
    /** "ホスト */
    private final Text proxyHostText;
    /** ポート */
    private final Spinner proxyPortSpinner;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public ProxyConfigComposite(Composite parent, int style) {
        super(parent, style);

        this.setLayout(new GridLayout(4, false));

        this.useProxyButton = new Button(this, SWT.CHECK);
        this.useProxyButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
        this.useProxyButton.setText("接続にプロキシを使用する*");
        this.useProxyButton.setSelection(AppConfig.get().isUseProxy());

        Label proxyHostLabel = new Label(this, SWT.NONE);
        proxyHostLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        proxyHostLabel.setText("ホスト:");

        this.proxyHostText = new Text(this, SWT.BORDER);
        GridData gdProxyHostText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdProxyHostText.widthHint = 100;
        this.proxyHostText.setLayoutData(gdProxyHostText);
        this.proxyHostText.setText(AppConfig.get().getProxyHost());

        Label proxyPortLabel = new Label(this, SWT.NONE);
        proxyPortLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        proxyPortLabel.setText("ポート:");

        this.proxyPortSpinner = new Spinner(this, SWT.BORDER);
        this.proxyPortSpinner.setMaximum(65535);
        this.proxyPortSpinner.setMinimum(1);
        this.proxyPortSpinner.setSelection(AppConfig.get().getProxyPort());
        GridData gdProxyPortSpinner = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdProxyPortSpinner.widthHint = 55;
        this.proxyPortSpinner.setLayoutData(gdProxyPortSpinner);
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    /**
     * 接続にプロキシを使用するを取得します。
     * @return 接続にプロキシを使用する
     */
    public boolean getUseProxy() {
        return this.useProxyButton.getSelection();
    }

    /**
     * "ホストを取得します。
     * @return "ホスト
     */
    public String getProxyHost() {
        return this.proxyHostText.getText();
    }

    /**
     * ポートを取得します。
     * @return ポート
     */
    public int getProxyPort() {
        return this.proxyPortSpinner.getSelection();
    }

    /**
     * 画面設定を登録します
     * @param config アプリケーション設定
     */
    public void setConfig(AppConfigBean config) {
        config.setUseProxy(this.getUseProxy());
        config.setProxyHost(this.getProxyHost());
        config.setProxyPort(this.getProxyPort());
    }
}
