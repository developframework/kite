package com.github.developframework.kite.core.exception;

/**
 * 无效的属性参数异常
 *
 * @author qiuzhenhao
 */
public class InvalidAttributeException extends KiteException {

    public InvalidAttributeException(String attributeName, String attributeValue, String hint) {
        super("属性“%s”无效的值“%s：%s”", attributeName, attributeValue, hint);
    }
}
