package logbook.util.builder;

import java.util.Collection;

import org.apache.commons.lang3.builder.Builder;

/**
 * Collectionを生成するBuilderです
 *
 * @param <T> Collection
 * @param <V> value
 */
public interface CollectionBuilder<T extends Collection<V>, V> extends Builder<T> {

    /**
     * 指定された要素をこのコレクションに追加します
     *
     * @param value このリストに追加される要素
     * @return CollectionBuilder
     * @see java.util.Collection#add(Object)
     */
    CollectionBuilder<T, V> add(V value);

    /**
     * 指定されたコレクションのすべての要素をこのコレクションに追加します
     *
     * @param collection このコレクションに追加される要素を含むコレクション
     * @return CollectionBuilder
     * @see java.util.Collection#addAll(java.util.Collection)
     */
    CollectionBuilder<T, V> addAll(Collection<V> collection);
}
