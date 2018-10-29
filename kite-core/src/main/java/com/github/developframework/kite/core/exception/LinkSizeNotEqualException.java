package com.github.developframework.kite.core.exception;

import com.github.developframework.kite.core.TemplateLocation;

/**
 * 一对一链接数组大小不一致异常
 * @author qiuzhenhao
 */
public class LinkSizeNotEqualException extends KiteException {

    public LinkSizeNotEqualException(TemplateLocation templateLocation) {
        super("The element <link> size is not equals parent array size in \"%s\"", templateLocation.toString());
    }
}
