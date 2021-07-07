package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.exception.LinkSizeNotEqualException;
import com.github.developframework.kite.core.structs.ElementAttributes;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.util.Optional;

/**
 * 一对一链接节点
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
            // 处理comparator功能
            KiteUtils.handleArrayComparator(context.dataModel, arrayAttributes.comparatorValue, array);
            // 处理limit功能
            final int length = arrayAttributes.limit != null && arrayAttributes.limit < array.length ? arrayAttributes.limit : array.length;
            if (length != context.arrayLength) {
                throw new LinkSizeNotEqualException(fragmentLocation);
            }
            // 处理map功能
            final Object v = KiteUtils.handleKiteConverter(context.dataModel, arrayAttributes.mapValue, array[context.arrayIndex]);
            if (elements.isEmpty()) {
                context.peekNodeProxy().putValue(displayName(context), v, contentAttributes.xmlCDATA);
            } else if (mergeParent) {
                context.pushValue(v);
                forEachAssemble(context);
                context.popValue();
            } else {
                context.parentPutNodeProxyAndPush(displayName(context));
                context.pushValue(v);
                forEachAssemble(context);
                context.pop();
            }
        } else if (!contentAttributes.nullHidden) {
            context.peekNodeProxy().putNull(displayName(context));
        }
    }
}
