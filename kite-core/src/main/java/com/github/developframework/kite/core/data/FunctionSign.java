package com.github.developframework.kite.core.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 功能符号
 * @author qiuzhenhao
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum FunctionSign {

    ROOT('#');

    @Getter
    private final char sign;

}
