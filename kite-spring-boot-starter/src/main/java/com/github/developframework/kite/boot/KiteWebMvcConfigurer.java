package com.github.developframework.kite.boot;

import com.github.developframework.kite.spring.mvc.DataModelReturnValueHandler;
import com.github.developframework.kite.spring.mvc.KiteResponseReturnValueHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@AllArgsConstructor
public class KiteWebMvcConfigurer implements WebMvcConfigurer {

    private final DataModelReturnValueHandler dataModelReturnValueHandler;

    private final KiteResponseReturnValueHandler kiteResponseReturnValueHandler;

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(kiteResponseReturnValueHandler);
        returnValueHandlers.add(dataModelReturnValueHandler);
    }
}
