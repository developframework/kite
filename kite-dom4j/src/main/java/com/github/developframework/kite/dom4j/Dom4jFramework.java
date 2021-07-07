package com.github.developframework.kite.dom4j;

import com.github.developframework.kite.core.Framework;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.Producer;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.strategy.KitePropertyNamingStrategy;
import com.github.developframework.kite.core.strategy.NamingStrategy;
import com.github.developframework.kite.core.structs.TemplatePackage;

import java.util.List;

/**
 * Dom4j实现
 *
 * @author qiushui on 2021-06-29.
 */
public class Dom4jFramework implements Framework<Void> {
    @Override
    public KitePropertyNamingStrategy namingStrategy() {
        return NamingStrategy.MIDDLE_LINE.getNamingStrategy();
    }

    @Override
    public Void getCore() {
        return null;
    }

    @Override
    public Producer buildProducer(KiteConfiguration configuration, DataModel dataModel, String namespace, String templateId) {
        return new Dom4jProducer(configuration, dataModel, namespace, templateId);
    }

    @Override
    public Producer buildProducer(KiteConfiguration configuration, DataModel dataModel, List<TemplatePackage> templatePackages) {
        return new Dom4jProducer(configuration, dataModel, templatePackages);
    }
}
