package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.dynamic.Condition;
import com.github.developframework.kite.core.element.IfKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.util.Iterator;

/**
 * if节点处理器
 *
 * @author qiuzhenhao
 */
public class IfJsonProcessor extends FunctionalJsonProcessor<IfKiteElement, ObjectNode> {

    public IfJsonProcessor(JsonProcessContext jsonProcessContext, IfKiteElement element, ObjectNode node) {
        super(jsonProcessContext, element, node);
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        Condition condition = KiteUtils.getComponentInstance(jsonProcessContext.getDataModel(), element.getConditionValue(), Condition.class, "condition");
        boolean verifyResult = condition.verify(jsonProcessContext.getDataModel(), parentProcessor.value);
        if (verifyResult) {
            // 执行if
            executeIfTrue(parentProcessor);
        } else {
            // 执行else
            executeIfFalse(parentProcessor);
        }
    }

    /**
     * 执行条件真的逻辑
     *
     * @param parentProcessor
     */
    private void executeIfTrue(final ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        for (Iterator<KiteElement> iterator = element.childElementIterator(); iterator.hasNext(); ) {
            final KiteElement childElement = iterator.next();
            final JsonProcessor<? extends KiteElement, ? extends JsonNode> nextProcessor = childElement.createJsonProcessor(jsonProcessContext, node);
            nextProcessor.process(parentProcessor);
        }
    }

    /**
     * 执行条件假的逻辑
     *
     * @param parentProcessor
     */
    private void executeIfFalse(final ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        element.getElseElement().ifPresent(elseElement -> {
            JsonProcessor<? extends KiteElement, ? extends JsonNode> nextProcessor = elseElement.createJsonProcessor(jsonProcessContext, (ObjectNode) parentProcessor.getNode());
            nextProcessor.process(parentProcessor);
        });
    }

}
