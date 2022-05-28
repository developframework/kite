package com.github.developframework.kite.core.node;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.ArrayAttributes;

/**
 * 数组节点代理
 *
 * @author qiushui on 2021-06-23.
 */
public interface ArrayNodeProxy extends NodeProxy {

    /**
     * 添加元素值
     *
     * @param value 元素值
     */
    void addValue(ArrayAttributes arrayAttributes, Object value);

    /**
     * 添加对象节点代理
     *
     * @param context 上下文
     * @return 对象元素节点代理
     */
    ObjectNodeProxy addObject(ArrayAttributes arrayAttributes, AssembleContext context);

    /**
     * 添加数组节点代理
     *
     * @param context 上下文
     * @return 对象元素节点代理
     */
    ArrayNodeProxy addArray(ArrayAttributes arrayAttributes, AssembleContext context);
}
