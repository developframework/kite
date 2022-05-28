package com.github.developframework.kite.dom4j;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.node.ArrayNodeProxy;
import com.github.developframework.kite.core.node.ObjectNodeProxy;
import org.dom4j.Element;

public class Dom4jAssembleContext extends AssembleContext {

    public Dom4jAssembleContext(KiteConfiguration configuration) {
        super(configuration, false);
    }

    @Override
    public ObjectNodeProxy createObjectNodeProxy(Object node) {
        return new Dom4jObjectNodeProxy((Element) node);
    }

    @Override
    public ArrayNodeProxy createArrayNodeProxy(Object node) {
        return new Dom4jArrayNodeProxy((Element) node);
    }

}
