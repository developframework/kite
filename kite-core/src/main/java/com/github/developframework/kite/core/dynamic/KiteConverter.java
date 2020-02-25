package com.github.developframework.kite.core.dynamic;

import java.util.Objects;

/**
 * 转化器
 *
 * @param <SOURCE> 源类型
 * @param <TARGET> 目标类型
 * @author qiuzhenhao
 */
@FunctionalInterface
public interface KiteConverter<SOURCE, TARGET> {

    /**
     * 转化方法
     *
     * @param source 源对象
     * @return 目标对象
     */
    TARGET convert(SOURCE source);

    /**
     * 判断是否为空的转换器
     *
     * @return
     */
    default KiteConverter<?, Boolean> nonNull() {
        return Objects::nonNull;
    }

    default KiteConverter<?, Boolean> isNull() {
        return Objects::isNull;
    }
}
