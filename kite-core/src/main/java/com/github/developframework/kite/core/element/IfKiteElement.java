package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.TemplateLocation;
import com.github.developframework.kite.core.processor.json.IfJsonProcessor;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.xml.IfXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import lombok.Setter;
import org.dom4j.Element;

import java.util.Optional;

/**
 * if节点
 *
 * @author qiuzhenhao
 */
public class IfKiteElement extends ContainerFunctionalKiteElement {

    private String conditionValue;
    @Setter
    private ElseKiteElement elseElement;

    public IfKiteElement(KiteConfiguration configuration, TemplateLocation templateLocation, String conditionValue) {
        super(configuration, templateLocation);
        this.conditionValue = conditionValue;
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext context, ObjectNode parentNode) {
        return new IfJsonProcessor(context, this, parentNode);
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Element> createXmlProcessor(XmlProcessContext context, Element parentNode) {
        return new IfXmlProcessor(context, this, parentNode);
    }

    public Optional<ElseKiteElement> getElseElement() {
        return Optional.ofNullable(elseElement);
    }

    public Optional<String> getConditionValue() {
        return Optional.ofNullable(conditionValue);
    }

}
