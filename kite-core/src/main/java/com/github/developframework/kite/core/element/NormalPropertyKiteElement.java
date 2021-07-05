package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.node.ObjectNodeProxy;
import com.github.developframework.kite.core.structs.FragmentLocation;

/**
 * 普通属性节点
 *
 * @author qiushui on 2021-06-23.
 */
public final class NormalPropertyKiteElement extends PropertyKiteElement {

    public NormalPropertyKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    protected boolean support(Class<?> sourceClass) {
        // 这里总是为true
        return true;
    }

    @Override
    protected void handle(ObjectNodeProxy parentNode, Object value, String displayName) {
        parentNode.putValue(displayName, value, contentAttributes.xmlCDATA);
    }
}
