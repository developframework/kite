package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.dynamic.RelFunction;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.TemplateLocation;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 关联节点
 *
 * @author qiushui on 2021-06-25.
 */
public final class RelevanceKiteElement extends ArrayKiteElement {

    private String relValue;

    private RelevanceType relevanceType;

    // 是否合并到父节点
    private boolean mergeParent;

    // 是否唯一值
    private boolean unique;

    // 内部转换器
    private String innerConverterValue;

    public RelevanceKiteElement(TemplateLocation templateLocation) {
        super(templateLocation);
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
                throw new KiteException("relevance data must array.");
            }
            final List<Object> matches = KiteUtils.handleInnerKiteConverter(
                    context.dataModel,
                    innerConverterValue,
                    relevanceMatch((Object[]) v, context)
            );
            final int size = matches.size();
            switch (relevanceType) {
                case MULTIPLE: {
                    super.assembleWithArrayObject(context, matches);
                }
                break;
                case SINGLE: {
                    assembleObject(context, size == 0 ? null : matches.get(0));
                }
                break;
                case AUTO: {
                    if (size == 0) {
                        assembleObject(context, null);
                    } else if (size == 1) {
                        assembleObject(context, matches.get(0));
                    } else {
                        super.assembleWithArrayObject(context, matches);
                    }
                }
                break;
            }
        } else if (!contentAttributes.nullHidden) {
            context.peekNodeProxy().putNull(displayName(context));
        }
    }

    /**
     * 组装单个对象
     */
    private void assembleObject(AssembleContext context, Object object) {
        if (mergeParent) {
            if (elements.isEmpty()) {
                context.peekNodeProxy().putValue(displayName(context), object, contentAttributes.xmlCDATA);
            } else {
                context.pushValue(object);
                forEachAssemble(context);
                context.popValue();
            }
        } else {
            context.parentPutNodeProxyAndPush(displayName(context));
            context.pushValue(object);
            forEachAssemble(context);
            context.pop();
        }
    }

    /**
     * 关联匹配
     */
    @SuppressWarnings("unchecked")
    private List<Object> relevanceMatch(Object[] array, AssembleContext context) {
        final Object parentValue = context.peekValue();
        final RelFunction<Object, Object> relFunction = KiteUtils.getComponent(
                context.dataModel,
                relValue,
                RelFunction.class,
                ElementDefinition.Attribute.REL
        );
        final List<Object> matches = new ArrayList<>(array.length);
        for (int i = 0; i < array.length; i++) {
            if (relFunction.relevant(parentValue, context.arrayIndex, array[i], i)) {
                matches.add(array[i]);
                if (unique && !matches.isEmpty()) {
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
