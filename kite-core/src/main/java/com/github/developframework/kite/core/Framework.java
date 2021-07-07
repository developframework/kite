package com.github.developframework.kite.core;

import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.strategy.KitePropertyNamingStrategy;
import com.github.developframework.kite.core.structs.TemplatePackage;

import java.util.List;

/**
 * @author qiushui on 2021-06-29.
 */
public interface Framework<CORE> {

    KitePropertyNamingStrategy namingStrategy();

    CORE getCore();

    Producer buildProducer(KiteConfiguration configuration, DataModel dataModel, String namespace, String templateId);

    Producer buildProducer(KiteConfiguration configuration, DataModel dataModel, List<TemplatePackage> templatePackages);
}
