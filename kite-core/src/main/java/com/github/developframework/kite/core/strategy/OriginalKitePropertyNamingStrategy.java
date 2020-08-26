package com.github.developframework.kite.core.strategy;

import com.github.developframework.kite.core.KiteConfiguration;

/**
 * 原文命名策略
 *
 * @author qiuzhenhao
 */
public class OriginalKitePropertyNamingStrategy implements KitePropertyNamingStrategy {

    @Override
    public String propertyShowName(KiteConfiguration configuration, String expressionString) {
        return expressionString;
    }
}
