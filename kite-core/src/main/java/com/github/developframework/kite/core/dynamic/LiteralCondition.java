package com.github.developframework.kite.core.dynamic;

import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.utils.KiteUtils;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.Optional;

/**
 * 常量条件
 *
 * @author qiushui on 2022-05-31.
 */
@RequiredArgsConstructor
public class LiteralCondition implements KiteCondition<Object> {

    private final DataDefinition dataDefinition;
    private final String literal;

    @Override
    public boolean verify(DataModel dataModel, Object currentValue) {
        return Optional.ofNullable(dataDefinition == null ? currentValue : KiteUtils.getData(dataModel, dataDefinition, currentValue))
                .map(v -> Objects.equals(literal, v.toString()))
                .orElse(false);
    }
}
