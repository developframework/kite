package com.github.developframework.kite.fastjson;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.node.ArrayNodeProxy;
import com.github.developframework.kite.core.node.ObjectNodeProxy;
import com.github.developframework.kite.core.structs.ArrayAttributes;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author qiushui on 2021-07-15.
 */
@RequiredArgsConstructor
public final class FastjsonArrayNodeProxy implements ArrayNodeProxy {

    @Getter
    private final JSONArray node;

    @Override
    public void addValue(ArrayAttributes arrayAttributes, Object value) {
        node.add(value);
    }

    @Override
    public ObjectNodeProxy addObject(ArrayAttributes arrayAttributes, AssembleContext context) {
        final JSONObject object = new JSONObject(true);
        node.add(object);
        return new FastjsonObjectNodeProxy(object);
    }
}
