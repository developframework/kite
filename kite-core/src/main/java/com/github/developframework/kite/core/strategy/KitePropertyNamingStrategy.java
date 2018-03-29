package com.github.developframework.kite.core.strategy;

import com.github.developframework.kite.core.KiteConfiguration;

/**
 * 属性节点命名策略
 * @author qiuzhenhao
 */
public interface KitePropertyNamingStrategy {

    String propertyShowName(KiteConfiguration configuration, String expressionString);
}
