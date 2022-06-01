package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.structs.ElementAttributes;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.FragmentLocation;
import lombok.Getter;

/**
 * template 模板
 *
 * @author qiushui on 2021-06-23.
 */
@ElementAttributes(
        value = ElementDefinition.Attribute.XML_ROOT,
        baseClass = ArrayKiteElement.class
)
@Getter
public final class Template extends Fragment {

    // xml根节点名称
    private String xmlRoot;

    // 内置array节点
    private ArrayKiteElement innerArrayKiteElement;

    public Template(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void configure(ElementDefinition elementDefinition) {
        super.configure(elementDefinition);
        xmlRoot = elementDefinition.getString(ElementDefinition.Attribute.XML_ROOT, "xml");
        if (contentAttributes.dataDefinition != DataDefinition.EMPTY) {
            // 如果data不为空的话需要构建一个内置array节点
            innerArrayKiteElement = new ArrayKiteElement(fragmentLocation);
            innerArrayKiteElement.configure(elementDefinition);
        }
    }
}
