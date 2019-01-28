package com.github.developframework.kite.boot;

import com.github.developframework.kite.spring.mvc.DataModelReturnValueHandler;
import com.github.developframework.kite.spring.mvc.KiteResponseReturnValueHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class KiteWebMvcConfigurer implements WebMvcConfigurer {

	@Autowired
	private DataModelReturnValueHandler dataModelReturnValueHandler;

	@Autowired
	private KiteResponseReturnValueHandler kiteResponseReturnValueHandler;

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
		returnValueHandlers.add(kiteResponseReturnValueHandler);
		returnValueHandlers.add(dataModelReturnValueHandler);
	}
}
