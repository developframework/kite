package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.json.ObjectJsonProcessor;
import com.github.developframework.kite.core.processor.xml.ObjectXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import org.dom4j.Node;

/**
 * 代理对象节点
 * @author qiuzhenhao
 */
public class ProxyObjectKiteElement extends ObjectKiteElement{

    public ProxyObjectKiteElement(KiteConfiguration configuration, ContainerKiteElement containerElement, DataDefinition dataDefinition) {
        super(configuration, containerElement.namespace, containerElement.templateId, dataDefinition, containerElement.alias);
        this.copyChildElement(containerElement);
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext context, ObjectNode parentNode, Expression parentExpression) {
        return new ObjectJsonProcessor(context, this, parentExpression);
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Node> createXmlProcessor(XmlProcessContext context, Node parentNode, Expression parentExpression) {
        return new ObjectXmlProcessor(context, this, parentExpression);
    }
}
