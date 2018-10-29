package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.json.SwitchJsonProcessor;
import com.github.developframework.kite.core.processor.xml.SwitchXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import lombok.Getter;
import lombok.Setter;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * switch节点
 *
 * @author qiushui on 2018-10-29.
 */
@Getter
public class SwitchKiteElement extends FunctionalKiteElement {

    private DataDefinition dataDefinition;

    private Map<String, CaseKiteElement> caseKiteElementMap = new HashMap<>();

    @Setter
    private CaseKiteElement defaultCaseKiteElement;

    public SwitchKiteElement(KiteConfiguration configuration, String namespace, String templateId, DataDefinition dataDefinition) {
        super(configuration, namespace, templateId);
        this.dataDefinition = dataDefinition;
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext jsonProcessContext, ObjectNode parentNode) {
        return new SwitchJsonProcessor(jsonProcessContext, this, parentNode);
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Element> createXmlProcessor(XmlProcessContext xmlProcessContext, Element parentNode) {
        return new SwitchXmlProcessor(xmlProcessContext, this, parentNode);
    }

    public Optional<CaseKiteElement> getDefaultCaseKiteElement() {
        return Optional.ofNullable(defaultCaseKiteElement);
    }
}
