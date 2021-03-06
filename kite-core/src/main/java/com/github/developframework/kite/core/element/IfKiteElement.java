package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.ElementAttributes;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.utils.KiteUtils;
import lombok.Setter;

/**
 * if 节点
 *
 * @author qiushui on 2021-06-27.
 */
@ElementAttributes("condition")
public final class IfKiteElement extends ContainerKiteElement {

    private String conditionValue;

    @Setter
    private ElseKiteElement elseKiteElement;

    public IfKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void configure(ElementDefinition elementDefinition) {
        super.configure(elementDefinition);
        conditionValue = elementDefinition.getString(ElementDefinition.Attribute.CONDITION);
    }

    @Override
    public void assemble(AssembleContext context) {
        final Object parentValue = context.peekValue();
        final Boolean flag =
                context.dataModel
                        .getData(conditionValue)
                        .filter(v -> v instanceof Boolean)
                        .map(v -> (Boolean) v)
                        .orElseGet(() -> KiteUtils.handleCondition(context.dataModel, conditionValue, parentValue));
        if (flag) {
            // 执行条件真的逻辑
            super.forEachAssemble(context);
        } else if(elseKiteElement != null){
            // 执行条件假的逻辑
            elseKiteElement.assemble(context);
        }
    }
}
