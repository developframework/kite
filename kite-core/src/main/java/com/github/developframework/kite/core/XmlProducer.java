package com.github.developframework.kite.core;

import com.github.developframework.kite.core.data.DataModel;

import java.io.Writer;

/**
 * Xml生产者
 * @author qiuzhenhao
 */
public interface XmlProducer extends Producer{

    void outputXml(Writer writer, DataModel dataModel, String namespace, String templateId);
}
