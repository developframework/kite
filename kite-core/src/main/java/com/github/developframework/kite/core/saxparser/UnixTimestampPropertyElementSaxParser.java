package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.element.PropertyKiteElement;
import com.github.developframework.kite.core.element.UnixTimestampPropertyKiteElement;

/**
 * unix时间戳型属性节点解析器
 * @author qiuzhenhao
 */
class UnixTimestampPropertyElementSaxParser extends PropertyElementSaxParser {

    UnixTimestampPropertyElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public String qName() {
        return "property-unixtimestamp";
    }

    @Override
    protected PropertyKiteElement createElementInstance(ParseContext parseContext, DataDefinition dataDefinition, String alias) {
        return new UnixTimestampPropertyKiteElement(kiteConfiguration, parseContext.getCurrentTemplateLocation(), dataDefinition, alias);
    }
}
