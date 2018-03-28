package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.processor.json.ElseJsonProcessor;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.xml.ElseXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import org.dom4j.Node;

/**
 * else节点
 * @author qiuzhenhao
 */
public class ElseKiteElement extends ContainerFunctionalKiteElement{

    public ElseKiteElement(KiteConfiguration configuration, String namespace, String templateId) {
        super(configuration, namespace, templateId);
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext context, ObjectNode parentNode, Expression parentExpression) {
        return new ElseJsonProcessor(context, this, parentNode, parentExpression);
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Node> createXmlProcessor(XmlProcessContext context, Node parentNode, Expression parentExpression) {
        return new ElseXmlProcessor(context, this, parentNode, parentExpression);
    }
}
