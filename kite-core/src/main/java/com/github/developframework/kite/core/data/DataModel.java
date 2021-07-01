package com.github.developframework.kite.core.data;

import com.github.developframework.expression.Expression;
import com.github.developframework.expression.ExpressionUtils;
import com.github.developframework.kite.core.dynamic.CaseTestFunction;
import com.github.developframework.kite.core.dynamic.KiteCondition;
import com.github.developframework.kite.core.dynamic.KiteConverter;
import com.github.developframework.kite.core.dynamic.RelFunction;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 数据模型
 *
 * @author qiuzhenhao
 */
@NoArgsConstructor
public class DataModel {

    @Getter
    private final Map<String, Object> dataMap = new HashMap<>();

    public DataModel(Map<String, Object> dataMap) {
        this.dataMap.putAll(dataMap);
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
     * 放入caseTestFunction
     *
     * @param dataName      数据名称
     * @param kiteConverter kiteConverter
     */
    public DataModel putConverter(String dataName, KiteConverter<?, ?> kiteConverter) {
        this.dataMap.put(dataName, kiteConverter);
        return this;
    }

    /**
     * 放入kiteCondition
     *
     * @param dataName      数据名称
     * @param kiteCondition kiteCondition
     */
    public DataModel putCondition(String dataName, KiteCondition<?> kiteCondition) {
        this.dataMap.put(dataName, kiteCondition);
        return this;
    }

    /**
     * 放入caseTestFunction
     *
     * @param dataName         数据名称
     * @param caseTestFunction caseTestFunction
     */
    public DataModel putCaseTestFunction(String dataName, CaseTestFunction<?> caseTestFunction) {
        this.dataMap.put(dataName, caseTestFunction);
        return this;
    }

    /**
     * 放入relFunction
     *
     * @param dataName    数据名称
     * @param relFunction relFunction
     */
    public DataModel putRelFunction(String dataName, RelFunction<?, ?> relFunction) {
        this.dataMap.put(dataName, relFunction);
        return this;
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
