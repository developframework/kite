package com.github.developframework.kite.core.dynamic;

/**
 * Case test函数接口
 *
 * @author qiushui on 2018-10-29.
 */
@FunctionalInterface
public interface CaseTestFunction<T> {

    boolean test(T data);
}
