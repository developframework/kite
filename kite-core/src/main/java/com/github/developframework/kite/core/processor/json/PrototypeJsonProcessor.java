package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.PrototypeKiteElement;

import java.util.Optional;

/**
 * 原型节点处理器
 *
 * @author qiuzhenhao
 */
public class PrototypeJsonProcessor extends ContentJsonProcessor<PrototypeKiteElement, ObjectNode>{

    public PrototypeJsonProcessor(JsonProcessContext jsonProcessContext, PrototypeKiteElement element) {
        super(jsonProcessContext, element);
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        Optional<Object> valueOptional = getDataValue(parentProcessor);
        if(valueOptional.isPresent()) {
            value = valueOptional.get();
            return true;
        }
        if (!element.isNullHidden()) {
            node.putNull(showName(parentProcessor));
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        ObjectMapper objectMapper = jsonProcessContext.getConfiguration().getObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(value);
        node.set(showName(parentProcessor), jsonNode);
    }
}
