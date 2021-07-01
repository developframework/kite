package com.github.developframework.kite.core.strategy;

import com.github.developframework.kite.core.Framework;

/**
 * 属性节点命名策略
 *
 * @author qiuzhenhao
 */
public interface KitePropertyNamingStrategy {

    String propertyDisplayName(Framework<?> framework, String name);
}
