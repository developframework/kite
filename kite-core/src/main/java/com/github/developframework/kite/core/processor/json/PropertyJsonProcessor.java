package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.dynamic.PropertyConverter;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.PropertyKiteElement;
import com.github.developframework.kite.core.exception.InvalidArgumentsException;
import com.github.developframework.kite.core.exception.KiteException;

import java.util.Optional;

/**
 * 抽象的属性节点处理器
 *
 * @author qiuzhenhao
 */
public abstract class PropertyJsonProcessor extends ContentJsonProcessor<PropertyKiteElement, ObjectNode> {

    public PropertyJsonProcessor(JsonProcessContext jsonProcessContext, PropertyKiteElement element, Expression parentExpression) {
        super(jsonProcessContext, element, parentExpression);
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        Optional<Object> valueOptional = jsonProcessContext.getDataModel().getData(expression);
        if (valueOptional.isPresent()) {
            this.value = valueOptional.get();
            this.node = (ObjectNode) parentProcessor.getNode();
            return true;
        }
        if (!element.isNullHidden()) {
            ((ObjectNode) parentProcessor.getNode()).putNull(element.showNameJSON());
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        final Object convertValue = getConvertValue(value);
        Class<?> convertValueClass = convertValue.getClass();
        if (support(convertValueClass)) {
            handle(this.node, convertValueClass, convertValue, element.showNameJSON());
        }
    }

    /**
     * 获取经过converter转化后的值
     *
     * @return
     */
    protected Object getConvertValue(Object dataValue) {
        return element.getConverterValue().map(converterValue -> {
            Optional<Object> converterOptional = jsonProcessContext.getDataModel().getData(converterValue);
            Object converter = converterOptional.orElseGet(() -> {
                try {
                    return Class.forName(converterValue).newInstance();
                } catch (ClassNotFoundException e) {
                    throw new InvalidArgumentsException("converter", converterValue, "Class not found, and it's also not a expression.");
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new KiteException("Can't new converter instance.");
                }
            });
            if (converter instanceof PropertyConverter) {
                return ((PropertyConverter) converter).convert(dataValue);
            } else {
                throw new InvalidArgumentsException("converter", converterValue, "It's not a PropertyConverter instance.");
            }
        }).orElse(dataValue);
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
