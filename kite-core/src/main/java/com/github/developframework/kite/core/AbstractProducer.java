package com.github.developframework.kite.core;

import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.structs.TemplatePackage;

import java.util.List;

/**
 * @author qiushui on 2021-07-15.
 */
public abstract class AbstractProducer implements Producer {

    protected final KiteConfiguration configuration;

    protected final String namespace;

    protected final String templateId;

    protected final AssembleContext context;
    
    public AbstractProducer(KiteConfiguration configuration, DataModel dataModel, String namespace, String templateId, boolean assembleJson) {
        this.configuration = configuration;
        this.context = new AssembleContext(configuration, assembleJson);
        this.context.dataModel = dataModel;
        this.namespace = namespace;
        this.templateId = templateId;
    }

    public AbstractProducer(KiteConfiguration configuration, DataModel dataModel, List<TemplatePackage> templatePackages, boolean assembleJson) {
        this.configuration = configuration;
        this.context = new AssembleContext(configuration, assembleJson);
        this.context.dataModel = dataModel;
        templatePackages.forEach(this.context.extraTemplatePackages::putTemplatePackage);
        final TemplatePackage templatePackage = templatePackages.get(0);
        this.namespace = templatePackage.getNamespace();
        this.templateId = templatePackage.getUniqueTemplate().getId();
    }
}
