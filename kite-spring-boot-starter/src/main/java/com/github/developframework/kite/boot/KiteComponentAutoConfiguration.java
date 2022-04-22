package com.github.developframework.kite.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.developframework.kite.core.Framework;
import com.github.developframework.kite.core.KiteFactory;
import com.github.developframework.kite.core.KiteOptions;
import com.github.developframework.kite.dom4j.Dom4jFramework;
import com.github.developframework.kite.jackson.JacksonFramework;
import com.github.developframework.kite.spring.KiteScanLoader;
import com.github.developframework.kite.spring.mvc.KiteWebMvcConfigurer;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 自动配置Kite
 */
@Slf4j
@Configuration
@Import(KiteWebMvcConfigurer.class)
@EnableConfigurationProperties(KiteProperties.class)
public class KiteComponentAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public KiteFactory defaultKiteFactory(
            KiteProperties kiteProperties,
            @Qualifier("jsonFramework") Framework<?> jsonFramework,
            @Qualifier("xmlFramework") Framework<?> xmlFramework
    ) {
        final KiteScanLoader loader = new KiteScanLoader(kiteProperties.getLocations());
        final KiteOptions options = new KiteOptions();
        configureOptions(options, kiteProperties);
        return loader.createKiteFactory(jsonFramework, xmlFramework, options);
    }

    @ConditionalOnClass(ObjectMapper.class)
    @ConditionalOnMissingBean(name = "jsonFramework")
    @Bean("jsonFramework")
    public JacksonFramework jacksonFramework(ObjectMapper objectMapper) {
        log.info("【Kite Boot】加载 jackson Framework");
        return new JacksonFramework(objectMapper);
    }

    @ConditionalOnClass(Document.class)
    @ConditionalOnMissingBean(name = "xmlFramework")
    @Bean("xmlFramework")
    public Dom4jFramework dom4jFramework() {
        log.info("【Kite Boot】加载 dom4j Framework");
        return new Dom4jFramework();
    }

    /**
     * 配置options
     *
     * @param options        options
     * @param kiteProperties kiteProperties
     */
    private void configureOptions(KiteOptions options, KiteProperties kiteProperties) {
        options.getJson().setNamingStrategy(kiteProperties.getJson().getNamingStrategy());
        log.info("json producer 激活命名策略“{}”", kiteProperties.getJson().getNamingStrategy());

        options.getXml().setNamingStrategy(kiteProperties.getXml().getNamingStrategy());
        log.info("xml producer 激活命名策略“{}”", kiteProperties.getXml().getNamingStrategy());

        if (kiteProperties.getXml() != null) {
            options.getXml().setSuppressDeclaration(kiteProperties.getXml().isSuppressDeclaration());
        }
    }
}
