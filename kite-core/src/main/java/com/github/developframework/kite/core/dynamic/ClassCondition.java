package com.github.developframework.kite.core.dynamic;

import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.utils.KiteUtils;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * @author qiushui on 2022-05-31.
 */
@RequiredArgsConstructor
public class ClassCondition implements KiteCondition<Object> {

    private final DataDefinition dataDefinition;
    private final Class<?> targetClass;

    @Override
    public boolean verify(DataModel dataModel, Object currentValue) {
        return Optional.ofNullable(dataDefinition == null ? currentValue : KiteUtils.getData(dataModel, dataDefinition, currentValue))
                .map(v -> targetClass.isAssignableFrom(v.getClass()))
                .orElse(false);
    }
}
