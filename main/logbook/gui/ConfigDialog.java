package logbook.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import logbook.config.AppConfig;
import logbook.config.ChartStylesheet;
import logbook.gui.logic.LayoutLogic;
import logbook.gui.widgets.CaptureConfigComposite;
import logbook.gui.widgets.ChartConfigComposite;
import logbook.gui.widgets.DevelopmentConfigComposite;
import logbook.gui.widgets.FleettabConfigComposite;
import logbook.gui.widgets.NotifyConfigComposite;
import logbook.gui.widgets.ProxyConfigComposite;
import logbook.gui.widgets.SystemConfigComposite;
import logbook.gui.widgets.UserscriptConfigComposite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 設定画面
 *
 */
public final class ConfigDialog extends Dialog {

    private final Map<String, Composite> compositeMap = new HashMap<String, Composite>();
    private Shell shell;
    private Composite composite;
    private ScrolledComposite scrolledComposite;

    /**
     * Create the dialog.
     * @param parent
     */
    public ConfigDialog(Shell parent) {
        super(parent, SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.RESIZE);
        this.setText("設定");
    }

    /**
     * Open the dialog.
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
        this.shell.setSize(550, 400);
        this.shell.setText(this.getText());
        this.shell.setLayout(new GridLayout(1, false));

        SashForm sashForm = new SashForm(this.shell, SWT.SMOOTH);
        sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        // ツリーメニュー
        Tree tree = new Tree(sashForm, SWT.BORDER);
        tree.addSelectionListener(new TreeSelectionAdapter(this));
        TreeItem systemroot = new TreeItem(tree, SWT.NONE);
        systemroot.setText("一般");
        systemroot.setData("system");
        TreeItem fleettab = new TreeItem(systemroot, SWT.NONE);
        fleettab.setText("艦隊タブ");
        fleettab.setData("fleettab");
        TreeItem notify = new TreeItem(systemroot, SWT.NONE);
        notify.setText("通知");
        notify.setData("notify");
        TreeItem capture = new TreeItem(systemroot, SWT.NONE);
        capture.setText("キャプチャ");
        capture.setData("capture");
        TreeItem chart = new TreeItem(systemroot, SWT.NONE);
        chart.setText("資材チャート");
        chart.setData("chart");
        TreeItem proxy = new TreeItem(systemroot, SWT.NONE);
        proxy.setText("プロキシ");
        proxy.setData("proxy");
        TreeItem extensionroot = new TreeItem(tree, SWT.NONE);
        extensionroot.setText("拡張");
        extensionroot.setData("extension");
        TreeItem userscript = new TreeItem(extensionroot, SWT.NONE);
        userscript.setText("ユーザースクリプト");
        userscript.setData("userscript");
        TreeItem development = new TreeItem(tree, SWT.NONE);
        development.setText("Development");
        development.setData("development");

        systemroot.setExpanded(true);
        extensionroot.setExpanded(true);

        this.scrolledComposite = new ScrolledComposite(sashForm, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        this.scrolledComposite.setExpandHorizontal(true);
        this.scrolledComposite.setExpandVertical(true);

        this.composite = new Composite(this.scrolledComposite, SWT.NONE);
        this.composite.setLayout(new GridLayout(1, false));

        // システム タブ
        SystemConfigComposite compositeSystem = new SystemConfigComposite(this.composite, SWT.NONE);
        this.compositeMap.put("system", compositeSystem);

        // 艦隊タブ タブ
        FleettabConfigComposite compositeFleetTab = new FleettabConfigComposite(this.composite, SWT.NONE);
        this.compositeMap.put("fleettab", compositeFleetTab);

        // 通知
        NotifyConfigComposite compositeNotify = new NotifyConfigComposite(this.composite, SWT.NONE);
        this.compositeMap.put("notify", compositeNotify);

        // キャプチャ タブ
        CaptureConfigComposite compositeCapture = new CaptureConfigComposite(this.composite, SWT.NONE);
        this.compositeMap.put("capture", compositeCapture);

        // 資材チャート タブ
        ChartConfigComposite compositeChart = new ChartConfigComposite(this.composite, SWT.NONE);
        this.compositeMap.put("chart", compositeChart);

        // プロキシ
        ProxyConfigComposite compositeProxy = new ProxyConfigComposite(this.composite, SWT.NONE);
        this.compositeMap.put("proxy", compositeProxy);

        // 拡張
        Composite compositeExtension = new Composite(this.composite, SWT.NONE);
        this.compositeMap.put("extension", compositeExtension);
        compositeExtension.setLayout(new GridLayout(1, false));

        // ユーザースクリプト
        UserscriptConfigComposite userScriptComposite = new UserscriptConfigComposite(this.composite, SWT.NONE);
        this.compositeMap.put("userscript", userScriptComposite);

        // Development タブ
        DevelopmentConfigComposite compositeDevelopment = new DevelopmentConfigComposite(this.composite, SWT.NONE);
        this.compositeMap.put("development", compositeDevelopment);

        // コマンドグループ
        Composite commandComposite = new Composite(this.shell, SWT.NONE);
        commandComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        GridLayout glCommandComposite = new GridLayout(2, false);
        glCommandComposite.verticalSpacing = 0;
        glCommandComposite.marginWidth = 0;
        glCommandComposite.marginHeight = 0;
        glCommandComposite.horizontalSpacing = 0;
        commandComposite.setLayout(glCommandComposite);

        // '*'の説明
        Composite commandLeft = new Composite(commandComposite, SWT.NONE);
        GridLayout glCommandLeft = new GridLayout(1, false);
        glCommandLeft.horizontalSpacing = 0;
        glCommandLeft.marginHeight = 0;
        glCommandLeft.verticalSpacing = 0;
        glCommandLeft.marginWidth = 0;
        commandLeft.setLayout(glCommandLeft);

        Label attentionLabel = new Label(commandLeft, SWT.NONE);
        attentionLabel.setText("*再起動後に有効になります");

        Composite commandRight = new Composite(commandComposite, SWT.NONE);
        RowLayout rlCommandRight = new RowLayout(SWT.HORIZONTAL);
        rlCommandRight.marginTop = 0;
        rlCommandRight.marginLeft = 0;
        rlCommandRight.marginRight = 0;
        rlCommandRight.marginBottom = 0;
        commandRight.setLayout(rlCommandRight);
        commandRight.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));

        // OK ボタン
        Button applyBtn = new Button(commandRight, SWT.NONE);
        applyBtn.setLayoutData(new RowData(100, SWT.DEFAULT));
        applyBtn.setText("OK");
        applyBtn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // 設定の保存アクション

                // system
                compositeSystem.setConfig(AppConfig.get());
                // fleettab
                compositeFleetTab.setConfig(AppConfig.get());
                // notify
                compositeNotify.setConfig(AppConfig.get());
                // capture
                compositeCapture.setConfig(AppConfig.get());
                // チャート
                compositeChart.setConfig(AppConfig.get());
                // proxy
                compositeProxy.setConfig(AppConfig.get());
                // ユーザースクリプト
                userScriptComposite.setConfig(AppConfig.get());
                // development
                // ユーザースクリプト
                compositeDevelopment.setConfig(AppConfig.get());
                try {
                    AppConfig.store();
                    ChartStylesheet.store();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                ConfigDialog.this.shell.close();
            }
        });

        Button cancelBtn = new Button(commandRight, SWT.NONE);
        cancelBtn.setLayoutData(new RowData(100, SWT.DEFAULT));
        cancelBtn.setText("キャンセル");
        cancelBtn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                ConfigDialog.this.shell.close();
            }
        });

        for (Entry<String, Composite> entry : this.compositeMap.entrySet()) {
            LayoutLogic.hide(entry.getValue(), true);
        }

        sashForm.setWeights(new int[] { 2, 5 });
        this.scrolledComposite.setContent(this.composite);
        this.scrolledComposite.setMinSize(this.composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    /**
     * ツリーをクリックした時に呼び出されるアダプター
     *
     */
    private static final class TreeSelectionAdapter extends SelectionAdapter {

        /** ダイアログ */
        private final ConfigDialog dialog;

        /**
         * コンストラクター
         */
        public TreeSelectionAdapter(ConfigDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            Object data = e.item.getData();
            if (data instanceof String) {
                String treeKey = (String) data;
                for (Entry<String, Composite> entry : this.dialog.compositeMap.entrySet()) {
                    if (entry.getKey().equals(treeKey)) {
                        LayoutLogic.hide(entry.getValue(), false);
                    } else {
                        LayoutLogic.hide(entry.getValue(), true);
                    }
                }
                this.dialog.composite.layout();
                this.dialog.scrolledComposite.setMinSize(this.dialog.composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
            }
        }
    }
}
