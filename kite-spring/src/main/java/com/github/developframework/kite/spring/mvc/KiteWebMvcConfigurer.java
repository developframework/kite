package com.github.developframework.kite.spring.mvc;

import com.github.developframework.kite.core.KiteFactory;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.spring.ControllerKtlTemplateScanner;
import com.github.developframework.kite.spring.KiteResponseBodyProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Configuration
public class KiteWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    @Qualifier("defaultKiteFactory")
    private KiteFactory kiteFactory;

    @Autowired(required = false)
    private KiteResponseBodyProcessor kiteResponseBodyProcessor;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        final Map<Method, FragmentLocation> fragmentLocationMap = new ControllerKtlTemplateScanner(kiteFactory, handlerMapping).scan();
        returnValueHandlers.add(new DataModelReturnValueHandler(kiteFactory, kiteResponseBodyProcessor, fragmentLocationMap));
        returnValueHandlers.add(new KiteResponseReturnValueHandler(kiteFactory, kiteResponseBodyProcessor, fragmentLocationMap));
    }
}
