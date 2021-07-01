package com.github.developframework.kite.core.node;

import com.github.developframework.kite.core.AssembleContext;

/**
 * 对象节点代理
 *
 * @author qiushui on 2021-06-23.
 */
public interface ObjectNodeProxy extends NodeProxy {

    /**
     * 拼接对象节点
     *
     * @param name
     * @return
     */
    ObjectNodeProxy putObjectNode(String name);

    /**
     * 拼接数组节点
     *
     * @param name
     * @return
     */
    ArrayNodeProxy putArrayNode(String name);

    /**
     * 拼接原文
     *
     * @param name
     * @param raw
     * @return
     */
    void putRaw(String name, String raw);

    /**
     * 拼接原生对象
     *
     * @param name
     * @param prototype
     */
    void putPrototype(AssembleContext context, String name, Object prototype);

    /**
     * 添加属性节点
     *
     * @param name
     * @param value
     */
    void putValue(String name, Object value, boolean xmlCDATA);

    /**
     * 添加null值
     *
     * @param name
     */
    void putNull(String name);

    /**
     * 添加xml属性（仅限xml支持）
     *
     * @param name
     * @param value
     */
    default void putAttribute(String name, Object value) {
    }

    ;
}
