package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.dynamic.Condition;
import com.github.developframework.kite.core.element.IfKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.utils.KiteUtils;
import org.dom4j.Element;

import java.util.Iterator;

/**
 * if节点处理器
 *
 * @author qiuzhenhao
 */
public class IfXmlProcessor extends FunctionalXmlProcessor<IfKiteElement, Element> {

    public IfXmlProcessor(XmlProcessContext xmlProcessContext, IfKiteElement element, Element node) {
        super(xmlProcessContext, element, node);
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        Condition condition = KiteUtils.getComponentInstance(xmlProcessContext.getDataModel(), element.getConditionValue(), Condition.class, "condition");
        boolean verifyResult = condition.verify(xmlProcessContext.getDataModel(), parentProcessor.value);
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
    private void executeIfTrue(final ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        for (Iterator<KiteElement> iterator = element.childElementIterator(); iterator.hasNext(); ) {
            final KiteElement childElement = iterator.next();
            final XmlProcessor<? extends KiteElement, ? extends Element> nextProcessor = childElement.createXmlProcessor(xmlProcessContext, node);
            nextProcessor.process(parentProcessor);
        }
    }

    /**
     * 执行条件假的逻辑
     *
     * @param parentProcessor
     */
    private void executeIfFalse(final ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        element.getElseElement().ifPresent(elseElement -> {
            XmlProcessor<? extends KiteElement, ? extends Element> elseProcessor = elseElement.createXmlProcessor(xmlProcessContext, parentProcessor.getNode());
            elseProcessor.process(parentProcessor);
        });
    }

}
