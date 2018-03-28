package com.github.developframework.kite.core.dynamic;

/**
 * 转化器
 * @author qiuzhenhao
 * @param <TARGET> 目标类型
 */
@FunctionalInterface
public interface PropertyConverter<TARGET> {

    /**
     * 转化方法
     * @param source 源对象
     * @return 目标对象
     */
    TARGET convert(Object source);
}
