package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.ElementAttributes;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.FragmentLocation;
import org.apache.commons.lang3.StringUtils;

/**
 * 包含功能节点
 *
 * @author qiushui on 2021-06-24.
 */
@ElementAttributes({
        ElementDefinition.Attribute.NAMESPACE,
        ElementDefinition.Attribute.ID,
})
public class IncludeKiteElement extends AbstractKiteElement {

    private FragmentLocation targetFragmentLocation;

    public IncludeKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void configure(ElementDefinition elementDefinition) {
        super.configure(elementDefinition);
        final String namespace = elementDefinition.getString(ElementDefinition.Attribute.NAMESPACE);
        this.targetFragmentLocation = new FragmentLocation(
                StringUtils.isEmpty(namespace) ? fragmentLocation.getNamespace() : namespace,
                elementDefinition.getString(ElementDefinition.Attribute.ID)
        );
    }

    @Override
    public void assemble(AssembleContext context) {
        context
                .extractFragment(targetFragmentLocation.getNamespace(), targetFragmentLocation.getFragmentId())
                .forEachAssemble(context);
    }
}
