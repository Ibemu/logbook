package logbook.gui;

import java.awt.Desktop;

import logbook.constants.AppConstants;
import logbook.server.proxy.Filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

/**
 * バージョン情報
 *
 */
public final class VersionDialog extends Dialog {
    private Shell shell;

    /**
     * Create the dialog.
     * @param parent
     */
    public VersionDialog(Shell parent) {
        super(parent, SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.RESIZE);
        this.setText("バージョン情報");
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
        this.shell.setText(this.getText());
        this.shell.setLayout(new GridLayout(1, false));

        // バージョン
        Group versionGroup = new Group(this.shell, SWT.NONE);
        versionGroup.setText("バージョン");
        versionGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        versionGroup.setLayout(new GridLayout(2, true));

        label(AppConstants.NAME, versionGroup);
        label(AppConstants.VERSION.toString(), versionGroup);

        label("", versionGroup);
        Link gowebsite = new Link(versionGroup, SWT.NONE);
        gowebsite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL, SWT.CENTER, false, false, 1, 1));
        gowebsite.setText("<a>ウェブサイト</a>");
        gowebsite.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                try {
                    Desktop.getDesktop().browse(AppConstants.HOME_PAGE_URI);
                } catch (Exception e) {
                }
            }
        });

        label(AppConstants.NAME_PLUS, versionGroup);
        label(AppConstants.VERSION_PLUS.toStringFull(), versionGroup);

        label("", versionGroup);
        gowebsite = new Link(versionGroup, SWT.NONE);
        gowebsite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL, SWT.CENTER, false, false, 1, 1));
        gowebsite.setText("<a>ウェブサイト</a>");
        gowebsite.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                try {
                    Desktop.getDesktop().browse(AppConstants.HOME_PAGE_URI_PLUS);
                } catch (Exception e) {
                }
            }
        });

        // 設定
        Group appGroup = new Group(this.shell, SWT.NONE);
        appGroup.setText("設定");
        appGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        appGroup.setLayout(new GridLayout(2, true));

        label("鎮守府サーバー", appGroup);
        label(StringUtils.defaultString(Filter.getServerName(), "未設定"), appGroup);

        // 設定
        Group javaGroup = new Group(this.shell, SWT.NONE);
        javaGroup.setText("環境");
        javaGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        javaGroup.setLayout(new GridLayout(2, true));

        double totalMemory = ((double) Runtime.getRuntime().totalMemory()) / 1024 / 1024;
        double freeMemory = ((double) Runtime.getRuntime().freeMemory()) / 1024 / 1024;

        label("利用可能メモリサイズ", javaGroup);
        label(Long.toString(Math.round(totalMemory)) + " MB", javaGroup);

        label("利用中メモリサイズ", javaGroup);
        label(Long.toString(Math.round(totalMemory - freeMemory)) + " MB", javaGroup);

        label("os.name", javaGroup);
        label(SystemUtils.OS_NAME, javaGroup);

        label("os.version", javaGroup);
        label(SystemUtils.OS_VERSION, javaGroup);

        label("java.vendor", javaGroup);
        label(SystemUtils.JAVA_VENDOR, javaGroup);

        label("java.version", javaGroup);
        label(SystemUtils.JAVA_VERSION, javaGroup);

        this.shell.pack();
    }

    private static Label label(String text, Composite composite) {
        Label label = new Label(composite, SWT.NONE);
        label.setText(text);
        label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        return label;
    }
}
