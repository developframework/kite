package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.json.XmlAttributeJsonProcessor;
import com.github.developframework.kite.core.processor.xml.XmlAttributeXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import org.dom4j.Node;

/**
 * @author qiuzhenhao
 */
public class XmlAttributeElement extends ContentKiteElement {

    public XmlAttributeElement(KiteConfiguration configuration, String namespace, String templateId, DataDefinition dataDefinition, String alias) {
        super(configuration, namespace, templateId, dataDefinition, alias);
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext jsonProcessContext, ObjectNode parentNode, Expression parentExpression) {
        Expression expression = parentExpression.getParentExpression() == null ? parentExpression : parentExpression.getParentExpression();
        return new XmlAttributeJsonProcessor(jsonProcessContext, this, JsonProcessor.childExpression(this, expression));
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Node> createXmlProcessor(XmlProcessContext xmlProcessContext, Node parentNode, Expression parentExpression) {
        Expression expression = parentExpression.getParentExpression() == null ? parentExpression : parentExpression.getParentExpression();
        return new XmlAttributeXmlProcessor(xmlProcessContext, this, XmlProcessor.childExpression(this, expression));
    }
}
