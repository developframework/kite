package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.TemplateLocation;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.util.Optional;

/**
 * 原生节点
 *
 * @author qiushui on 2021-06-29.
 */
public class PrototypeKiteElement extends ContentKiteElement {

    public PrototypeKiteElement(TemplateLocation templateLocation) {
        super(templateLocation);
    }

    @Override
    public void assemble(AssembleContext context) {
        final Optional<Object> dataValue = KiteUtils.getDataValue(context, this);
        if (dataValue.isPresent()) {
            context.peekNodeProxy().putPrototype(context, displayName(context), dataValue.get());
        } else if (!contentAttributes.nullHidden) {
            context.peekNodeProxy().putNull(displayName(context));
        }
    }
}
