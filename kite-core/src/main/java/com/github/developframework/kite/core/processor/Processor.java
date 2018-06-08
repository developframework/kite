package com.github.developframework.kite.core.processor;

import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.data.FunctionSign;
import com.github.developframework.kite.core.element.ContentKiteElement;

/**
 * 处理器顶级基类
 * @author qiuzhenhao
 */
public abstract class Processor {

    /**
     * 生成子表达式
     * @param contentElement 内容节点
     * @param parentExpression 父表达式
     * @return 子表达式
     */
    public static Expression childExpression(ContentKiteElement contentElement, Expression parentExpression) {
        final DataDefinition dataDefinition = contentElement.getDataDefinition();
        if (dataDefinition.getFunctionSign() == FunctionSign.ROOT) {
            return dataDefinition.getExpression();
        }
        if (parentExpression == null) {
            return dataDefinition.getExpression();
        }
        return Expression.concat(parentExpression, dataDefinition.getExpression());
    }
}
