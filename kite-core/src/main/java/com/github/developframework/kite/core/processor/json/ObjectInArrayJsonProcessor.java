package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.ObjectKiteElement;
import lombok.Getter;

/**
 * 数组元素处理器
 *
 * @author qiuzhenhao
 */
@Getter
public class ObjectInArrayJsonProcessor extends ObjectJsonProcessor {

    protected int index;

    protected int size;

    public ObjectInArrayJsonProcessor(JsonProcessContext context, ObjectKiteElement element, int index, int size) {
        super(context, element);
        this.index = index;
        this.size = size;
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        if (value != null) {
            this.node = jsonProcessContext.getConfiguration().getObjectMapper().createObjectNode();
            return true;
        }
        return false;
    }
}
