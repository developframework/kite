package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.json.ObjectJsonProcessor;
import com.github.developframework.kite.core.processor.xml.ObjectXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import org.dom4j.Element;

/**
 * 对象节点
 * @author qiuzhenhao
 */
public class ObjectKiteElement extends ContainerKiteElement {

    public ObjectKiteElement(KiteConfiguration configuration, String namespace, String templateId, DataDefinition dataDefinition, String alias) {
        super(configuration, namespace, templateId, dataDefinition, alias);
    }

    public ObjectKiteElement(KiteConfiguration configuration, ContainerKiteElement containerElement, DataDefinition dataDefinition) {
        super(configuration, containerElement.namespace, containerElement.templateId, dataDefinition, containerElement.alias);
        this.copyChildElement(containerElement);
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext jsonProcessContext, ObjectNode parentNode) {
        return new ObjectJsonProcessor(jsonProcessContext, this);
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Element> createXmlProcessor(XmlProcessContext xmlProcessContext, Element parentNode) {
        return new ObjectXmlProcessor(xmlProcessContext, this);
    }
}
