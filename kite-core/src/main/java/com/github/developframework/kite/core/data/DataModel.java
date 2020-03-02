package com.github.developframework.kite.core.data;

import com.github.developframework.expression.Expression;
import com.github.developframework.expression.ExpressionUtils;
import com.github.developframework.kite.core.dynamic.*;
import com.github.developframework.kite.core.exception.DataUndefinedException;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 数据模型
 *
 * @author qiuzhenhao
 */
public class DataModel {

    @Getter
    private Map<String, Object> dataMap = new HashMap<>();

    public DataModel() {
    }

    public DataModel(Map<String, Object> dataMap) {
        this.dataMap.putAll(dataMap);
    }

    /**
     * 是否包含某个数据
     *
     * @param dataName 数据名称
     * @return 是否包含
     */
    public boolean contains(String dataName) {
        return dataMap.containsKey(dataName);
    }

    /**
     * 放入数据
     *
     * @param dataName 数据名称
     * @param data     数据值
     */
    public DataModel putData(String dataName, Object data) {
        this.dataMap.put(dataName, data);
        return this;
    }

    /**
     * 放入转换器
     *
     * @param dataName  数据名称
     * @param converter 转换器
     * @return
     */
    public DataModel putConverter(String dataName, KiteConverter<?, ?> converter) {
        return putData(dataName, converter);
    }

    /**
     * 放入条件
     *
     * @param dataName  数据名称
     * @param condition 条件
     * @return
     */
    public DataModel putCondition(String dataName, KiteCondition<?> condition) {
        return putData(dataName, condition);
    }

    /**
     * 放入Case test函数接口
     *
     * @param dataName 数据名称
     * @param function Case test函数接口
     * @return
     */
    public DataModel putCaseTestFunction(String dataName, CaseTestFunction<?> function) {
        return putData(dataName, function);
    }

    /**
     * 放入关联函数接口
     *
     * @param dataName 数据名称
     * @param function 关联函数接口
     * @return
     */
    public DataModel putRelFunction(String dataName, RelFunction<?, ?> function) {
        return putData(dataName, function);
    }

    /**
     * 放入数组映射接口
     *
     * @param dataName 数据名称
     * @param function 数组映射接口
     * @return
     */
    public DataModel putMapFunction(String dataName, MapFunction<?, ?> function) {
        return putData(dataName, function);
    }

    /**
     * 获得数据
     *
     * @param expression 表达式定义
     * @return 数据值
     */
    public Optional<Object> getData(Expression expression) {
        return Optional.ofNullable(ExpressionUtils.getValue(dataMap, expression));
    }

    /**
     * 获得数据
     *
     * @param expressionValue 表达式字符串
     * @return 数据值
     */
    public Optional<Object> getData(String expressionValue) {
        return getData(Expression.parse(expressionValue));
    }

    /**
     * 从Object获得数据
     *
     * @param object     对象
     * @param expression 表达式定义
     * @return 数据值
     */
    public Optional<Object> getData(Object object, Expression expression) {
        return Optional.ofNullable(ExpressionUtils.getValue(object, expression));
    }

    /**
     * 从Object获得数据
     *
     * @param object          对象
     * @param expressionValue 表达式字符串
     * @return 数据值
     */
    public Optional<Object> getData(Object object, String expressionValue) {
        return getData(object, Expression.parse(expressionValue));
    }

    /**
     * 获得数据 如果没有抛出异常
     *
     * @param expression 表达式定义
     * @return 数据值
     */
    public Object getDataRequired(Expression expression) {
        Object value = ExpressionUtils.getValue(dataMap, expression);
        if (value == null) {
            throw new DataUndefinedException(expression.toString());
        }
        return value;
    }

    /**
     * 从Object获得数据 如果没有抛出异常
     *
     * @param object     对象
     * @param expression 表达式定义
     * @return 数据值
     */
    public Object getDataRequired(Object object, Expression expression) {
        Object value = ExpressionUtils.getValue(object, expression);
        if (value == null) {
            throw new DataUndefinedException(expression.toString());
        }
        return value;
    }

    /**
     * 获得数据 如果没有抛出异常
     *
     * @param expressionValue 表达式字符串
     * @return 数据值
     */
    public Object getDataRequired(String expressionValue) {
        return getDataRequired(Expression.parse(expressionValue));
    }

    /**
     * 从Object获得数据 如果没有抛出异常
     *
     * @param object          对象
     * @param expressionValue 表达式字符串
     * @return 数据值
     */
    public Object getDataRequired(Object object, String expressionValue) {
        Object value = ExpressionUtils.getValue(object, expressionValue);
        if (value == null) {
            throw new DataUndefinedException(expressionValue);
        }
        return value;
    }


    /**
     * 构造只有一个数据的DataModel
     *
     * @param dataName 数据名称
     * @param data     数据值
     * @return DataModel
     */
    public static DataModel singleton(String dataName, Object data) {
        return new DataModel().putData(dataName, data);
    }

    /**
     * 生成一个构造器
     *
     * @return 构造器
     */
    public static DataModel builder() {
        return new DataModel();
    }
}
