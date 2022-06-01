package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.util.Optional;

/**
 * object 对象元素
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
            context.prepareNext(
                    context.nodeStack.peek().putObjectNode(displayName(context)),
                    dataValue.get(),
                    this::forEachAssemble
            );
        } else if (!contentAttributes.nullHidden) {
            context.nodeStack.peek().putNull(displayName(context));
        }
    }
}
