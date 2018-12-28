package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.element.RelevanceKiteElement;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;

/**
 * 关联节点解析器
 * @author qiuzhenhao
 */
class RelevanceElementParser extends ContainerElementSaxParser<RelevanceKiteElement> {


    RelevanceElementParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public String qName() {
        return "relevance";
    }

    @Override
    protected RelevanceKiteElement createElementInstance(ParseContext parseContext, DataDefinition dataDefinition, String alias) {
        return new RelevanceKiteElement(kiteConfiguration, parseContext.getCurrentTemplateLocation(), dataDefinition, alias);
    }

    @Override
    protected void addOtherAttributes(RelevanceKiteElement element, Attributes attributes) {
        super.addOtherAttributes(element, attributes);
        element.setRelFunctionValue(attributes.getValue("rel"));
        element.setMapFunctionValue(attributes.getValue("map"));
        element.setComparatorValue(attributes.getValue("comparator"));
        element.setRelevanceType(attributes.getValue("type"));
        element.setXmlItemName(attributes.getValue("xml-item"));
        element.setNullEmpty(attributes.getValue("null-empty"));
        String limitValue = attributes.getValue("limit");
        if (StringUtils.isNumeric(limitValue)) {
            element.setLimit(Integer.parseInt(limitValue));
        }
    }
}
