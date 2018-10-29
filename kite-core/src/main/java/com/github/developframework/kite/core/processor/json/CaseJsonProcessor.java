package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.element.CaseKiteElement;
import com.github.developframework.kite.core.element.KiteElement;

import java.util.Iterator;

/**
 * case节点Json处理器
 *
 * @author qiushui on 2018-10-28.
 */
public class CaseJsonProcessor extends FunctionalJsonProcessor<CaseKiteElement, ObjectNode> {

    public CaseJsonProcessor(JsonProcessContext jsonProcessContext, CaseKiteElement element, ObjectNode node) {
        super(jsonProcessContext, element, node);
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        for (Iterator<KiteElement> iterator = element.childElementIterator(); iterator.hasNext(); ) {
            final KiteElement childElement = iterator.next();
            final JsonProcessor<? extends KiteElement, ? extends JsonNode> nextProcessor = childElement.createJsonProcessor(jsonProcessContext, node);
            nextProcessor.process(parentProcessor);
        }
    }
}
