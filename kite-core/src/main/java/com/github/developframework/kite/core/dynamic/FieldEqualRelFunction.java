package com.github.developframework.kite.core.dynamic;

import com.github.developframework.expression.ExpressionUtils;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

/**
 * @author qiushui on 2021-06-29.
 */
@RequiredArgsConstructor
public final class FieldEqualRelFunction implements RelFunction<Object, Object> {

    private final String sourceField;

    private final String targetField;

    @Override
    public boolean relevant(Object sourceItem, int sourceIndex, Object target, int targetIndex) {
        final Object sourceValue = ExpressionUtils.getValue(sourceItem, sourceField);
        final Object targetValue = ExpressionUtils.getValue(target, targetField);
        return Objects.equals(sourceValue, targetValue);
    }
}
