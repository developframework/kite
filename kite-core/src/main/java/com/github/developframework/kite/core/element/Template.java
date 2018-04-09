package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.json.TemplateJsonProcessor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * Kite 模板
 *
 * @author qiuzhenhao
 */
public class Template extends ObjectKiteElement {

    /* 扩展口 */
    @Setter
    @Getter
    private Extend extend;

    @Setter
    @Getter
    private String mapFunctionValue;

    @Setter
    private String xmlRootName;

    @Setter
    private String xmlItemName;

    public Template(KiteConfiguration configuration, String namespace, String templateId) {
        super(configuration, namespace, templateId, null, null);
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext jsonProcessContext, ObjectNode parentNode, Expression parentExpression) {
        TemplateJsonProcessor templateProcessor = new TemplateJsonProcessor(jsonProcessContext, this, JsonProcessor.childExpression(this, parentExpression));
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

        private String namespace;

        private String templateId;

        private String port;

        public Extend(String extendValue, String defaultNamespace) {
            String front = StringUtils.substringBefore(extendValue, ":");
            this.port = StringUtils.substringAfter(extendValue, ":");
            if (front.contains(".")) {
                this.namespace = StringUtils.substringBefore(front, ".");
                this.templateId = StringUtils.substringAfter(front, ".");
            } else {
                this.namespace = defaultNamespace;
                this.templateId = front;
            }
        }
    }

    public String getXmlRootName() {
        if(StringUtils.isBlank(xmlRootName)) {
            throw new KiteException("\"xml-root\" is undefined in template \"%s : %s\".", namespace, templateId);
        }
        return xmlRootName;
    }

    public String getXmlItemName() {
        if(StringUtils.isBlank(xmlItemName)) {
            throw new KiteException("\"xml-item\" is undefined in template \"%s : %s\".", namespace, templateId);
        }
        return xmlItemName;
    }
}
