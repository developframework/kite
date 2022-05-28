package com.github.developframework.kite.fastjson;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.node.ArrayNodeProxy;
import com.github.developframework.kite.core.node.ObjectNodeProxy;

public class FastjsonAssembleContext extends AssembleContext {

    public FastjsonAssembleContext(KiteConfiguration configuration) {
        super(configuration, true);
    }

    @Override
    public ObjectNodeProxy createObjectNodeProxy() {
        return new FastjsonObjectNodeProxy(new JSONObject(true));
    }

    @Override
    public ArrayNodeProxy createArrayNodeProxy() {
        return new FastjsonArrayNodeProxy(new JSONArray());
    }
}
