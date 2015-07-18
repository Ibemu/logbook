package logbook.util.builder;

import java.util.Map;

import org.apache.commons.lang3.builder.Builder;

/**
 * Mapを生成するBuilderです
 *
 * @param <T> Map
 * @param <K> key
 * @param <V> value
 */
public interface MapBuilder<T extends Map<K, V>, K, V> extends Builder<T> {

    /**
     * 指定された値と指定されたキーをこのマップで関連付けます
     *
     * @param key 指定された値が関連付けられるキー
     * @param value 指定されたキーに関連付けられる値
     * @return MapBuilder
     * @see java.util.Map#put(Object, Object)
     */
    MapBuilder<T, K, V> put(K key, V value);

    /**
     * 指定されたマップのすべてのマッピングをこのマップにコピーします
     *
     * @param map このマップに格納されるマッピング
     * @return MapBuilder
     * @see java.util.Map#putAll(java.util.Map)
     */
    MapBuilder<T, K, V> putAll(Map<K, V> map);
}
