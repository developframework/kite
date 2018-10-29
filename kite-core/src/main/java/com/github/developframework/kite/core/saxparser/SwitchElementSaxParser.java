package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.element.SwitchKiteElement;
import org.xml.sax.Attributes;

/**
 * switch节点解析器
 *
 * @author qiushui on 2018-10-29.
 */
public class SwitchElementSaxParser extends AbstractElementSaxParser {


    public SwitchElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public String qName() {
        return "switch";
    }

    @Override
    public void handleAtStartElement(ParseContext parseContext, Attributes attributes) {
        final DataDefinition dataDefinition = new DataDefinition(attributes.getValue("data"));
        SwitchKiteElement switchKiteElement = new SwitchKiteElement(kiteConfiguration, parseContext.getCurrentTemplateLocation(), dataDefinition);
        parseContext.setCurrentSwitchElement(switchKiteElement);
        addChildElement(parseContext, switchKiteElement);
        parseContext.getStack().push(switchKiteElement);
    }

    @Override
    public void handleAtEndElement(ParseContext parseContext) {
        parseContext.getStack().pop();
    }
}
