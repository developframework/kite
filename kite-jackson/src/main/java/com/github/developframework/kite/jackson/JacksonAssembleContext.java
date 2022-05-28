package com.github.developframework.kite.jackson;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.node.ArrayNodeProxy;
import com.github.developframework.kite.core.node.ObjectNodeProxy;

public final class JacksonAssembleContext extends AssembleContext {

    public JacksonAssembleContext(KiteConfiguration configuration) {
        super(configuration, true);
    }

    @Override
    public ObjectNodeProxy createObjectNodeProxy(Object node) {
        return new JacksonObjectNodeProxy((ObjectNode) node);
    }

    @Override
    public ArrayNodeProxy createArrayNodeProxy(Object node) {
        return new JacksonArrayNodeProxy((ArrayNode) node);
    }
}
