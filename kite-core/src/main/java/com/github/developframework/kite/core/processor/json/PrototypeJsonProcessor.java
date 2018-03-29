package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.dynamic.PropertyConverter;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.PrototypeKiteElement;
import com.github.developframework.kite.core.exception.InvalidArgumentsException;
import com.github.developframework.kite.core.exception.KiteException;

import java.util.Optional;

/**
 * 原型节点处理器
 *
 * @author qiuzhenhao
 */
public class PrototypeJsonProcessor extends ContentJsonProcessor<PrototypeKiteElement, ObjectNode>{

    public PrototypeJsonProcessor(JsonProcessContext jsonProcessContext, PrototypeKiteElement element, Expression parentExpression) {
        super(jsonProcessContext, element, parentExpression);
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        Optional<Object> valueOptional = jsonProcessContext.getDataModel().getData(expression);
        if(valueOptional.isPresent()) {
            this.value = valueOptional.get();
            return true;
        }
        if (!element.isNullHidden()) {
            node.putNull(element.showNameJSON());
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        // 经过converter转化后的值
        Optional<Object> convertValueOptional = element.getConverterValue().map(converterValue -> {
            Optional<Object> converterOptional = jsonProcessContext.getDataModel().getData(converterValue);
            Object obj = converterOptional.orElseGet(() -> {
                try {
                    return Class.forName(converterValue).newInstance();
                } catch (ClassNotFoundException e) {
                    throw new InvalidArgumentsException("converter", converterValue, "Class not found, and it's also not a expression.");
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new KiteException("Can't new converter instance.");
                }
            });
            if (obj instanceof PropertyConverter) {
                return ((PropertyConverter) obj).convert(value);
            } else {
                throw new InvalidArgumentsException("converter", converterValue, "It's not a PropertyConverter instance.");
            }
        });
        final Object convertValue = convertValueOptional.orElse(value);

        ObjectMapper objectMapper = jsonProcessContext.getConfiguration().getObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(convertValue);
        node.set(element.showNameJSON(), jsonNode);
    }
}
