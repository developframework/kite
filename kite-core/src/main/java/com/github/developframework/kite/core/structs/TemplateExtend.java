package com.github.developframework.kite.core.structs;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author qiushui on 2021-06-28.
 */
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class TemplateExtend {

    private final FragmentLocation fragmentLocation;

    private final String slot;

    @Override
    public String toString() {
        return fragmentLocation + ":" + slot;
    }
}
