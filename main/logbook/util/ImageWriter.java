package logbook.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * SWTのControl,Imageを画像ファイルとして書き込みます
 *
 */
public class ImageWriter {
    private final Path path;
    private final OpenOption[] options;
    private int format = SWT.IMAGE_PNG;
    private int compression = -1;

    /**
     * 指定されたpathとoptionでImageWriterを構築します。<br>
     * デフォルトの画像フォーマットはSWT.IMAGE_PNGです。
     *
     * @param path
     *            開くまたは作成するファイルへのパス
     * @param options
     *            ファイルを開く方法を指定するオプション
     */
    public ImageWriter(Path path, OpenOption... options) {
        this.path = path;
        this.options = options;
    }

    /**
     * 画像フォーマットを指定します。
     * <dl>
     * <dt><code>SWT.IMAGE_BMP</code></dt>
     * <dd>Windows BMP file format, no compression</dd>
     * <dt><code>SWT.IMAGE_BMP_RLE</code></dt>
     * <dd>Windows BMP file format, RLE compression if appropriate</dd>
     * <dt><code>SWT.IMAGE_GIF</code></dt>
     * <dd>GIF file format</dd>
     * <dt><code>SWT.IMAGE_ICO</code></dt>
     * <dd>Windows ICO file format</dd>
     * <dt><code>SWT.IMAGE_JPEG</code></dt>
     * <dd>JPEG file format</dd>
     * <dt><code>SWT.IMAGE_PNG</code></dt>
     * <dd>PNG file format</dd>
     * </dl>
     *
     * @param format
     *            画像フォーマット
     * @return ImageWriter
     */
    public ImageWriter format(int format) {
        this.format = format;
        return this;
    }

    /**
     * This is the compression used when saving jpeg and png files.<br>
     * <br>
     * When saving <b>jpeg</b> files, the value is from 1 to 100, where 1 is
     * very high compression but low quality, and 100 is no compression and high
     * quality; default is 75.<br>
     * When saving <b>png</b> files, the value is from 0 to 3, but they do not
     * impact the quality because PNG is lossless compression. 0 is
     * uncompressed, 1 is low compression and fast, 2 is default compression,
     * and 3 is high compression but slow.<br>
     *
     * @param compression
     *            compression value
     * @return ImageWriter
     */
    public ImageWriter compression(int compression) {
        this.compression = compression;
        return this;
    }

    /**
     * 指定されたcontrolを画像イメージとして書き込みます。
     *
     * @param control
     *            画像イメージとして書き込むControl
     * @throws IOException
     */
    public void write(Control control) throws IOException {
        Point size = control.getSize();
        GC gc = new GC(control);
        try {
            Image image = new Image(Display.getDefault(), size.x, size.y);
            try {
                gc.copyArea(image, 0, 0);
                this.write(image);
            } finally {
                image.dispose();
            }
        } finally {
            gc.dispose();
        }
    }

    /**
     * 指定されたimageを書き込みます。
     *
     * @param image
     *            書き込むImage
     * @throws IOException
     */
    public void write(Image image) throws IOException {
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(this.path, this.options))) {
            ImageLoader il = new ImageLoader();
            il.data = new ImageData[] { image.getImageData() };
            il.compression = this.compression;
            il.save(out, this.format);
        }
    }
}
