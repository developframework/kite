package com.github.developframework.kite.core;

import com.github.developframework.kite.core.exception.KiteParseException;
import com.github.developframework.kite.core.parser.KtlParser;
import com.github.developframework.kite.core.parser.Parser;
import com.github.developframework.kite.core.parser.XmlParser;
import com.github.developframework.kite.core.source.ClasspathConfigurationSource;
import com.github.developframework.kite.core.source.ConfigurationSource;
import com.github.developframework.kite.core.structs.TemplatePackageRegistry;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qiushui on 2021-06-24.
 */
@Slf4j
public abstract class KiteFactoryBuilder {

    /**
     * 构建空的KiteFactory
     *
     * @param options 配置项
     * @return KiteFactory
     */
    public static KiteFactory build(KiteOptions options) {
        final TemplatePackageRegistry registry = new TemplatePackageRegistry();
        return new KiteFactory(new KiteConfiguration(options, registry));
    }

    /**
     * 从XML构建
     *
     * @param options  配置项
     * @param xmlFiles classpath下的xml文件名
     * @return KiteFactory
     */
    public static KiteFactory buildFromClasspathXml(KiteOptions options, String... xmlFiles) {
        final Set<ConfigurationSource> sources = Stream.of(xmlFiles).map(ClasspathConfigurationSource::new).collect(Collectors.toSet());
        return buildFromXml(options, sources);
    }

    /**
     * 从XML构建
     *
     * @param options 配置项
     * @param sources 资源
     * @return KiteFactory
     */
    public static KiteFactory buildFromXml(KiteOptions options, Set<ConfigurationSource> sources) {
        final XmlParser parser = new XmlParser();
        return buildKiteFactory(options, parser, sources);
    }

    /**
     * 从KTL构建
     *
     * @param options  配置项
     * @param ktlFiles classpath下的ktl文件名
     * @return KiteFactory
     */
    public static KiteFactory buildFromClasspathKtl(KiteOptions options, String... ktlFiles) {
        final Set<ConfigurationSource> sources = Stream.of(ktlFiles).map(ClasspathConfigurationSource::new).collect(Collectors.toSet());
        return buildFromKtl(options, sources);
    }

    /**
     * 从KTL构建
     *
     * @param options 配置项
     * @param sources 资源
     * @return KiteFactory
     */
    public static KiteFactory buildFromKtl(KiteOptions options, Set<ConfigurationSource> sources) {
        final KtlParser parser = new KtlParser(options.getKtlIndent());
        return buildKiteFactory(options, parser, sources);
    }

    private static KiteFactory buildKiteFactory(KiteOptions options, Parser parser, Set<ConfigurationSource> sources) {
        final TemplatePackageRegistry registry = new TemplatePackageRegistry();
        sources
                .stream()
                .flatMap(source -> {
                    try {
                        return parser.read(source).stream();
                    } catch (IOException e) {
                        throw new KiteParseException("Error parsing configuration source \"%s\": %s", source.getSourceName(), e.getMessage());
                    } finally {
                        log.debug("【Kite】The configuration source \"{}\" has been loaded", source.getSourceName());
                    }
                })
                .forEach(registry::putTemplatePackage);
        return new KiteFactory(new KiteConfiguration(options, registry));
    }
}
