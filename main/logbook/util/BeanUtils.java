package logbook.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

import javax.annotation.CheckForNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * JavaBeanのutilです
 *
 */
public final class BeanUtils {

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(BeanUtils.class);
    }

    /**
     * JavaBeanオブジェクトをXML形式でファイルに書き込みます
     *
     * @param path ファイル
     * @param obj JavaBean
     * @throws IOException IOException
     */
    public static void writeObject(Path path, Object obj) throws IOException {
        if (Files.exists(path)) {
            if (Files.isDirectory(path)) {
                throw new IOException("File '" + path + "' exists but is a directory");
            }
            if (!Files.isWritable(path)) {
                throw new IOException("File '" + path + "' cannot be written to");
            }
        } else {
            Path parent = path.getParent();
            if (parent != null) {
                if (!Files.exists(parent)) {
                    Files.createDirectories(parent);
                }
            }
        }
        Path backup = path.resolveSibling(path.getFileName() + ".backup");
        if (Files.exists(path) && (Files.size(path) > 0)) {
            // ファイルが存在してかつサイズが0を超える場合、ファイルをバックアップにリネームする
            Files.move(path, backup, StandardCopyOption.REPLACE_EXISTING);
        }
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(Files.newOutputStream(path,
                StandardOpenOption.CREATE)))) {
            encoder.setExceptionListener(e -> {
                Class<?> clazz = null;
                if (obj != null) {
                    clazz = obj.getClass();
                }
                LoggerHolder.LOG.warn("File '" + path + "', Bean Class '" + clazz + "' の書き込み時に例外", e);
            });
            encoder.writeObject(obj);
        }
    }

    /**
     * <p>
     * XML形式で書き込まれたファイルからJavaBeanオブジェクトを復元します<br>
     * 復元時に型の検査を行います
     * </p>
     *
     * @param path ファイル
     * @param clazz 期待する型
     * @return オブジェクト
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @CheckForNull
    public static <T> T readObject(Path path, Class<T> clazz) {
        Path target = path;
        try {
            if (!Files.isReadable(target) || (Files.size(target) <= 0)) {
                // ファイルが読み込めないまたはサイズがゼロの場合バックアップファイルを読み込む
                target = path.resolveSibling(path.getFileName() + ".backup");
                if (!Files.isReadable(target)) {
                    // バックアップファイルも読めない場合nullを返す
                    return null;
                }
            }
        } catch (IOException e) {
            return null;
        }
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(Files.newInputStream(target)))) {
            decoder.setExceptionListener(e -> {
                LoggerHolder.LOG.warn("File '" + path + "', Bean Class '" + clazz + "' の読み込み時に例外", e);
            });
            Object obj = decoder.readObject();
            if (clazz.isInstance(obj)) {
                return (T) obj;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
