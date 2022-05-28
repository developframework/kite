package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.node.ObjectNodeProxy;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.util.Optional;

/**
 * @author qiushui on 2021-06-23.
 */
public abstract class PropertyKiteElement extends ContainerKiteElement {

    public PropertyKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void assemble(AssembleContext context) {
        final Optional<Object> dataValue = KiteUtils.getDataValue(context, this);
        if (dataValue.isPresent()) {
            Object value = dataValue.get();
            final Class<?> valueClass = value.getClass();
            if (support(valueClass)) {
                handle(context.nodeStack.peek(), value, displayName(context));
            }
        } else if (!contentAttributes.nullHidden) {
            context.nodeStack.peek().putNull(displayName(context));
        }
    }

    /**
     * 判断是否支持sourceClass类型
     *
     * @param sourceClass 源类型
     * @return 是否支持
     */
    protected abstract boolean support(Class<?> sourceClass);

    /**
     * 属性具体处理
     *
     * @param parentNode  父树节点
     * @param value       值
     * @param displayName 显示的名称
     */
    protected abstract void handle(ObjectNodeProxy parentNode, Object value, String displayName);
}
