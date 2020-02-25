package com.github.developframework.kite.core.data;

import com.github.developframework.expression.Expression;
import com.github.developframework.expression.ExpressionUtils;
import com.github.developframework.kite.core.exception.DataUndefinedException;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Hash数据模型
 * @author qiuzhenhao
 */
public class HashDataModel implements DataModel {

    @Getter
    private Map<String, Object> dataMap = new HashMap<>();

    public HashDataModel() {
    }

    public HashDataModel(Map<String, Object> dataMap) {
        this.dataMap.putAll(dataMap);
    }

    @Override
    public boolean contains(String dataName) {
        return dataMap.containsKey(dataName);
    }

    @Override
    public DataModel putData(String dataName, Object data) {
        this.dataMap.put(dataName, data);
        return this;
    }

    @Override
    public Optional<Object> getData(Expression expression) {
        return Optional.ofNullable(ExpressionUtils.getValue(dataMap, expression));
    }

    @Override
    public Optional<Object> getData(String expressionValue) {
        return getData(Expression.parse(expressionValue));
    }

    @Override
    public Optional<Object> getData(Object object, Expression expression) {
        return Optional.ofNullable(ExpressionUtils.getValue(object, expression));
    }

    @Override
    public Optional<Object> getData(Object object, String expressionValue) {
        return getData(object, Expression.parse(expressionValue));
    }

    @Override
    public Object getDataRequired(Expression expression) {
        Object value = ExpressionUtils.getValue(dataMap, expression);
        if(value == null) {
            throw new DataUndefinedException(expression.toString());
        }
        return value;
    }

    @Override
    public Object getDataRequired(Object object, Expression expression) {
        Object value = ExpressionUtils.getValue(object, expression);
        if (value == null) {
            throw new DataUndefinedException(expression.toString());
        }
        return value;
    }

    @Override
    public Object getDataRequired(String expressionValue) {
        return getDataRequired(Expression.parse(expressionValue));
    }

    @Override
    public Object getDataRequired(Object object, String expressionValue) {
        Object value = ExpressionUtils.getValue(object, expressionValue);
        if (value == null) {
            throw new DataUndefinedException(expressionValue);
        }
        return value;
    }


    /**
     * 构造只有一个数据的DataModel
     * @param dataName 数据名称
     * @param data 数据值
     * @return DataModel
     */
    public static DataModel singleton(String dataName, Object data) {
        DataModel dataModel = new HashDataModel();
        dataModel.putData(dataName, data);
        return dataModel;
    }

    /**
     * 生成一个构造器
     * @return 构造器
     */
    public static DataModelBuilder builder() {
        return new DataModelBuilder(HashDataModel.class);
    }
}
