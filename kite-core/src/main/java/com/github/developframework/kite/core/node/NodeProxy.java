package com.github.developframework.kite.core.node;

/**
 * 真实节点代理
 *
 * @author qiushui on 2021-06-23.
 */
@FunctionalInterface
public interface NodeProxy {

    /**
     * 获得真实节点
     *
     * @return 真实节点
     */
    Object getNode();
}
