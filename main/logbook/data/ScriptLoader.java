package logbook.data;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.CheckForNull;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import logbook.config.AppConfig;

import org.apache.commons.io.FilenameUtils;

/**
 * スクリプトを読み込みEventListenerの実装を取得する
 *
 */
public final class ScriptLoader implements Closeable {

    /** ClassLoader */
    private final URLClassLoader classLoader;

    /** ScriptEngineManager */
    private final ScriptEngineManager manager;

    /**
     * コンストラクター
     */
    public ScriptLoader() {
        this.classLoader = URLClassLoader.newInstance(this.getLibraries());
        this.manager = new ScriptEngineManager(this.classLoader);
    }

    /**
     * スクリプトを読み込みEventListenerの実装を取得する<br>
     *
     * @param script スクリプト
     * @return スクリプトにより実装されたEventListener、スクリプトエンジンが見つからない、もしくはコンパイル済み関数がEventListenerを実装しない場合null
     * @throws IOException
     * @throws ScriptException
     */
    @CheckForNull
    public EventListener getEventListener(Path script) throws IOException, ScriptException {
        try (BufferedReader reader = Files.newBufferedReader(script, StandardCharsets.UTF_8)) {
            // 拡張子からScriptEngineを取得
            String ext = FilenameUtils.getExtension(script.toString());
            ScriptEngine engine = this.manager.getEngineByExtension(ext);
            if (engine != null) {
                // eval
                engine.eval(reader);
                // 実装を取得
                EventListener listener = ((Invocable) engine).getInterface(EventListener.class);

                if (listener != null) {
                    return new ScriptEventAdapter(listener, script);
                }
            }
            return null;
        }
    }

    /**
     * ScriptEngineManagerで使用する追加のライブラリ
     *
     * @return ライブラリ
     */
    public URL[] getLibraries() {
        String[] engines = AppConfig.get().getScriptEngines();
        List<URL> libs = new ArrayList<>();
        for (String engine : engines) {
            Path path = Paths.get(engine);
            if (Files.isReadable(path)) {
                try {
                    libs.add(path.toUri().toURL());
                } catch (MalformedURLException e) {
                    // ここに入るパターンはないはず
                    e.printStackTrace();
                }
            }
        }
        return libs.toArray(new URL[libs.size()]);
    }

    @Override
    public void close() throws IOException {
        this.classLoader.close();
    }
}
