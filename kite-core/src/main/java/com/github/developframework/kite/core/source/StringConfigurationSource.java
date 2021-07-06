package com.github.developframework.kite.core.source;

import lombok.AllArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * string配置源
 *
 * @author qiuzhenhao
 */
@AllArgsConstructor
public class StringConfigurationSource implements ConfigurationSource {

    private final String string;

    private final Charset charset;

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(string.getBytes(charset));
    }

    @Override
    public String getSourceName() {
        return "string source";
    }
}
