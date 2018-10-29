package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.element.ElseKiteElement;
import org.xml.sax.Attributes;

/**
 * else节点解析器
 * @author qiuzhenhao
 */
class ElseElementSaxParser extends AbstractElementSaxParser{

    ElseElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public String qName() {
        return "else";
    }

    @Override
    public void handleAtStartElement(ParseContext parseContext, Attributes attributes) {
        final ElseKiteElement elseElement = new ElseKiteElement(kiteConfiguration, parseContext.getCurrentTemplateLocation());
        parseContext.getCurrentIfElement().setElseElement(elseElement);
        parseContext.getStack().push(elseElement);
    }

    @Override
    public void handleAtEndElement(ParseContext parseContext) {
        parseContext.getStack().pop();
    }
}
