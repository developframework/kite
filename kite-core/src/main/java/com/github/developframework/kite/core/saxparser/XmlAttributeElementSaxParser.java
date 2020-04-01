package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.element.XmlAttributeElement;
import org.xml.sax.Attributes;

/**
 * xml属性节点解析器
 *
 * @author qiuzhenhao
 */
class XmlAttributeElementSaxParser extends ContentElementSaxParser<XmlAttributeElement> {

    XmlAttributeElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    protected XmlAttributeElement createElementInstance(ParseContext parseContext, DataDefinition dataDefinition, String alias) {
        return new XmlAttributeElement(kiteConfiguration, parseContext.getCurrentTemplateLocation(), dataDefinition, alias);
    }

    @Override
    protected void addOtherAttributes(XmlAttributeElement element, Attributes attributes) {
        // 无操作
    }

    @Override
    protected void otherOperation(ParseContext parseContext, XmlAttributeElement element) {
        // 无操作
    }

    @Override
    public String qName() {
        return "xml-attribute";
    }
}
