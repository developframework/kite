package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.element.ContainerKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.strategy.KitePropertyNamingStrategy;
import com.github.developframework.kite.core.strategy.NamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;

import java.util.Stack;

/**
 * 容器节点解析器
 *
 * @param <T> 容器节点类型
 * @author qiuzhenhao
 */
@Slf4j
abstract class ContainerElementSaxParser<T extends ContainerKiteElement> extends ContentElementSaxParser<T> {

    ContainerElementSaxParser(KiteConfiguration kiteConfiguration) {
        super(kiteConfiguration);
    }

    @Override
    public void handleAtEndElement(ParseContext parseContext) {
        ((ContainerKiteElement) parseContext.getStack().pop()).loadForClassAllProperty();
    }

    @Override
    protected void addOtherAttributes(ParseContext parseContext, T element, Attributes attributes) {
        element.setNullHidden(attributes.getValue("null-hidden"));
        element.setForClass(attributes.getValue("for-class"));
        element.setChildrenNamingStrategy(
                parseChildrenNamingStrategy(parseContext, attributes.getValue("children-naming-strategy"))
        );
    }

    @Override
    protected void otherOperation(ParseContext parseContext, T element) {
        parseContext.getStack().push(element);
    }

    /**
     * 解析children-naming-strategy
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected final KitePropertyNamingStrategy parseChildrenNamingStrategy(ParseContext parseContext, String namingStrategyStr) {
        if (StringUtils.isEmpty(namingStrategyStr)) {
            Stack<KiteElement> stack = parseContext.getStack();
            if (!stack.isEmpty()) {
                KiteElement parentKiteElement = stack.peek();
                if (parentKiteElement instanceof ContainerKiteElement) {
                    return ((ContainerKiteElement) parentKiteElement).getChildrenNamingStrategy();
                }
            }
        } else if (namingStrategyStr.equals("DEFAULT")) {
            return null;
        } else {
            try {
                NamingStrategy namingStrategy = NamingStrategy.valueOf(namingStrategyStr);
                return namingStrategy.getNamingStrategy();
            } catch (IllegalArgumentException e) {
                try {
                    Class clazz = Class.forName(namingStrategyStr);
                    if (KitePropertyNamingStrategy.class.isAssignableFrom(clazz)) {
                        return (KitePropertyNamingStrategy) clazz.getConstructor().newInstance();
                    }
                } catch (Exception ex) {
                    log.warn("\"children-naming-strategy\" value \"{}\" cloud not parse.", namingStrategyStr);
                }
            }
        }
        return null;
    }
}
