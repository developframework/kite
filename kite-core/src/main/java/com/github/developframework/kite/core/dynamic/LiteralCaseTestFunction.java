package com.github.developframework.kite.core.dynamic;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

/**
 * 常量Case test
 *
 * @author qiushui on 2022-05-30.
 */
@RequiredArgsConstructor
public class LiteralCaseTestFunction implements CaseTestFunction<Object> {

    private final String literal;

    @Override
    public boolean test(Object data) {
        return Objects.equals(data == null ? null : data.toString(), literal);
    }
}
