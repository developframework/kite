package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.json.XmlAttributeJsonProcessor;
import com.github.developframework.kite.core.processor.xml.XmlAttributeXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import org.dom4j.Element;

/**
 * @author qiuzhenhao
 */
public class XmlAttributeElement extends ContentKiteElement {

    public XmlAttributeElement(KiteConfiguration configuration, String namespace, String templateId, DataDefinition dataDefinition, String alias) {
        super(configuration, namespace, templateId, dataDefinition, alias);
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext jsonProcessContext, ObjectNode parentNode) {
        return new XmlAttributeJsonProcessor(jsonProcessContext, this);
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Element> createXmlProcessor(XmlProcessContext xmlProcessContext, Element parentNode) {
        return new XmlAttributeXmlProcessor(xmlProcessContext, this);
    }
}
