package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.util.Optional;

/**
 * 扁平化节点
 *
 * @author qiushui on 2021-07-16.
 */
public final class FlatKiteElement extends ContainerKiteElement {

    public FlatKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void assemble(AssembleContext context) {
        final Optional<Object> dataValue = KiteUtils.getDataValue(context, this);
        context.pushValue(dataValue.orElse(null));
        super.forEachAssemble(context);
        context.popValue();
    }
}
