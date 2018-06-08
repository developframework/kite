package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.element.ContainerKiteElement;
import org.xml.sax.Attributes;

/**
 * 忽略的属性节点解析器
 * @author qiuzhenhao
 */
class IgnorePropertyElementSaxParser extends AbstractElementSaxParser{

    IgnorePropertyElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public String qName() {
        return "property-ignore";
    }

    @Override
    public void handleAtStartElement(ParseContext parseContext, Attributes attributes) {
        final String name = attributes.getValue("name").trim();
        ((ContainerKiteElement) parseContext.getStack().peek()).addIgnoreProperty(name);
    }

    @Override
    public void handleAtEndElement(ParseContext parseContext) {
        // 无操作
    }
}
