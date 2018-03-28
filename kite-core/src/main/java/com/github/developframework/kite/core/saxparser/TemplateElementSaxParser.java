package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.element.Template;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;

/**
 * 模板节点解析器
 * @author qiuzhenhao
 */
class TemplateElementSaxParser extends ContainerElementSaxParser<Template>{

    TemplateElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public String qName() {
        return "template";
    }

    @Override
    public void handleAtStartElement(ParseContext parseContext, Attributes attributes) {
        final String id = attributes.getValue("id").trim();
        final String data = attributes.getValue("data");
        final String extend = attributes.getValue("extend");
        final String mapFunctionValue = attributes.getValue("map-function");
        final String xmlRootName = attributes.getValue("xml-root");
        final String xmlItemName = attributes.getValue("xml-item");
        final Template template = new Template(kiteConfiguration, parseContext.getCurrentTemplatePackage().getNamespace(), id);
        if (StringUtils.isNotBlank(data)) {
            template.setDataDefinition(new DataDefinition(data));
        } else {
            template.setDataDefinition(DataDefinition.EMPTY_DATA_DEFINITION);
        }
        if (StringUtils.isNotBlank(extend)) {
            String defaultNamespace = parseContext.getCurrentTemplatePackage().getNamespace();
            template.setExtend(template.new Extend(extend.trim(), defaultNamespace));
        }
        if (StringUtils.isNotBlank(mapFunctionValue)) {
            template.setMapFunctionValue(mapFunctionValue);
        }
        if (StringUtils.isNotBlank(xmlRootName)) {
            template.setXmlRootName(xmlRootName);
        }
        if (StringUtils.isNotBlank(xmlItemName)) {
            template.setXmlItemName(xmlItemName);
        }
        template.setForClass(attributes.getValue("for-class"));
        parseContext.setCurrentTemplate(template);
        parseContext.getStack().push(template);
    }

    @Override
    public void handleAtEndElement(ParseContext parseContext) {
        Template template = (Template) parseContext.getStack().pop();
        template.loadForClassAllProperty();
        parseContext.getCurrentTemplatePackage().push(template);
    }

    @Override
    protected Template createElementInstance(ParseContext parseContext, DataDefinition dataDefinition, String alias) {
        // 无操作
        return null;
    }

    @Override
    protected void addOtherAttributes(Template element, Attributes attributes) {
        // 无操作
    }
}
