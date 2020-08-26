package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.element.AbstractKiteElement;
import com.github.developframework.kite.core.element.ContainChildElementable;
import com.github.developframework.kite.core.element.KiteElement;

/**
 * 抽象的xml解析器
 *
 * @author qiuzhenhao
 */
abstract class AbstractElementSaxParser implements ElementSaxParser {

    protected KiteConfiguration kiteConfiguration;

    AbstractElementSaxParser(KiteConfiguration kiteConfiguration) {
        this.kiteConfiguration = kiteConfiguration;
    }

    protected void addToParentElement(ParseContext parseContext, KiteElement kiteElement) {
        AbstractKiteElement parentKiteElement = (AbstractKiteElement) parseContext.getStack().peek();
        if (kiteElement instanceof AbstractKiteElement) {
            ((AbstractKiteElement) kiteElement).setChildrenNamingStrategy(parentKiteElement.getChildrenNamingStrategy());
        }
        if (parentKiteElement instanceof ContainChildElementable) {
            ((ContainChildElementable) parentKiteElement).addChildElement(kiteElement);
        }
    }
}
