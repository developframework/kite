package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.processor.json.*;
import com.github.developframework.kite.core.processor.xml.*;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * 一对一链接节点
 *
 * @author qiuzhenhao
 */
public class LinkKiteElement extends ObjectKiteElement {

    public LinkKiteElement(KiteConfiguration kiteConfiguration, String namespace, String templateId, DataDefinition dataDefinition, String alias) {
        super(kiteConfiguration, namespace, templateId, dataDefinition, alias);
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext context, ObjectNode parentNode, Expression parentExpression) {
        LinkJsonProcessor linkProcessor = new LinkJsonProcessor(context, this, JsonProcessor.childExpression(this, parentExpression));
        linkProcessor.setNode(parentNode);
        return linkProcessor;
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Node> createXmlProcessor(XmlProcessContext context, Node parentNode, Expression parentExpression) {
        LinkXmlProcessor linkProcessor = new LinkXmlProcessor(context, this, JsonProcessor.childExpression(this, parentExpression));
        linkProcessor.setNode((Element) parentNode);
        return linkProcessor;
    }

    /**
     * 创建一个代理节点处理任务
     *
     * @return 代理节点处理任务
     */
    public ContentKiteElement createProxyContentElement() {
        if (isChildElementEmpty()) {
            // 如果没有子节点，视为普通属性节点处理
            return new NormalPropertyKiteElement(configuration, namespace, templateId, dataDefinition, alias) {
                @Override
                public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext context, ObjectNode parentNode, Expression parentExpression) {
                    return new NormalPropertyJsonProcessor(context, this, parentExpression);
                }

                @Override
                public XmlProcessor<? extends KiteElement, ? extends Node> createXmlProcessor(XmlProcessContext context, Node parentNode, Expression parentExpression) {
                    return new NormalPropertyXmlProcessor(context, this, parentExpression);
                }
            };
        } else {
            // 如果有子节点，视为对象节点处理
            return new ProxyObjectKiteElement(configuration, this, dataDefinition);
        }
    }

    /**
     * 内置节点
     */
    public class LinkInsideObjectElement extends ObjectKiteElement {

        public LinkInsideObjectElement(KiteConfiguration configuration, String namespace, String templateId, DataDefinition dataDefinition, String alias) {
            super(configuration, namespace, templateId, dataDefinition, alias);
        }

        @Override
        public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext context, ObjectNode parentNode, Expression parentExpression) {
            return new ObjectJsonProcessor(context, this, parentExpression);
        }

        @Override
        public XmlProcessor<? extends KiteElement, ? extends Node> createXmlProcessor(XmlProcessContext context, Node parentNode, Expression parentExpression) {
            return new ObjectXmlProcessor(context, this, parentExpression);
        }
    }
}
