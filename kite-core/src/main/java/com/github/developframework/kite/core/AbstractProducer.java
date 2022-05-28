package com.github.developframework.kite.core;

import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.element.ArrayKiteElement;
import com.github.developframework.kite.core.element.Template;
import com.github.developframework.kite.core.node.ArrayNodeProxy;
import com.github.developframework.kite.core.node.NodeProxy;
import com.github.developframework.kite.core.node.ObjectNodeProxy;
import com.github.developframework.kite.core.structs.TemplatePackage;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.util.List;

/**
 * @author qiushui on 2021-07-15.
 */
public abstract class AbstractProducer implements Producer {

    protected final KiteConfiguration configuration;

    protected final String namespace;

    protected final String templateId;

    protected final AssembleContext context;
    
    public AbstractProducer(KiteConfiguration configuration, DataModel dataModel, String namespace, String templateId) {
        this.configuration = configuration;
        this.context = buildAssembleContext(dataModel);
        this.namespace = namespace;
        this.templateId = templateId;
    }

    public AbstractProducer(KiteConfiguration configuration, DataModel dataModel, List<TemplatePackage> templatePackages) {
        this.configuration = configuration;
        this.context = buildAssembleContext(dataModel);
        templatePackages.forEach(this.context.extraTemplatePackages::putTemplatePackage);
        final TemplatePackage templatePackage = templatePackages.get(0);
        this.namespace = templatePackage.getNamespace();
        this.templateId = templatePackage.getUniqueTemplate().getId();
    }

    protected abstract AssembleContext buildAssembleContext(DataModel dataModel);

    protected NodeProxy buildRootNodeProxy() {
        final Template template = context.extractTemplate(namespace, templateId);
        final DataDefinition dataDefinition = template.getContentAttributes().dataDefinition;
        Object rootValue = null;
        if (dataDefinition != DataDefinition.EMPTY) {
            rootValue = context.dataModel.getData(dataDefinition.getExpression()).orElse(null);
            if (rootValue == null) {
                return null;
            }
        }
        context.valueStack.push(context.dataModel);
        final NodeProxy rootNodeProxy;
        if (KiteUtils.objectIsArray(rootValue)) {
            // 以数组为根节点
            ArrayNodeProxy arrayNodeProxy = context.createArrayNodeProxy();
            ArrayKiteElement innerArrayKiteElement = template.getInnerArrayKiteElement();
            innerArrayKiteElement.assembleArrayItems(context, innerArrayKiteElement.getArrayAttributes(), rootValue, arrayNodeProxy);
            rootNodeProxy = arrayNodeProxy;
        } else {
            // 以对象为根节点
            ObjectNodeProxy objectNodeProxy = context.createObjectNodeProxy();
            context.prepareNextOnlyNode(objectNodeProxy, template::assemble);
            rootNodeProxy = objectNodeProxy;
        }
        context.valueStack.pop();
        return rootNodeProxy;
    }
}
