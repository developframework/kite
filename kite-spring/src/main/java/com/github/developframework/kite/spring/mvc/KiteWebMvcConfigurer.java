package com.github.developframework.kite.spring.mvc;

import com.github.developframework.kite.core.KiteFactory;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.spring.ControllerKtlTemplateScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Configuration
public class KiteWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private KiteFactory defaultKiteFactory;
    @Lazy
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    @Autowired
    private DataModelReturnValueHandler dataModelReturnValueHandler;
    @Autowired
    private KiteResponseReturnValueHandler kiteResponseReturnValueHandler;

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        final Map<Method, FragmentLocation> map = new ControllerKtlTemplateScanner(defaultKiteFactory, requestMappingHandlerMapping).scan();
        dataModelReturnValueHandler.addFragmentLocations(map);
        kiteResponseReturnValueHandler.addFragmentLocations(map);
        returnValueHandlers.add(dataModelReturnValueHandler);
        returnValueHandlers.add(kiteResponseReturnValueHandler);
    }
}
