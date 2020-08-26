package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.element.ContentKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.ThisKiteElement;
import com.github.developframework.kite.core.utils.KiteUtils;

/**
 * 指代自身的Json处理器
 *
 * @author qiushui on 2018-10-28.
 */
public class ThisJsonProcessor extends ContainerJsonProcessor<ThisKiteElement, ObjectNode> {

    public ThisJsonProcessor(JsonProcessContext jsonProcessContext, ThisKiteElement element) {
        super(jsonProcessContext, element);
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<?, ?> parentProcessor) {
        value = KiteUtils.handleKiteConverter(jsonProcessContext.getDataModel(), element.getConverterValue(), parentProcessor.value);
        if (value == null && !element.isNullHidden()) {
            ((ObjectNode) parentProcessor.node).putNull(showName(parentProcessor));
            return false;
        }
        return true;
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        ContentKiteElement contentElement = KiteUtils.isArrayOrCollection(value) ? element.createProxyArrayElement() : element.createProxyObjectElement();
        JsonProcessor<? extends KiteElement, ? extends JsonNode> nextProcessor = contentElement.createJsonProcessor(jsonProcessContext, (ObjectNode) parentProcessor.node);
        nextProcessor.value = value;
        nextProcessor.process(parentProcessor);
    }
}
