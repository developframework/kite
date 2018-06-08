package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.dynamic.Condition;
import com.github.developframework.kite.core.element.IfKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.exception.KiteException;
import org.dom4j.Node;

import java.util.Iterator;
import java.util.Optional;

/**
 * if节点处理器
 *
 * @author qiuzhenhao
 */
public class IfXmlProcessor extends FunctionalXmlProcessor<IfKiteElement, Node> {

    public IfXmlProcessor(XmlProcessContext xmlProcessContext, IfKiteElement element, Node node, Expression parentExpression) {
        super(xmlProcessContext, element, node, parentExpression);
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
        final String conditionValue = element.getConditionValue();
        Optional<Object> conditionOptional = xmlProcessContext.getDataModel().getData(conditionValue);
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
            verifyResult = ((Condition) condition).verify(xmlProcessContext.getDataModel(), parentProcessor.getExpression());
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
    private void executeIfTrue(final ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
        for (Iterator<KiteElement> iterator = element.childElementIterator(); iterator.hasNext(); ) {
            final KiteElement childElement = iterator.next();
            final XmlProcessor<? extends KiteElement, ? extends Node> nextProcessor = childElement.createXmlProcessor(xmlProcessContext, node, expression);
            nextProcessor.process(parentProcessor);
        }
    }

    /**
     * 执行条件假的逻辑
     *
     * @param parentProcessor
     */
    private void executeIfFalse(final ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
        element.getElseElement().ifPresent(elseElement -> {
            XmlProcessor<? extends KiteElement, ? extends Node> elseProcessor = elseElement.createXmlProcessor(xmlProcessContext, parentProcessor.getNode(), parentProcessor.getExpression());
            elseProcessor.process(parentProcessor);
        });
    }

}
