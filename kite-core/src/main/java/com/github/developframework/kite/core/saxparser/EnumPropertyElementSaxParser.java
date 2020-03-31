package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.element.EnumPropertyKiteElement;
import com.github.developframework.kite.core.element.PropertyKiteElement;

/**
 * 枚举类型属性节点解析器
 *
 * @author qiuzhenhao
 */
class EnumPropertyElementSaxParser extends PropertyElementSaxParser {

    EnumPropertyElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public String qName() {
        return "property-enum";
    }

    @Override
    protected PropertyKiteElement createElementInstance(ParseContext parseContext, DataDefinition dataDefinition, String alias) {
        return new EnumPropertyKiteElement(kiteConfiguration, parseContext.getCurrentTemplateLocation(), dataDefinition, alias);
    }
}
