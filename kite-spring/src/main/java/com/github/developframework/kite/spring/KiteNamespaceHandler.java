package com.github.developframework.kite.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * spring注册kite命名空间的Handler
 *
 * @author qiuzhenhao
 */
public class KiteNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("scan", new KiteScanBeanDefinitionParser());
    }
}
