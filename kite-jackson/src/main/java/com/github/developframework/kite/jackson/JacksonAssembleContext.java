package com.github.developframework.kite.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.node.ArrayNodeProxy;
import com.github.developframework.kite.core.node.ObjectNodeProxy;

public final class JacksonAssembleContext extends AssembleContext {

    private final ObjectMapper objectMapper;

    public JacksonAssembleContext(KiteConfiguration configuration) {
        super(configuration, true);
        objectMapper = ((JacksonFramework) configuration.getJsonFramework()).getCore();
    }

    @Override
    public ObjectNodeProxy createObjectNodeProxy() {
        return new JacksonObjectNodeProxy(objectMapper.createObjectNode());
    }

    @Override
    public ArrayNodeProxy createArrayNodeProxy() {
        return new JacksonArrayNodeProxy(objectMapper.createArrayNode());
    }
}
