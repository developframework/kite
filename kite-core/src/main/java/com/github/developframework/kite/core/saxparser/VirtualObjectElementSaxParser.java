package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.element.VirtualObjectKiteElement;
import org.xml.sax.Attributes;

/**
 * 虚拟对象节点解析器
 * @author qiuzhenhao
 */
class VirtualObjectElementSaxParser extends ContainerElementSaxParser<VirtualObjectKiteElement>{

    VirtualObjectElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public String qName() {
        return "object-virtual";
    }

    @Override
    protected VirtualObjectKiteElement createElementInstance(ParseContext parseContext, DataDefinition dataDefinition, String alias) {
        return new VirtualObjectKiteElement(kiteConfiguration, parseContext.getCurrentTemplateLocation(), dataDefinition, alias);
    }

    @Override
    protected void addOtherAttributes(ParseContext parseContext, VirtualObjectKiteElement element, Attributes attributes) {
        // 无操作
    }
}
