package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.dynamic.RelFunction;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.structs.ElementAttributes;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 关联节点
 *
 * @author qiushui on 2021-06-25.
 */
@ElementAttributes({
        ElementDefinition.Attribute.REL,
        ElementDefinition.Attribute.TYPE,
        ElementDefinition.Attribute.MERGE_PARENT,
        ElementDefinition.Attribute.UNIQUE,
        ElementDefinition.Attribute.INNER_CONVERTER
})
public final class RelevanceKiteElement extends ArrayKiteElement {

    private String relValue;

    private RelevanceType relevanceType;

    // 是否合并到父节点
    private boolean mergeParent;

    // 是否唯一值
    private boolean unique;

    // 内部转换器
    private String innerConverterValue;

    public RelevanceKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void configure(ElementDefinition elementDefinition) {
        super.configure(elementDefinition);
        relValue = elementDefinition.getString(ElementDefinition.Attribute.REL);
        relevanceType = elementDefinition.getEnum(ElementDefinition.Attribute.TYPE, RelevanceType.class, RelevanceType.AUTO);
        mergeParent = elementDefinition.getBoolean(ElementDefinition.Attribute.MERGE_PARENT, false);
        unique = elementDefinition.getBoolean(ElementDefinition.Attribute.UNIQUE, false);
        innerConverterValue = elementDefinition.getString(ElementDefinition.Attribute.INNER_CONVERTER);
    }

    @Override
    public void assemble(AssembleContext context) {
        final Optional<Object> dataValue = KiteUtils.getDataValue(context, this);
        if (dataValue.isPresent()) {
            Object v = dataValue.get();
            if (!KiteUtils.objectIsArray(v)) {
                throw new KiteException("relevance的data必须是array或List/Set");
            }
            final List<Object> matches = KiteUtils.handleInnerKiteConverter(
                    context.dataModel,
                    innerConverterValue,
                    relevanceMatch(v, context)
            );
            final int size = matches.size();
            switch (relevanceType) {
                case MULTIPLE: {
                    assembleArray(context, matches);
                }
                break;
                case SINGLE: {
                    assembleSingle(context, size == 0 ? null : matches.get(0));
                }
                break;
                case AUTO: {
                    if (size == 0) {
                        assembleSingle(context, null);
                    } else if (size == 1) {
                        assembleSingle(context, matches.get(0));
                    } else {
                        assembleArray(context, matches);
                    }
                }
                break;
            }
        } else if (!contentAttributes.nullHidden) {
            context.nodeStack.peek().putNull(displayName(context));
        }
    }

    /**
     * 组装单个对象
     */
    private void assembleSingle(AssembleContext context, Object v) {
        if (v == null) {
            context.nodeStack.peek().putNull(displayName(context));
        } else if (KiteUtils.objectIsArray(v)) {
            // 元素任然是数组型
            assembleArrayItems(
                    context,
                    /* 嵌套数组需要跳过函数处理 */
                    arrayAttributes.basic(),
                    v,
                    context.nodeStack.peek().putArrayNode(displayName(context))
            );
        } else if (mergeParent) {
            // 合并到父级
            if (elements.isEmpty()) {
                context.nodeStack.peek().putValue(displayName(context), v, contentAttributes.xmlCDATA);
            } else {
                context.prepareNextOnlyValue(v, this::forEachAssemble);
            }
        } else {
            // 元素是普通对象
            context.prepareNext(
                    context.nodeStack.peek().putObjectNode(displayName(context)),
                    v,
                    this::forEachAssemble
            );
        }
    }

    /**
     * 关联匹配
     */
    @SuppressWarnings("unchecked")
    private List<Object> relevanceMatch(Object v, AssembleContext context) {
        final RelFunction<Object, Object> relFunction = KiteUtils.getComponent(
                context.dataModel,
                relValue,
                RelFunction.class,
                ElementDefinition.Attribute.REL
        );
        final Object[] array = KiteUtils.objectToArray(v, contentAttributes.dataDefinition);
        final List<Object> matches = new ArrayList<>(array.length);
        final Object parentValue = context.valueStack.peek();
        for (int i = 0; i < array.length; i++) {
            if (relFunction.relevant(parentValue, context.arrayIndex, array[i], i)) {
                matches.add(array[i]);
                if (unique) {
                    // 唯一值时立即中断循环，提高性能
                    break;
                }
            }
        }
        return matches;
    }

    /**
     * 关联类型
     */
    public enum RelevanceType {

        AUTO,       // 依据数据数量自动选择采用对象或数组结构
        SINGLE,     // 明确单项数据选择采用对象结构
        MULTIPLE    // 明确多项数据选择采用数组结构
    }
}
