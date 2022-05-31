package com.github.developframework.kite.core.dynamic;

import com.github.developframework.kite.core.data.DataModel;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

/**
 * 常量条件
 *
 * @author qiushui on 2022-05-31.
 */
@RequiredArgsConstructor
public class LiteralCondition implements KiteCondition<Object> {

    private final String literal;

    @Override
    public boolean verify(DataModel dataModel, Object currentValue) {
        return Objects.equals(literal, currentValue == null ? null : currentValue.toString());
    }
}
