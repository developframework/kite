package com.github.developframework.kite.core.structs;

import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.exception.InvalidAttributeException;
import com.github.developframework.kite.core.exception.KiteException;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

/**
 * 组件
 *
 * @author qiushui on 2022-05-30.
 */
public final class KiteComponent<T> {

    // 属性值
    private final String attributeValue;

    // 组件
    private T component;

    @SuppressWarnings("unchecked")
    public KiteComponent(String attributeName, String attributeValue, Class<? super T> tClass, Function<String, T> function) {
        this.attributeValue = attributeValue;
        this.component = function.apply(attributeValue);
        if (component == null) {
            try {
                Object obj = Class.forName(attributeValue).getConstructor().newInstance();
                if (tClass.isAssignableFrom(obj.getClass())) {
                    this.component = (T) obj;
                } else {
                    throw new InvalidAttributeException(attributeName, attributeValue, "没有类“" + tClass.getSimpleName() + "”的实例");
                }
            } catch (ClassNotFoundException e) {
                throw new InvalidAttributeException(attributeName, attributeValue, "类不存在，并且也不是一个expression");
            } catch (IllegalAccessException | InstantiationException e) {
                throw new KiteException("不能new“" + tClass.getSimpleName() + "”的实例");
            } catch (NoSuchMethodException | InvocationTargetException e) {
                throw new KiteException(tClass.getSimpleName() + "没有无参构造方法");
            }
        }
    }

    @SuppressWarnings("unchecked")
    public T getComponent(DataModel dataModel, String attributeName) {
        if (component != null) {
            return component;
        }
        return (T) dataModel.getData(attributeValue).orElseThrow(() -> new InvalidAttributeException(attributeName, attributeValue, "组件未定义"));
    }
}
