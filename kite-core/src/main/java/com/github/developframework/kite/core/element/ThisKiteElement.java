package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.TemplateLocation;
import com.github.developframework.kite.core.utils.KiteUtils;

/**
 * this节点
 *
 * @author qiushui on 2021-06-30.
 */
public final class ThisKiteElement extends ContainerKiteElement {

    public ThisKiteElement(TemplateLocation templateLocation) {
        super(templateLocation);
    }

    @Override
    public void assemble(AssembleContext context) {
        if (elements.isEmpty()) {
            final Object v = KiteUtils.handleKiteConverter(context.dataModel, contentAttributes.converterValue, context.peekValue());
            context.peekNodeProxy().putValue(displayName(context), v, contentAttributes.xmlCDATA);
        } else {
            context.parentPutNodeProxyAndPush(displayName(context));
            forEachAssemble(context);
            context.popNodeProxy();
        }
    }
}
