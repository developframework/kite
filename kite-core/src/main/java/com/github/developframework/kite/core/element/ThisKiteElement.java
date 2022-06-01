package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.utils.KiteUtils;

/**
 * this 元素
 *
 * @author qiushui on 2021-06-30.
 */
public final class ThisKiteElement extends ContainerKiteElement {

    public ThisKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void assemble(AssembleContext context) {
        if (elements.isEmpty()) {
            final Object v = KiteUtils.handleKiteConverter(context.dataModel, contentAttributes.converterComponent, context.valueStack.peek());
            context.nodeStack.peek().putValue(displayName(context), v, contentAttributes.xmlCDATA);
        } else {
            context.prepareNextOnlyNode(
                    context.nodeStack.peek().putObjectNode(displayName(context)),
                    this::forEachAssemble
            );
        }
    }
}
