package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.TemplateLocation;

/**
 * 功能型节点
 *
 * @author qiuzhenhao
 */
public abstract class FunctionalKiteElement extends AbstractKiteElement {

    public FunctionalKiteElement(KiteConfiguration configuration, TemplateLocation templateLocation) {
        super(configuration, templateLocation);
    }
}
