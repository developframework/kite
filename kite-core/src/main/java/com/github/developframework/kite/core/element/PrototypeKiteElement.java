package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.json.PrototypeJsonProcessor;
import com.github.developframework.kite.core.processor.xml.PrototypeXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import org.dom4j.Element;

/**
 * @author qiuzhenhao
 */
public class PrototypeKiteElement extends PropertyKiteElement {


    public PrototypeKiteElement(KiteConfiguration configuration, String namespace, String templateId, DataDefinition dataDefinition, String alias) {
        super(configuration, namespace, templateId, dataDefinition, alias);
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext context, ObjectNode parentNode) {
        PrototypeJsonProcessor prototypeProcessor = new PrototypeJsonProcessor(context, this);
        prototypeProcessor.setNode(parentNode);
        return prototypeProcessor;
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Element> createXmlProcessor(XmlProcessContext context, Element parentNode) {
        PrototypeXmlProcessor prototypeProcessor = new PrototypeXmlProcessor(context, this);
        prototypeProcessor.setNode(parentNode);
        return prototypeProcessor;
    }
}
