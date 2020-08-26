package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.developframework.expression.ExpressionUtils;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.data.FunctionSign;
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
    protected Optional<Object> getDataValue(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        if (Objects.nonNull(value)) {
            return Optional.of(value);
        }
        DataDefinition dataDefinition = element.getDataDefinition();
        Object nextValue;
        if (dataDefinition.getFunctionSign() == FunctionSign.ROOT || Objects.isNull(parentProcessor.value)) {
            nextValue = jsonProcessContext.getDataModel().getData(dataDefinition.getExpression()).orElse(null);
        } else {
            nextValue = ExpressionUtils.getValue(parentProcessor.value, dataDefinition.getExpression());
        }
        // 处理转换器
        nextValue = KiteUtils.handleKiteConverter(jsonProcessContext.getDataModel(), element.getConverterValue(), nextValue);
        return Optional.ofNullable(nextValue);
    }

    protected String showName(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        return element.showNameJSON(parentProcessor.element.getChildrenNamingStrategy());
    }
}
