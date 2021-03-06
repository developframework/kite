package com.github.developframework.kite.spring;

import com.github.developframework.kite.core.source.ConfigurationSource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * spring resource配置源
 *
 * @author qiuzhenhao
 */
@RequiredArgsConstructor
public class SpringResourceConfigurationSource implements ConfigurationSource {

    // spring的Resource接口
    private final Resource resource;

    @Override
    public InputStream getInputStream() throws IOException {
        return resource.getInputStream();
    }

    @Override
    public String getSourceName() {
        return resource.getFilename();
    }
}
