package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.developframework.expression.ArrayExpression;
import com.github.developframework.expression.Expression;
import com.github.developframework.expression.ObjectExpression;
import com.github.developframework.kite.core.element.ContentKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.LinkKiteElement;
import com.github.developframework.kite.core.exception.InvalidArgumentsException;
import com.github.developframework.kite.core.exception.LinkSizeNotEqualException;

import java.util.Collection;
import java.util.Optional;

/**
 * 一对一链接节点处理器
 *
 * @author qiuzhenhao
 */
public class LinkJsonProcessor extends ObjectJsonProcessor {

    public LinkJsonProcessor(JsonProcessContext jsonProcessContext, LinkKiteElement element, Expression parentExpression) {
        super(jsonProcessContext, element, parentExpression);
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        final ObjectInArrayJsonProcessor objectInArrayProcessor = (ObjectInArrayJsonProcessor) parentProcessor;
        Optional<Object> valueOptional = jsonProcessContext.getDataModel().getData(expression);
        if (valueOptional.isPresent()) {
            this.value = valueOptional.get();
            int size;
            if (value.getClass().isArray()) {
                size = ((Object[]) value).length;
            } else if (value instanceof Collection<?>) {
                size = ((Collection<?>) value).size();
            } else {
                throw new InvalidArgumentsException("data", expression.toString(), "Data must be array or List type.");
            }

            if (size != objectInArrayProcessor.getSize()) {
                throw new LinkSizeNotEqualException(element.getNamespace(), element.getTemplateId());
            }
            return true;
        }
        if (!element.isNullHidden()) {
            node.putNull(element.showName());
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        final ObjectInArrayJsonProcessor objectInArrayProcessor = (ObjectInArrayJsonProcessor) parentProcessor;
        final ArrayExpression arrayExpression = (ArrayExpression) objectInArrayProcessor.getExpression();
        ArrayExpression targetExpression = new ArrayExpression(((ObjectExpression) expression).getPropertyName(), arrayExpression.getIndex());
        ContentKiteElement contentElement = ((LinkKiteElement) element).createProxyContentElement();
        JsonProcessor<? extends KiteElement, ? extends JsonNode> nextProcessor = contentElement.createJsonProcessor(jsonProcessContext, objectInArrayProcessor.node, targetExpression);
        nextProcessor.process(parentProcessor);
    }
}
