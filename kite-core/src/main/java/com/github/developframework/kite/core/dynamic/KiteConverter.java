package com.github.developframework.kite.core.dynamic;

/**
 * 转化器
 * @author qiuzhenhao
 * @param <SOURCE> 源类型
 * @param <TARGET> 目标类型
 */
@FunctionalInterface
public interface KiteConverter<SOURCE, TARGET> {

    /**
     * 转化方法
     * @param source 源对象
     * @return 目标对象
     */
    TARGET convert(SOURCE source);
}
