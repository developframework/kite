package com.github.developframework.kite.core.exception;

/**
 * Kite 异常
 * @author qiuzhenhao
 */
public class KiteException extends RuntimeException {

    public KiteException(String message) {
        super(message);
    }

	public KiteException(String format, Object... parameters) {
        super(String.format(format, parameters));
    }
}
