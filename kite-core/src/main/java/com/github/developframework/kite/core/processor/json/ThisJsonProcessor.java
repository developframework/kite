package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.ExpressionUtils;
import com.github.developframework.kite.core.dynamic.KiteConverter;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.ThisKiteElement;
import com.github.developframework.kite.core.utils.KiteUtils;

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
        if (value != null) {
            node = ((ObjectNode) parentProcessor.node).putObject(element.showNameJSON());
            return true;
        }
        if (!element.isNullHidden()) {
            ((ObjectNode) parentProcessor.node).putNull(element.showNameJSON());
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor parentProcessor) {
        for (Iterator<KiteElement> iterator = element.childElementIterator(); iterator.hasNext(); ) {
            final KiteElement childKiteElement = iterator.next();
            final JsonProcessor<? extends KiteElement, ? extends JsonNode> childJsonProcessor = childKiteElement.createJsonProcessor(jsonProcessContext, node);
            childJsonProcessor.process(this);
        }
    }
}
