package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.element.ContainChildElementable;
import com.github.developframework.kite.core.element.KiteElement;

/**
 * 抽象的xml解析器
 *
 * @author qiuzhenhao
 */
abstract class AbstractElementSaxParser implements ElementSaxParser{

    protected KiteConfiguration kiteConfiguration;

    public AbstractElementSaxParser(KiteConfiguration kiteConfiguration) {
        this.kiteConfiguration = kiteConfiguration;
    }

    protected void addChildElement(ParseContext parseContext, KiteElement kiteElement) {
        KiteElement parentKiteElement = parseContext.getStack().peek();
        if(parentKiteElement instanceof ContainChildElementable) {
            ((ContainChildElementable) parentKiteElement).addChildElement(kiteElement);
        }
    }
}
