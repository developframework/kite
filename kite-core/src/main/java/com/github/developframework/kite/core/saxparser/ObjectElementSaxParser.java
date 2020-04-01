package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.element.ObjectKiteElement;

/**
 * 对象节点解析器
 * @author qiuzhenhao
 */
class ObjectElementSaxParser extends ContainerElementSaxParser<ObjectKiteElement>{

    ObjectElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public String qName() {
        return "object";
    }

    @Override
    protected ObjectKiteElement createElementInstance(ParseContext parseContext, DataDefinition dataDefinition, String alias) {
        return new ObjectKiteElement(kiteConfiguration, parseContext.getCurrentTemplateLocation(), dataDefinition, alias);
    }
}
