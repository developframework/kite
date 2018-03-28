package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.ObjectKiteElement;

import java.util.Optional;

/**
 * 副本模板节点处理器
 * @author qiuzhenhao
 */
public class DuplicateTemplateJsonProcessor extends ObjectJsonProcessor{

    public DuplicateTemplateJsonProcessor(JsonProcessContext processContext, ObjectKiteElement element, Expression parentExpression) {
        super(processContext, element, parentExpression);
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        // 始终为true
        return true;
    }

    @Override
    public void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        if (parentProcessor instanceof ObjectInArrayJsonProcessor) {
            ObjectInArrayJsonProcessor processor = new ObjectInArrayJsonProcessor(jsonProcessContext, element, parentProcessor.expression, ((ObjectInArrayJsonProcessor) parentProcessor).size) {

                @Override
                protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
                    Optional<Object> valueOptional = jsonProcessContext.getDataModel().getData(expression);
                    if (valueOptional.isPresent()) {
                        this.value = valueOptional.get();
                        this.node = (ObjectNode) parentProcessor.node;
                        return true;
                    }
                    return false;
                }
            };
            processor.setNode((ObjectNode) parentProcessor.node);
            processor.process(parentProcessor);
        } else {
            super.handleCoreLogic(parentProcessor);
        }
    }
}
