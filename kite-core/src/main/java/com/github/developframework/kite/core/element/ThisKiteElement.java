package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.TemplateLocation;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.json.ThisJsonProcessor;
import com.github.developframework.kite.core.processor.xml.ThisXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import org.dom4j.Element;

/**
 * 指代自身的节点
 *
 * @author qiushui on 2018-10-28.
 */
public class ThisKiteElement extends ContainerKiteElement {

    public ThisKiteElement(KiteConfiguration configuration, TemplateLocation templateLocation, DataDefinition dataDefinition, String alias) {
        super(configuration, templateLocation, dataDefinition, alias);
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext jsonProcessContext, ObjectNode parentNode) {
        return new ThisJsonProcessor(jsonProcessContext, this);
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Element> createXmlProcessor(XmlProcessContext xmlProcessContext, Element parentNode) {
        return new ThisXmlProcessor(xmlProcessContext, this);
    }

    public ContentKiteElement createProxyObjectElement() {
        if (isChildElementEmpty()) {
            // 如果没有子节点，视为普通属性节点处理
            return new ProxyNormalPropertyKiteElement(configuration, templateLocation, dataDefinition, alias);
        } else {
            // 如果有子节点，视为数组节点处理
            return new ObjectKiteElement(configuration, this, dataDefinition);
        }
    }

    public ContentKiteElement createProxyArrayElement() {
        ArrayKiteElement arrayKiteElement = new ArrayKiteElement(configuration, this, dataDefinition);
//        arrayKiteElement.setXmlItemName(xmlItemName);
//        arrayKiteElement.setMapFunctionValue(mapFunctionValue);
//        arrayKiteElement.setComparatorValue(comparatorValue);
//        arrayKiteElement.setConverterValue(converterValue);
        return arrayKiteElement;
    }
}
