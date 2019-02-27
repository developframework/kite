package com.github.developframework.kite.core.exception;

import develop.toolkit.base.exception.FormatRuntimeException;
import lombok.extern.slf4j.Slf4j;

/**
 * Kite 异常
 * @author qiuzhenhao
 */
@Slf4j
public class KiteException extends FormatRuntimeException {

    public KiteException(String message) {
        super(message);
        log.error(super.getMessage());
    }

	public KiteException(String format, Object... parameters) {
		super(format, parameters);
        log.error(super.getMessage());
    }
}
