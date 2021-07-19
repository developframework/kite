package com.github.developframework.kite.spring.mvc;

import com.github.developframework.kite.core.KiteFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class KiteWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    @Qualifier("defaultKiteFactory")
    private KiteFactory kiteFactory;

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(new DataModelReturnValueHandler(kiteFactory));
        returnValueHandlers.add(new KiteResponseReturnValueHandler(kiteFactory));
    }
}
