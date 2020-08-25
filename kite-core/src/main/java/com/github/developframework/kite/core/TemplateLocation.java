package com.github.developframework.kite.core;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 模板位置
 *
 * @author qiushui on 2018-10-29.
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class TemplateLocation {

    private final String namespace;

    private final String templateId;

    @Override
    public String toString() {
        return namespace + " : " + templateId;
    }
}
