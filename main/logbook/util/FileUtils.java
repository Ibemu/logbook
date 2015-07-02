package logbook.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import logbook.config.AppConfig;
import logbook.constants.AppConstants;

import org.apache.commons.lang3.StringUtils;

/**
 * ファイルに関する処理のutilです
 *
 */
public class FileUtils {

    /**
     * 書き込み先のファイルを返します
     *
     * @param name ファイル名
     * @param altername 代替ファイル名
     * @return File
     * @throws IOException
     */
    public static Path getStoreFile(String name, String altername) throws IOException {
        // 報告書の保存先にファイルを保存します
        Path report = Paths.get(AppConfig.get().getReportPath(), name);
        Path parent = report.getParent();
        if (parent != null) {
            try {
                if (!Files.exists(parent)) {
                    Files.createDirectories(parent);
                }
            } catch (IOException e) {
                // 報告書の保存先ディレクトリの作成に失敗した場合はカレントフォルダにファイルを保存
                report = Paths.get(name);
            }
        }
        if (isLocked(report)) {
            // ロックされている場合は代替ファイルに書き込みます
            report = report.resolveSibling(altername);
        }
        return report;
    }

    /**
     * ファイルがロックされているかを確認します
     *
     * @param file ファイル
     * @return
     * @throws IOException
     */
    public static boolean isLocked(Path path) {
        if (!Files.isRegularFile(path)) {
            return false;
        }
        try (FileChannel fc = FileChannel.open(path,
                StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            FileLock lock = fc.tryLock();
            if (lock != null) {
                lock.release();
                return false;
            } else {
                return true;
            }
        } catch (IOException e) {
            return true;
        }
    }

    /**
     * 報告書をCSVファイルに書き込む(最初の列を取り除く)
     *
     * @param file ファイル
     * @param header ヘッダー
     * @param body 内容
     * @param applend 追記フラグ
     * @throws IOException
     */
    public static void writeCsvStripFirstColumn(Path path, String[] header, List<String[]> body, boolean applend)
            throws IOException {
        // 報告書の項番を除く
        String[] copyheader = Arrays.copyOfRange(header, 1, header.length);
        List<String[]> copybody = new ArrayList<String[]>();
        for (String[] strings : body) {
            copybody.add(Arrays.copyOfRange(strings, 1, strings.length));
        }
        FileUtils.writeCsv(path, copyheader, copybody, applend);
    }

    /**
     * 報告書をCSVファイルに書き込む
     *
     * @param file ファイル
     * @param header ヘッダー
     * @param body 内容
     * @param applend 追記フラグ
     * @throws IOException
     */
    public static void writeCsv(Path path, String[] header, List<String[]> body, boolean applend)
            throws IOException {
        OpenOption[] options;
        if (applend) {
            options = new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.APPEND };
        } else {
            options = new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING };
        }
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(path, options))) {
            if (!Files.exists(path) || (Files.size(path) <= 0)) {
                out.write(StringUtils.join(header, ',').getBytes(AppConstants.CHARSET));
                out.write("\r\n".getBytes(AppConstants.CHARSET));
            }
            for (String[] colums : body) {
                out.write(StringUtils.join(colums, ',').getBytes(AppConstants.CHARSET));
                out.write("\r\n".getBytes(AppConstants.CHARSET));
            }
        }
    }
}
