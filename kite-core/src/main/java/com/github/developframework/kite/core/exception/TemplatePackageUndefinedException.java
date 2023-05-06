package com.github.developframework.kite.core.exception;

/**
 * 模板包未定义异常
 *
 * @author qiuzhenhao
 */
public class TemplatePackageUndefinedException extends KiteException {

    public TemplatePackageUndefinedException(String namespace) {
        super("template-package namespace \"%s\" is undefined", namespace);
    }
}
