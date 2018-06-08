package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.element.ElseKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import org.dom4j.Node;

import java.util.Iterator;

/**
 * Else节点处理器
 * @author qiuzhenhao
 */
public class ElseXmlProcessor extends FunctionalXmlProcessor<ElseKiteElement, Node>{

    public ElseXmlProcessor(XmlProcessContext xmlProcessContext, ElseKiteElement element, Node node, Expression parentExpression) {
        super(xmlProcessContext, element, node, parentExpression);
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
        for (Iterator<KiteElement> iterator = element.childElementIterator(); iterator.hasNext();) {
            final KiteElement childElement = iterator.next();
            final XmlProcessor<? extends KiteElement, ? extends Node> nextProcessor = childElement.createXmlProcessor(xmlProcessContext, node, expression);
            nextProcessor.process(parentProcessor);
        }
    }
}
