package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.ObjectKiteElement;
import lombok.Getter;

import java.util.Optional;

/**
 * 数组元素处理器
 *
 * @author qiuzhenhao
 */
public class ObjectInArrayJsonProcessor extends ObjectJsonProcessor {

    @Getter
    protected int size;

    public ObjectInArrayJsonProcessor(JsonProcessContext context, ObjectKiteElement element, Expression parentExpression, int size) {
        super(context, element, parentExpression);
        this.size = size;
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        Optional<Object> valueOptional = jsonProcessContext.getDataModel().getData(expression);
        if (valueOptional.isPresent()) {
            this.value = valueOptional.get();
            this.node = jsonProcessContext.getConfiguration().getObjectMapper().createObjectNode();
            return true;
        }
        return false;
    }
}
