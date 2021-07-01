package com.github.developframework.kite.spring;

import com.github.developframework.kite.core.Framework;
import com.github.developframework.kite.core.KiteFactory;
import com.github.developframework.kite.core.KiteFactoryBuilder;
import com.github.developframework.kite.core.KiteOptions;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.source.ConfigurationSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Kite的扫描加载器
 *
 * @author qiuzhenhao
 */
@RequiredArgsConstructor
public class KiteScanLoader {

    @Getter
    private final String locations;

    /**
     * 扫描配置文件并创建KiteFactory
     */
    public KiteFactory createKiteFactory(Framework<?> jsonFramework, Framework<?> xmlFramework, KiteOptions options) {
        final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        final String[] locationsArray = StringUtils.tokenizeToStringArray(locations, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
        final Set<ConfigurationSource> sources = Stream
                .of(locationsArray)
                .flatMap(location -> {
                    try {
                        return Stream.of(resolver.getResources(location)).map(SpringResourceConfigurationSource::new);
                    } catch (IOException e) {
                        throw new KiteException("Happen IOException when Spring ResourcePatternResolver get resource: %s", e.getMessage());
                    }
                })
                .collect(Collectors.toSet());
        final KiteFactory kiteFactory = KiteFactoryBuilder.buildFromXml(options, sources);
        kiteFactory.useJsonFramework(jsonFramework);
        kiteFactory.useXmlFramework(xmlFramework);
        return kiteFactory;
    }
}
