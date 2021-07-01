package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.TemplateLocation;

/**
 * 插槽节点
 *
 * @author qiushui on 2021-06-28.
 */
public class SlotKiteElement extends AbstractKiteElement {

    public SlotKiteElement(TemplateLocation templateLocation) {
        super(templateLocation);
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
