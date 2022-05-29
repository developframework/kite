package com.github.developframework.kite.core.dynamic;


import com.github.developframework.expression.ExpressionUtils;
import lombok.RequiredArgsConstructor;

/**
 * 根据表达式取值的转化器
 *
 * @author qiuzhenhao
 */
@RequiredArgsConstructor
public class FieldKiteConverter implements KiteConverter<Object, Object> {

    private final String expression;

    @Override
    public Object convert(Object source) {
        return ExpressionUtils.getValue(source, expression);
    }
}
