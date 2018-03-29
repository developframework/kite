package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.ObjectKiteElement;

import java.util.Iterator;
import java.util.Optional;

/**
 * 对象节点处理器
 *
 * @author qiuzhenhao
 */
public class ObjectJsonProcessor extends ContainerJsonProcessor<ObjectKiteElement, ObjectNode> {

    public ObjectJsonProcessor(JsonProcessContext jsonProcessContext, ObjectKiteElement element, Expression parentExpression) {
        super(jsonProcessContext, element,  parentExpression);
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        Optional<Object> valueOptional = jsonProcessContext.getDataModel().getData(expression);
        if (valueOptional.isPresent()) {
            this.value = valueOptional.get();
            this.node = ((ObjectNode) parentProcessor.getNode()).putObject(element.showNameJSON());
            return true;
        }
        if (!element.isNullHidden()) {
            node.putNull(element.showNameJSON());
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        for (Iterator<KiteElement> iterator = element.childElementIterator(); iterator.hasNext();) {
            final KiteElement childKiteElement = iterator.next();
            final JsonProcessor<? extends KiteElement, ? extends JsonNode> childJsonProcessor = childKiteElement.createJsonProcessor(jsonProcessContext, node, expression);
            childJsonProcessor.process(this);
        }
    }
}
