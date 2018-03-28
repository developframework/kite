package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.ObjectKiteElement;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.Iterator;
import java.util.Optional;

/**
 * 对象节点处理器
 *
 * @author qiuzhenhao
 */
public class ObjectXmlProcessor extends ContainerXmlProcessor<ObjectKiteElement, Element> {

    public ObjectXmlProcessor(XmlProcessContext xmlProcessContext, ObjectKiteElement element, Expression parentExpression) {
        super(xmlProcessContext, element,  parentExpression);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
        Optional<Object> valueOptional = xmlProcessContext.getDataModel().getData(expression);
        if (valueOptional.isPresent()) {
            this.value = valueOptional.get();
            this.node = ((Element) parentProcessor.getNode()).addElement(element.showName());
            return true;
        }
        if (!element.isNullHidden()) {
            node.addElement(element.showName());
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
        for (Iterator<KiteElement> iterator = element.childElementIterator(); iterator.hasNext();) {
            final KiteElement childKiteElement = iterator.next();
            final XmlProcessor<? extends KiteElement, ? extends Node> childXmlProcessor = childKiteElement.createXmlProcessor(xmlProcessContext, node, expression);
            childXmlProcessor.process(this);
        }
    }
}
