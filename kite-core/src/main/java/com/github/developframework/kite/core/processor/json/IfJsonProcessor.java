package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.dynamic.Condition;
import com.github.developframework.kite.core.element.IfKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.exception.KiteException;

import java.util.Iterator;
import java.util.Optional;

/**
 * if节点处理器
 *
 * @author qiuzhenhao
 */
public class IfJsonProcessor extends FunctionalJsonProcessor<IfKiteElement, ObjectNode> {

    public IfJsonProcessor(JsonProcessContext jsonProcessContext, IfKiteElement element, ObjectNode node, Expression parentExpression) {
        super(jsonProcessContext, element, node, parentExpression);
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        final String conditionValue = element.getConditionValue();
        Optional<Object> conditionOptional = jsonProcessContext.getDataModel().getData(conditionValue);
        Object condition = conditionOptional.orElseGet(() -> {
            try {
                return Class.forName(conditionValue).newInstance();
            } catch (ClassNotFoundException e) {
                throw new KiteException("The condition's Class \"%s\" not found, and it's also not a expression.", conditionValue);
            } catch (IllegalAccessException | InstantiationException e) {
                throw new KiteException("Can't new condition instance.");
            }
        });
        boolean verifyResult;
        if (condition instanceof Boolean) {
            verifyResult = ((Boolean) condition).booleanValue();
        } else if (condition instanceof Condition) {
            // 验证条件
            verifyResult = ((Condition) condition).verify(jsonProcessContext.getDataModel(), parentProcessor.getExpression());
        } else {
            throw new KiteException("The expression \"%s\" is not Condition instance.", element.getConditionValue());
        }
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
            final JsonProcessor<? extends KiteElement, ? extends JsonNode> nextProcessor = childElement.createJsonProcessor(jsonProcessContext, node, expression);
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
            JsonProcessor<? extends KiteElement, ? extends JsonNode> elseProcessor = elseElement.createJsonProcessor(jsonProcessContext, (ObjectNode) parentProcessor.getNode(), parentProcessor.getExpression());
            elseProcessor.process(parentProcessor);
        });
    }

}
