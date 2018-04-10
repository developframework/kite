package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.XmlAttributeElement;

/**
 * XML属性Json处理器（不处理）
 * @author qiuzhenhao
 */
public class XmlAttributeJsonProcessor extends ContentJsonProcessor<XmlAttributeElement, JsonNode>{


    public XmlAttributeJsonProcessor(JsonProcessContext jsonProcessContext, XmlAttributeElement element, Expression parentExpression) {
        super(jsonProcessContext, element, parentExpression);
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        // 不处理
    }
}