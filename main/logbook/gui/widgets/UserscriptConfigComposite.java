package logbook.gui.widgets;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import logbook.config.AppConfig;
import logbook.config.bean.AppConfigBean;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

/**
 * 設定画面-ユーザースクリプト
 *
 */
public final class UserscriptConfigComposite extends Composite {

    /** ユーザースクリプトを使用する */
    private final Button useScript;
    /** ユーザースクリプト */
    private final List scriptList;
    /** スクリプトエンジン */
    private final List engineList;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public UserscriptConfigComposite(Composite parent, int style) {
        super(parent, style);

        this.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        this.setLayout(new GridLayout(1, false));

        this.useScript = new Button(this, SWT.CHECK);
        this.useScript.setText("ユーザースクリプトを使用する");
        this.useScript.setSelection(AppConfig.get().isUseUserScript());

        Group scriptgroup = new Group(this, SWT.NONE);
        scriptgroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        scriptgroup.setText("ユーザースクリプト*");
        scriptgroup.setLayout(new GridLayout(2, false));

        this.scriptList = new List(scriptgroup, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
        GridData gdScriptList = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        gdScriptList.heightHint = 60;
        this.scriptList.setLayoutData(gdScriptList);
        if (AppConfig.get().getUserScripts() != null) {
            this.scriptList.setItems(AppConfig.get().getUserScripts());
        }

        Button addScript = new Button(scriptgroup, SWT.NONE);
        addScript.setText("追加");
        addScript.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(UserscriptConfigComposite.this.getShell(), SWT.OPEN | SWT.MULTI);
                dialog.open();
                String dir = dialog.getFilterPath();
                String[] files = dialog.getFileNames();

                if ((dir != null) && !dir.isEmpty()) {
                    for (String file : files) {
                        Path path = Paths.get(dir, file);
                        if (Files.isReadable(path)) {
                            String script = path.toString();
                            if (Arrays.asList(UserscriptConfigComposite.this.scriptList.getItems())
                                    .indexOf(script) == -1) {
                                UserscriptConfigComposite.this.scriptList.add(script);
                            }
                        }
                    }
                }
            }
        });

        Button removeScript = new Button(scriptgroup, SWT.NONE);
        removeScript.setText("除去");
        removeScript.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                UserscriptConfigComposite.this.scriptList.remove(UserscriptConfigComposite.this.scriptList
                        .getSelectionIndices());
            }
        });

        Label lblNewLabel = new Label(scriptgroup, SWT.NONE);
        lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        lblNewLabel.setText("信頼出来ないユーザースクリプトはコンピュータに損害を与える恐れがあります");

        Group enginegroup = new Group(this, SWT.NONE);
        enginegroup.setLayout(new GridLayout(2, false));
        enginegroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        enginegroup.setText("スクリプトエンジン*");

        this.engineList = new List(enginegroup, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
        GridData gdEngineList = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        gdEngineList.heightHint = 60;
        this.engineList.setLayoutData(gdEngineList);
        if (AppConfig.get().getScriptEngines() != null) {
            this.engineList.setItems(AppConfig.get().getScriptEngines());
        }

        Button addEngine = new Button(enginegroup, SWT.NONE);
        addEngine.setText("追加");
        addEngine.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(UserscriptConfigComposite.this.getShell(), SWT.OPEN | SWT.MULTI);
                dialog.setFilterExtensions(new String[] { "*.jar" });
                dialog.open();
                String dir = dialog.getFilterPath();
                String[] files = dialog.getFileNames();

                if ((dir != null) && !dir.isEmpty()) {
                    for (String file : files) {
                        Path path = Paths.get(dir, file);
                        if (Files.isReadable(path)) {
                            String script = path.toString();
                            if (Arrays.asList(UserscriptConfigComposite.this.engineList.getItems())
                                    .indexOf(script) == -1) {
                                UserscriptConfigComposite.this.engineList.add(script);
                            }
                        }
                    }
                }
            }
        });

        Button removeEngine = new Button(enginegroup, SWT.NONE);
        removeEngine.setText("除去");
        removeEngine.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                UserscriptConfigComposite.this.engineList.remove(UserscriptConfigComposite.this.engineList
                        .getSelectionIndices());
            }
        });
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    /**
     * ユーザースクリプトを使用するを取得します。
     * @return ユーザースクリプトを使用する
     */
    public boolean getUseScript() {
        return this.useScript.getSelection();
    }

    /**
     * ユーザースクリプトを取得します。
     * @return ユーザースクリプト
     */
    public String[] getScriptList() {
        return this.scriptList.getItems();
    }

    /**
     * スクリプトエンジンを取得します。
     * @return スクリプトエンジン
     */
    public String[] getEngineList() {
        return this.engineList.getItems();
    }

    /**
     * 画面設定を登録します
     * @param config アプリケーション設定
     */
    public void setConfig(AppConfigBean config) {
        config.setUseUserScript(this.getUseScript());
        config.setUserScripts(this.getScriptList());
        config.setScriptEngines(this.getEngineList());
    }
}
