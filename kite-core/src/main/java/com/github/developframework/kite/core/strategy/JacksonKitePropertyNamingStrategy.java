package com.github.developframework.kite.core.strategy;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.github.developframework.kite.core.KiteConfiguration;

/**
 * 使用Jackson的命名策略
 * @author qiuzhenhao
 */
public class JacksonKitePropertyNamingStrategy implements KitePropertyNamingStrategy {


    @Override
    public String propertyShowName(KiteConfiguration configuration, String expressionString) {
        PropertyNamingStrategy strategy = configuration.getObjectMapper().getPropertyNamingStrategy();
        if(strategy == null) {
            return expressionString;
        }
        return strategy.nameForField(null, null, expressionString);
    }
}
