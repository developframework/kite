package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.TemplateLocation;
import com.github.developframework.kite.core.processor.json.IncludeJsonProcessor;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.xml.IncludeXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import lombok.Getter;
import lombok.Setter;
import org.dom4j.Element;

/**
 * 包含功能节点
 * @author qiuzhenhao
 */
@Getter
@Setter
public class IncludeKiteElement extends FunctionalKiteElement {

    private TemplateLocation targetTemplateLocation;

    public IncludeKiteElement(KiteConfiguration configuration, TemplateLocation templateLocation, TemplateLocation targetTemplateLocation) {
        super(configuration, templateLocation);
        this.targetTemplateLocation = targetTemplateLocation;
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext context, ObjectNode parentNode) {
        return new IncludeJsonProcessor(context, this, parentNode);
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Element> createXmlProcessor(XmlProcessContext context, Element parentNode) {
        return new IncludeXmlProcessor(context, this, parentNode);
    }
}
