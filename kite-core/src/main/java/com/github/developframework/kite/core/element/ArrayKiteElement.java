package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.processor.json.ArrayJsonProcessor;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.xml.ArrayXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

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
    protected String mapFunctionValue;
    @Setter
    protected String xmlItemName;
    @Setter
    protected String comparatorValue;

    public ArrayKiteElement(KiteConfiguration configuration, String namespace, String templateId, DataDefinition dataDefinition, String alias) {
        super(configuration, namespace, templateId, dataDefinition, alias);
        this.itemObjectElement = new ObjectKiteElement(configuration, namespace, templateId, dataDefinition, alias);
    }

    public ArrayKiteElement(KiteConfiguration configuration, ContainerKiteElement containerElement, DataDefinition dataDefinition) {
        this(configuration, containerElement.namespace, containerElement.templateId, dataDefinition, containerElement.alias);
        this.copyChildElement(containerElement);
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext context, ObjectNode parentNode) {
        return new ArrayJsonProcessor(context, this);
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Element> createXmlProcessor(XmlProcessContext context, Element parentNode) {
        return new ArrayXmlProcessor(context, this);
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

    public Optional<String> getMapFunctionValue() {
        return Optional.ofNullable(mapFunctionValue);
    }

    public Optional<String> getComparatorValue() {
        return Optional.ofNullable(comparatorValue);
    }

    public String getXmlItemName() {
        if(StringUtils.isBlank(xmlItemName)) {
            throw new KiteException("\"xml-item\" is undefined in template \"%s : %s\".", namespace, templateId);
        }
        return xmlItemName;
    }
}
