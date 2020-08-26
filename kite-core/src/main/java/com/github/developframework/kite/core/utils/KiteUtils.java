package com.github.developframework.kite.core.utils;

import com.github.developframework.expression.ExpressionUtils;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.dynamic.KiteConverter;
import com.github.developframework.kite.core.dynamic.MapFunction;
import com.github.developframework.kite.core.element.ContentKiteElement;
import com.github.developframework.kite.core.exception.InvalidArgumentsException;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.strategy.KitePropertyNamingStrategy;
import com.github.developframework.kite.core.strategy.NamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

/**
 * @author qiushui on 2018-10-17.
 */
@Slf4j
@SuppressWarnings({"unchecked", "rawtypes"})
public final class KiteUtils {

    public static <T> T getComponentInstance(DataModel dataModel, String value, Class<T> tClass, String attributeName) {
        return (T) dataModel.getData(value).orElseGet(() -> {
            try {
                Object obj = Class.forName(value).getConstructor().newInstance();
                if (tClass.isAssignableFrom(obj.getClass())) {
                    return obj;
                } else {
                    throw new InvalidArgumentsException(attributeName, value, "It's not a " + tClass.getSimpleName() + " instance.");
                }
            } catch (ClassNotFoundException e) {
                throw new InvalidArgumentsException(attributeName, value, "class not found, and it's also not a expression.");
            } catch (IllegalAccessException | InstantiationException e) {
                throw new KiteException("Can't new " + tClass.getSimpleName() + " instance.");
            } catch (NoSuchMethodException | InvocationTargetException e) {
                throw new KiteException(tClass.getSimpleName() + " No noArg Constructor.");
            }
        });
    }

    public static Object[] objectToArray(Object object, ContentKiteElement element) {
        if (object.getClass().isArray()) {
            return (Object[]) object;
        } else if (object instanceof List<?>) {
            return ((List<?>) object).toArray();
        } else if (object instanceof Set<?>) {
            return ((Set<?>) object).toArray();
        } else {
            throw new InvalidArgumentsException("data", element.getDataDefinition().toString(), "Data must be array or List/Set type, the value class is " + object.getClass().getName());
        }
    }

    public static boolean isArrayOrCollection(Object object) {
        return object != null && (object.getClass().isArray() || object instanceof List<?> || object instanceof Set<?>);
    }

    /**
     * 解析children-naming-strategy
     */
    public static KitePropertyNamingStrategy parseChildrenNamingStrategy(String namingStrategyStr) {
        if (StringUtils.isEmpty(namingStrategyStr) || namingStrategyStr.equals("DEFAULT")) {
            return null;
        } else {
            try {
                NamingStrategy namingStrategy = NamingStrategy.valueOf(namingStrategyStr);
                return namingStrategy.getNamingStrategy();
            } catch (IllegalArgumentException e) {
                try {
                    Class clazz = Class.forName(namingStrategyStr);
                    if (KitePropertyNamingStrategy.class.isAssignableFrom(clazz)) {
                        return (KitePropertyNamingStrategy) clazz.getConstructor().newInstance();
                    }
                } catch (Exception ex) {
                    // 不处理
                }
            }
        }
        return null;
    }

    public static Object handleKiteConverter(DataModel dataModel, String converterValue, Object value) {
        if (converterValue == null) {
            return value;
        } else if (converterValue.startsWith("this.")) {
            return ExpressionUtils.getValue(value, converterValue.substring(5));
        } else {
            return KiteUtils
                    .getComponentInstance(dataModel, converterValue, KiteConverter.class, "converter")
                    .convert(value);
        }
    }

    public static Object handleMapFunction(DataModel dataModel, String mapFunctionValue, Object value, int i) {
        if (mapFunctionValue == null) {
            return value;
        } else if (mapFunctionValue.startsWith("this.")) {
            return ExpressionUtils.getValue(value, mapFunctionValue.substring(5));
        } else {
            return KiteUtils
                    .getComponentInstance(dataModel, mapFunctionValue, MapFunction.class, "map")
                    .apply(value, i);
        }
    }
}
