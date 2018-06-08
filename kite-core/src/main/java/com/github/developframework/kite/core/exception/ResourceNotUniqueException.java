package com.github.developframework.kite.core.exception;

/**
 * 资源定义不唯一异常
 * @author qiuzhenhao
 */
public class ResourceNotUniqueException extends KiteException {

    public ResourceNotUniqueException(String resourceName, String name) {
        super("%s \"%s\" have been defined.", resourceName, name);
    }
}
