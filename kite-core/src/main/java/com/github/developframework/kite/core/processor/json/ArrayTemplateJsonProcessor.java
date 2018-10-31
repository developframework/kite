package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.developframework.kite.core.element.ArrayKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.Template;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.util.Optional;

/**
 * 数组模板处理器
 *
 * @author qiuzhenhao
 */
public class ArrayTemplateJsonProcessor extends ArrayJsonProcessor {


    public ArrayTemplateJsonProcessor(JsonProcessContext processContext, Template template, ArrayKiteElement arrayElement) {
        super(processContext, arrayElement);
        element.copyChildElement(template);
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        Optional<Object> valueOptional = getDataValue(parentProcessor);
        if (valueOptional.isPresent()) {
            this.value = KiteUtils.objectToArray(valueOptional.get(), element);
            return true;
        }
        return false;
    }
}
