package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.element.EnumPropertyKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import org.xml.sax.Attributes;

/**
 * enum节点解析器
 *
 * @author qiushui on 2020-03-31.
 */
class EnumElementSaxParser implements ElementSaxParser {

    @Override
    public String qName() {
        return "enum";
    }

    @Override
    public void handleAtStartElement(ParseContext parseContext, Attributes attributes) {
        KiteElement parentKiteElement = parseContext.getStack().peek();
        if (parentKiteElement instanceof EnumPropertyKiteElement) {
            ((EnumPropertyKiteElement) parentKiteElement).putEnumText(attributes.getValue("value"), attributes.getValue("text"));
        }
    }

    @Override
    public void handleAtEndElement(ParseContext parseContext) {
        // 无操作
    }
}
