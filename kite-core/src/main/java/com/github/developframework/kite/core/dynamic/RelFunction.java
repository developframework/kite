package com.github.developframework.kite.core.dynamic;

/**
 * 关联函数接口
 * @author qiuzhenhao
 */
public interface RelFunction<S, T> {

    /**
     * 判断是否关联
     * @param sourceItem 源元素
     * @param sourceIndex 源索引
     * @param target 目标数组
     * @param targetIndex 目标索引
     * @return 是否关联
     */
    boolean relevant(S sourceItem, int sourceIndex, T target, int targetIndex);

}
