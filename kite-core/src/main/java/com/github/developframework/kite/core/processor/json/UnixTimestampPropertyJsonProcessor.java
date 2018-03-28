package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.element.PropertyKiteElement;

import java.util.Objects;

/**
 * unix时间戳型属性节点处理器
 * @author qiuzhenhao
 */
public class UnixTimestampPropertyJsonProcessor extends DatePropertyJsonProcessor {

    public UnixTimestampPropertyJsonProcessor(JsonProcessContext context, PropertyKiteElement element, Expression parentExpression) {
        super(context, element, parentExpression);
    }

    @Override
    protected void handle(ObjectNode parentNode, Class<?> clazz, Object value, String showName) {
        java.util.Date date = transformDate(clazz, value);
        if (Objects.isNull(date)) {
            parentNode.putNull(showName);
            return;
        }
        parentNode.put(showName, date.getTime() / 1000);
    }
}
