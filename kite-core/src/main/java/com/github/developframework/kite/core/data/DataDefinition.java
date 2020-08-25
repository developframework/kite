package com.github.developframework.kite.core.data;

import com.github.developframework.expression.EmptyExpression;
import com.github.developframework.expression.Expression;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 数据定义
 *
 * @author qiuzhenhao
 */
@Getter
public class DataDefinition {

    public static final DataDefinition EMPTY_DATA_DEFINITION = new DataDefinition(null);

    /* 功能符号 */
    private FunctionSign functionSign;
    /* 表达式 */
    private final Expression expression;

    public DataDefinition(FunctionSign functionSign, Expression expression) {
        this.functionSign = functionSign;
        this.expression = expression;
    }

    public DataDefinition(String dataValue) {
        if (StringUtils.isEmpty(dataValue)) {
            expression = EmptyExpression.INSTANCE;
        } else {
            dataValue = dataValue.trim();
            char firstChar = dataValue.charAt(0);
            boolean hasFunctionSign = false;
            for (FunctionSign sign : FunctionSign.values()) {
                if (sign.getSign() == firstChar) {
                    this.functionSign = sign;
                    hasFunctionSign = true;
                    break;
                }
            }
            if (hasFunctionSign) {
                this.expression = Expression.parse(dataValue.substring(1));
            } else {
                this.expression = Expression.parse(dataValue);
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = hash * 31 + functionSign.hashCode();
        hash = hash * 31 + expression.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DataDefinition) {
            DataDefinition otherDataDefinition = (DataDefinition) obj;
            return functionSign == otherDataDefinition.functionSign && expression.equals(otherDataDefinition.expression);
        }
        return false;
    }

    @Override
    public String toString() {
        if(functionSign == null) {
            return expression.toString();
        }
        return functionSign.getSign() + expression.toString();
    }
}
