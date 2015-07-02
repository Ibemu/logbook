package logbook.gui.widgets;

import logbook.config.AppConfig;
import logbook.config.bean.AppConfigBean;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 設定画面-ユーザースクリプト
 *
 */
public final class DevelopmentConfigComposite extends Composite {

    /** JSONを保存する */
    private final Button storeJson;
    /** JSON保存先 */
    private final Text storeJsonPath;
    /** 起動時に本家の更新チェック */
    private final Button checkUpdateOriginal;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public DevelopmentConfigComposite(Composite parent, int style) {
        super(parent, style);

        this.setLayout(new GridLayout(2, false));

        new Label(this, SWT.NONE);
        this.storeJson = new Button(this, SWT.CHECK);
        this.storeJson.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        this.storeJson.setText("JSONを保存する");
        this.storeJson.setSelection(AppConfig.get().isStoreJson());

        Label lblJson = new Label(this, SWT.NONE);
        lblJson.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblJson.setText("JSON保存先");

        this.storeJsonPath = new Text(this, SWT.BORDER);
        GridData gdStoreJsonPath = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdStoreJsonPath.widthHint = 120;
        this.storeJsonPath.setLayoutData(gdStoreJsonPath);
        this.storeJsonPath.setText(AppConfig.get().getStoreJsonPath());

        new Label(this, SWT.NONE);
        this.checkUpdateOriginal = new Button(this, SWT.CHECK);
        this.checkUpdateOriginal.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        this.checkUpdateOriginal.setText("起動時に本家の更新チェック*");
        this.checkUpdateOriginal.setSelection(AppConfig.get().isCheckUpdateOriginal());
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    /**
     * JSONを保存するを取得します。
     * @return JSONを保存する
     */
    public boolean getStoreJson() {
        return this.storeJson.getSelection();
    }

    /**
     * JSON保存先を取得します。
     * @return JSON保存先
     */
    public String getStoreJsonPath() {
        return this.storeJsonPath.getText();
    }

    /**
     * 起動時に本家の更新チェックを取得します。
     * @return 起動時に本家の更新チェック
     */
    public boolean getCheckUpdateOriginal() {
        return this.checkUpdateOriginal.getSelection();
    }

    /**
     * 画面設定を登録します
     * @param config アプリケーション設定
     */
    public void setConfig(AppConfigBean config) {
        config.setStoreJson(this.getStoreJson());
        config.setStoreJsonPath(this.getStoreJsonPath());
        config.setCheckUpdateOriginal(this.getCheckUpdateOriginal());
    }
}
