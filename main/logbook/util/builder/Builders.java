package logbook.util.builder;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * CollectionやMapを生成するBuilderを作成するstaticメソッドで構成されます
 *
 */
public class Builders {

    /**
     * CollectionBuilderの実装
     *
     * @param <T> Collection
     * @param <V> value
     */
    private static class CollectionBuilderImpl<T extends Collection<V>, V> implements CollectionBuilder<T, V> {
        private final T c;

        public CollectionBuilderImpl(Supplier<T> supplier) {
            this.c = supplier.get();
        }

        @Override
        public CollectionBuilder<T, V> add(V value) {
            this.c.add(value);
            return this;
        }

        @Override
        public CollectionBuilder<T, V> addAll(Collection<V> collection) {
            this.c.addAll(collection);
            return this;
        }

        @Override
        public T build() {
            return this.c;
        }
    }

    /**
     * MapBuilderの実装
     *
     * @param <T> Map
     * @param <K> key
     * @param <V> value
     */
    private static class MapBuilderImpl<T extends Map<K, V>, K, V> implements MapBuilder<T, K, V> {
        private final T map;

        public MapBuilderImpl(Supplier<T> supplier) {
            this.map = supplier.get();
        }

        @Override
        public MapBuilder<T, K, V> put(K key, V value) {
            this.map.put(key, value);
            return this;
        }

        @Override
        public MapBuilder<T, K, V> putAll(Map<K, V> map) {
            this.map.putAll(map);
            return this;
        }

        @Override
        public T build() {
            return this.map;
        }
    }

    /**
     * CollectionBuilderを作成します<br>
     * 次はArrayListのインスタンスにappleとgrapeを追加する例です
     * <pre>{@code
     *     List<String> list = Builders.newCollectionBuilder(ArrayList<String>::new)
     *             .add("apple")
     *             .add("grape")
     *             .build();
     * }</pre>
     *
     * @param supplier Collectionインスタンスを返すサプライヤ
     * @return CollectionBuilder
     */
    public static <T extends Collection<V>, V> CollectionBuilder<T, V> newCollectionBuilder(Supplier<T> supplier) {
        return new CollectionBuilderImpl<T, V>(supplier);
    }

    /**
     * MapBuilderを作成します<br>
     * 次はLinkedHashMapのインスタンスにappleとgrapeの値段を設定する例です
     * <pre>{@code
     *     Map<String, Integer> map = Builders.newMapBuilder(LinkedHashMap<String, Integer>::new)
     *             .put("apple", 100)
     *             .put("grape", 200)
     *             .build();
     * }</pre>
     * @param supplier Mapインスタンスを返すサプライヤ
     * @return MapBuilder
     */
    public static <T extends Map<K, V>, K, V> MapBuilder<T, K, V> newMapBuilder(Supplier<T> supplier) {
        return new MapBuilderImpl<T, K, V>(supplier);
    }
}
