package com.github.developframework.kite.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.developframework.kite.boot.annotation.EnableKite;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.KiteFactory;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.strategy.DoNothingKitePropertyNamingStrategy;
import com.github.developframework.kite.core.strategy.KitePropertyNamingStrategy;
import com.github.developframework.kite.spring.KiteScanLoader;
import com.github.developframework.kite.spring.mvc.DataModelReturnValueHandler;
import com.github.developframework.kite.spring.mvc.KiteResponseReturnValueHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    @ConditionalOnProperty(name = "kite.json.useDefault", havingValue = "true", matchIfMissing = true)
    public KiteFactory kiteFactoryForDefaultObjectMapper(KiteProperties kiteProperties, ObjectMapper objectMapper) {
        log.info("Kite framework use Jackson default ObjectMapper.");
        final KiteScanLoader loader = new KiteScanLoader(kiteProperties.getLocations());
        KiteFactory kiteFactory = loader.createKiteFactory(objectMapper);
        configKiteConfiguration(kiteFactory.getKiteConfiguration(), kiteProperties);
        return kiteFactory;
    }

    @Bean
    @ConditionalOnProperty(name = "kite.json.useDefault", havingValue = "false")
    public KiteFactory kiteFactoryForNewObjectMapper(KiteProperties kiteProperties) {
        log.info("Kite framework use a new ObjectMapper.");
        final KiteScanLoader loader = new KiteScanLoader(kiteProperties.getLocations());
        KiteFactory kiteFactory = loader.createKiteFactory();
        configKiteConfiguration(kiteFactory.getKiteConfiguration(), kiteProperties);
        return kiteFactory;
    }

    @Bean
    public DataModelReturnValueHandler dataModelReturnValueHandler(KiteFactory kiteFactory) {
        return new DataModelReturnValueHandler(kiteFactory);
    }

    @Bean
    public KiteResponseReturnValueHandler kiteResponseReturnValueHandler(KiteFactory kiteFactory) {
        return new KiteResponseReturnValueHandler(kiteFactory);
    }

    /**
     * 配置KiteConfiguration
     *
     * @param kiteConfiguration    kiteConfiguration
     * @param kiteProperties kiteProperties
     */
    private void configKiteConfiguration(KiteConfiguration kiteConfiguration, KiteProperties kiteProperties) {
        final String jsonNamingStrategy = kiteProperties.getJson() == null ? null : kiteProperties.getJson().getNamingStrategy();
        final String xmlNamingStrategy = kiteProperties.getXml() == null ? null : kiteProperties.getXml().getNamingStrategy();

        kiteConfiguration.setForJsonStrategy(getKitePropertyNamingStrategy(jsonNamingStrategy));
        log.info("The \"{}\" is activate on JsonProducer.", kiteConfiguration.getForJsonStrategy().getClass().getSimpleName());

        kiteConfiguration.setForXmlStrategy(getKitePropertyNamingStrategy(xmlNamingStrategy));
        log.info("The \"{}\" is activate on XmlProducer.", kiteConfiguration.getForXmlStrategy().getClass().getSimpleName());

        if(kiteProperties.getXml() != null) {
            if(kiteProperties.getXml().getSuppressDeclaration() != null) {
                kiteConfiguration.setXmlSuppressDeclaration(kiteProperties.getXml().getSuppressDeclaration().booleanValue());
            }
        }
    }

    private KitePropertyNamingStrategy getKitePropertyNamingStrategy(String namingStrategyValue) {
        if (StringUtils.isNotBlank(namingStrategyValue)) {

            // 识别内置的命名策略
            if(namingStrategyValue.toLowerCase().equals("doNothing")) {
                return new DoNothingKitePropertyNamingStrategy();
            }
            try {
                Class<?> namingStrategyClass = Class.forName(namingStrategyValue);
                if (KitePropertyNamingStrategy.class.isAssignableFrom(namingStrategyClass)) {
                    return (KitePropertyNamingStrategy) namingStrategyClass.newInstance();
                } else {
                    throw new KiteException("Class \"%s\" is not subclass \"%s\".", namingStrategyValue, KitePropertyNamingStrategy.class.getName());
                }
            } catch (ClassNotFoundException e) {
                throw new KiteException("Class \"%s\" is not found.", namingStrategyValue);
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
