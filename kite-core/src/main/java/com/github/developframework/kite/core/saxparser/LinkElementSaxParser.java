package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.element.LinkKiteElement;
import org.xml.sax.Attributes;

/**
 * 一对一链接节点解析器
 *
 * @author qiuzhenhao
 */
class LinkElementSaxParser extends ContainerElementSaxParser<LinkKiteElement>  {

    LinkElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public String qName() {
        return "link";
    }

    @Override
    protected LinkKiteElement createElementInstance(ParseContext parseContext, DataDefinition dataDefinition, String alias) {
        return new LinkKiteElement(kiteConfiguration, parseContext.getCurrentTemplateLocation(), dataDefinition, alias);
    }

    @Override
    protected void addOtherAttributes(ParseContext parseContext, LinkKiteElement element, Attributes attributes) {
        super.addOtherAttributes(parseContext, element, attributes);
        element.setMapFunctionValue(attributes.getValue("map"));
    }
}
