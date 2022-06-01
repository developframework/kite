package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;

/**
 * 声明元素
 *
 * @author qiushui on 2021-06-23.
 */
public interface KiteElement {

    /**
     * 组装
     *
     * @param context 上下文
     */
    void assemble(AssembleContext context);

}
