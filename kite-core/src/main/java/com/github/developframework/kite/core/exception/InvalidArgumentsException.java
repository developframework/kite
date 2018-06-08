package com.github.developframework.kite.core.exception;

/**
 * 无效的参数异常
 * @author qiuzhenhao
 */
public class InvalidArgumentsException extends KiteException {

    public InvalidArgumentsException(String attributeName, String argumentName, String hint) {
        super("Invalid arguments \"%s\" in attribute \"%s\": %s", argumentName, attributeName, hint);
    }
}
