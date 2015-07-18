package logbook.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * 矩形選択ウインドウ
 *
 */
public final class FullScreenDialog extends Dialog {

    private final Image image;
    private final Monitor monitor;
    private Shell shell;
    private int startx;
    private int starty;
    private int endx;
    private int endy;
    private Font font;
    private Rectangle ret;

    /**
     * Create the dialog.
     *
     * @param parent
     * @param image 矩形選択の背景画像
     * @param monitor モニター
     */
    public FullScreenDialog(Shell parent, Image image, Monitor monitor) {
        super(parent, SWT.NO_TRIM);
        this.setText("矩形選択");
        this.image = image;
        this.monitor = monitor;
    }

    /**
     * Open the dialog.
     *
     * @return the result
     */
    public Rectangle open() {
        this.createContents();
        this.shell.open();
        this.shell.layout();
        Display display = this.getParent().getDisplay();
        while (!this.shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        if (this.ret != null) {
            return getAbsoluteRectangle(this.monitor, this.ret);
        }
        return null;
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        // フルスクリーンのウインドウを作成します
        this.shell = new Shell(this.getParent(), this.getStyle());
        this.shell.setText(this.getText());
        this.shell.setBounds(this.monitor.getBounds());
        this.shell.setFullScreen(true);
        // 描画に使用するフォントを設定します
        FontData normal = this.shell.getFont().getFontData()[0];
        this.font = SWTResourceManager.getFont(normal.getName(), 12, SWT.NORMAL);

        GridLayout glShell = new GridLayout(1, false);
        glShell.verticalSpacing = 0;
        glShell.marginWidth = 0;
        glShell.horizontalSpacing = 0;
        glShell.marginHeight = 0;
        this.shell.setLayout(glShell);

        Canvas canvas = new Canvas(this.shell, SWT.NONE);
        canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        canvas.addPaintListener(e -> {
            GC gc = e.gc;
            gc.drawImage(this.image, 0, 0);
            gc.setFont(this.font);
            gc.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
            gc.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
            gc.drawString("キャプチャする領域をマウスでドラッグして下さい。 [Esc]キーでキャンセル", 2, 2);
            gc.dispose();
        });
        canvas.addMouseMoveListener(e -> {
            if ((e.stateMask & SWT.BUTTON1) != 0) {
                this.endx = e.x;
                this.endy = e.y;

                GC gc = new GC(canvas);
                gc.drawImage(this.image, 0, 0);
                gc.setAlpha(192);
                gc.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
                gc.drawRectangle(getRectangle(this.startx, this.starty, this.endx, this.endy));
                gc.setAlpha(128);
                gc.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
                gc.drawRectangle(getRectangle(this.startx - 1, this.starty - 1, this.endx + 1, this.endy + 1));
                gc.dispose();
            }
        });
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent e) {
                FullScreenDialog.this.endx = e.x;
                FullScreenDialog.this.endy = e.y;

                Rectangle rectangle = getRectangle(FullScreenDialog.this.startx, FullScreenDialog.this.starty,
                        FullScreenDialog.this.endx, FullScreenDialog.this.endy);
                // 範囲が十分ある場合
                if ((rectangle.width > 2) && (rectangle.height > 2)) {
                    MessageBox msg = new MessageBox(FullScreenDialog.this.shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
                    msg.setText("矩形選択");
                    msg.setMessage("この範囲でよろしいですか？");
                    if (msg.open() == SWT.YES) {
                        FullScreenDialog.this.ret = getRectangle(FullScreenDialog.this.startx,
                                FullScreenDialog.this.starty, FullScreenDialog.this.endx, FullScreenDialog.this.endy);
                        FullScreenDialog.this.shell.close();
                        return;
                    }
                }
                canvas.redraw();
            }

            @Override
            public void mouseDown(MouseEvent e) {
                FullScreenDialog.this.startx = FullScreenDialog.this.endx = e.x;
                FullScreenDialog.this.starty = FullScreenDialog.this.endy = e.y;
            }
        });
        canvas.setCursor(SWTResourceManager.getCursor(SWT.CURSOR_CROSS));
        this.shell.setActive();
    }

    private static Rectangle getRectangle(int x1, int y1, int x2, int y2) {
        return new Rectangle(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
    }

    private static Rectangle getAbsoluteRectangle(Monitor monitor, Rectangle r) {
        Rectangle m = monitor.getBounds();
        return new Rectangle(r.x + m.x, r.y + m.y, r.width, r.height);
    }
}
