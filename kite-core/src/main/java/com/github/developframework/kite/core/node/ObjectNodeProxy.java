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
     * @param name 属性名称
     * @return 对象节点
     */
    ObjectNodeProxy putObjectNode(String name);

    /**
     * 拼接数组节点
     *
     * @param name 属性名称
     * @return 数组节点
     */
    ArrayNodeProxy putArrayNode(String name);

    /**
     * 拼接原文
     *
     * @param name 属性名称
     * @param raw  原文
     */
    void putRaw(String name, String raw);

    /**
     * 拼接原生对象
     *
     * @param name      属性名称
     * @param prototype 原生对象
     */
    void putPrototype(AssembleContext context, String name, Object prototype);

    /**
     * 添加属性节点
     *
     * @param name     属性名称
     * @param value    属性节点
     * @param xmlCDATA xml是否用CDATA包括
     */
    void putValue(String name, Object value, boolean xmlCDATA);

    /**
     * 添加null值
     *
     * @param name 属性名称
     */
    void putNull(String name);

    /**
     * 添加xml属性（仅限xml支持）
     *
     * @param name  属性名称
     * @param value xml属性值
     */
    default void putAttribute(String name, Object value) {
    }
}
