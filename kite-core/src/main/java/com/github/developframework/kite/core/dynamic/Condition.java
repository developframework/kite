package com.github.developframework.kite.core.dynamic;

import com.github.developframework.kite.core.data.DataModel;

/**
 * 条件
 * @author qiuzhenhao
 */
@FunctionalInterface
public interface Condition<T> {

    /**
     * 判断条件
     * @param dataModel 数据模型
     * @param currentValue 当前值
     * @return 判断结果
     */
    boolean verify(DataModel dataModel, T currentValue);
}
