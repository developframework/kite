package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.ElementAttributes;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.FragmentLocation;
import lombok.Getter;

/**
 * enum 元素
 *
 * @author qiushui on 2021-06-24.
 */
@ElementAttributes({
        ElementDefinition.Attribute.ENUM_VALUE,
        ElementDefinition.Attribute.ENUM_TEXT
})
@Getter
public class EnumValueKiteElement extends AbstractKiteElement {

    private String enumValue;

    private String enumText;

    public EnumValueKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void configure(ElementDefinition elementDefinition) {
        enumValue = elementDefinition.getString(ElementDefinition.Attribute.ENUM_VALUE);
        enumText = elementDefinition.getString(ElementDefinition.Attribute.ENUM_TEXT);
    }

    @Override
    public void assemble(AssembleContext context) {
        // 无内容
    }
}
