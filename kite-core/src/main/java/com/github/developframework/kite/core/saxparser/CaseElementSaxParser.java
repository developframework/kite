package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.element.CaseKiteElement;
import org.xml.sax.Attributes;

/**
 * case节点解析器
 *
 * @author qiushui on 2018-10-28.
 */
class CaseElementSaxParser extends AbstractElementSaxParser {

    CaseElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public String qName() {
        return "case";
    }

    @Override
    public void handleAtStartElement(ParseContext parseContext, Attributes attributes) {
        final String testValue = attributes.getValue("test");
        CaseKiteElement caseKiteElement = new CaseKiteElement(kiteConfiguration, parseContext.getCurrentTemplateLocation(), testValue);
        parseContext.getCurrentSwitchElement().putCase(testValue, caseKiteElement);
        parseContext.getStack().push(caseKiteElement);
    }

    @Override
    public void handleAtEndElement(ParseContext parseContext) {
        parseContext.getStack().pop();
    }
}
