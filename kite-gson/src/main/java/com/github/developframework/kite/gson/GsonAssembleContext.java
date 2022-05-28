package com.github.developframework.kite.gson;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.node.ArrayNodeProxy;
import com.github.developframework.kite.core.node.ObjectNodeProxy;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GsonAssembleContext extends AssembleContext {

    public GsonAssembleContext(KiteConfiguration configuration) {
        super(configuration, true);
    }

    @Override
    public ObjectNodeProxy createObjectNodeProxy() {
        return new GsonObjectNodeProxy(new JsonObject());
    }

    @Override
    public ArrayNodeProxy createArrayNodeProxy() {
        return new GsonArrayNodeProxy(new JsonArray());
    }
}
