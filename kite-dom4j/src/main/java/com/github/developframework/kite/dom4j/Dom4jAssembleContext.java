package com.github.developframework.kite.dom4j;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.node.ArrayNodeProxy;
import com.github.developframework.kite.core.node.ObjectNodeProxy;

public class Dom4jAssembleContext extends AssembleContext {

    public Dom4jAssembleContext(KiteConfiguration configuration) {
        super(configuration, false);
    }

    @Override
    public ObjectNodeProxy createObjectNodeProxy() {
        return new Dom4jObjectNodeProxy(null);
    }

    @Override
    public ArrayNodeProxy createArrayNodeProxy() {
        return new Dom4jArrayNodeProxy(null);
    }

}
