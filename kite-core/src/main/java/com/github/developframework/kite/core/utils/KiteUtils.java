package com.github.developframework.kite.core.utils;

import com.github.developframework.expression.ExpressionUtils;
import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.data.FunctionSign;
import com.github.developframework.kite.core.dynamic.CaseTestFunction;
import com.github.developframework.kite.core.dynamic.KiteCondition;
import com.github.developframework.kite.core.dynamic.KiteConverter;
import com.github.developframework.kite.core.element.ContentKiteElement;
import com.github.developframework.kite.core.exception.InvalidAttributeException;
import com.github.developframework.kite.core.structs.ContentAttributes;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.KiteComponent;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qiushui on 2018-10-17.
 */
public abstract class KiteUtils {

    /**
     * 获得值
     *
     * @param context 上下文
     * @param element 元素
     * @return 值
     */
    public static Optional<Object> getDataValue(AssembleContext context, ContentKiteElement element) {
        final ContentAttributes contentAttributes = element.getContentAttributes();
        final Object v = getData(context.dataModel, contentAttributes.dataDefinition, context.valueStack.peek());
        // 处理转换器
        return Optional.ofNullable(handleKiteConverter(context.dataModel, contentAttributes.converterComponent, v));
    }

    public static Object getData(DataModel dataModel, DataDefinition dataDefinition, Object parentValue) {
        if (parentValue instanceof DataModel || dataDefinition.getFunctionSign() == FunctionSign.ROOT) {
            return dataModel.getData(dataDefinition.getExpression()).orElse(null);
        } else {
            return ExpressionUtils.getValue(parentValue, dataDefinition.getExpression());
        }
    }

    /**
     * 判断实例是否是数组或集合
     */
    public static boolean objectIsArray(Object object) {
        return object != null && (object.getClass().isArray() || object instanceof List || object instanceof Set);
    }

    /**
     * 实例转化成数组
     */
    public static Object[] objectToArray(Object object, DataDefinition dataDefinition) {
        if (object.getClass().isArray()) {
            return (Object[]) object;
        } else if (object instanceof List<?>) {
            return ((List<?>) object).toArray();
        } else if (object instanceof Set<?>) {
            return ((Set<?>) object).toArray();
        } else {
            throw new InvalidAttributeException(ElementDefinition.Attribute.DATA, dataDefinition.toString(), "data必须是array或List/Set类型，当前类为" + object.getClass().getName());
        }
    }

    /**
     * 处理KiteConverter
     */
    public static Object handleKiteConverter(DataModel dataModel, KiteComponent<KiteConverter<Object, Object>> converterComponent, Object value) {
        if (converterComponent == null) {
            return value;
        }
        return converterComponent.getComponent(dataModel).convert(value);
    }

    /**
     * 处理KiteConverter
     */
    public static List<Object> handleInnerKiteConverter(DataModel dataModel, KiteComponent<KiteConverter<Object, Object>> innerConverterComponent, List<Object> values) {
        if (innerConverterComponent == null) {
            return values;
        }
        final KiteConverter<Object, Object> converter = innerConverterComponent.getComponent(dataModel);
        return values.stream().map(converter::convert).collect(Collectors.toList());
    }

    /**
     * 处理CastTest
     */
    public static boolean handleCastTest(DataModel dataModel, KiteComponent<CaseTestFunction<Object>> caseComponent, Object value) {
        return caseComponent.getComponent(dataModel).test(value);
    }

    /**
     * 处理Comparator
     */
    public static void handleArrayComparator(DataModel dataModel, KiteComponent<Comparator<Object>> comparatorComponent, final Object[] array) {
        if (comparatorComponent != null) {
            Arrays.sort(array, comparatorComponent.getComponent(dataModel));
        }
    }

    /**
     * 处理Condition
     */
    public static boolean handleCondition(DataModel dataModel, KiteComponent<KiteCondition<Object>> conditionComponent, Object parentValue) {
        return conditionComponent.getComponent(dataModel).verify(dataModel, parentValue);
    }

    /**
     * 获取字面量
     */
    public static Optional<String> getLiteral(String content) {
        if (content != null) {
            final int length = content.length();
            if (length > 3 && content.charAt(0) == '\'' && content.charAt(length - 1) == '\'') {
                return Optional.of(content.substring(1, length - 1));
            }
        }
        return Optional.empty();
    }
}
