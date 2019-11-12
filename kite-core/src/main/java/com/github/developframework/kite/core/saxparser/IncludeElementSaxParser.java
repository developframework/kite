package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.TemplateLocation;
import com.github.developframework.kite.core.element.IncludeKiteElement;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;

/**
 * 包含节点解析器
 * @author qiuzhenhao
 */
class IncludeElementSaxParser extends AbstractElementSaxParser{

    IncludeElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public String qName() {
        return "include";
    }

    @Override
    public void handleAtStartElement(ParseContext parseContext, Attributes attributes) {
        final String templateId = attributes.getValue("id").trim();
        String namespace = attributes.getValue("namespace");
        namespace = StringUtils.isNotEmpty(namespace) ? namespace.trim() : parseContext.getCurrentTemplatePackage().getNamespace();
        final IncludeKiteElement includeElement = new IncludeKiteElement(kiteConfiguration, parseContext.getCurrentTemplateLocation(), new TemplateLocation(namespace, templateId));
        addChildElement(parseContext, includeElement);
    }

    @Override
    public void handleAtEndElement(ParseContext parseContext) {
        // 无操作
    }
}
