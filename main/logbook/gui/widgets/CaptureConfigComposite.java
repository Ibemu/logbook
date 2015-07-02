package logbook.gui.widgets;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import logbook.config.AppConfig;
import logbook.config.bean.AppConfigBean;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 設定画面-キャプチャ
 *
 */
public final class CaptureConfigComposite extends Composite {

    /** 保存先 */
    private final Text captureDir;
    /** フォーマット */
    private final Combo imageformatCombo;
    /** 書式 */
    private final Text imageNameFormat;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public CaptureConfigComposite(Composite parent, int style) {
        super(parent, style);

        this.setLayout(new GridLayout(3, false));

        Label label4 = new Label(this, SWT.NONE);
        label4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        label4.setText("保存先");

        this.captureDir = new Text(this, SWT.BORDER);
        GridData gdCaptureDir = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gdCaptureDir.widthHint = 120;
        this.captureDir.setLayoutData(gdCaptureDir);
        this.captureDir.setText(AppConfig.get().getCapturePath());

        Button savedirBtn = new Button(this, SWT.NONE);
        savedirBtn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dialog = new DirectoryDialog(CaptureConfigComposite.this.getShell());
                dialog.setMessage("保存先を指定して下さい");
                String path = dialog.open();
                if (path != null) {
                    CaptureConfigComposite.this.captureDir.setText(path);
                }
            }
        });
        savedirBtn.setText("選択...");

        Label label5 = new Label(this, SWT.NONE);
        label5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        label5.setText("フォーマット");

        this.imageformatCombo = new Combo(this, SWT.READ_ONLY);
        this.imageformatCombo.setItems(new String[] { "jpg", "png" });
        this.imageformatCombo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
        this.imageformatCombo.select(0);
        for (int i = 0; i < this.imageformatCombo.getItems().length; i++) {
            if (AppConfig.get().getImageFormat().equals(this.imageformatCombo.getItem(i))) {
                this.imageformatCombo.select(i);
                break;
            }
        }
        new Label(this, SWT.NONE);

        Label label6 = new Label(this, SWT.NONE);
        label6.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        label6.setText("書式");

        this.imageNameFormat = new Text(this, SWT.BORDER);
        GridData gdImageNameFormat = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
        gdImageNameFormat.widthHint = 250;
        this.imageNameFormat.setLayoutData(gdImageNameFormat);
        this.imageNameFormat.setText(AppConfig.get().getImageNameFormat());

        Label label11 = new Label(this, SWT.NONE);
        label11.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        label11.setText("プレビュー");

        Text imageNamePrev = new Text(this, SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
        GridData gdImageNamePrev = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        gdImageNamePrev.heightHint = imageNamePrev.getLineHeight() * 3;
        gdImageNamePrev.widthHint = 250;
        imageNamePrev.setLayoutData(gdImageNamePrev);
        imageNamePrev.setText(prevImageNameFormat(AppConfig.get().getImageNameFormat()));

        this.imageNameFormat.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                imageNamePrev
                        .setText(prevImageNameFormat(CaptureConfigComposite.this.imageNameFormat.getText()));
            }
        });

        Label label12 = new Label(this, SWT.NONE);
        label12.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 3, 1));
        label12.setText("英字を書式に含めるには ' (シングルクォーテーション)で囲みます");
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    /**
     * 保存先を取得します。
     * @return 保存先
     */
    public String getCaptureDir() {
        return this.captureDir.getText();
    }

    /**
     * フォーマットを取得します
     * @return フォーマット
     */
    public String getImageformat() {
        return this.imageformatCombo.getItem(this.imageformatCombo.getSelectionIndex());
    }

    /**
     * 書式を取得します。
     * @return 書式
     */
    public String getImageNameFormat() {
        return this.imageNameFormat.getText();
    }

    /**
     * 画面設定を登録します
     * @param config アプリケーション設定
     */
    public void setConfig(AppConfigBean config) {
        config.setCapturePath(this.getCaptureDir());
        config.setImageFormat(this.getImageformat());
        config.setImageNameFormat(this.getImageNameFormat());
    }

    /**
     * 画像ファイル名のプレビュー
     *
     * @param format
     * @return
     */
    private static String prevImageNameFormat(String format) {
        try {
            Calendar calendar = Calendar.getInstance();
            if (format.isEmpty()) {
                throw new IllegalArgumentException();
            }
            String prev = new SimpleDateFormat(format).format(calendar.getTime())
                    + "." + AppConfig.get().getImageFormat();
            try {
                Path path = Paths.get(AppConfig.get().getCapturePath(), prev);
                return path.normalize().toString();
            } catch (InvalidPathException ex) {
                return "ファイル名に無効な文字が含まれています :" + ex.getReason();
            }
        } catch (IllegalArgumentException ex) {
            return "日付書式が無効です :" + ex.getMessage();
        }
    }
}
