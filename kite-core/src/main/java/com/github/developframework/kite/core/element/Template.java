package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.TemplateLocation;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.json.TemplateJsonProcessor;
import com.github.developframework.kite.core.processor.xml.TemplateXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.util.Optional;

/**
 * Kite 模板
 *
 * @author qiuzhenhao
 */
public class Template extends ObjectKiteElement {

    /* 扩展口 */
    @Setter
    private Extend extend;

    @Setter
    @Getter
    private String mapFunctionValue;

    @Setter
    private String xmlRootName;

    @Setter
    private String xmlItemName;

    public Template(KiteConfiguration configuration, TemplateLocation templateLocation) {
        super(configuration, templateLocation, null, null);
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext jsonProcessContext, ObjectNode parentNode) {
        TemplateJsonProcessor templateProcessor = new TemplateJsonProcessor(jsonProcessContext, this);
        templateProcessor.setNode(parentNode);
        return templateProcessor;
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Element> createXmlProcessor(XmlProcessContext xmlProcessContext, Element parentNode) {
        TemplateXmlProcessor templateProcessor = new TemplateXmlProcessor(xmlProcessContext, this);
        templateProcessor.setNode(parentNode);
        return templateProcessor;
    }

    public Optional<Extend> getExtend() {
        return Optional.ofNullable(extend);
    }

    /**
     * 创建一个副本模板节点
     * @return 副本模板节点
     */
    public DuplicateTemplateKiteElement createDuplicateTemplateKiteElement() {
        return new DuplicateTemplateKiteElement(configuration, this);
    }

    @Getter
    public class Extend {

        private TemplateLocation extendTemplateLocation;

        private String port;

        public Extend(String extendValue, String defaultNamespace) {
            String front = StringUtils.substringBefore(extendValue, ":");
            this.port = StringUtils.substringAfter(extendValue, ":");
            String namespace, templateId;
            if (front.contains(".")) {
                namespace = StringUtils.substringBefore(front, ".");
                templateId = StringUtils.substringAfter(front, ".");
            } else {
                namespace = defaultNamespace;
                templateId = front;
            }
            extendTemplateLocation = new TemplateLocation(namespace, templateId);
        }
    }

    public String getXmlRootName() {
        if (StringUtils.isBlank(xmlRootName) && dataDefinition == DataDefinition.EMPTY_DATA_DEFINITION) {
            throw new KiteException("\"xml-root\" is undefined in template \"%s\".", templateLocation);
        }
        return xmlRootName;
    }

    public String getXmlItemName() {
        if(StringUtils.isBlank(xmlItemName)) {
            throw new KiteException("\"xml-item\" is undefined in template \"%s\".", templateLocation);
        }
        return xmlItemName;
    }
}
