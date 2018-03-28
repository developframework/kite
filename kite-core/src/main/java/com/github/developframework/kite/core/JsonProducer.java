package com.github.developframework.kite.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.github.developframework.kite.core.data.DataModel;

/**
 * Json生产者
 * @author qiuzhenhao
 */
public interface JsonProducer extends Producer {

    void outputJson(JsonGenerator jsonGenerator, DataModel dataModel, String namespace, String templateId);
}
