package logbook.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import logbook.annotation.Name;

/**
 * Beanのフィールドに対応する名前とフィールドのgetメソッドを持つ
 *
 * @param <T> Bean
 */
public class BeanProperty<T> {

    private final Class<T> clazz;
    private final List<String> names = new ArrayList<>();
    private final Map<String, Function<T, Object>> getterMap = new LinkedHashMap<>();

    /**
     * BeanクラスのBeanPropertyインスタンスを取得する
     *
     * @param clazz Beanクラス
     * @return
     */
    public static <T> BeanProperty<T> getInstance(Class<T> clazz) {
        return PropertyCache.get(clazz);
    }

    private BeanProperty(Class<T> clazz) {
        // Search field
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if ((field.getModifiers() & Modifier.STATIC) != 0) {
                // NOT static only
                continue;
            }
            // カラム名
            String header;
            Name anno = field.getAnnotation(Name.class);
            if (anno != null) {
                header = anno.value();
            } else {
                continue;
            }
            this.names.add(header);
            this.getterMap.put(header, this.function(clazz, field));
        }
        this.clazz = clazz;
    }

    /**
     * Beanが持つフィールド名のListを取得する
     *
     * @return フィールド名のList
     */
    public List<String> getNames() {
        return Collections.unmodifiableList(this.names);
    }

    /**
     * フィールド名にアクセスするFunctionを取得する
     *
     * @param name フィールド名
     * @return フィールド名にアクセスするFunction
     */
    public Function<T, Object> getFunction(String name) {
        return this.getterMap.get(name);
    }

    /**
     * フィールド名に対応する位置のインデックスを返します
     *
     * @param name フィールド名
     * @return 指定されたフィールド名の位置のインデックス。そのフィールド名がない場合は -1
     */
    public int getNameIndex(String name) {
        return this.names.indexOf(name);
    }

    /**
     * Beanが持つプロパティーの値をString配列として取得します
     *
     * @param bean Bean
     * @return Beanが持つ全てのプロパティーの値
     */
    public String[] getStringValues(T bean) {
        String[] values = new String[this.names.size()];
        for (int i = 0; i < this.names.size(); i++) {
            values[i] = String.valueOf(this.getValue(bean, this.names.get(i)));
        }
        return values;
    }

    /**
     * Beanが持つプロパティーの値をObject配列として取得します
     *
     * @param bean Bean
     * @return Beanが持つ全てのプロパティーの値
     */
    public Object[] getValues(T bean) {
        Object[] values = new Object[this.names.size()];
        for (int i = 0; i < this.names.size(); i++) {
            values[i] = this.getValue(bean, this.names.get(i));
        }
        return values;
    }

    /**
     * Beanから指定した名前でObjectを取得します
     *
     * @param bean Bean
     * @param name 名前
     * @return Beanが持つプロパティーの値
     */
    public Object getValue(T bean, String name) {
        Function<T, Object> func = this.getterMap.get(name);
        if (func != null) {
            return func.apply(bean);
        }
        return null;
    }

    /**
     * BeanからインデックスでObjectを取得します
     *
     * @param bean Bean
     * @param name 名前
     * @return Beanが持つプロパティーの値
     */
    public Object getValue(T bean, int index) {
        Function<T, Object> func = this.getterMap.get(this.names.get(index));
        if (func != null) {
            return func.apply(bean);
        }
        return null;
    }

    /**
     * フィールド名 からgetメソッドにアクセスするFunctionを作成する
     *
     * @param clazz Bean
     * @param field フィールド
     * @return getメソッドまたはpublicフィールドにアクセスするFunction
     */
    private Function<T, Object> function(Class<T> clazz, Field field) {
        // フィールドがpublicならフィールドアクセスする
        if ((field.getModifiers() & Modifier.PUBLIC) != 0) {
            return o -> {
                try {
                    return field.get(o);
                } catch (Exception e) {
                    BeanProperty.handle(e);
                    return null;
                }
            };
        }
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), clazz, field.getName(), null);
            // getメソッド
            Method method = descriptor.getReadMethod();
            if (method != null) {
                return o -> {
                    try {
                        return method.invoke(o);
                    } catch (Exception e) {
                        BeanProperty.handle(e);
                        return null;
                    }
                };
            }
        } catch (IntrospectionException e) {
            BeanProperty.handle(e);
        }
        return null;
    }

    private static void handle(Exception e) {
        e.printStackTrace();
    }

    /**
     * BeanPropertyをキャッシュする
     */
    private static class PropertyCache {
        private static final BeanProperty<?>[] cache = new BeanProperty[10];

        @SuppressWarnings("unchecked")
        private synchronized static <T> BeanProperty<T> get(Class<T> clazz) {
            for (int i = 0; i < cache.length; i++) {
                if (cache[i] == null) {
                    return (BeanProperty<T>) (cache[i] = new BeanProperty<T>(clazz));
                } else if (clazz.equals(cache[i].clazz)) {
                    return (BeanProperty<T>) cache[i];
                }
            }
            return new BeanProperty<T>(clazz);
        }
    }
}
