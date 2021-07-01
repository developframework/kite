package com.github.developframework.kite.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.node.ArrayNodeProxy;
import com.github.developframework.kite.core.node.ObjectNodeProxy;
import com.github.developframework.kite.core.structs.ArrayAttributes;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiushui on 2021-06-23.
 */
@RequiredArgsConstructor
public final class JacksonArrayNodeProxy implements ArrayNodeProxy {

    @Getter
    private final ArrayNode node;

    @Override
    public void addValue(ArrayAttributes arrayAttributes, Object value) {
        if (value == null) {
            node.addNull();
        } else if (value instanceof String) {
            node.add((String) value);
        } else if (value instanceof Integer) {
            node.add((Integer) value);
        } else if (value instanceof Long) {
            node.add((Long) value);
        } else if (value instanceof BigDecimal) {
            node.add((BigDecimal) value);
        } else if (value instanceof Float) {
            node.add((Float) value);
        } else if (value instanceof Double) {
            node.add((Double) value);
        } else if (value instanceof Boolean) {
            node.add((Boolean) value);
        } else if (value instanceof Short) {
            node.add((Short) value);
        } else if (value instanceof BigInteger) {
            node.add((BigInteger) value);
        } else {
            node.add(value.toString());
        }
    }

    @Override
    public ObjectNodeProxy addObject(ArrayAttributes arrayAttributes, AssembleContext context) {
        final ObjectMapper objectMapper = (ObjectMapper) context.getConfiguration().getJsonFramework().getCore();
        final ObjectNode objectNode = objectMapper.createObjectNode();
        node.add(objectNode);
        return new JacksonObjectNodeProxy(objectNode);
    }
}
