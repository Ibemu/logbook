package logbook.gui.logic;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import logbook.config.AppConfig;
import logbook.util.BeanProperty;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * SWTのテーブルをラップして使いやすくするクラス<br>
 * Beanクラスの{@link logbook.annotation.Name}注釈が付与されたプロパティが列として認識されます
 * <pre>{@code
 *
 *     TableWrapper<ItemBean> wrapper = new TableWrapper(table, ItemBean.class)
 *             .setContentSupplier(getSupplier()) // テーブルの内容を供給するサプライヤー
 *             .setDecorator(getDecorator()) // テーブルの行を装飾
 *             .setFilter(e -> e.price <= 1000) // 内容のフィルター
 *             .addSortParameter(1, SWT.DOWN) // 2列目を降順でソートする
 *             .addSortParameter(0, SWT.UP) // 1列目を昇順でソートする
 *             .reload() // 内容を読み込み
 *             .update(); // テーブルへ描画
 * }</pre>
 */
public final class TableWrapper<T> {

    /** テーブル */
    private final Table table;
    /** Beanの情報 */
    private final BeanProperty<T> property;
    /** 1列目にインデックス番号を付ける */
    private final boolean visibleIndex;
    /** サプライヤー */
    private Supplier<Stream<T>> contentSupplier;
    /** TableItemの装飾 */
    private TableItemDecorator<T> decorator;
    /** テーブルソート */
    private Comparator<String> comparator = new EmptyLast(new TableComparator());
    /** フィルター */
    private Predicate<T> filter;
    /** このテーブルを持つダイアログクラス */
    private Class<?> dialogClass;

    /** テーブルの内容 */
    private List<T> content;
    /** ソート */
    private final List<SortParameter> parameters = new ArrayList<>();

    /**
     * TableWrapperを構築します
     *
     * @param table テーブル
     * @param clazz Beanクラス
     */
    public TableWrapper(Table table, Class<T> clazz) {
        this(table, true, clazz);
    }

    /**
     * TableWrapperを構築します
     *
     * @param table テーブル
     * @param visibleIndex 1列目にインデックス番号を付ける
     * @param clazz Beanクラス
     */
    public TableWrapper(Table table, boolean visibleIndex, Class<T> clazz) {
        this.table = table;
        this.property = BeanProperty.getInstance(clazz);
        this.visibleIndex = visibleIndex;

        // ヘッダーセット
        List<String> names = this.property.getNames();
        SelectionListener listener = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (e.getSource() instanceof TableColumn) {
                    TableWrapper.this.sort((TableColumn) e.getSource());
                    TableWrapper.this.update();
                }
            }
        };
        if (visibleIndex) {
            // インデックス列にはSelectionListenerを付けない
            TableColumn col = new TableColumn(this.table, SWT.LEFT);
            col.setText("");
        }
        for (String name : names) {
            TableColumn col = new TableColumn(this.table, SWT.LEFT);
            col.addSelectionListener(listener);
            col.setText(name);
        }
    }

    /**
     * このテーブルを持つダイアログクラスをセットします
     *
     * @param clazz
     * @return
     */
    public TableWrapper<T> setDialogClass(Class<?> clazz) {
        this.dialogClass = clazz;
        return this;
    }

    /**
     * テーブルの内容を供給するサプライヤーをセットします
     *
     * @param contentSupplier テーブルの内容を供給するサプライヤー
     * @return TableWrapper
     */
    public TableWrapper<T> setContentSupplier(Supplier<Stream<T>> contentSupplier) {
        Objects.requireNonNull(contentSupplier);
        this.contentSupplier = contentSupplier;
        return this;
    }

    /**
     * TableItemの装飾を行うためのTableItemDecoratorをセットします
     *
     * @param decorator TableItemDecorator
     * @return TableWrapper
     */
    public TableWrapper<T> setDecorator(TableItemDecorator<T> decorator) {
        this.decorator = decorator;
        return this;
    }

    /**
     * テーブルソートのためのComparatorをセットします
     *
     * @param comparator Comparator
     * @return TableWrapper
     */
    public TableWrapper<T> setComparator(Comparator<String> comparator) {
        Objects.requireNonNull(comparator);
        this.comparator = comparator;
        return this;
    }

    /**
     * フィルターを適用します
     *
     * @param filter フィルター関数、フィルターを解除する場合はnull
     * @return TableWrapper
     */
    public TableWrapper<T> setFilter(Predicate<T> filter) {
        this.filter = filter;
        return this;
    }

    /**
     * テーブルの内容を供給するサプライヤーからテーブルの内容を取得します
     *
     * @return TableWrapper
     */
    public TableWrapper<T> reload() {
        Stream<T> stream = this.contentSupplier.get();
        if (this.filter != null)
            stream = stream.filter(this.filter);
        this.content = this.sort(stream).collect(Collectors.toList());
        return this;
    }

    /**
     * テーブルを再描画する
     *
     * @return TableWrapper
     */
    public TableWrapper<T> update() {
        this.table.setRedraw(false);
        TableItem[] items = this.table.getItems();
        int size = this.content.size();
        int override = Math.min(items.length, size);
        // Override Row
        for (int i = 0; i < override; i++) {
            T bean = this.content.get(i);
            String[] text = this.property.getStringValues(bean);
            this.setText(i, items[i], text);
            if (this.decorator != null) {
                this.decorator.update(items[i], bean, i);
            }
        }
        // Create Row
        for (int i = override; i < size; i++) {
            T bean = this.content.get(i);
            String[] text = this.property.getStringValues(bean);
            TableItem item = new TableItem(this.table, SWT.NONE);
            this.setText(i, item, text);
            if (this.decorator != null) {
                this.decorator.update(item, bean, i);
            }
        }
        // Dispose Row
        for (int i = size; i < items.length; i++) {
            items[i].dispose();
        }
        // Pack headers
        boolean[] visibles = null;
        if (this.dialogClass != null) {
            visibles = AppConfig.get().getVisibleColumnMap().get(this.dialogClass.getName());
        }
        TableColumn[] columns = this.table.getColumns();
        // 列の表示・非表示設定のサイズがカラム数と異なっている場合は破棄する
        if ((visibles != null) && (visibles.length != columns.length)) {
            AppConfig.get().getVisibleColumnMap().remove(this.dialogClass.getName());
            visibles = null;
        }
        for (int i = 0; i < columns.length; i++) {
            if ((visibles == null) || visibles[i]) {
                columns[i].pack();
            } else {
                columns[i].setWidth(0);
            }
        }
        this.table.setRedraw(true);
        return this;
    }

    /**
     * ソートを設定する
     *
     * @param index ソートを行うカラムのインデックス
     * @param order 昇順の場合SWT.UP, 降順の場合SWT.DOWN
     * @return TableWrapper
     */
    public TableWrapper<T> addSortParameter(int index, int order) {
        this.parameters.removeIf(e -> e.index == index);
        this.parameters.add(new SortParameter(index, order));
        return this;
    }

    /**
     * 既に読み込まれているテーブルの内容をソートする
     */
    public TableWrapper<T> sort() {
        this.content = this.sort(this.content.stream())
                .collect(Collectors.toList());
        return this;
    }

    /**
     * 選択している内容を取得します
     *
     * @param factory Bean配列を生成するIntFunction
     * @return 選択している内容
     */
    public T[] getSelection(IntFunction<T[]> factory) {
        int[] indices = this.table.getSelectionIndices();
        T[] objs = factory.apply(indices.length);
        Arrays.setAll(objs, i -> this.content.get(indices[i]));
        return objs;
    }

    /**
     * テーブルの内容を取得します
     *
     * @return テーブルの内容
     */
    public List<T> getContent() {
        return Collections.unmodifiableList(this.content);
    }

    /**
     * 基になるSWTのテーブルを取得します
     *
     * @return テーブル
     */
    public Table getTable() {
        return this.table;
    }

    /**
     * カラム名のインデックス位置を取得します
     *
     * @param name カラム名
     * @return インデックス位置
     */
    public int getColumnIndex(String name) {
        return this.property.getNameIndex(name);
    }

    private void setText(int index, TableItem item, String[] text) {
        int shift = 0;
        if (this.visibleIndex) {
            item.setText(shift++, Integer.toString(index + 1));
        }
        for (int i = 0; i < text.length; i++) {
            item.setText(i + shift, text[i]);
        }
    }

    private void sort(TableColumn column) {
        int index = this.getColumnIndex(column.getText());
        int order;
        if ((this.table.getSortColumn() == column)
                && (this.table.getSortDirection() == SWT.UP)) {
            order = SWT.DOWN;
        } else {
            this.table.setSortColumn(column);
            order = SWT.UP;
        }
        this.table.setSortDirection(order);
        this.addSortParameter(index, order);
        this.sort();
    }

    private Stream<T> sort(Stream<T> stream) {
        for (SortParameter p : this.parameters) {
            Comparator<String> comparator = p.order == SWT.UP
                    ? this.comparator : this.comparator.reversed();
            stream = stream.sorted((o1, o2) -> {
                String v1 = this.property.getValue(o1, p.index).map(String::valueOf).orElse(null);
                String v2 = this.property.getValue(o2, p.index).map(String::valueOf).orElse(null);
                return comparator.compare(v1, v2);
            });
        }
        return stream;
    }

    /**
     * ソートのパラメーター
     */
    private static final class SortParameter {
        /** インデックス */
        private final int index;
        /** 昇順・降順 */
        private final int order;

        public SortParameter(int index, int order) {
            this.index = index;
            this.order = order;
        }
    }

    /**
     * テーブルソート
     */
    private static final class TableComparator implements Comparator<String> {
        private final Pattern pattern = Pattern.compile("(?:\\d+日)?(?:\\d+時間)?(?:\\d+分)?(?:\\d+秒)?");

        @Override
        public int compare(String o1, String o2) {
            if (o1.equals(o2))
                return 0;
            if (StringUtils.isNumeric(o1) && StringUtils.isNumeric(o2)) {
                // 数値文字列の場合
                Long o1l = Long.valueOf(o1);
                Long o2l = Long.valueOf(o2);
                return o1l.compareTo(o2l);
            } else if (this.pattern.matcher(o1).matches()) {
                try {
                    // 時刻文字列の場合
                    // SimpleDateFormatは24時間超えるような時刻でも正しく?パースしてくれる
                    Date o1date = DateUtils.parseDate(o1, "ss秒", "mm分ss秒", "HH時間mm分", "dd日HH時間mm分");
                    Date o2date = DateUtils.parseDate(o2, "ss秒", "mm分ss秒", "HH時間mm分", "dd日HH時間mm分");
                    return o1date.compareTo(o2date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            // 文字列の場合
            return o1.compareTo(o2);
        }
    }

    /**
     * nullまたは空文字をnullまたは空文字以外より大きいとみなすコンパレータ
     */
    private static final class EmptyLast implements Comparator<String> {
        private final Comparator<String> comparator;

        public EmptyLast(Comparator<String> comparator) {
            this.comparator = comparator;
        }

        @Override
        public int compare(String o1, String o2) {
            if ((o1 == null) || o1.isEmpty())
                return ((o2 == null) || o2.isEmpty()) ? 0 : 1;
            if ((o2 == null) || o2.isEmpty())
                return -1;
            return this.comparator.compare(o1, o2);
        }
    }
}
