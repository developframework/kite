package com.github.developframework.kite.fastjson;

import com.github.developframework.kite.core.Framework;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.Producer;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.strategy.KitePropertyNamingStrategy;
import com.github.developframework.kite.core.strategy.NamingStrategy;
import com.github.developframework.kite.core.structs.TemplatePackage;

import java.util.List;

/**
 * @author qiushui on 2021-07-15.
 */
public final class FastjsonFramework implements Framework<Void> {

    @Override
    public KitePropertyNamingStrategy namingStrategy() {
        return NamingStrategy.UNDERLINE.getNamingStrategy();
    }

    @Override
    public Void getCore() {
        return null;
    }

    @Override
    public Producer buildProducer(KiteConfiguration configuration, DataModel dataModel, String namespace, String templateId) {
        return new FastjsonProducer(configuration, dataModel, namespace, templateId);
    }

    @Override
    public Producer buildProducer(KiteConfiguration configuration, DataModel dataModel, List<TemplatePackage> templatePackages) {
        return new FastjsonProducer(configuration, dataModel, templatePackages);
    }
}
