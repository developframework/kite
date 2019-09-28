package com.github.developframework.kite.core.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Kite 异常
 * @author qiuzhenhao
 */
@Slf4j
public class KiteException extends RuntimeException {

    public KiteException(String message) {
        super(message);
        log.error(super.getMessage());
    }

	public KiteException(String format, Object... parameters) {
        super(String.format(format, parameters));
        log.error(super.getMessage());
    }
}
