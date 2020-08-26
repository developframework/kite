package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.element.JsonKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.exception.KiteException;

import java.io.IOException;
import java.util.Optional;

/**
 * json节点处理处理器
 *
 * @author qiushui on 2018-12-28.
 */
public class JsonJsonProcessor extends ContentJsonProcessor<JsonKiteElement, ObjectNode> {

    public JsonJsonProcessor(JsonProcessContext jsonProcessContext, JsonKiteElement element) {
        super(jsonProcessContext, element);
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        Optional<Object> valueOptional = getDataValue(parentProcessor);
        if (valueOptional.isPresent()) {
            this.value = valueOptional.get();
            if (!(value instanceof String)) {
                throw new KiteException("json element data \"%s\" is not java.lang.String in template \"%s\".", element.getDataDefinition(), element.getTemplateLocation());
            }
            node = (ObjectNode) parentProcessor.node;
            return true;
        }
        if (!element.isNullHidden()) {
            ((ObjectNode) parentProcessor.node).putNull(showName(parentProcessor));
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        try {
            JsonNode jsonNode = jsonProcessContext.getConfiguration().getObjectMapper().readTree((String) value);
            node.set(showName(parentProcessor), jsonNode);
        } catch (IOException e) {
            throw new KiteException("json element data \"%s\" parse failed in template \"%s\".", element.getDataDefinition(), element.getTemplateLocation());
        }
    }
}
