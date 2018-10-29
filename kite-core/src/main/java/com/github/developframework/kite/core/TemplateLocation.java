package com.github.developframework.kite.core;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 模板位置
 *
 * @author qiushui on 2018-10-29.
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class TemplateLocation {

    private String namespace;

    private String templateId;

    @Override
    public String toString() {
        return namespace + " : " + templateId;
    }
}
