package com.github.developframework.kite.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.github.developframework.kite.core.saxparser.KiteConfigurationSaxReader;
import lombok.Getter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Kite 工厂
 * @author qiuzhenhao
 */
public class KiteFactory {

    @Getter
    private KiteConfiguration kiteConfiguration;

    public KiteFactory(String... configs) {
        this(new ObjectMapper(), configs);
    }

    public KiteFactory(ObjectMapper objectMapper, String... configs) {
        Objects.requireNonNull(objectMapper);
        Objects.requireNonNull(configs);
        Set<ConfigurationSource> sources = new HashSet<>();
        for (String config : configs) {
            sources.add(new FileConfigurationSource(config));
        }
        KiteConfigurationSaxReader reader = new KiteConfigurationSaxReader(sources);
        kiteConfiguration = reader.readConfiguration();
        kiteConfiguration.setObjectMapper(objectMapper);
    }

    public KiteFactory(ObjectMapper objectMapper, Set<ConfigurationSource> sources) {
        Objects.requireNonNull(objectMapper);
        Objects.requireNonNull(sources);
        KiteConfigurationSaxReader reader = new KiteConfigurationSaxReader(sources);
        kiteConfiguration = reader.readConfiguration();
        kiteConfiguration.setObjectMapper(objectMapper);
    }

    public KiteFactory(ObjectMapper objectMapper, KiteConfiguration kiteConfiguration) {
        Objects.requireNonNull(kiteConfiguration);
        Objects.requireNonNull(objectMapper);
        kiteConfiguration.setObjectMapper(objectMapper);
        this.kiteConfiguration = kiteConfiguration;
    }

    /**
     * 返回ObjectMapper
     * @return ObjectMapper
     */
    public ObjectMapper getObjectMapper() {
        return kiteConfiguration.getObjectMapper();
    }

    /**
     * 获得Json生成器
     * @return Json生成器
     */
    public JsonProducer getJsonProducer() {
        return new DefaultJsonProducer(kiteConfiguration);
    }

    /**
     * 获得Xml生成器
     * @return Xml生成器
     */
    public XmlProducer getXmlProducer() {
        return new DefaultXmlProducer(kiteConfiguration);
    }

    /**
     * 设置propertyNamingStrategy
     * @param propertyNamingStrategy propertyNamingStrategy
     */
    public void setPropertyNamingStrategy(PropertyNamingStrategy propertyNamingStrategy) {
        kiteConfiguration.getObjectMapper().setPropertyNamingStrategy(propertyNamingStrategy);
    }

}
