package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.processor.json.DatePropertyJsonProcessor;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.json.PropertyJsonProcessor;
import com.github.developframework.kite.core.processor.xml.DatePropertyXmlProcessor;
import com.github.developframework.kite.core.processor.xml.PropertyXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import lombok.Getter;
import lombok.Setter;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * 日期时间型属性节点
 * @author qiuzhenhao
 */
public class DatePropertyKiteElement extends PropertyKiteElement{

    @Getter
    @Setter
    private String pattern;

    public DatePropertyKiteElement(KiteConfiguration configuration, String namespace, String templateId, DataDefinition dataDefinition, String alias) {
        super(configuration, namespace, templateId, dataDefinition, alias);
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext context, ObjectNode parentNode, Expression parentExpression) {
        PropertyJsonProcessor processor = new DatePropertyJsonProcessor(context, this, JsonProcessor.childExpression(this, parentExpression), pattern);
        processor.setNode(parentNode);
        return processor;
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Node> createXmlProcessor(XmlProcessContext context, Node parentNode, Expression parentExpression) {
        PropertyXmlProcessor processor = new DatePropertyXmlProcessor(context, this, XmlProcessor.childExpression(this, parentExpression), pattern);
        processor.setNode((Element) parentNode);
        return processor;
    }
}
