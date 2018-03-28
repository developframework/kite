package com.github.developframework.kite.spring;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.KiteFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * KiteScan的spring bean 解析器
 *
 * @author qiuzhenhao
 */
public class KiteScanBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return KiteFactory.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        final String locations = element.getAttribute("locations");
        final String objectMapperRef = element.getAttribute("object-mapper-ref");
        if (StringUtils.hasText(locations)) {
            KiteScanLoader loader = new KiteScanLoader(locations);
            KiteConfiguration kiteConfiguration = loader.createKiteConfiguration();
            builder.addConstructorArgValue(kiteConfiguration);
            if (StringUtils.hasText(objectMapperRef)) {
                builder.addConstructorArgReference(objectMapperRef);
            }
        }
    }
}
