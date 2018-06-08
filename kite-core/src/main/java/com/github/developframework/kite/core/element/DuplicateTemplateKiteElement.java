package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.processor.json.DuplicateTemplateJsonProcessor;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.xml.DuplicateTemplateXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * 模板副本
 *
 * @author qiuzhenhao
 */
public final class DuplicateTemplateKiteElement extends ProxyObjectKiteElement {

    public DuplicateTemplateKiteElement(KiteConfiguration configuration, Template template) {
        super(configuration, template, null);
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext context, ObjectNode parentNode, Expression parentExpression) {
        DuplicateTemplateJsonProcessor processor = new DuplicateTemplateJsonProcessor(context, this, parentExpression);
        processor.setNode(parentNode);
        return processor;
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Node> createXmlProcessor(XmlProcessContext context, Node parentNode, Expression parentExpression) {
        DuplicateTemplateXmlProcessor processor = new DuplicateTemplateXmlProcessor(context, this, parentExpression);
        processor.setNode((Element) parentNode);
        return processor;
    }
}
