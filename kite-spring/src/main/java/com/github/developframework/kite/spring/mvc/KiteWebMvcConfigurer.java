package com.github.developframework.kite.spring.mvc;

import com.github.developframework.kite.spring.KiteStartupListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class KiteWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(dataModelReturnValueHandler());
        returnValueHandlers.add(kiteResponseReturnValueHandler());
    }

    @Bean
    public DataModelReturnValueHandler dataModelReturnValueHandler() {
        return new DataModelReturnValueHandler();
    }

    @Bean
    public KiteResponseReturnValueHandler kiteResponseReturnValueHandler() {
        return new KiteResponseReturnValueHandler();
    }

    @Bean
    public KiteStartupListener startupListener() {
        return new KiteStartupListener();
    }
}
