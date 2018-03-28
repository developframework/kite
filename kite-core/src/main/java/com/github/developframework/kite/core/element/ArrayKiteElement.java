package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.processor.json.ArrayJsonProcessor;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.xml.ArrayXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import lombok.Getter;
import lombok.Setter;
import org.dom4j.Node;

import java.util.Optional;

/**
 * 数组节点
 * @author qiuzhenhao
 */
public class ArrayKiteElement extends ContainerKiteElement{

    /* 元素对象节点 */
    @Getter
    private ObjectKiteElement itemObjectElement;

    @Setter
    private String mapFunctionValue;
    @Setter
    @Getter
    private String xmlItemName;

    public ArrayKiteElement(KiteConfiguration configuration, String namespace, String templateId, DataDefinition dataDefinition, String alias) {
        super(configuration, namespace, templateId, dataDefinition, alias);
        this.itemObjectElement = new ObjectKiteElement(configuration, namespace, templateId, dataDefinition, alias);
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext context, ObjectNode parentNode, Expression parentExpression) {
        return new ArrayJsonProcessor(context, this, JsonProcessor.childExpression(this, parentExpression));
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Node> createXmlProcessor(XmlProcessContext context, Node parentNode, Expression parentExpression) {
        return new ArrayXmlProcessor(context, this, XmlProcessor.childExpression(this, parentExpression));
    }

    @Override
    public void addChildElement(KiteElement element) {
        super.addChildElement(element);
        this.itemObjectElement.addChildElement(element);
    }

    @Override
    public void copyChildElement(ContainerKiteElement otherContainerElement) {
        super.copyChildElement(otherContainerElement);
        this.itemObjectElement.copyChildElement(otherContainerElement);
    }

    public Optional<String> getMapFunctionValueOptional() {
        return Optional.ofNullable(mapFunctionValue);
    }
}
