package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.element.FunctionalKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import org.dom4j.Node;

/**
 * 功能型节点处理器
 * @author qiuzhenhao
 */
public abstract class FunctionalXmlProcessor<ELEMENT extends FunctionalKiteElement, NODE extends Node> extends XmlProcessor<ELEMENT, NODE> {


    public FunctionalXmlProcessor(XmlProcessContext xmlProcessContext, ELEMENT element, NODE node, Expression parentExpression) {
        super(xmlProcessContext, element, node, parentExpression);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
        return true;
    }

}
