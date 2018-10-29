package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.TemplateLocation;
import com.github.developframework.kite.core.processor.json.CaseJsonProcessor;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.xml.CaseXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import lombok.Getter;
import org.dom4j.Element;

/**
 * case节点
 *
 * @author qiushui on 2018-10-28.
 */
public class CaseKiteElement extends ContainerFunctionalKiteElement {

    @Getter
    private String testValue;

    public CaseKiteElement(KiteConfiguration configuration, TemplateLocation templateLocation, String testValue) {
        super(configuration, templateLocation);
        this.testValue = testValue;
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext jsonProcessContext, ObjectNode parentNode) {
        return new CaseJsonProcessor(jsonProcessContext, this, parentNode);
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Element> createXmlProcessor(XmlProcessContext xmlProcessContext, Element parentNode) {
        return new CaseXmlProcessor(xmlProcessContext, this, parentNode);
    }
}
