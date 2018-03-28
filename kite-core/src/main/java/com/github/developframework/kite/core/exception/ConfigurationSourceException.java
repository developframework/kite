package com.github.developframework.kite.core.exception;

/**
 * 配置源异常
 * @author qiuzhenhao
 */
public class ConfigurationSourceException extends KiteException {

    public ConfigurationSourceException(String source) {
        super("The kite configuration source \"\" is not found.", source);
    }
}
