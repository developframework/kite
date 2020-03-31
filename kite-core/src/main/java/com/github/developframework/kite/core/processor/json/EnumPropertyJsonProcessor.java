package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.element.EnumPropertyKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.exception.KiteException;

import java.util.Optional;

/**
 * 枚举属性处理器
 *
 * @author qiuzhenhao on 2020-03-31.
 */
public class EnumPropertyJsonProcessor extends ContentJsonProcessor<EnumPropertyKiteElement, ObjectNode> {

    public EnumPropertyJsonProcessor(JsonProcessContext jsonProcessContext, EnumPropertyKiteElement element) {
        super(jsonProcessContext, element);
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        Optional<Object> valueOptional = getDataValue(parentProcessor);
        if (valueOptional.isPresent()) {
            value = valueOptional.get();
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
        String enumValue = value.toString();
        String enumText = element.getEnumText(enumValue);
        if (enumText == null) {
            throw new KiteException("No enum value for \"%s\" in template \"%s\".", enumValue, element.getTemplateLocation());
        }
        node.put(element.showNameJSON(), enumText);
    }
}
