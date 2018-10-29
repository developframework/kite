package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.element.ContentKiteElement;
import com.github.developframework.kite.core.element.ThisKiteElement;

/**
 * 指代自身节点解析器
 *
 * @author qiushui on 2018-10-28.
 */
public class ThisElementSaxParser extends ContainerElementSaxParser {

    ThisElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public String qName() {
        return "this";
    }

    @Override
    protected ContentKiteElement createElementInstance(ParseContext parseContext, DataDefinition dataDefinition, String alias) {
        return new ThisKiteElement(kiteConfiguration, parseContext.getCurrentTemplateLocation(), dataDefinition, alias);
    }
}
