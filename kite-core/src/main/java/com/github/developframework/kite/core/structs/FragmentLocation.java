package com.github.developframework.kite.core.structs;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 片段位置
 *
 * @author qiushui on 2021-06-23.
 */
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class FragmentLocation {

    private final String namespace;

    private final String fragmentId;

    @Override
    public String toString() {
        return namespace + "." + fragmentId;
    }
}
