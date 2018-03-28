package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.processor.json.IncludeJsonProcessor;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.xml.IncludeXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import lombok.Getter;
import lombok.Setter;
import org.dom4j.Node;

/**
 * 包含功能节点
 * @author qiuzhenhao
 */
@Getter
@Setter
public class IncludeKiteElement extends FunctionalKiteElement {

    /* 命名空间 */
    private String targetNamespace;
    /* 模板ID */
    private String targetTemplateId;

    public IncludeKiteElement(KiteConfiguration configuration, String namespace, String templateId, String targetNamespace, String targetTemplateId) {
        super(configuration, namespace, templateId);
        this.targetNamespace = targetNamespace;
        this.targetTemplateId = targetTemplateId;
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext context, ObjectNode parentNode, Expression parentExpression) {
        return new IncludeJsonProcessor(context, this, parentNode, parentExpression);
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Node> createXmlProcessor(XmlProcessContext context, Node parentNode, Expression parentExpression) {
        return new IncludeXmlProcessor(context, this, parentNode, parentExpression);
    }
}
