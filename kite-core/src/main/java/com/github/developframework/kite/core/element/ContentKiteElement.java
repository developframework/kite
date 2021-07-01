package com.github.developframework.kite.core.element;

import com.github.developframework.expression.ArrayExpression;
import com.github.developframework.expression.Expression;
import com.github.developframework.expression.MethodExpression;
import com.github.developframework.expression.ObjectExpression;
import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.Framework;
import com.github.developframework.kite.core.strategy.KitePropertyNamingStrategy;
import com.github.developframework.kite.core.strategy.NamingStrategy;
import com.github.developframework.kite.core.structs.ContentAttributes;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.TemplateLocation;
import lombok.Getter;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * @author qiushui on 2021-06-23.
 */
public abstract class ContentKiteElement extends AbstractKiteElement {

    @Getter
    protected ContentAttributes contentAttributes;

    public ContentKiteElement(TemplateLocation templateLocation) {
        super(templateLocation);
    }

    @Override
    public void configure(ElementDefinition elementDefinition) {
        super.configure(elementDefinition);
        contentAttributes = ContentAttributes.of(elementDefinition);
    }

    /**
     * 经过命名策略修改决定显示名称
     *
     * @param context 上下文
     * @return 显示名称
     */
    protected String displayName(AssembleContext context) {
        if (isNotEmpty(contentAttributes.alias)) {
            return contentAttributes.alias;
        }
        final Framework<?> framework = context.switchFramework();
        final NamingStrategy namingStrategy = contentAttributes.namingStrategy != null ?
                contentAttributes.namingStrategy : context.getOptionNamingStrategy();
        final KitePropertyNamingStrategy propertyNamingStrategy = namingStrategy == NamingStrategy.FRAMEWORK ?
                framework.namingStrategy() : namingStrategy.getNamingStrategy();
        return propertyNamingStrategy.propertyDisplayName(framework, determineNameFromExpression());
    }

    /**
     * 从表达式中决定名称
     */
    private String determineNameFromExpression() {
        Expression expression = contentAttributes.dataDefinition.getExpression();
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
