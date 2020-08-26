package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.ObjectKiteElement;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Optional;

/**
 * 对象节点处理器
 *
 * @author qiuzhenhao
 */
@Slf4j
public class ObjectJsonProcessor extends ContainerJsonProcessor<ObjectKiteElement, ObjectNode> {

    public ObjectJsonProcessor(JsonProcessContext jsonProcessContext, ObjectKiteElement element) {
        super(jsonProcessContext, element);
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        Optional<Object> valueOptional = getDataValue(parentProcessor);
        if (valueOptional.isPresent()) {
            this.value = valueOptional.get();
            this.node = ((ObjectNode) parentProcessor.node).putObject(showName(parentProcessor));
            return true;
        }
        if (!element.isNullHidden()) {
            ((ObjectNode) parentProcessor.node).putNull(showName(parentProcessor));
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        for (Iterator<KiteElement> iterator = element.childElementIterator(); iterator.hasNext();) {
            final KiteElement childKiteElement = iterator.next();
            final JsonProcessor<? extends KiteElement, ? extends JsonNode> nextProcessor = childKiteElement.createJsonProcessor(jsonProcessContext, node);
            nextProcessor.process(this);
        }
    }
}
