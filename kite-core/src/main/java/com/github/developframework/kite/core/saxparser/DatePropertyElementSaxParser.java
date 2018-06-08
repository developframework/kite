package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.element.DatePropertyKiteElement;
import com.github.developframework.kite.core.element.PropertyKiteElement;
import org.xml.sax.Attributes;

/**
 * 日期时间类型属性节点解析器
 * @author qiuzhenhao
 */
class DatePropertyElementSaxParser extends PropertyElementSaxParser {

    DatePropertyElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public String qName() {
        return "property-date";
    }

    @Override
    protected PropertyKiteElement createElementInstance(ParseContext parseContext, DataDefinition dataDefinition, String alias) {
        return new DatePropertyKiteElement(kiteConfiguration, parseContext.getCurrentTemplate().getNamespace(), parseContext.getCurrentTemplate().getTemplateId(), dataDefinition, alias);
    }

    @Override
    protected void addOtherAttributes(PropertyKiteElement element, Attributes attributes) {
        super.addOtherAttributes(element, attributes);
        ((DatePropertyKiteElement)element).setPattern(attributes.getValue("pattern"));
    }
}
