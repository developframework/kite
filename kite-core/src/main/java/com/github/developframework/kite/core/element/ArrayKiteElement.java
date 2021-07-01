package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.node.ArrayNodeProxy;
import com.github.developframework.kite.core.structs.ArrayAttributes;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.TemplateLocation;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.util.Optional;

/**
 * 数组节点
 *
 * @author qiushui on 2021-06-24.
 */
public class ArrayKiteElement extends ContainerKiteElement {

    // 数组属性
    protected ArrayAttributes arrayAttributes;

    public ArrayKiteElement(TemplateLocation templateLocation) {
        super(templateLocation);
    }

    @Override
    public void configure(ElementDefinition elementDefinition) {
        super.configure(elementDefinition);
        arrayAttributes = ArrayAttributes.of(elementDefinition);
    }

    @Override
    public void assemble(AssembleContext context) {
        final Optional<Object> dataValue = KiteUtils.getDataValue(context, this);
        assembleWithArrayObject(context, dataValue.orElse(null));
    }

    /**
     * 组装数组
     *
     * @param context  上下文
     * @param arrayObj 数组对象
     */
    public final void assembleWithArrayObject(AssembleContext context, Object arrayObj) {
        if (arrayObj != null) {
            final ArrayNodeProxy arrayNodeProxy = context.peekNodeProxy().putArrayNode(displayName(context));
            assembleArrayItems(context, arrayObj, arrayNodeProxy);
        } else if (!contentAttributes.nullHidden) {
            // 处理null-empty功能
            if (arrayAttributes.nullEmpty) {
                context.peekNodeProxy().putArrayNode(displayName(context));
            } else {
                context.peekNodeProxy().putNull(displayName(context));
            }
        }
    }

    /**
     * 组装数组（指定了节点）
     *
     * @param context        上下文
     * @param arrayObj       数组对象
     * @param arrayNodeProxy 数组节点代理
     */
    public final void assembleArrayItems(AssembleContext context, Object arrayObj, ArrayNodeProxy arrayNodeProxy) {
        final Object[] array = KiteUtils.objectToArray(arrayObj, contentAttributes.dataDefinition);
        // 处理comparator功能
        KiteUtils.handleArrayComparator(context.dataModel, arrayAttributes.comparatorValue, array);
        // 记录上层数组长度和索引
        final int parentLength = context.arrayLength, parentIndex = context.arrayIndex;

        // 处理limit功能
        context.arrayLength = arrayAttributes.limit != null && arrayAttributes.limit < array.length ? arrayAttributes.limit : array.length;
        for (context.arrayIndex = 0; context.arrayIndex < context.arrayLength; context.arrayIndex++) {
            // 处理map功能
            final Object v = KiteUtils.handleKiteConverter(context.dataModel, arrayAttributes.mapValue, array[context.arrayIndex]);
            if (elements.isEmpty()) {
                arrayNodeProxy.addValue(arrayAttributes, v);
            } else {
                context.pushValue(v);
                context.pushNodeProxy(arrayNodeProxy.addObject(arrayAttributes, context));
                forEachAssemble(context);
                context.pop();
            }
        }

        // 恢复上层数组长度和索引
        context.arrayLength = parentLength;
        context.arrayIndex = parentIndex;
    }
}
