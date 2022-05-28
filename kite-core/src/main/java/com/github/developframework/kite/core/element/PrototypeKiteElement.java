package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.util.Optional;

/**
 * 原生节点
 *
 * @author qiushui on 2021-06-29.
 */
public class PrototypeKiteElement extends ContentKiteElement {

    public PrototypeKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void assemble(AssembleContext context) {
        final Optional<Object> dataValue = KiteUtils.getDataValue(context, this);
        if (dataValue.isPresent()) {
            context.nodeStack.peek().putPrototype(context, displayName(context), dataValue.get());
        } else if (!contentAttributes.nullHidden) {
            context.nodeStack.peek().putNull(displayName(context));
        }
    }
}
