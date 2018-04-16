package com.github.developframework.kite.core;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * xml内容配置源
 * @author qiuzhenhao
 */
public class XmlContentConfigurationSource implements ConfigurationSource{

    private String xml;

    private Charset charset;

    public XmlContentConfigurationSource(String xml, Charset charset) {
        this.xml = xml;
        this.charset = charset;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(xml.getBytes(charset));
    }

    @Override
    public String getSourceName() {
        return "xml content source";
    }
}
