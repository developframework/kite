package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.FragmentLocation;

/**
 * slot 插槽元素
 *
 * @author qiushui on 2021-06-28.
 */
public class SlotKiteElement extends AbstractKiteElement {

    public SlotKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void configure(ElementDefinition elementDefinition) {
        // 无内容
    }

    @Override
    public void assemble(AssembleContext context) {
        // 无内容
    }
}
