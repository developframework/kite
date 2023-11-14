package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.node.ArrayNodeProxy;
import com.github.developframework.kite.core.structs.ArrayAttributes;
import com.github.developframework.kite.core.structs.ElementAttributes;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.utils.KiteUtils;
import lombok.Getter;

import java.util.Optional;

/**
 * array 数组节点
 *
 * @author qiushui on 2021-06-24.
 */
@ElementAttributes({
        ElementDefinition.Attribute.MAP,
        ElementDefinition.Attribute.COMPARATOR,
        ElementDefinition.Attribute.LIMIT,
        ElementDefinition.Attribute.NULL_EMPTY,
        ElementDefinition.Attribute.XML_ITEM
})
public class ArrayKiteElement extends ContainerKiteElement {

    // 数组属性
    @Getter
    protected ArrayAttributes arrayAttributes;

    public ArrayKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void configure(ElementDefinition elementDefinition) {
        super.configure(elementDefinition);
        arrayAttributes = ArrayAttributes.of(elementDefinition);
    }

    @Override
    public void assemble(AssembleContext context) {
        final Optional<Object> dataValue = KiteUtils.getDataValue(context, this);
        assembleArray(context, dataValue.orElse(null));
    }

    /**
     * 组装数组
     *
     * @param context  上下文
     * @param arrayObj 数组对象
     */
    public final void assembleArray(AssembleContext context, Object arrayObj) {
        if (arrayObj != null) {
            final ArrayNodeProxy arrayNodeProxy = context.nodeStack.peek().putArrayNode(displayName(context));
            assembleArrayItems(context, arrayAttributes, arrayObj, arrayNodeProxy);
        } else if (!contentAttributes.nullHidden) {
            // 处理null-empty功能
            if (arrayAttributes.nullEmpty) {
                context.nodeStack.peek().putArrayNode(displayName(context));
            } else {
                context.nodeStack.peek().putNull(displayName(context));
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
    public final void assembleArrayItems(AssembleContext context, ArrayAttributes arrayAttributes, Object arrayObj, ArrayNodeProxy arrayNodeProxy) {
        final Object[] array = KiteUtils.objectToArray(arrayObj, contentAttributes.dataDefinition);
        // 处理comparator功能
        KiteUtils.handleArrayComparator(context.dataModel, arrayAttributes.comparatorComponent, array);
        // 记录上层数组长度和索引
        final int parentLength = context.arrayLength, parentIndex = context.arrayIndex;

        // 处理limit功能
        context.arrayLength = arrayAttributes.limit != null && arrayAttributes.limit > 0 && arrayAttributes.limit < array.length ? arrayAttributes.limit : array.length;
        for (context.arrayIndex = 0; context.arrayIndex < context.arrayLength; context.arrayIndex++) {
            // 处理map功能
            final Object item = KiteUtils.handleKiteConverter(context.dataModel, arrayAttributes.mapComponent, array[context.arrayIndex]);
            if (KiteUtils.objectIsArray(item)) {
                // 元素任然是数组型
                assembleArrayItems(
                        context,
                        /* 嵌套数组需要跳过函数处理 */
                        arrayAttributes.basic(),
                        item,
                        arrayNodeProxy.addArray(arrayAttributes, context)
                );
            } else if (elements.isEmpty()) {
                arrayNodeProxy.addValue(arrayAttributes, item);
            } else {
                // 元素是普通对象
                context.prepareNext(arrayNodeProxy.addObject(arrayAttributes, context), item, this::forEachAssemble);
            }
        }

        // 恢复上层数组长度和索引
        context.arrayLength = parentLength;
        context.arrayIndex = parentIndex;
    }
}
