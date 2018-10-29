package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.element.ExtendPortKiteElement;
import org.xml.sax.Attributes;

/**
 * 扩展端口节点解析器
 * @author qiuzhenhao
 */
class ExtendPortElementSaxParser extends AbstractElementSaxParser {

    ExtendPortElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public String qName() {
        return "extend-port";
    }

    @Override
    public void handleAtStartElement(ParseContext parseContext, Attributes attributes) {
        final String portName = attributes.getValue("port-name");
        final ExtendPortKiteElement extendPortElement = new ExtendPortKiteElement(kiteConfiguration, parseContext.getCurrentTemplateLocation(), portName);
        addChildElement(parseContext, extendPortElement);
    }

    @Override
    public void handleAtEndElement(ParseContext parseContext) {
        // 无操作
    }
}
