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
            this.value = valueOptional.get();
            this.node = parentProcessor.node.addElement(element.showNameXML());
            return true;
        }
        if (!element.isNullHidden()) {
            parentProcessor.node.addElement(element.showNameXML());
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        for (Iterator<KiteElement> iterator = element.childElementIterator(); iterator.hasNext();) {
            final KiteElement childKiteElement = iterator.next();
            final XmlProcessor<? extends KiteElement, ? extends Element> childXmlProcessor = childKiteElement.createXmlProcessor(xmlProcessContext, node);
            childXmlProcessor.process(this);
        }
    }
}
