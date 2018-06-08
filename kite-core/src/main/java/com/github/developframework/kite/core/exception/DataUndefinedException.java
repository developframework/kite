package com.github.developframework.kite.core.exception;

/**
 * 数据未定义异常
 * @author qiuzhenhao
 */
public class DataUndefinedException extends KiteException {

    public DataUndefinedException(String dataName) {
        super("Data \"%s\" is undefined in DataModel.", dataName);
    }
}
