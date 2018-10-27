package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.json.NormalPropertyJsonProcessor;
import com.github.developframework.kite.core.processor.json.PropertyJsonProcessor;
import com.github.developframework.kite.core.processor.xml.NormalPropertyXmlProcessor;
import com.github.developframework.kite.core.processor.xml.PropertyXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import org.dom4j.Element;

/**
 * 普通的属性节点
 * @author qiuzhenhao
 */
public class NormalPropertyKiteElement extends PropertyKiteElement {

    public NormalPropertyKiteElement(KiteConfiguration configuration, String namespace, String templateId, DataDefinition dataDefinition, String alias) {
        super(configuration, namespace, templateId, dataDefinition, alias);
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext jsonProcessContext, ObjectNode parentNode) {
        PropertyJsonProcessor processor = new NormalPropertyJsonProcessor(jsonProcessContext, this);
        processor.setNode(parentNode);
        return processor;
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Element> createXmlProcessor(XmlProcessContext xmlProcessContext, Element parentNode) {
        PropertyXmlProcessor processor = new NormalPropertyXmlProcessor(xmlProcessContext, this);
        processor.setNode(parentNode);
        return processor;
    }
}
