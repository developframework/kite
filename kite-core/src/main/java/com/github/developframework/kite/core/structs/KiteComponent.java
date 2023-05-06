package com.github.developframework.kite.core.structs;

import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.exception.InvalidAttributeException;
import com.github.developframework.kite.core.exception.KiteException;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

/**
 * 组件
 *
 * @author qiushui on 2022-05-30.
 */
public final class KiteComponent<T> {

    private final String attributeName;

    // 属性值
    @Getter
    private final String attributeValue;

    // 组件
    private T component;

    @SuppressWarnings("unchecked")
    public KiteComponent(String attributeName, String attributeValue, Class<? super T> tClass, Function<String, T> function) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.component = function.apply(attributeValue);
        if (component == null) {
            try {
                component = (T) Class.forName(attributeValue).getConstructor().newInstance();
            } catch (ClassNotFoundException e) {
                // 不处理
            } catch (IllegalAccessException | InstantiationException e) {
                throw new KiteException("Cannot new an %s instance", tClass.getSimpleName());
            } catch (NoSuchMethodException | InvocationTargetException e) {
                throw new KiteException(tClass.getSimpleName() + "is no parameterless constructors");
            }
        }
    }

    @SuppressWarnings("unchecked")
    public T getComponent(DataModel dataModel) {
        if (component != null) {
            return component;
        }
        return (T) dataModel.getData(attributeValue).orElseThrow(() -> new InvalidAttributeException(attributeName, attributeValue, "undefined"));
    }
}
