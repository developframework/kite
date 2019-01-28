package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.ExpressionUtils;
import com.github.developframework.kite.core.dynamic.KiteConverter;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.ThisKiteElement;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.math.BigDecimal;
import java.util.Iterator;

/**
 * 指代自身的Json处理器
 *
 * @author qiushui on 2018-10-28.
 */
public class ThisJsonProcessor extends ContainerJsonProcessor<ThisKiteElement, ObjectNode> {

    public ThisJsonProcessor(JsonProcessContext jsonProcessContext, ThisKiteElement element) {
        super(jsonProcessContext, element);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected boolean prepare(ContentJsonProcessor parentProcessor) {
        if (element.getConverterValue().isPresent()) {
            String converterValue = element.getConverterValue().get();
            if (converterValue.startsWith("this.")) {
                // 简单表达式
                value = ExpressionUtils.getValue(parentProcessor.value, converterValue.substring(5));
            } else {
                KiteConverter converter = KiteUtils.getComponentInstance(jsonProcessContext.getDataModel(), converterValue, KiteConverter.class, "converter");
                value = converter.convert(parentProcessor.value);
            }
        } else {
            value = parentProcessor.value;
        }
        if (element.isChildElementEmpty()) {
            node = (ObjectNode) parentProcessor.node;
            return true;
        } else {
            if (value != null) {
                node = ((ObjectNode) parentProcessor.node).putObject(element.showNameJSON());
                return true;
            }
            if (!element.isNullHidden()) {
                ((ObjectNode) parentProcessor.node).putNull(element.showNameJSON());
            }
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor parentProcessor) {
        if (element.isChildElementEmpty()) {
            empty(node, value);
        } else {
            for (Iterator<KiteElement> iterator = element.childElementIterator(); iterator.hasNext(); ) {
                final KiteElement childKiteElement = iterator.next();
                final JsonProcessor<? extends KiteElement, ? extends JsonNode> childJsonProcessor = childKiteElement.createJsonProcessor(jsonProcessContext, node);
                childJsonProcessor.process(this);
            }
        }
    }

    /**
     * 空子标签处理
     *
     * @param node      节点
     * @param itemValue 数组元素值
     */
    private void empty(ObjectNode node, Object itemValue) {
        String showName = element.showNameJSON();
        if (itemValue == null) {
            if (!element.isNullHidden()) {
                node.putNull(showName);
            }
            return;
        }
        if (itemValue instanceof String) {
            node.put(showName, (String) itemValue);
        } else if (itemValue instanceof Integer) {
            node.put(showName, (Integer) itemValue);
        } else if (itemValue instanceof Long) {
            node.put(showName, (Long) itemValue);
        } else if (itemValue instanceof Short) {
            node.put(showName, (Short) itemValue);
        } else if (itemValue instanceof Boolean) {
            node.put(showName, (Boolean) itemValue);
        } else if (itemValue instanceof Float) {
            node.put(showName, (Float) itemValue);
        } else if (itemValue instanceof Double) {
            node.put(showName, (Double) itemValue);
        } else if (itemValue instanceof BigDecimal) {
            node.put(showName, (BigDecimal) itemValue);
        } else if (itemValue instanceof Character) {
            node.put(showName, (Character) itemValue);
        } else if (itemValue instanceof Byte) {
            node.put(showName, (Byte) itemValue);
        } else {
            node.put(showName, itemValue.toString());
        }
    }
}
