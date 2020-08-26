package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.TemplateLocation;
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

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * 数组节点
 * @author qiuzhenhao
 */
@Getter
public class ArrayKiteElement extends ContainerKiteElement{

    /* 元素对象节点 */
    private final ObjectKiteElement itemObjectElement;

    @Setter
    protected String mapFunctionValue;
    @Setter
    protected String xmlItemName;
    @Setter
    protected String comparatorValue;
    @Setter
    protected Integer limit;

    protected boolean nullEmpty;

    public ArrayKiteElement(KiteConfiguration configuration, TemplateLocation templateLocation, DataDefinition dataDefinition, String alias) {
        super(configuration, templateLocation, dataDefinition, alias);
        this.itemObjectElement = new ObjectKiteElement(configuration, templateLocation, dataDefinition, alias);
    }

    public ArrayKiteElement(KiteConfiguration configuration, ContainerKiteElement containerElement, DataDefinition dataDefinition) {
        this(configuration, containerElement.templateLocation, dataDefinition, containerElement.alias);
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

    public String getXmlItemName() {
        if (StringUtils.isEmpty(xmlItemName)) {
            throw new KiteException("\"xml-item\" is undefined in template \"%s\".", templateLocation.toString());
        }
        return xmlItemName;
    }

    public void setNullEmpty(String nullEmptyStr) {
        this.nullEmpty = isNotEmpty(nullEmptyStr) && Boolean.parseBoolean(nullEmptyStr);
    }
}
