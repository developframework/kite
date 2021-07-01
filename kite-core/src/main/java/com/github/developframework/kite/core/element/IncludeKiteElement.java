package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.TemplateLocation;
import org.apache.commons.lang3.StringUtils;

/**
 * 包含功能节点
 *
 * @author qiushui on 2021-06-24.
 */
public class IncludeKiteElement extends AbstractKiteElement {

    private TemplateLocation targetTemplateLocation;

    public IncludeKiteElement(TemplateLocation templateLocation) {
        super(templateLocation);
    }

    @Override
    public void configure(ElementDefinition elementDefinition) {
        super.configure(elementDefinition);
        final String namespace = elementDefinition.getString(ElementDefinition.Attribute.NAMESPACE);
        this.targetTemplateLocation = new TemplateLocation(
                StringUtils.isEmpty(namespace) ? templateLocation.getNamespace() : namespace,
                elementDefinition.getString(ElementDefinition.Attribute.ID)
        );
    }

    @Override
    public void assemble(AssembleContext context) {
        context
                .getConfiguration()
                .getTemplatePackageRegistry()
                .extractTemplate(targetTemplateLocation)
                .forEachAssemble(context);
    }
}
