package com.github.developframework.kite.gson;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.node.ArrayNodeProxy;
import com.github.developframework.kite.core.node.ObjectNodeProxy;
import com.github.developframework.kite.core.structs.ArrayAttributes;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author qiushui on 2021-07-15.
 */
@RequiredArgsConstructor
public final class GsonArrayNodeProxy implements ArrayNodeProxy {

    @Getter
    private final JsonArray node;

    @Override
    public void addValue(ArrayAttributes arrayAttributes, Object value) {
        if (value == null) {
            node.add(JsonNull.INSTANCE);
        } else if (value instanceof String) {
            node.add((String) value);
        } else if (value instanceof Number) {
            node.add((Number) value);
        } else if (value instanceof Boolean) {
            node.add((Boolean) value);
        } else {
            node.add(value.toString());
        }
    }

    @Override
    public ObjectNodeProxy addObject(ArrayAttributes arrayAttributes, AssembleContext context) {
        JsonObject jsonObject = new JsonObject();
        node.add(jsonObject);
        return new GsonObjectNodeProxy(jsonObject);
    }

}
