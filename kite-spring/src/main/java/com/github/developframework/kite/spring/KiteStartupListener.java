package com.github.developframework.kite.spring;

import com.github.developframework.kite.core.KiteFactory;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.spring.mvc.DataModelReturnValueHandler;
import com.github.developframework.kite.spring.mvc.KiteResponseReturnValueHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author qiushui on 2022-05-10.
 */
public class KiteStartupListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private KiteFactory defaultKiteFactory;
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    @Autowired
    private DataModelReturnValueHandler dataModelReturnValueHandler;
    @Autowired
    private KiteResponseReturnValueHandler kiteResponseReturnValueHandler;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        final Map<Method, FragmentLocation> map = new ControllerKtlTemplateScanner(defaultKiteFactory, requestMappingHandlerMapping).scan();
        dataModelReturnValueHandler.setFragmentLocationMap(map);
        kiteResponseReturnValueHandler.setFragmentLocationMap(map);
    }
}
