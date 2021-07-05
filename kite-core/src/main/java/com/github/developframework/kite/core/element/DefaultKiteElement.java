package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.FragmentLocation;

/**
 * @author qiushui on 2021-06-28.
 */
public final class DefaultKiteElement extends ContainerKiteElement {

    public DefaultKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void assemble(AssembleContext context) {
        forEachAssemble(context);
    }
}
