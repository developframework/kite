package com.github.developframework.kite.spring;

import com.github.developframework.kite.core.KiteFactory;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.spring.mvc.DataModelReturnValueHandler;
import com.github.developframework.kite.spring.mvc.KiteResponseReturnValueHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author qiushui on 2022-05-09.
 */
@Configuration
public class KiteComponentConfiguration {

    @Autowired
    private KiteFactory defaultKiteFactory;

    @Autowired(required = false)
    private KiteResponseBodyProcessor kiteResponseBodyProcessor;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Bean
    public Map<Method, FragmentLocation> fragmentLocationMap() {
        return new ControllerKtlTemplateScanner(defaultKiteFactory, requestMappingHandlerMapping).scan();
    }

    @Bean
    public DataModelReturnValueHandler dataModelReturnValueHandler() {
        return new DataModelReturnValueHandler(defaultKiteFactory, kiteResponseBodyProcessor, fragmentLocationMap());
    }

    @Bean
    public KiteResponseReturnValueHandler kiteResponseReturnValueHandler() {
        return new KiteResponseReturnValueHandler(defaultKiteFactory, kiteResponseBodyProcessor, fragmentLocationMap());
    }
}
