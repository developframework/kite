package com.github.developframework.kite.core.data;

import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.dynamic.*;

import java.io.Serializable;
import java.util.Optional;

/**
 * 数据模型接口
 * @author qiuzhenhao
 */
public interface DataModel extends Serializable {

    boolean contains(String dataName);

    /**
     * 放入数据
     *
     * @param dataName 数据名称
     * @param data     数据值
     */
    DataModel putData(String dataName, Object data);

    /**
     * 放入转换器
     *
     * @param dataName  数据名称
     * @param converter 转换器
     * @return
     */
    default DataModel putConverter(String dataName, KiteConverter<?, ?> converter) {
        return putData(dataName, converter);
    }

    /**
     * 放入条件
     *
     * @param dataName  数据名称
     * @param condition 条件
     * @return
     */
    default DataModel putCondition(String dataName, KiteCondition<?> condition) {
        return putData(dataName, condition);
    }

    /**
     * 放入Case test函数接口
     *
     * @param dataName 数据名称
     * @param function Case test函数接口
     * @return
     */
    default DataModel putCaseTestFunction(String dataName, CaseTestFunction<?> function) {
        return putData(dataName, function);
    }

    /**
     * 放入数组映射接口
     *
     * @param dataName 数据名称
     * @param function 数组映射接口
     * @return
     */
    default DataModel putMapFunction(String dataName, MapFunction<?, ?> function) {
        return putData(dataName, function);
    }

    /**
     * 放入关联函数接口
     *
     * @param dataName 数据名称
     * @param function 关联函数接口
     * @return
     */
    default DataModel putRelFunction(String dataName, RelFunction<?, ?> function) {
        return putData(dataName, function);
    }

    /**
     * 获得数据
     *
     * @param expression 表达式定义
     * @return 数据值
     */
    Optional<Object> getData(Expression expression);

    /**
     * 获得数据
     * @param expressionValue 表达式字符串
     * @return 数据值
     */
    Optional<Object> getData(String expressionValue);

    /**
     * 从Object获得数据
     *
     * @param object     对象
     * @param expression 表达式定义
     * @return 数据值
     */
    Optional<Object> getData(Object object, Expression expression);

    /**
     * 从Object获得数据
     *
     * @param object          对象
     * @param expressionValue 表达式字符串
     * @return 数据值
     */
    Optional<Object> getData(Object object, String expressionValue);

    /**
     * 获得数据 如果没有抛出异常
     * @param expression 表达式定义
     * @return 数据值
     */
    Object getDataRequired(Expression expression);

    /**
     * 从Object获得数据 如果没有抛出异常
     *
     * @param object     对象
     * @param expression 表达式定义
     * @return 数据值
     */
    Object getDataRequired(Object object, Expression expression);

    /**
     * 获得数据 如果没有抛出异常
     * @param expressionValue 表达式字符串
     * @return 数据值
     */
    Object getDataRequired(String expressionValue);

    /**
     * 从Object获得数据 如果没有抛出异常
     *
     * @param object          对象
     * @param expressionValue 表达式字符串
     * @return 数据值
     */
    Object getDataRequired(Object object, String expressionValue);

}
