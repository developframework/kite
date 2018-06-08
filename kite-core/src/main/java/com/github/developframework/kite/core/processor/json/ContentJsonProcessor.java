package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.element.ContentKiteElement;

/**
 * 内容节点处理器
 *
 * @author qiuzhenhao
 */
public abstract class ContentJsonProcessor<ELEMENT extends ContentKiteElement, NODE extends JsonNode> extends JsonProcessor<ELEMENT, NODE> {

    public ContentJsonProcessor(JsonProcessContext jsonProcessContext, ELEMENT element, Expression parentExpression) {
        super(jsonProcessContext, element, null, parentExpression);
    }

}
