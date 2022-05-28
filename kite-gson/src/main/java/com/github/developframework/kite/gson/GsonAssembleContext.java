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
    public ObjectNodeProxy createObjectNodeProxy(Object node) {
        return new GsonObjectNodeProxy((JsonObject) node);
    }

    @Override
    public ArrayNodeProxy createArrayNodeProxy(Object node) {
        return new GsonArrayNodeProxy((JsonArray) node);
    }
}
