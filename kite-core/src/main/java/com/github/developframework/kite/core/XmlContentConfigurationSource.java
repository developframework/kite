package com.github.developframework.kite.core;

import lombok.AllArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * xml内容配置源
 * @author qiuzhenhao
 */
@AllArgsConstructor
public class XmlContentConfigurationSource implements ConfigurationSource{

    private final String xml;

    private final Charset charset;

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(xml.getBytes(charset));
    }

    @Override
    public String getSourceName() {
        return "xml content source";
    }
}
