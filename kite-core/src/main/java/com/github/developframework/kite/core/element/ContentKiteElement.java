package com.github.developframework.kite.core.element;

import com.github.developframework.expression.ArrayExpression;
import com.github.developframework.expression.Expression;
import com.github.developframework.expression.ObjectExpression;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * 内容节点基类
 *
 * @author qiuzhenhao
 */
@Getter
public abstract class ContentKiteElement extends KiteElement {

    @Setter
    protected DataDefinition dataDefinition;

    protected String alias;

    protected boolean nullHidden;

    public ContentKiteElement(KiteConfiguration configuration, String namespace, String templateId, DataDefinition dataDefinition, String alias) {
        super(configuration, namespace, templateId);
        this.dataDefinition = dataDefinition;
        this.alias = alias;
    }

    public void setNullHidden(String nullHiddenStr) {
        this.nullHidden = StringUtils.isBlank(nullHiddenStr) ? false : new Boolean(nullHiddenStr).booleanValue();
    }

    /**
     * 生成显示名称（json）
     * @return 显示名称
     */
    public String showNameJSON() {
        if (StringUtils.isNotBlank(alias)) {
            return alias;
        }
        final String expressionString = expressionString();
        return configuration.getForJsonStrategy().propertyShowName(configuration, expressionString);
    }

    /**
     * 生成显示名称（xml）
     * @return 显示名称
     */
    public String showNameXML() {
        if (StringUtils.isNotBlank(alias)) {
            return alias;
        }
        final String expressionString = expressionString();
        return configuration.getForXmlStrategy().propertyShowName(configuration, expressionString);
    }

    private String expressionString() {
        Expression expression = dataDefinition.getExpression();
        if (expression instanceof ObjectExpression) {
            return ((ObjectExpression) expression).getPropertyName();
        } else if (expression instanceof ArrayExpression) {
            ArrayExpression arrayExpression = ((ArrayExpression) expression);
            return arrayExpression.getPropertyName() + "_" + arrayExpression.getIndex();
        } else {
            return "aaa";
        }
    }

}
