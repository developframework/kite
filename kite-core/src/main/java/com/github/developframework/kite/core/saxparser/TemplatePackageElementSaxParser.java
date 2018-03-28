package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.element.TemplatePackage;
import org.xml.sax.Attributes;

/**
 * 模板包解析器
 * @author qiuzhenhao
 */
class TemplatePackageElementSaxParser extends AbstractElementSaxParser{

    TemplatePackageElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public String qName() {
        return "template-package";
    }

    @Override
    public void handleAtStartElement(ParseContext parseContext, Attributes attributes) {
        final String namespace = attributes.getValue("namespace").trim();
        parseContext.setCurrentTemplatePackage(new TemplatePackage(namespace));
    }

    @Override
    public void handleAtEndElement(ParseContext parseContext) {
        parseContext.getConfiguration().putTemplatePackage(parseContext.getCurrentTemplatePackage());
    }
}
