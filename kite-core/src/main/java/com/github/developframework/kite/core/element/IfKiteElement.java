package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.dynamic.ClassCondition;
import com.github.developframework.kite.core.dynamic.KiteCondition;
import com.github.developframework.kite.core.dynamic.LiteralCondition;
import com.github.developframework.kite.core.exception.InvalidAttributeException;
import com.github.developframework.kite.core.structs.ElementAttributes;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.structs.KiteComponent;
import com.github.developframework.kite.core.utils.KiteUtils;
import lombok.Setter;

/**
 * if 元素
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

    private KiteComponent<KiteCondition<Object>> parseCondition(String attributeName, String conditionValue) {
        return new KiteComponent<>(
                attributeName,
                conditionValue,
                KiteCondition.class,
                value -> {
                    /*
                        支持的简单格式：
                            this instanceof java.lang.Integer
                            v = 'abc'
                     */
                    if (value != null) {
                        final String[] parts = value.split("\\s+");
                        if (parts.length == 3) {
                            final DataDefinition dataDefinition = parts[0].equals("this") ? null : new DataDefinition(parts[0]);
                            switch (parts[1]) {
                                case "instanceof": {
                                    try {
                                        return new ClassCondition(dataDefinition, Class.forName(parts[2]));
                                    } catch (ClassNotFoundException e) {
                                        throw new InvalidAttributeException(attributeName, value, "未找到类");
                                    }
                                }
                                case "=": {
                                    return KiteUtils.getLiteral(parts[2])
                                            .map(literal -> new LiteralCondition(dataDefinition, literal))
                                            .orElseThrow(() -> new InvalidAttributeException(attributeName, value, "格式错误"));
                                }
                            }
                        }
                    }
                    return null;
                }
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
