package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.element.ContainerKiteElement;
import org.dom4j.Node;

/**
 * 容器节点处理器
 *
 * @author qiuzhenhao
 */
public abstract class ContainerXmlProcessor<ELEMENT extends ContainerKiteElement, NODE extends Node> extends ContentXmlProcessor<ELEMENT, NODE> {

    public ContainerXmlProcessor(XmlProcessContext xmlProcessContext, ELEMENT element, Expression parentExpression) {
        super(xmlProcessContext, element, parentExpression);
    }
}
