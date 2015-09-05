package logbook.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import logbook.data.DataType;

/**
 * イベントリスナーがどのイベントを受け取るかを表す注釈です
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventTarget {

    DataType[] value();
}
