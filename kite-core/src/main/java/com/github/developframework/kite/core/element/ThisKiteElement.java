package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.utils.KiteUtils;

/**
 * this 元素
 *
 * @author qiushui on 2021-06-30.
 */
public final class ThisKiteElement extends ArrayKiteElement {

    public ThisKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void assemble(AssembleContext context) {
        final Object v = KiteUtils.handleKiteConverter(context.dataModel, contentAttributes.converterComponent, context.valueStack.peek());
        if (elements.isEmpty()) {
            context.nodeStack.peek().putValue(displayName(context), v, contentAttributes.xmlCDATA);
        } else if (KiteUtils.objectIsArray(v)) {
            // 元素任然是数组型
            assembleArrayItems(
                    context,
                    /* 嵌套数组需要跳过函数处理 */
                    arrayAttributes.basic(),
                    v,
                    context.nodeStack.peek().putArrayNode(displayName(context))
            );
        } else {
            context.prepareNext(
                    context.nodeStack.peek().putObjectNode(displayName(context)),
                    v,
                    this::forEachAssemble
            );
        }
    }
}
