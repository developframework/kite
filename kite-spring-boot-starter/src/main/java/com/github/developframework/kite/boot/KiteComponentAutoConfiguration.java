package com.github.developframework.kite.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.developframework.kite.boot.annotation.EnableKite;
import com.github.developframework.kite.core.KiteFactory;
import com.github.developframework.kite.spring.KiteScanLoader;
import com.github.developframework.kite.spring.mvc.DataModelReturnValueHandler;
import com.github.developframework.kite.spring.mvc.KiteResponseReturnValueHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 自动配置Kite
 * {@link EnableKite}
 */
@Configuration
@Import(KiteWebMvcConfigurer.class)
@AutoConfigureAfter(JacksonAutoConfiguration.class)
@Slf4j
public class KiteComponentAutoConfiguration {

    @Bean
    @Autowired
    @ConditionalOnProperty(name = "kite.objectmapper.usedefault", havingValue = "true", matchIfMissing = true)
    public KiteFactory kiteFactoryForDefaultObjectMapper(KiteProperties kiteProperties, ObjectMapper objectMapper) {
        log.info("Kite framework use Jackson default ObjectMapper.");
	    final KiteScanLoader loader = new KiteScanLoader(kiteProperties.getLocations());
        return loader.createKiteFactory(objectMapper);
    }

    @Bean
    @ConditionalOnProperty(name = "kite.objectmapper.usedefault", havingValue = "false")
    public KiteFactory kiteFactoryForNewObjectMapper(KiteProperties kiteProperties) {
        log.info("Kite framework use a new ObjectMapper.");
        final KiteScanLoader loader = new KiteScanLoader(kiteProperties.getLocations());
        return loader.createKiteFactory();
    }

    @Bean
    public DataModelReturnValueHandler dataModelReturnValueHandler(KiteFactory kiteFactory) {
        return new DataModelReturnValueHandler(kiteFactory);
    }

    @Bean
    public KiteResponseReturnValueHandler kiteResponseReturnValueHandler(KiteFactory kiteFactory) {
        return new KiteResponseReturnValueHandler(kiteFactory);
    }
}
