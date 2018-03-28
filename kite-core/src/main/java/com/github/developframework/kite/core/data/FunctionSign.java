package com.github.developframework.kite.core.data;

import lombok.Getter;

/**
 * 功能符号
 * @author qiuzhenhao
 */
public enum FunctionSign {

    ROOT('#');

    @Getter
    private char sign;

    FunctionSign(char sign) {
        this.sign = sign;
    }
}
