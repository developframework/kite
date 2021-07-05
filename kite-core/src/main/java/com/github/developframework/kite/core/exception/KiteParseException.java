package com.github.developframework.kite.core.exception;

/**
 * 解析异常
 *
 * @author qiuzhenhao
 */
public class KiteParseException extends KiteException {

    public KiteParseException(String format, Object... objs) {
        super(format, objs);
    }
}
