package logbook.data;

import java.nio.file.Path;

/**
 * {@link ScriptLoader#getEventListener(Path)}によって実装されるEventListener
 *
 */
public final class ScriptEventAdapter implements EventListener {

    private final EventListener listener;
    private final Path path;

    /**
     * コンストラクター
     */
    public ScriptEventAdapter(EventListener listener, Path path) {
        this.listener = listener;
        this.path = path;
    }

    @Override
    public void update(DataType type, Data data) {
        this.listener.update(type, data);
    }

    /**
     * スクリプトのPathを返します
     *
     * @return スクリプトのPath
     */
    public Path getPath() {
        return this.path;
    }
}
