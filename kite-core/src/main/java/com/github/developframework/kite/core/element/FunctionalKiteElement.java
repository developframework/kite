package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.KiteConfiguration;

/**
 * 功能型节点
 *
 * @author qiuzhenhao
 */
public abstract class FunctionalKiteElement extends AbstractKiteElement {

    public FunctionalKiteElement(KiteConfiguration configuration, String namespace, String templateId) {
        super(configuration, namespace, templateId);
    }
}
