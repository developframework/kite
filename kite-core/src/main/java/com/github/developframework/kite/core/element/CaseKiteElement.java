package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.dynamic.CaseTestFunction;
import com.github.developframework.kite.core.dynamic.LiteralCaseTestFunction;
import com.github.developframework.kite.core.structs.ElementAttributes;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.structs.KiteComponent;
import com.github.developframework.kite.core.utils.KiteUtils;

/**
 * case节点
 *
 * @author qiushui on 2021-06-28.
 */
@ElementAttributes({
        ElementDefinition.Attribute.CASE_TEST
})
public final class CaseKiteElement extends ContainerKiteElement {

    private KiteComponent<CaseTestFunction<Object>> caseComponent;

    public CaseKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void configure(ElementDefinition elementDefinition) {
        super.configure(elementDefinition);
        caseComponent = parseCase(
                ElementDefinition.Attribute.CASE_TEST,
                elementDefinition.getString(ElementDefinition.Attribute.CASE_TEST)
        );
    }

    @Override
    public void assemble(AssembleContext context) {
        forEachAssemble(context);
    }

    public boolean match(AssembleContext context, Object value) {
        return KiteUtils.handleCastTest(context.dataModel, caseComponent, value);
    }

    private KiteComponent<CaseTestFunction<Object>> parseCase(String attributeName, String caseTestFunctionValue) {
        return new KiteComponent<>(
                attributeName,
                caseTestFunctionValue,
                CaseTestFunction.class,
                value -> KiteUtils.getLiteral(value).map(LiteralCaseTestFunction::new).orElse(null)
        );
    }
}
