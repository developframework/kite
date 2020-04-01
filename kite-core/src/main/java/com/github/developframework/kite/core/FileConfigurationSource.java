package com.github.developframework.kite.core;

import com.github.developframework.kite.core.exception.ConfigurationSourceException;

import java.io.InputStream;

/**
 * 文件配置源
 * @author qiuzhenhao
 */
public class FileConfigurationSource implements ConfigurationSource{

    private String filename;

    public FileConfigurationSource(String filename) {
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
