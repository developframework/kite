package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.json.RelevanceJsonProcessor;
import com.github.developframework.kite.core.processor.xml.RelevanceXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Node;

/**
 * 关联节点
 *
 * @author qiuzhenhao
 */
@Getter
public class RelevanceKiteElement extends ArrayKiteElement {

    @Setter
    private String relFunctionValue;

    private RelevanceType relevanceType;

    public RelevanceKiteElement(KiteConfiguration configuration, String namespace, String templateId, DataDefinition dataDefinition, String alias) {
        super(configuration, namespace, templateId, dataDefinition, alias);
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext context, ObjectNode parentNode, Expression parentExpression) {
        return new RelevanceJsonProcessor(context, this, parentExpression);
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Node> createXmlProcessor(XmlProcessContext context, Node parentNode, Expression parentExpression) {
        return new RelevanceXmlProcessor(context, this, parentExpression);
    }

    /**
     * 设置关联类型
     *
     * @param relevanceTypeValue 关联类型值
     */
    public void setRelevanceType(String relevanceTypeValue) {
        if (StringUtils.isNotBlank(relevanceTypeValue)) {
            this.relevanceType = RelevanceType.valueOf(relevanceTypeValue.toUpperCase());
        } else {
            this.relevanceType = RelevanceType.AUTO;
        }
    }

    /**
     * 关联类型
     */
    public enum RelevanceType {

        AUTO,       // 依据数据数量自动选择采用对象或数组结构
        SINGLE,     // 明确单项数据选择采用对象结构
        MULTIPLE    // 明确多项数据选择采用数组结构
    }
}
