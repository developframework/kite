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
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.structs.ContentAttributes;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.KiteComponent;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qiushui on 2018-10-17.
 */
@Slf4j
@SuppressWarnings("unchecked")
public final class KiteUtils {

    /**
     * 获得值
     *
     * @param context 上下文
     * @param element 元素
     * @return 值
     */
    public static Optional<Object> getDataValue(AssembleContext context, ContentKiteElement element) {
        final ContentAttributes contentAttributes = element.getContentAttributes();
        final DataDefinition dataDefinition = contentAttributes.dataDefinition;
        Object v = context.valueStack.peek();
        if (v instanceof DataModel || dataDefinition.getFunctionSign() == FunctionSign.ROOT) {
            v = context.dataModel.getData(dataDefinition.getExpression()).orElse(null);
        } else {
            v = ExpressionUtils.getValue(v, dataDefinition.getExpression());
        }
        // 处理转换器
        return Optional.ofNullable(handleKiteConverter(context.dataModel, contentAttributes.converterComponent, v));
    }

    /**
     * 获得组件实例
     */
    public static <T> T getComponent(DataModel dataModel, String value, Class<T> tClass, String attributeName) {
        return (T) dataModel.getData(value).orElseGet(() -> {
            try {
                Object obj = Class.forName(value).getConstructor().newInstance();
                if (tClass.isAssignableFrom(obj.getClass())) {
                    return obj;
                } else {
                    throw new InvalidAttributeException(attributeName, value, "没有类“" + tClass.getSimpleName() + "”的实例");
                }
            } catch (ClassNotFoundException e) {
                throw new InvalidAttributeException(attributeName, value, "类不存在，并且也不是一个expression");
            } catch (IllegalAccessException | InstantiationException e) {
                throw new KiteException("不能new“" + tClass.getSimpleName() + "”的实例");
            } catch (NoSuchMethodException | InvocationTargetException e) {
                throw new KiteException(tClass.getSimpleName() + "没有无参构造方法");
            }
        });
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
        return converterComponent.getComponent(dataModel, ElementDefinition.Attribute.CONVERTER).convert(value);
    }

    /**
     * 处理KiteConverter
     */
    public static List<Object> handleInnerKiteConverter(DataModel dataModel, KiteComponent<KiteConverter<Object, Object>> innerConverterComponent, List<Object> values) {
        if (innerConverterComponent == null) {
            return values;
        }
        final KiteConverter<Object, Object> converter = innerConverterComponent.getComponent(dataModel, ElementDefinition.Attribute.INNER_CONVERTER);
        return values
                .stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    /**
     * 处理CastTest
     */
    public static boolean handleCastTest(DataModel dataModel, KiteComponent<CaseTestFunction<Object>> caseComponent, Object value) {
        return caseComponent.getComponent(dataModel, ElementDefinition.Attribute.CASE_TEST).test(value);
    }

    /**
     * 处理Comparator
     */
    public static void handleArrayComparator(DataModel dataModel, KiteComponent<Comparator<Object>> comparatorComponent, final Object[] array) {
        if (comparatorComponent != null) {
            Arrays.sort(
                    array,
                    comparatorComponent.getComponent(dataModel, ElementDefinition.Attribute.COMPARATOR)
            );
        }
    }

    /**
     * 处理Condition
     */
    public static boolean handleCondition(DataModel dataModel, String conditionValue, Object parentValue) {
        return getComponent(dataModel, conditionValue, KiteCondition.class, ElementDefinition.Attribute.CONDITION).verify(dataModel, parentValue);
    }

    /**
     * 判断是否是字面量
     */
    private static boolean isLiteral(String content) {
        return content != null && content.length() > 3 && content.charAt(0) == '\'' && content.charAt(content.length() - 1) == '\'';
    }

    /**
     * 获取字面量
     */
    public static String getLiteral(String content) {
        return isLiteral(content) ? content.substring(1, content.length() - 1) : null;
    }
}
