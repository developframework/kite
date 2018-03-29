package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.ObjectKiteElement;

/**
 * 虚拟对象节点处理器
 *
 * @author qiuzhenhao
 */
public class VirtualObjectJsonProcessor extends ObjectJsonProcessor {

    public VirtualObjectJsonProcessor(JsonProcessContext context, ObjectKiteElement element, Expression parentExpression) {
        super(context, element, parentExpression);
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        this.node = ((ObjectNode) parentProcessor.getNode()).putObject(element.showNameJSON());
        // 始终为true
        return true;
    }
}
