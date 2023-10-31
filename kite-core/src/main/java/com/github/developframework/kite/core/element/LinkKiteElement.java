package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.exception.LinkSizeNotEqualException;
import com.github.developframework.kite.core.structs.ElementAttributes;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.util.Optional;

/**
 * link 一对一链接元素
 *
 * @author qiushui on 2021-06-24.
 */
@ElementAttributes({
        ElementDefinition.Attribute.MERGE_PARENT
})
public final class LinkKiteElement extends ArrayKiteElement {

    // 是否合并到父节点
    private boolean mergeParent;

    public LinkKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void configure(ElementDefinition elementDefinition) {
        super.configure(elementDefinition);
        mergeParent = elementDefinition.getBoolean(ElementDefinition.Attribute.MERGE_PARENT, false);
    }

    @Override
    public void assemble(AssembleContext context) {
        final Optional<Object> dataValue = KiteUtils.getDataValue(context, this);
        if (dataValue.isPresent()) {
            final Object[] array = KiteUtils.objectToArray(dataValue.get(), contentAttributes.dataDefinition);
            // 处理limit功能
            final int length = arrayAttributes.limit != null && arrayAttributes.limit < array.length ? arrayAttributes.limit : array.length;
            if (length != context.arrayLength) {
                throw new LinkSizeNotEqualException(fragmentLocation, length, context.arrayLength);
            }
            // 处理comparator功能
            KiteUtils.handleArrayComparator(context.dataModel, arrayAttributes.comparatorComponent, array);
            // 处理map功能
            final Object item = KiteUtils.handleKiteConverter(context.dataModel, arrayAttributes.mapComponent, array[context.arrayIndex]);
            if (item == null && !contentAttributes.nullHidden) {
                context.nodeStack.peek().putNull(displayName(context));
            }

            if (KiteUtils.objectIsArray(item)) {
                // 元素任然是数组型
                assembleArrayItems(
                        context,
                        /* 嵌套数组需要跳过函数处理 */
                        arrayAttributes.basic(),
                        item,
                        context.nodeStack.peek().putArrayNode(displayName(context))
                );
            } else if (elements.isEmpty()) {
                context.nodeStack.peek().putValue(displayName(context), item, contentAttributes.xmlCDATA);
            } else if (mergeParent) {
                // 合并到父级
                context.prepareNextOnlyValue(item, this::forEachAssemble);
            } else {
                // 元素是普通对象
                context.prepareNext(
                        context.nodeStack.peek().putObjectNode(displayName(context)),
                        item,
                        this::forEachAssemble
                );
            }
        } else if (!contentAttributes.nullHidden) {
            context.nodeStack.peek().putNull(displayName(context));
        }
    }
}
