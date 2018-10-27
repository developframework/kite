package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.element.ElseKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import org.dom4j.Element;

import java.util.Iterator;

/**
 * Else节点处理器
 * @author qiuzhenhao
 */
public class ElseXmlProcessor extends FunctionalXmlProcessor<ElseKiteElement, Element> {

    public ElseXmlProcessor(XmlProcessContext xmlProcessContext, ElseKiteElement element, Element node) {
        super(xmlProcessContext, element, node);
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        for (Iterator<KiteElement> iterator = element.childElementIterator(); iterator.hasNext();) {
            final KiteElement childElement = iterator.next();
            final XmlProcessor<? extends KiteElement, ? extends Element> nextProcessor = childElement.createXmlProcessor(xmlProcessContext, node);
            nextProcessor.process(parentProcessor);
        }
    }
}
