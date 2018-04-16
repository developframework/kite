package com.github.developframework.kite.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.developframework.kite.core.KiteFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * KiteFactory 的 FactoryBean
 * @author qiuzhenhao
 */
public class KiteFactoryFactoryBean implements FactoryBean<KiteFactory>  {

    @Getter
    @Setter
    private String[] configs;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public KiteFactory getObject() {
        return new KiteFactory(objectMapper, configs);
    }

    @Override
    public Class<?> getObjectType() {
        return KiteFactory.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
