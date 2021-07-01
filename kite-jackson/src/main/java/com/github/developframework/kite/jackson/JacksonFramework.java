package com.github.developframework.kite.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.developframework.kite.core.Framework;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.Producer;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.strategy.KitePropertyNamingStrategy;
import lombok.RequiredArgsConstructor;

/**
 * 由jackson框架实现
 *
 * @author qiushui on 2021-06-29.
 */
@RequiredArgsConstructor
public class JacksonFramework implements Framework<ObjectMapper> {

    private final ObjectMapper objectMapper;

    @Override
    public KitePropertyNamingStrategy namingStrategy() {
        return new JacksonKitePropertyNamingStrategy();
    }

    @Override
    public ObjectMapper getCore() {
        return objectMapper;
    }

    @Override
    public Producer buildProducer(KiteConfiguration configuration, DataModel dataModel, String namespace, String templateId) {
        return new JacksonProducer(configuration, dataModel, namespace, templateId);
    }
}
