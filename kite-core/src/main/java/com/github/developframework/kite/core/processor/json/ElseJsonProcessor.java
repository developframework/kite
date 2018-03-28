package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.element.ElseKiteElement;
import com.github.developframework.kite.core.element.KiteElement;

import java.util.Iterator;

/**
 * Else节点处理器
 * @author qiuzhenhao
 */
public class ElseJsonProcessor extends FunctionalJsonProcessor<ElseKiteElement, ObjectNode>{

    public ElseJsonProcessor(JsonProcessContext jsonProcessContext, ElseKiteElement element, ObjectNode node, Expression parentExpression) {
        super(jsonProcessContext, element, node, parentExpression);
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        for (Iterator<KiteElement> iterator = element.childElementIterator(); iterator.hasNext();) {
            final KiteElement childElement = iterator.next();
            final JsonProcessor<? extends KiteElement, ? extends JsonNode> nextProcessor = childElement.createJsonProcessor(jsonProcessContext, node, expression);
            nextProcessor.process(parentProcessor);
        }
    }
}
