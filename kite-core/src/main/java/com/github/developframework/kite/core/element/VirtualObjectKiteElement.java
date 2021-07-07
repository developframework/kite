package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.ElementAttributes;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.FragmentLocation;

/**
 * 虚拟对象节点
 *
 * @author qiushui on 2021-06-24.
 */
@ElementAttributes(ElementDefinition.Attribute.ALIAS)
public class VirtualObjectKiteElement extends ContainerKiteElement {

    private String alias;

    public VirtualObjectKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
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
