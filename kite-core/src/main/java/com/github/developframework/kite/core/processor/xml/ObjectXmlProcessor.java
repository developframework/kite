package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.ObjectKiteElement;
import org.dom4j.Element;

import java.util.Iterator;
import java.util.Optional;

/**
 * 对象节点处理器
 *
 * @author qiuzhenhao
 */
public class ObjectXmlProcessor extends ContainerXmlProcessor<ObjectKiteElement, Element> {

    public ObjectXmlProcessor(XmlProcessContext xmlProcessContext, ObjectKiteElement element) {
        super(xmlProcessContext, element);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        Optional<Object> valueOptional = getDataValue(parentProcessor);
        if (valueOptional.isPresent()) {
            value = valueOptional.get();
            node = parentProcessor.node.addElement(showName(parentProcessor));
            return true;
        }
        if (!element.isNullHidden()) {
            parentProcessor.node.addElement(showName(parentProcessor));
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        for (Iterator<KiteElement> iterator = element.childElementIterator(); iterator.hasNext();) {
            final KiteElement childKiteElement = iterator.next();
            final XmlProcessor<? extends KiteElement, ? extends Element> nextProcessor = childKiteElement.createXmlProcessor(xmlProcessContext, node);
            nextProcessor.process(this);
        }
    }
}
