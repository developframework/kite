package com.github.developframework.kite.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.node.ArrayNodeProxy;
import com.github.developframework.kite.core.node.ObjectNodeProxy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author qiushui on 2021-07-15.
 */
@RequiredArgsConstructor
public final class FastjsonObjectNodeProxy implements ObjectNodeProxy {

    @Getter
    private final JSONObject node;

    @Override
    public ObjectNodeProxy putObjectNode(String name) {
        final JSONObject jsonObject = new JSONObject(true);
        node.put(name, jsonObject);
        return new FastjsonObjectNodeProxy(jsonObject);
    }

    @Override
    public ArrayNodeProxy putArrayNode(String name) {
        final JSONArray jsonArray = new JSONArray();
        node.put(name, jsonArray);
        return new FastjsonArrayNodeProxy(jsonArray);
    }

    @Override
    public void putRaw(AssembleContext context, String name, String raw) {
        node.put(name, JSON.parse(raw, ParserConfig.getGlobalInstance(), Feature.OrderedField));
    }

    @Override
    public void putPrototype(AssembleContext context, String name, Object prototype) {
        putRaw(context, name, JSON.toJSONString(prototype));
    }

    @Override
    public void putValue(String name, Object value, boolean xmlCDATA) {
        node.put(name, value);
    }

    @Override
    public void putNull(String name) {
        node.put(name, null);
    }
}
