package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.element.ContainerKiteElement;
import org.xml.sax.Attributes;

/**
 * 容器节点解析器
 * @author qiuzhenhao
 * @param <T> 容器节点类型
 */
abstract class ContainerElementSaxParser<T extends ContainerKiteElement> extends ContentElementSaxParser<T> {

    ContainerElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public void handleAtEndElement(ParseContext parseContext) {
        ((ContainerKiteElement) parseContext.getStack().pop()).loadForClassAllProperty();
    }

    @Override
    protected void addOtherAttributes(T element, Attributes attributes) {
        element.setNullHidden(attributes.getValue("null-hidden"));
        element.setForClass(attributes.getValue("for-class"));
    }

    @Override
    protected void otherOperation(ParseContext parseContext, T element) {
        parseContext.getStack().push(element);
    }
}
