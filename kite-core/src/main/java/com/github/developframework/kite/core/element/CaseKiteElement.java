package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.utils.KiteUtils;

/**
 * case节点
 *
 * @author qiushui on 2021-06-28.
 */
public final class CaseKiteElement extends ContainerKiteElement {

    private String caseTestFunctionValue;

    public CaseKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void configure(ElementDefinition elementDefinition) {
        super.configure(elementDefinition);
        caseTestFunctionValue = elementDefinition.getString(ElementDefinition.Attribute.CASE_TEST);
    }

    @Override
    public void assemble(AssembleContext context) {
        forEachAssemble(context);
    }

    public boolean match(AssembleContext context, Object value) {
        return KiteUtils.handleCastTest(context.dataModel, caseTestFunctionValue, value);
    }
}
