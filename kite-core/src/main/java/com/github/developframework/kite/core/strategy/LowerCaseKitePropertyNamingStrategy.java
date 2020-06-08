package com.github.developframework.kite.core.strategy;

import com.github.developframework.kite.core.KiteConfiguration;

/**
 * 全小写命名策略
 *
 * @author qiushui on 2020-06-08.
 */
public class LowerCaseKitePropertyNamingStrategy implements KitePropertyNamingStrategy {

    @Override
    public String propertyShowName(KiteConfiguration configuration, String expressionString) {
        return expressionString.toLowerCase();
    }
}
