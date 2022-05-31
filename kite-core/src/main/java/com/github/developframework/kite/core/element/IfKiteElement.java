package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.dynamic.KiteCondition;
import com.github.developframework.kite.core.dynamic.LiteralCondition;
import com.github.developframework.kite.core.structs.ElementAttributes;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.structs.KiteComponent;
import com.github.developframework.kite.core.utils.KiteUtils;
import lombok.Setter;

/**
 * if 节点
 *
 * @author qiushui on 2021-06-27.
 */
@ElementAttributes("condition")
public final class IfKiteElement extends ContainerKiteElement {

    private KiteComponent<KiteCondition<Object>> conditionComponent;

    @Setter
    private ElseKiteElement elseKiteElement;

    public IfKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void configure(ElementDefinition elementDefinition) {
        super.configure(elementDefinition);
        conditionComponent = parseCondition(
                ElementDefinition.Attribute.CONDITION,
                elementDefinition.getString(ElementDefinition.Attribute.CONDITION)
        );
    }

    private KiteComponent<KiteCondition<Object>> parseCondition(String attributeName, String attributeValue) {
        return new KiteComponent<>(
                attributeName,
                attributeValue,
                KiteCondition.class,
                value -> KiteUtils.getLiteral(value).map(LiteralCondition::new).orElse(null)
        );
    }

    @Override
    public void assemble(AssembleContext context) {
        final Object currentValue = context.valueStack.peek();
        final boolean predicate =
                context.dataModel
                        .getData(conditionComponent.getAttributeValue())
                        .filter(v -> v instanceof Boolean)
                        .map(v -> (Boolean) v)
                        .orElseGet(() -> KiteUtils.handleCondition(context.dataModel, conditionComponent, currentValue));
        if (predicate) {
            // 执行条件真的逻辑
            forEachAssemble(context);
        } else if (elseKiteElement != null) {
            // 执行条件假的逻辑
            elseKiteElement.assemble(context);
        }
    }
}
