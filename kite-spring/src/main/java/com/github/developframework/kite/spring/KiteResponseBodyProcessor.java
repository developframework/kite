package com.github.developframework.kite.spring;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * kite 响应体内容处理器
 *
 * @author qiuzhenhao on 2021-04-28.
 */
public interface KiteResponseBodyProcessor {

    /**
     * 在响应内容写出到输出流前处理
     *
     * @param methodParameter 方法参数
     * @param webRequest      请求体
     * @param content         响应内容
     */
    void beforeWrite(MethodParameter methodParameter, NativeWebRequest webRequest, String content);
}
