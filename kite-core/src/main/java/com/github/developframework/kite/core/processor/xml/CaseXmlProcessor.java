package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.element.CaseKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import org.dom4j.Element;

import java.util.Iterator;

/**
 * case节点Xml处理器
 *
 * @author qiushui on 2018-10-28.
 */
public class CaseXmlProcessor extends FunctionalXmlProcessor<CaseKiteElement, Element> {

    public CaseXmlProcessor(XmlProcessContext xmlProcessContext, CaseKiteElement element, Element parentNode) {
        super(xmlProcessContext, element, parentNode);
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        for (Iterator<KiteElement> iterator = element.childElementIterator(); iterator.hasNext(); ) {
            final KiteElement childElement = iterator.next();
            final XmlProcessor<? extends KiteElement, ? extends Element> nextProcessor = childElement.createXmlProcessor(xmlProcessContext, node);
            nextProcessor.process(parentProcessor);
        }
    }
}
