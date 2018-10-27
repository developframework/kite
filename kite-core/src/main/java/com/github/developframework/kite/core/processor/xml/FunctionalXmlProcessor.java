package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.element.FunctionalKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import org.dom4j.Element;

/**
 * 功能型节点处理器
 * @author qiuzhenhao
 */
public abstract class FunctionalXmlProcessor<ELEMENT extends FunctionalKiteElement, NODE extends Element> extends XmlProcessor<ELEMENT, NODE> {


    public FunctionalXmlProcessor(XmlProcessContext xmlProcessContext, ELEMENT element, NODE node) {
        super(xmlProcessContext, element, node);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        return true;
    }

}
