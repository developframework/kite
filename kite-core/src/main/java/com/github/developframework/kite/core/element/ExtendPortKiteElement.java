package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.processor.json.ExtendPortJsonProcessor;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.xml.ExtendPortXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import lombok.Getter;
import org.dom4j.Node;

/**
 * 扩展端口节点
 * @author qiuzhenhao
 */
public class ExtendPortKiteElement extends FunctionalKiteElement {

    @Getter
    private String portName;

    public ExtendPortKiteElement(KiteConfiguration configuration, String namespace, String templateId, String portName) {
        super(configuration, namespace, templateId);
        this.portName = portName;
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext jsonProcessContext, ObjectNode parentNode, Expression parentExpression) {
        return new ExtendPortJsonProcessor(jsonProcessContext, this, parentNode, parentExpression);
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Node> createXmlProcessor(XmlProcessContext xmlProcessContext, Node parentNode, Expression parentExpression) {
        return new ExtendPortXmlProcessor(xmlProcessContext, this, parentNode, parentExpression);
    }
}
