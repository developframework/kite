package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.developframework.kite.core.element.ContentKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.LinkKiteElement;
import com.github.developframework.kite.core.exception.LinkSizeNotEqualException;
import com.github.developframework.kite.core.utils.KiteUtils;
import lombok.Setter;

import java.util.Optional;

/**
 * 一对一链接节点处理器
 *
 * @author qiuzhenhao
 */
public class LinkJsonProcessor extends ObjectJsonProcessor {

    @Setter
    private int index;

    public LinkJsonProcessor(JsonProcessContext jsonProcessContext, LinkKiteElement element) {
        super(jsonProcessContext, element);
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        index = ((ObjectInArrayJsonProcessor) parentProcessor).getIndex();
        Optional<Object> valueOptional = getDataValue(parentProcessor);
        if (valueOptional.isPresent()) {
            Object[] array = KiteUtils.objectToArray(valueOptional.get(), element);
            if (array.length != ((ObjectInArrayJsonProcessor) parentProcessor).getSize()) {
                throw new LinkSizeNotEqualException(element.getTemplateLocation());
            }
            value = array[index];
            return true;
        }
        if (!element.isNullHidden()) {
            node.putNull(element.showNameJSON());
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        ContentKiteElement contentElement = ((LinkKiteElement) element).createProxyContentElement(value.getClass());
        JsonProcessor<? extends KiteElement, ? extends JsonNode> nextProcessor = contentElement.createJsonProcessor(jsonProcessContext, ((ObjectInArrayJsonProcessor) parentProcessor).node);
        nextProcessor.setValue(value);
        nextProcessor.process(parentProcessor);
    }
}
