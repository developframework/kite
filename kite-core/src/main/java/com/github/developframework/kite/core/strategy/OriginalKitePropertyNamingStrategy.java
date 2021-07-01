package com.github.developframework.kite.core.strategy;

import com.github.developframework.kite.core.Framework;

/**
 * 原文命名策略
 *
 * @author qiuzhenhao
 */
public class OriginalKitePropertyNamingStrategy implements KitePropertyNamingStrategy {

    @Override
    public String propertyDisplayName(Framework<?> framework, String expressionString) {
        return expressionString;
    }
}
