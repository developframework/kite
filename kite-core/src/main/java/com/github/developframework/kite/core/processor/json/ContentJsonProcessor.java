package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.data.FunctionSign;
import com.github.developframework.kite.core.dynamic.KiteConverter;
import com.github.developframework.kite.core.element.ContentKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.util.Objects;
import java.util.Optional;

/**
 * 内容节点处理器
 *
 * @author qiuzhenhao
 */
public abstract class ContentJsonProcessor<ELEMENT extends ContentKiteElement, NODE extends JsonNode> extends JsonProcessor<ELEMENT, NODE> {

    public ContentJsonProcessor(JsonProcessContext jsonProcessContext, ELEMENT element) {
        super(jsonProcessContext, element, null);
    }

    /**
     * 获得值
     *
     * @param parentProcessor 上层处理器
     * @return 值
     */
    @SuppressWarnings("unchecked")
    protected Optional<Object> getDataValue(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        if (Objects.nonNull(value)) {
            return Optional.of(value);
        }
        DataDefinition dataDefinition = element.getDataDefinition();
        Optional<Object> nextValueOptional;
        if (dataDefinition.getFunctionSign() == FunctionSign.ROOT || Objects.isNull(parentProcessor.value)) {
            nextValueOptional = jsonProcessContext.getDataModel().getData(dataDefinition.getExpression());
        } else {
            nextValueOptional = jsonProcessContext.getDataModel().getData(parentProcessor.value, dataDefinition.getExpression());
        }
        if (nextValueOptional.isPresent()) {
            // 处理转换器
            Optional<KiteConverter> converter = KiteUtils.getComponentInstance(jsonProcessContext.getDataModel(), element.getConverterValue(), KiteConverter.class, "converter");
            if (converter.isPresent()) {
                return Optional.ofNullable(converter.get().convert(nextValueOptional.get()));
            } else {
                return nextValueOptional;
            }
        } else {
            return Optional.empty();
        }
    }
}
