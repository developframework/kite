package com.github.developframework.kite.core.dynamic;

import lombok.RequiredArgsConstructor;

/**
 * @author qiushui on 2022-05-31.
 */
@RequiredArgsConstructor
public class ClassCaseTestFunction implements CaseTestFunction<Object> {

    private final Class<?> targetClass;

    @Override
    public boolean test(Object data) {
        return data != null && targetClass.isAssignableFrom(data.getClass());
    }
}
