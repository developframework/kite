package com.github.developframework.kite.core.element;

import com.github.developframework.expression.ExpressionUtils;
import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.data.FunctionSign;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.FragmentLocation;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author qiushui on 2021-06-28.
 */
public final class SwitchKiteElement extends AbstractKiteElement {

    private DataDefinition checkData;

    private final List<CaseKiteElement> caseElements = new LinkedList<>();

    private DefaultKiteElement defaultElement;

    public SwitchKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void configure(ElementDefinition elementDefinition) {
        super.configure(elementDefinition);
        checkData = elementDefinition.getDataDefinition(ElementDefinition.Attribute.CHECK_DATA);
        for (KiteElement child : elementDefinition.getChildren()) {
            if (child instanceof CaseKiteElement) {
                caseElements.add((CaseKiteElement) child);
            } else if (child instanceof DefaultKiteElement) {
                defaultElement = (DefaultKiteElement) child;
            }
        }
    }

    @Override
    public void assemble(AssembleContext context) {
        getCheckDataValue(context, context.peekValue())
                .ifPresent(v -> {
                    boolean finish = false;
                    for (CaseKiteElement caseElement : caseElements) {
                        if (caseElement.match(context, v)) {
                            caseElement.assemble(context);
                            finish = true;
                            break;
                        }
                    }
                    if (!finish && defaultElement != null) {
                        defaultElement.assemble(context);
                    }
                });
    }

    private Optional<Object> getCheckDataValue(AssembleContext context, Object parentValue) {
        if (checkData == DataDefinition.EMPTY) {
            return Optional.ofNullable(parentValue);
        } else if (checkData.getFunctionSign() == FunctionSign.ROOT || parentValue instanceof DataModel) {
            return context.dataModel.getData(checkData.getExpression());
        } else {
            return Optional.ofNullable(ExpressionUtils.getValue(parentValue, checkData.getExpression()));
        }
    }
}
