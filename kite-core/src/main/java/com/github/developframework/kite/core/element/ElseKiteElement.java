package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.TemplateLocation;

/**
 * else 节点
 *
 * @author qiushui on 2021-06-27.
 */
public final class ElseKiteElement extends ContainerKiteElement {

    public ElseKiteElement(TemplateLocation templateLocation) {
        super(templateLocation);
    }

    @Override
    public void assemble(AssembleContext context) {
        forEachAssemble(context);
    }
}
