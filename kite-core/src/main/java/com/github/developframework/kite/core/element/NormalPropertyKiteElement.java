package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.processor.json.*;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.json.PropertyJsonProcessor;
import com.github.developframework.kite.core.processor.xml.NormalPropertyXmlProcessor;
import com.github.developframework.kite.core.processor.xml.PropertyXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * 普通的属性节点
 * @author qiuzhenhao
 */
public class NormalPropertyKiteElement extends PropertyKiteElement {

    public NormalPropertyKiteElement(KiteConfiguration configuration, String namespace, String templateId, DataDefinition dataDefinition, String alias) {
        super(configuration, namespace, templateId, dataDefinition, alias);
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext jsonProcessContext, ObjectNode parentNode, Expression parentExpression) {
        PropertyJsonProcessor processor = new NormalPropertyJsonProcessor(jsonProcessContext, this, JsonProcessor.childExpression(this, parentExpression));
        processor.setNode(parentNode);
        return processor;
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Node> createXmlProcessor(XmlProcessContext xmlProcessContext, Node parentNode, Expression parentExpression) {
        PropertyXmlProcessor processor = new NormalPropertyXmlProcessor(xmlProcessContext, this, XmlProcessor.childExpression(this, parentExpression));
        processor.setNode((Element) parentNode);
        return processor;
    }
}
