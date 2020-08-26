package com.github.developframework.kite.core.element;

import com.github.developframework.expression.ArrayExpression;
import com.github.developframework.expression.Expression;
import com.github.developframework.expression.MethodExpression;
import com.github.developframework.expression.ObjectExpression;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.TemplateLocation;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.strategy.KitePropertyNamingStrategy;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * 内容节点基类
 *
 * @author qiuzhenhao
 */
@Getter
public abstract class ContentKiteElement extends AbstractKiteElement {

    @Setter
    protected DataDefinition dataDefinition;

    @Setter
    protected String converterValue;

    @Setter
    protected String alias;

    protected boolean nullHidden;

    public ContentKiteElement(KiteConfiguration configuration, TemplateLocation templateLocation, DataDefinition dataDefinition, String alias) {
        super(configuration, templateLocation);
        this.dataDefinition = dataDefinition;
        this.alias = alias;
    }

    public Optional<String> getConverterValue() {
        return Optional.ofNullable(converterValue);
    }

    public void setNullHidden(String nullHiddenStr) {
        this.nullHidden = isNotEmpty(nullHiddenStr) && Boolean.parseBoolean(nullHiddenStr);
    }

    /**
     * 生成显示名称（json）
     *
     * @return 显示名称
     */
    public String showNameJSON(KitePropertyNamingStrategy parentNamingStrategy) {
        if (isNotEmpty(alias)) {
            return alias;
        }
        final String expressionString = expressionString();
        final KitePropertyNamingStrategy ns = parentNamingStrategy == null ? configuration.getForJsonStrategy() : parentNamingStrategy;
        return ns.propertyShowName(configuration, expressionString);
    }

    /**
     * 生成显示名称（xml）
     *
     * @return 显示名称
     */
    public String showNameXML(KitePropertyNamingStrategy parentNamingStrategy) {
        if (isNotEmpty(alias)) {
            return alias;
        }
        final String expressionString = expressionString();
        final KitePropertyNamingStrategy ns = parentNamingStrategy == null ? configuration.getForXmlStrategy() : parentNamingStrategy;
        return ns.propertyShowName(configuration, expressionString);
    }

    private String expressionString() {
        Expression expression = dataDefinition.getExpression();
        if (expression instanceof ObjectExpression) {
            return ((ObjectExpression) expression).getPropertyName();
        } else if (expression instanceof ArrayExpression) {
            ArrayExpression arrayExpression = ((ArrayExpression) expression);
            return arrayExpression.getPropertyName() + "_" + arrayExpression.getIndex();
        } else if (expression instanceof MethodExpression) {
            MethodExpression methodExpression = ((MethodExpression) expression);
            return methodExpression.getMethodName();
        } else {
            throw new AssertionError();
        }
    }
}
