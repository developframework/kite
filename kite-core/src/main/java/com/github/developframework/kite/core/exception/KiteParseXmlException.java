package com.github.developframework.kite.core.exception;

/**
 * 解析XML异常
 *
 * @author qiuzhenhao
 */
public class KiteParseXmlException extends KiteException {

    public KiteParseXmlException(String format, Object... objs) {
        super(format, objs);
    }
}
