package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.element.NormalPropertyKiteElement;
import com.github.developframework.kite.core.element.PropertyKiteElement;
import org.xml.sax.Attributes;

/**
 * 属性节点解析器
 * @author qiuzhenhao
 */
class PropertyElementSaxParser extends ContentElementSaxParser<PropertyKiteElement>{

    PropertyElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public String qName() {
        return "property";
    }

    @Override
    protected PropertyKiteElement createElementInstance(ParseContext parseContext, DataDefinition dataDefinition, String alias) {
        return new NormalPropertyKiteElement(kiteConfiguration, parseContext.getCurrentTemplate().getNamespace(), parseContext.getCurrentTemplate().getTemplateId(), dataDefinition, alias);
    }

    @Override
    protected void addOtherAttributes(PropertyKiteElement element, Attributes attributes) {
        element.setNullHidden(attributes.getValue("null-hidden"));
        element.setConverterValue(attributes.getValue("converter"));
        element.setXmlCdata(attributes.getValue("xml-cdata"));
    }

    @Override
    protected void otherOperation(ParseContext parseContext, PropertyKiteElement element) {
        // 无操作
    }
}
