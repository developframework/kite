package com.github.developframework.kite.gson;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.node.ArrayNodeProxy;
import com.github.developframework.kite.core.node.ObjectNodeProxy;
import com.google.gson.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author qiushui on 2021-07-15.
 */
@RequiredArgsConstructor
public final class GsonObjectNodeProxy implements ObjectNodeProxy {

    @Getter
    private final JsonObject node;

    @Override
    public ObjectNodeProxy putObjectNode(String name) {
        JsonObject jsonObject = new JsonObject();
        node.add(name, jsonObject);
        return new GsonObjectNodeProxy(jsonObject);
    }

    @Override
    public ArrayNodeProxy putArrayNode(String name) {
        JsonArray jsonArray = new JsonArray();
        node.add(name, jsonArray);
        return new GsonArrayNodeProxy(jsonArray);
    }

    @Override
    public void putRaw(AssembleContext context, String name, String raw) {
        node.add(name, JsonParser.parseString(raw));
    }

    @Override
    public void putPrototype(AssembleContext context, String name, Object prototype) {
        Gson gson = (Gson) context.getConfiguration().getJsonFramework().getCore();
        final JsonElement jsonElement = gson.toJsonTree(prototype);
        node.add(name, jsonElement);
    }

    @Override
    public void putValue(String name, Object value, boolean xmlCDATA) {
        if (value instanceof String) {
            node.addProperty(name, (String) value);
        } else if (value instanceof Number) {
            node.addProperty(name, (Number) value);
        } else if (value instanceof Boolean) {
            node.addProperty(name, (Boolean) value);
        } else {
            node.addProperty(name, value.toString());
        }
    }

    @Override
    public void putNull(String name) {
        node.add(name, JsonNull.INSTANCE);
    }
}
