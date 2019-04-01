package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.TemplateLocation;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.json.LinkJsonProcessor;
import com.github.developframework.kite.core.processor.xml.LinkXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import lombok.Getter;
import lombok.Setter;
import org.dom4j.Element;

import java.util.Collection;

/**
 * 一对一链接节点
 *
 * @author qiuzhenhao
 */
public class LinkKiteElement extends ObjectKiteElement {

    @Setter
    @Getter
    protected String mapFunctionValue;

    public LinkKiteElement(KiteConfiguration kiteConfiguration, TemplateLocation templateLocation, DataDefinition dataDefinition, String alias) {
        super(kiteConfiguration, templateLocation, dataDefinition, alias);
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext context, ObjectNode parentNode) {
        LinkJsonProcessor linkProcessor = new LinkJsonProcessor(context, this);
        linkProcessor.setNode(parentNode);
        return linkProcessor;
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Element> createXmlProcessor(XmlProcessContext context, Element parentNode) {
        LinkXmlProcessor linkProcessor = new LinkXmlProcessor(context, this);
        linkProcessor.setNode(parentNode);
        return linkProcessor;
    }

    /**
     * 创建一个代理节点处理任务
     *
     * @return 代理节点处理任务
     */
    public ContentKiteElement createProxyContentElement(Class<?> valueClass) {
        if (valueClass.isArray() || Collection.class.isAssignableFrom(valueClass)) {
            // 如果值类型是数组或集合，视为数组节点处理
            ArrayKiteElement arrayKiteElement = new ArrayKiteElement(configuration, this, dataDefinition);
            mapFunctionValue = arrayKiteElement.getMapFunctionValue().orElse(null);
            return arrayKiteElement;
        } else if (isChildElementEmpty()) {
            // 如果没有子节点，视为普通属性节点处理
            return new ProxyNormalPropertyKiteElement(configuration, templateLocation, dataDefinition, alias);
        } else {
            // 如果有子节点，视为对象节点处理
            return new ObjectKiteElement(configuration, this, dataDefinition);
        }
    }
}
