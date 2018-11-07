package com.github.developframework.kite.core.utils;

import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.element.ContentKiteElement;
import com.github.developframework.kite.core.exception.InvalidArgumentsException;
import com.github.developframework.kite.core.exception.KiteException;

import java.util.List;
import java.util.Set;

/**
 * @author qiushui on 2018-10-17.
 * @since 0.1
 */
public final class KiteUtils {

    @SuppressWarnings("unchecked")
    public static <T> T getComponentInstance(DataModel dataModel, String value, Class<T> tClass, String attributeName) {
        return (T) dataModel.getData(value).orElseGet(() -> {
            try {
                Object obj = Class.forName(value).newInstance();
                if (tClass.isAssignableFrom(obj.getClass())) {
                    return obj;
                } else {
                    throw new InvalidArgumentsException(attributeName, value, "It's not a " + tClass.getSimpleName() + " instance.");
                }
            } catch (ClassNotFoundException e) {
                throw new InvalidArgumentsException(attributeName, value, "class not found, and it's also not a expression.");
            } catch (IllegalAccessException | InstantiationException e) {
                throw new KiteException("Can't new " + tClass.getSimpleName() + " instance.");
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
}
