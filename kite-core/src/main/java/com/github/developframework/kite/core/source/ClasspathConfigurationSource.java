package com.github.developframework.kite.core.source;

import com.github.developframework.kite.core.exception.ConfigurationSourceException;

import java.io.InputStream;

/**
 * classpath文件配置源
 *
 * @author qiuzhenhao
 */
public class ClasspathConfigurationSource implements ConfigurationSource {

    private final String filename;

    public ClasspathConfigurationSource(String filename) {
        this.filename = filename.startsWith("/") ? filename : ("/" + filename);
    }

    @Override
    public InputStream getInputStream() {
        InputStream is = this.getClass().getResourceAsStream(filename);
        if (is == null) {
            throw new ConfigurationSourceException(filename);
        }
        return is;
    }

    @Override
    public String getSourceName() {
        return filename;
    }
}
