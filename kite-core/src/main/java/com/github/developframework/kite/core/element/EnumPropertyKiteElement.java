package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.TemplateLocation;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.processor.json.EnumPropertyJsonProcessor;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.xml.EnumPropertyXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import lombok.Getter;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * 枚举属性节点
 *
 * @author qiuzhenhao on 2020-03-31.
 */
public class EnumPropertyKiteElement extends PropertyKiteElement {

    @Getter
    private Map<String, String> enumMap;

    public EnumPropertyKiteElement(KiteConfiguration configuration, TemplateLocation templateLocation, DataDefinition dataDefinition, String alias) {
        super(configuration, templateLocation, dataDefinition, alias);
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext context, ObjectNode parentNode) {
        EnumPropertyJsonProcessor processor = new EnumPropertyJsonProcessor(context, this);
        processor.setNode(parentNode);
        return processor;
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Element> createXmlProcessor(XmlProcessContext context, Element parentNode) {
        EnumPropertyXmlProcessor processor = new EnumPropertyXmlProcessor(context, this);
        processor.setNode(parentNode);
        return processor;
    }

    public void putEnumText(String enumValue, String text) {
        if (enumMap == null) {
            enumMap = new HashMap<>();
        }
        enumMap.putIfAbsent(enumValue, text);
    }

    public String getEnumText(String enumValue) {
        if (enumMap == null) {
            return null;
        }
        return enumMap.get(enumValue);
    }
}
