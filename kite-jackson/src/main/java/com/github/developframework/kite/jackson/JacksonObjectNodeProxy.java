package com.github.developframework.kite.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.RawValue;
import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.node.ArrayNodeProxy;
import com.github.developframework.kite.core.node.ObjectNodeProxy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

/**
 * 使用Jackson实现节点代理
 *
 * @author qiushui on 2021-06-23.
 */
@RequiredArgsConstructor
public final class JacksonObjectNodeProxy implements ObjectNodeProxy {

    @Getter
    private final ObjectNode node;

    @Override
    public ObjectNodeProxy putObjectNode(String name) {
        return new JacksonObjectNodeProxy(node.putObject(name));
    }

    @Override
    public ArrayNodeProxy putArrayNode(String name) {
        return new JacksonArrayNodeProxy(node.putArray(name));
    }

    @Override
    public void putRaw(AssembleContext context, String name, String raw) {
        node.putRawValue(name, new RawValue(raw));
    }

    @Override
    public void putPrototype(AssembleContext context, String name, Object prototype) {
        final ObjectMapper objectMapper = (ObjectMapper) context.getConfiguration().getJsonFramework().getCore();
        final JsonNode prototypeNode = objectMapper.valueToTree(prototype);
        node.set(name, prototypeNode);
    }

    @Override
    public void putValue(String name, Object value, boolean xmlCDATA) {
        final Class<?> valueClass = value.getClass();
        if (valueClass == String.class) {
            node.put(name, (String) value);
        } else if (valueClass == Integer.class) {
            node.put(name, (Integer) value);
        } else if (valueClass == Long.class) {
            node.put(name, (Long) value);
        } else if (valueClass == Boolean.class) {
            node.put(name, (Boolean) value);
        } else if (valueClass == Float.class) {
            node.put(name, (Float) value);
        } else if (valueClass == Double.class) {
            node.put(name, (Double) value);
        } else if (valueClass == BigDecimal.class) {
            node.put(name, (BigDecimal) value);
        } else {
            node.put(name, value.toString());
        }
    }

    @Override
    public void putNull(String name) {
        node.putNull(name);
    }
}
