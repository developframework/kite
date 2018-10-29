package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.element.IfKiteElement;
import org.xml.sax.Attributes;

/**
 * if节点解析器
 * @author qiuzhenhao
 */
class IfElementSaxParser extends AbstractElementSaxParser {

    IfElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public String qName() {
        return "if";
    }

    @Override
    public void handleAtStartElement(ParseContext parseContext, Attributes attributes) {
        final String condition = attributes.getValue("condition").trim();
        final IfKiteElement ifElement = new IfKiteElement(kiteConfiguration, parseContext.getCurrentTemplate().getNamespace(), parseContext.getCurrentTemplate().getTemplateId(), condition);
        parseContext.setCurrentIfElement(ifElement);
        addChildElement(parseContext, ifElement);
        parseContext.getStack().push(ifElement);
    }

    @Override
    public void handleAtEndElement(ParseContext parseContext) {
        parseContext.getStack().pop();
    }
}
