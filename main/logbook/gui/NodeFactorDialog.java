package logbook.gui;

import org.apache.commons.lang3.math.NumberUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import logbook.gui.listener.SelectedListener;

/**
 * 分岐点係数ダイアログ
 *
 */
public final class NodeFactorDialog extends Dialog {

    private Shell shell;
    private double factor;
    private Text factorText;

    /**
     * Create the dialog.
     *
     * @param parent
     */
    public NodeFactorDialog(Shell parent, double value) {
        super(parent, SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.RESIZE);
        this.setText("分岐点係数の設定");
        this.factor = value;
    }

    /**
     * Open the dialog.
     */
    public double open() {
        this.createContents();
        this.shell.open();
        this.shell.layout();
        Display display = this.getParent().getDisplay();
        while (!this.shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return this.factor;
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

        Label factorlabel = new Label(composite, SWT.NONE);
        factorlabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        factorlabel.setText("係数 :");

        this.factorText = new Text(composite, SWT.BORDER);
        this.factorText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        this.factorText.setText(Double.toString(this.factor));
        this.factorText.addListener(SWT.Traverse, e -> {
            if ((e.detail == SWT.TRAVERSE_RETURN) && NumberUtils.isNumber(this.factorText.getText())) {
                this.factor = Double.parseDouble(this.factorText.getText());
                this.shell.close();
            }
        });
        this.factorText.selectAll();

        Composite compositeButton = new Composite(this.shell, SWT.NONE);
        compositeButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
        compositeButton.setLayout(new GridLayout(2, false));

        Button okButton = new Button(compositeButton, SWT.NONE);
        okButton.setText("OK(&O)");
        okButton.addSelectionListener((SelectedListener) e -> {
            if (NumberUtils.isNumber(this.factorText.getText()))
                this.factor = Double.parseDouble(this.factorText.getText());

            this.shell.close();
        });
        Button chancelButton = new Button(compositeButton, SWT.NONE);
        chancelButton.setText("キャンセル(&C)");
        chancelButton.addSelectionListener((SelectedListener) e -> this.shell.close());

        this.shell.pack();
    }
}
