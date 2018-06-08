package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.element.ContentKiteElement;
import org.dom4j.Node;

/**
 * 内容节点处理器
 *
 * @author qiuzhenhao
 */
public abstract class ContentXmlProcessor<ELEMENT extends ContentKiteElement, NODE extends Node> extends XmlProcessor<ELEMENT, NODE> {

    public ContentXmlProcessor(XmlProcessContext xmlProcessContext, ELEMENT element, Expression parentExpression) {
        super(xmlProcessContext, element, null, parentExpression);
    }

}
