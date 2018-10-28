package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.data.FunctionSign;
import com.github.developframework.kite.core.dynamic.KiteConverter;
import com.github.developframework.kite.core.element.ContentKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.utils.KiteUtils;
import org.dom4j.Element;

import java.util.Objects;
import java.util.Optional;

/**
 * 内容节点处理器
 *
 * @author qiuzhenhao
 */
public abstract class ContentXmlProcessor<ELEMENT extends ContentKiteElement, NODE extends Element> extends XmlProcessor<ELEMENT, NODE> {

    public ContentXmlProcessor(XmlProcessContext xmlProcessContext, ELEMENT element) {
        super(xmlProcessContext, element, null);
    }

    /**
     * 获得值
     *
     * @param parentProcessor 上层处理器
     * @return 值
     */
    @SuppressWarnings("unchecked")
    protected Optional<Object> getDataValue(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        if (Objects.nonNull(value)) {
            return Optional.of(value);
        }
        DataDefinition dataDefinition = element.getDataDefinition();
        Optional<Object> nextValueOptional;
        if (dataDefinition.getFunctionSign() == FunctionSign.ROOT || Objects.isNull(parentProcessor.value)) {
            nextValueOptional = xmlProcessContext.getDataModel().getData(dataDefinition.getExpression());
        } else {
            nextValueOptional = xmlProcessContext.getDataModel().getData(parentProcessor.value, dataDefinition.getExpression());
        }
        if (nextValueOptional.isPresent()) {
            // 处理转换器
            if (element.getConverterValue().isPresent()) {
                KiteConverter converter = KiteUtils.getComponentInstance(xmlProcessContext.getDataModel(), element.getConverterValue().get(), KiteConverter.class, "converter");
                return Optional.ofNullable(converter.convert(nextValueOptional.get()));
            } else {
                return nextValueOptional;
            }
        } else {
            return Optional.empty();
        }
    }
}
