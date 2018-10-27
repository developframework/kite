package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.dynamic.PropertyConverter;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.PropertyKiteElement;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.util.Optional;

/**
 * 抽象的属性节点处理器
 *
 * @author qiuzhenhao
 */
public abstract class PropertyJsonProcessor extends ContentJsonProcessor<PropertyKiteElement, ObjectNode> {

    public PropertyJsonProcessor(JsonProcessContext jsonProcessContext, PropertyKiteElement element) {
        super(jsonProcessContext, element);
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        Optional<Object> valueOptional = getDataValue(parentProcessor);
        if (valueOptional.isPresent()) {
            value = valueOptional.get();
            if (element.getConverterValue().isPresent()) {
                PropertyConverter converter = KiteUtils.getComponentInstance(jsonProcessContext.getDataModel(), element.getConverterValue().get(), PropertyConverter.class, "converter");
                value = converter.convert(value);
            }
            node = (ObjectNode) parentProcessor.getNode();
            return true;
        }
        if (!element.isNullHidden()) {
            ((ObjectNode) parentProcessor.getNode()).putNull(element.showNameJSON());
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        Class<?> valueClass = value.getClass();
        if (support(valueClass)) {
            handle(node, valueClass, value, element.showNameJSON());
        }
    }

    /**
     * 判断是否支持sourceClass类型
     *
     * @param sourceClass 源类型
     * @return 是否支持
     */
    protected abstract boolean support(Class<?> sourceClass);

    /**
     * 属性具体处理
     *
     * @param parentNode  父树节点
     * @param sourceClass sourceClass
     * @param value       值
     * @param showName    显示的名称
     */
    protected abstract void handle(ObjectNode parentNode, Class<?> sourceClass, Object value, String showName);
}
