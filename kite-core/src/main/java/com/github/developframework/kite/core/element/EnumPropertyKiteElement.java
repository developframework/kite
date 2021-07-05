package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.util.Optional;

/**
 * @author qiushui on 2021-06-24.
 */
public class EnumPropertyKiteElement extends ContainerKiteElement {

    public EnumPropertyKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void assemble(AssembleContext context) {
        final Optional<Object> dataValue = KiteUtils.getDataValue(context, this);
        if (dataValue.isPresent()) {
            final String v = dataValue.get().toString();
            for (KiteElement element : elements) {
                EnumValueKiteElement e = (EnumValueKiteElement) element;
                if (e.getEnumValue().equals(v)) {
                    context.peekNodeProxy().putValue(displayName(context), e.getEnumText(), contentAttributes.xmlCDATA);
                    return;
                }
            }
            throw new KiteException("No enum value for \"%s\" in template \"%s\".", v, fragmentLocation);
        } else if (!contentAttributes.nullHidden) {
            context.peekNodeProxy().putNull(displayName(context));
        }
    }
}
