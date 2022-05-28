package com.github.developframework.kite.dom4j;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.node.ArrayNodeProxy;
import com.github.developframework.kite.core.node.ObjectNodeProxy;

public class Dom4jAssembleContext extends AssembleContext {

    public Dom4jAssembleContext(KiteConfiguration configuration, DataModel dataModel) {
        super(configuration, dataModel, false);
    }

    @Override
    public ObjectNodeProxy createObjectNodeProxy() {
        // 不使用这个方法
        return null;
    }

    @Override
    public ArrayNodeProxy createArrayNodeProxy() {
        // 不使用这个方法
        return null;
    }

}
