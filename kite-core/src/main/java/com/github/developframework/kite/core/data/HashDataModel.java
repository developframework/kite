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
public class HashDataModel implements DataModel{

    @Getter
    private Map<String, Object> dataMap = new HashMap<String, Object>();

    public HashDataModel() {
    }

    public HashDataModel(Map<String, Object> dataMap) {
        this.dataMap.putAll(dataMap);
    }

    @Override
    public void putData(String dataName, Object data) {
        this.dataMap.put(dataName, data);
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
    public Object getDataRequired(Expression expression) {
        Object value = ExpressionUtils.getValue(dataMap, expression);
        if(value == null) {
            throw new DataUndefinedException(expression.toString());
        }
        return value;
    }

    @Override
    public Object getDataRequired(String expressionValue) {
        return getDataRequired(Expression.parse(expressionValue));
    }


    /**
     * 构造只有一个数据的DataModel
     * @param dataName 数据名称
     * @param data 数据值
     * @return DataModel
     */
    public static final DataModel singleton(String dataName, Object data) {
        DataModel dataModel = new HashDataModel();
        dataModel.putData(dataName, data);
        return dataModel;
    }

}
