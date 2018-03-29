package com.github.developframework.kite.core.strategy;

import com.github.developframework.kite.core.KiteConfiguration;

/**
 * 什么也不做的命名策略
 * @author qiuzhenhao
 */
public class DoNothingKitePropertyNamingStrategy implements KitePropertyNamingStrategy{

    @Override
    public String propertyShowName(KiteConfiguration configuration, String expressionString) {
        return expressionString;
    }
}
