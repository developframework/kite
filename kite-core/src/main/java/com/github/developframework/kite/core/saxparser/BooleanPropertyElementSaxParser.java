package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.element.BooleanPropertyKiteElement;
import com.github.developframework.kite.core.element.PropertyKiteElement;

/**
 * 布尔型属性节点解析器
 * @author qiuzhenhao
 */
class BooleanPropertyElementSaxParser extends PropertyElementSaxParser {

    BooleanPropertyElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public String qName() {
        return "property-boolean";
    }

    @Override
    protected PropertyKiteElement createElementInstance(ParseContext parseContext, DataDefinition dataDefinition, String alias) {
        return new BooleanPropertyKiteElement(kiteConfiguration, parseContext.getCurrentTemplateLocation(), dataDefinition, alias);
    }
}
