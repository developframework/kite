package com.github.developframework.kite.core.exception;

/**
 * 一对一链接数组大小不一致异常
 * @author qiuzhenhao
 */
public class LinkSizeNotEqualException extends KiteException {

    public LinkSizeNotEqualException(String namespace, String templateId) {
        super("The element <link> size is not equals parent array size in package \"%s\" template \"%s\"", namespace, templateId);
    }
}
