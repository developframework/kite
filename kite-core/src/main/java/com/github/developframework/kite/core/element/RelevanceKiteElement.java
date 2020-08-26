package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.TemplateLocation;
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
import org.dom4j.Element;

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

    @Setter
    protected String innerConverterValue;

    public RelevanceKiteElement(KiteConfiguration configuration, TemplateLocation templateLocation, DataDefinition dataDefinition, String alias) {
        super(configuration, templateLocation, dataDefinition, alias);
    }

    @Override
    public JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext context, ObjectNode parentNode) {
        return new RelevanceJsonProcessor(context, this);
    }

    @Override
    public XmlProcessor<? extends KiteElement, ? extends Element> createXmlProcessor(XmlProcessContext context, Element parentNode) {
        return new RelevanceXmlProcessor(context, this);
    }

    /**
     * 设置关联类型
     *
     * @param relevanceTypeValue 关联类型值
     */
    public void setRelevanceType(String relevanceTypeValue) {
        this.relevanceType = StringUtils.isNotEmpty(relevanceTypeValue) ? RelevanceType.valueOf(relevanceTypeValue.toUpperCase()) : RelevanceType.MULTIPLE;
    }

    public ContentKiteElement createProxyObjectElement() {
        if (isChildElementEmpty()) {
            // 如果没有子节点，视为普通属性节点处理
            return new ProxyNormalPropertyKiteElement(configuration, templateLocation, dataDefinition, alias);
        } else {
            // 如果有子节点，视为数组节点处理
            return new ObjectKiteElement(configuration, this, dataDefinition);
        }
    }

    public ArrayKiteElement createProxyArrayElement() {
        ArrayKiteElement arrayKiteElement = new ArrayKiteElement(configuration, this, dataDefinition);
        arrayKiteElement.setXmlItemName(xmlItemName);
        arrayKiteElement.setMapFunctionValue(mapFunctionValue);
        arrayKiteElement.setComparatorValue(comparatorValue);
        arrayKiteElement.setConverterValue(converterValue);
        arrayKiteElement.setChildrenNamingStrategy(childrenNamingStrategy);
        return arrayKiteElement;
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
