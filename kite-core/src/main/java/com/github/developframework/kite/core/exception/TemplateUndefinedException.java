package com.github.developframework.kite.core.exception;

/**
 * 模板未定义异常
 * @author qiuzhenhao
 */
public class TemplateUndefinedException extends KiteException {

    public TemplateUndefinedException(String namespace, String templateId) {
        super("The template \"%s\" is undefined in template-package \"%s\".", templateId, namespace);
    }
}
