package com.github.developframework.kite.core.structs;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author qiushui on 2021-06-23.
 */
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class TemplateLocation {

    private final String namespace;

    private final String templateId;

    @Override
    public String toString() {
        return namespace + "." + templateId;
    }
}
