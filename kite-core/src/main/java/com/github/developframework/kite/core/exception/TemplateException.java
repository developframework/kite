package com.github.developframework.kite.core.exception;

/**
 * 模板异常
 *
 * @author qiuzhenhao
 */
public class TemplateException extends KiteException {

    public TemplateException(String format, Object... parameters) {
        super(format, parameters);
    }
}
