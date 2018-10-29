package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.element.PrototypeKiteElement;

/**
 * 原型节点解析器
 * @author qiuzhenhao
 */
class PrototypeElementSaxParser extends PropertyElementSaxParser{

    PrototypeElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public String qName() {
        return "prototype";
    }

    @Override
    protected PrototypeKiteElement createElementInstance(ParseContext parseContext, DataDefinition dataDefinition, String alias) {
        return new PrototypeKiteElement(kiteConfiguration, parseContext.getCurrentTemplateLocation(), dataDefinition, alias);
    }

}
