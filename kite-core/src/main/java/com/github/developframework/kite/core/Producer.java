package com.github.developframework.kite.core;

import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 生产者接口
 *
 * @author qiuzhenhao
 */
public interface Producer {

    /**
     * 生产字符串
     *
     * @param pretty 是否美化
     * @return 字符串
     */
    String produce(boolean pretty);

    /**
     * 输出到流
     *
     * @param charset 字符集
     * @param pretty  是否美化
     */
    void output(OutputStream outputStream, Charset charset, boolean pretty);
}
