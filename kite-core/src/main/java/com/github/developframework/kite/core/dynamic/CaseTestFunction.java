package com.github.developframework.kite.core.dynamic;

/**
 * Case test函数接口
 *
 * @author qiushui on 2018-10-29.
 */
@FunctionalInterface
public interface CaseTestFunction<T> {

    /**
     * 测试选择
     *
     * @param data switch传入的值
     * @return 是否选中该分支
     */
    boolean test(T data);
}
