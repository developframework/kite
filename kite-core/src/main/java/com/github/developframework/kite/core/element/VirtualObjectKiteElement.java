package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.TemplateLocation;

/**
 * 虚拟对象节点
 *
 * @author qiushui on 2021-06-24.
 */
public class VirtualObjectKiteElement extends ContainerKiteElement {

    private String alias;

    public VirtualObjectKiteElement(TemplateLocation templateLocation) {
        super(templateLocation);
    }

    @Override
    public void configure(ElementDefinition elementDefinition) {
        super.configure(elementDefinition);
        alias = elementDefinition.getString(ElementDefinition.Attribute.ALIAS);
    }

    @Override
    public void assemble(AssembleContext context) {
        context.pushNodeProxy(context.peekNodeProxy().putObjectNode(alias));
        this.forEachAssemble(context);
        context.popNodeProxy();
    }
}
