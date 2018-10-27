package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.ObjectKiteElement;
import org.dom4j.Element;

import java.util.Iterator;

/**
 * 副本模板节点处理器
 * @author qiuzhenhao
 */
public class DuplicateTemplateXmlProcessor extends ObjectXmlProcessor{

    public DuplicateTemplateXmlProcessor(XmlProcessContext jsonProcessContext, ObjectKiteElement element) {
        super(jsonProcessContext, element);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        // 始终为true
        return true;
    }

    @Override
    public void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        for (Iterator<KiteElement> iterator = element.childElementIterator(); iterator.hasNext(); ) {
            final KiteElement childKiteElement = iterator.next();
            final XmlProcessor<? extends KiteElement, ? extends Element> nextProcessor = childKiteElement.createXmlProcessor(xmlProcessContext, node);
            if (parentProcessor instanceof ObjectInArrayXmlProcessor) {
                nextProcessor.process(parentProcessor);
            } else {
                nextProcessor.process(this);
            }
        }
    }
}
