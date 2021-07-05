package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.util.Optional;

/**
 * 对象元素
 *
 * @author qiushui on 2021-06-24.
 */
public class ObjectKiteElement extends ContainerKiteElement {

    public ObjectKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void assemble(AssembleContext context) {
        final Optional<Object> dataValue = KiteUtils.getDataValue(context, this);
        if (dataValue.isPresent()) {
            context.parentPutNodeProxyAndPush(displayName(context));
            context.pushValue(dataValue.get());
            forEachAssemble(context);
            context.pop();
        } else if (!contentAttributes.nullHidden) {
            context.peekNodeProxy().putNull(displayName(context));
        }
    }
}
