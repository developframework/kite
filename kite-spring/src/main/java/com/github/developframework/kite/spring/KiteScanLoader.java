package com.github.developframework.kite.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.developframework.kite.core.ConfigurationSource;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.KiteFactory;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.saxparser.KiteConfigurationSaxReader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Kite的扫描加载器
 *
 * @author qiuzhenhao
 */
@AllArgsConstructor
public class KiteScanLoader {

    @Getter
    private final String locations;

    /**
     * 创建kiteConfiguration
     */
    public KiteConfiguration createKiteConfiguration() {
        final String[] locationsArray = StringUtils.tokenizeToStringArray(locations, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
        final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            final Set<ConfigurationSource> sources = new HashSet<>();
            for (String locationOne : locationsArray) {
                final Resource[] resources = resolver.getResources(locationOne);
                for (Resource resource : resources) {
                    sources.add(new SpringResourceConfigurationSource(resource));
                }
            }
            final KiteConfigurationSaxReader reader = new KiteConfigurationSaxReader(sources);
            return reader.readConfiguration();
        } catch (IOException e) {
            throw new KiteException("Happen IOException when Spring ResourcePatternResolver get resource: %s", e.getMessage());
        }
    }

    /**
     * 根据默认的ObjectMapper创建KiteFactory
     */
    public KiteFactory createKiteFactory() {
        return new KiteFactory(new ObjectMapper(), createKiteConfiguration());
    }

    /**
     * 根据自定义的ObjectMapper创建KiteFactory
     */
    public KiteFactory createKiteFactory(ObjectMapper objectMapper) {
        return new KiteFactory(objectMapper, createKiteConfiguration());
    }
}
