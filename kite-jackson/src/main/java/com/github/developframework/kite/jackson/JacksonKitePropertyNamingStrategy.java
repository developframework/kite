package com.github.developframework.kite.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.github.developframework.kite.core.Framework;
import com.github.developframework.kite.core.strategy.KitePropertyNamingStrategy;

/**
 * 使用Jackson的命名策略
 *
 * @author qiuzhenhao
 */
public class JacksonKitePropertyNamingStrategy implements KitePropertyNamingStrategy {

    @Override
    public String propertyDisplayName(Framework<?> framework, String expressionString) {
        ObjectMapper objectMapper = (ObjectMapper) framework.getCore();
        PropertyNamingStrategy strategy = objectMapper.getPropertyNamingStrategy();
        if (strategy == null) {
            return expressionString;
        }
        return strategy.nameForField(null, null, expressionString);
    }
}
