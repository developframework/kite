package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.element.ContentKiteElement;
import org.xml.sax.Attributes;

/**
 * 内容节点解析器
 * @author qiuzhenhao
 * @param <T> 节点类型
 */
abstract class ContentElementSaxParser<T extends ContentKiteElement> extends AbstractElementSaxParser {

    ContentElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public void handleAtStartElement(ParseContext parseContext, Attributes attributes) {
        final DataDefinition dataDefinition = new DataDefinition(attributes.getValue("data"));
        final String alias = attributes.getValue("alias");
        T element = createElementInstance(parseContext, dataDefinition, alias);
        element.setConverterValue(attributes.getValue("converter"));
        addOtherAttributes(element, attributes);
        addChildElement(parseContext, element);
        otherOperation(parseContext, element);
    }

    @Override
    public void handleAtEndElement(ParseContext parseContext) {
        // 无操作
    }

    /**
     * 创建节点实例
     * @param parseContext 上下文
     * @param dataDefinition 数据定义
     * @param alias 别名
     * @return 节点实例
     */
    protected abstract T createElementInstance(ParseContext parseContext, DataDefinition dataDefinition, String alias);

    /**
     * 增加其它属性
     * @param element 节点
     * @param attributes 属性
     */
    protected abstract void addOtherAttributes(T element, Attributes attributes);

    /**
     * 其它操作
     * @param parseContext 上下文
     * @param element 节点
     */
    protected abstract void otherOperation(ParseContext parseContext, T element);
}
