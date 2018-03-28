package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.ObjectKiteElement;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * 虚拟对象节点处理器
 *
 * @author qiuzhenhao
 */
public class VirtualObjectXmlProcessor extends ObjectXmlProcessor {

    public VirtualObjectXmlProcessor(XmlProcessContext context, ObjectKiteElement element, Expression parentExpression) {
        super(context, element, parentExpression);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
        this.node = ((Element) parentProcessor.getNode()).addElement(element.showName());
        // 始终为true
        return true;
    }
}
