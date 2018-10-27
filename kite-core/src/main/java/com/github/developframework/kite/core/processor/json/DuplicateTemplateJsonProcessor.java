package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.ObjectKiteElement;

import java.util.Iterator;

/**
 * 副本模板节点处理器
 * @author qiuzhenhao
 */
public class DuplicateTemplateJsonProcessor extends ObjectJsonProcessor{

    public DuplicateTemplateJsonProcessor(JsonProcessContext processContext, ObjectKiteElement element) {
        super(processContext, element);
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        // 始终为true
        return true;
    }

    @Override
    public void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        for (Iterator<KiteElement> iterator = element.childElementIterator(); iterator.hasNext(); ) {
            final KiteElement childKiteElement = iterator.next();
            final JsonProcessor<? extends KiteElement, ? extends JsonNode> childJsonProcessor = childKiteElement.createJsonProcessor(jsonProcessContext, node);
            if (parentProcessor instanceof ObjectInArrayJsonProcessor) {
                childJsonProcessor.process(parentProcessor);
            } else {
                childJsonProcessor.process(this);
            }
        }
    }
}
