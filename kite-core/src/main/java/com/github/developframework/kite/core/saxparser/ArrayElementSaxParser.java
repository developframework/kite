package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.element.ArrayKiteElement;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;

/**
 * 数组节点解析器
 * @author qiuzhenhao
 */
class ArrayElementSaxParser extends ContainerElementSaxParser<ArrayKiteElement> {

    ArrayElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public String qName() {
        return "array";
    }

    @Override
    protected ArrayKiteElement createElementInstance(ParseContext parseContext, DataDefinition dataDefinition, String alias) {
        return new ArrayKiteElement(kiteConfiguration, parseContext.getCurrentTemplate().getNamespace(), parseContext.getCurrentTemplate().getTemplateId(), dataDefinition, alias);
    }

    @Override
    protected void addOtherAttributes(ArrayKiteElement element, Attributes attributes) {
        super.addOtherAttributes(element, attributes);
        element.setMapFunctionValue(attributes.getValue("map"));
        element.setXmlItemName(attributes.getValue("xml-item"));
        element.setComparatorValue(attributes.getValue("comparator"));
        String limitValue = attributes.getValue("limit");
        if (StringUtils.isNumeric(limitValue)) {
            element.setLimit(Integer.parseInt(limitValue));
        }
    }
}
