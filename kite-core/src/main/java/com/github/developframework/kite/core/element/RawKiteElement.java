package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.util.Optional;

/**
 * raw 原文元素
 *
 * @author qiushui on 2021-06-28.
 */
public class RawKiteElement extends ContentKiteElement {

    public RawKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void assemble(AssembleContext context) {
        final Optional<Object> dataValue = KiteUtils.getDataValue(context, this);
        if (dataValue.isPresent()) {
            context.nodeStack.peek().putRaw(context, displayName(context), (String) dataValue.get());
        } else if (!contentAttributes.nullHidden) {
            context.nodeStack.peek().putNull(displayName(context));
        }
    }
}
