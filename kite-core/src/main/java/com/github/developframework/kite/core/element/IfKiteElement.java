package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.processor.json.IfJsonProcessor;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.xml.IfXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import lombok.Getter;
import lombok.Setter;
import org.dom4j.Node;

import java.util.Optional;

/**
 * if节点
 *
 * @author qiuzhenhao
 */
public class IfKiteElement extends ContainerFunctionalKiteElement {

    @Getter
    private String conditionValue;
    @Setter
    private ElseKiteElement elseElement;

    public IfKiteElement(KiteConfiguration configuration, String namespace, String templateId, String conditionValue) {
        super(configuration, namespace, templateId);
        this.conditionValue = conditionValue;
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext context, ObjectNode parentNode, Expression parentExpression) {
        return new IfJsonProcessor(context, this, parentNode, parentExpression);
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Node> createXmlProcessor(XmlProcessContext context, Node parentNode, Expression parentExpression) {
        return new IfXmlProcessor(context, this, parentNode, parentExpression);
    }

    public Optional<ElseKiteElement> getElseElement() {
        return Optional.ofNullable(elseElement);
    }

}
