package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.element.JsonKiteElement;
import org.xml.sax.Attributes;

/**
 * json节点解析器
 *
 * @author qiushui on 2018-12-28.
 */
public class JsonElementSaxParser extends ContentElementSaxParser<JsonKiteElement> {

    JsonElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    protected JsonKiteElement createElementInstance(ParseContext parseContext, DataDefinition dataDefinition, String alias) {
        return new JsonKiteElement(kiteConfiguration, parseContext.getCurrentTemplateLocation(), dataDefinition, alias);
    }

    @Override
    protected void addOtherAttributes(JsonKiteElement element, Attributes attributes) {

    }

    @Override
    protected void otherOperation(ParseContext parseContext, JsonKiteElement element) {

    }

    @Override
    public String qName() {
        return "json";
    }
}
