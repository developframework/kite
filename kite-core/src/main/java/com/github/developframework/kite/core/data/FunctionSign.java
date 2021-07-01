package com.github.developframework.kite.core.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 功能符号
 *
 * @author qiuzhenhao
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum FunctionSign {

    ROOT('#');

    @Getter
    private final char sign;

}
