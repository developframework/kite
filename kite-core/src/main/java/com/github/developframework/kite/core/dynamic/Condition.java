package com.github.developframework.kite.core.dynamic;

import com.github.developframework.kite.core.data.DataModel;

/**
 * 条件
 * @author qiuzhenhao
 */
@FunctionalInterface
public interface Condition {

    /**
     * 判断条件
     * @param dataModel 数据模型
     * @param conditionValue 条件
     * @return 判断结果
     */
    boolean verify(DataModel dataModel, String conditionValue);
}
