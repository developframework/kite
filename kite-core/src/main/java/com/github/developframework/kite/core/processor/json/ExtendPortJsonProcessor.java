package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.ExtendPortKiteElement;

/**
 * 扩展端口处理器
 * @author qiuzhenhao
 */
public class ExtendPortJsonProcessor extends FunctionalJsonProcessor<ExtendPortKiteElement, JsonNode> {

    public ExtendPortJsonProcessor(JsonProcessContext jsonProcessContext, ExtendPortKiteElement element, JsonNode node, Expression parentExpression) {
        super(jsonProcessContext, element, node, parentExpression);
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        jsonProcessContext.getExtendCallback(element.getPortName()).ifPresent(extendCallback -> extendCallback.call(parentProcessor));
    }

    /**
     * 扩展口回调接口
     *
     * @author qiuzhenhao
     */
    public interface ExtendCallback {

        void call(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor);
    }
}
