package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.exception.InvalidAttributeException;
import com.github.developframework.kite.core.structs.ElementAttributes;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.utils.KiteUtils;
import lombok.Getter;

import java.util.Optional;

/**
 * fragment 片段
 *
 * @author qiushui on 2021-07-05.
 */
@ElementAttributes({
        ElementDefinition.Attribute.ID,
        ElementDefinition.Attribute.EXTEND
})
public class Fragment extends ContainerKiteElement {

    // 片段ID
    @Getter
    protected String id;

    // extend指向的模板地址
    protected FragmentLocation fragmentLocationExtend;

    public Fragment(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void configure(ElementDefinition elementDefinition) {
        super.configure(elementDefinition);
        id = elementDefinition.getString(ElementDefinition.Attribute.ID);
        // 解析extend指向的模板地址
        fragmentLocationExtend = parseExtend(elementDefinition);
    }

    @Override
    public void assemble(AssembleContext context) {
        if (fragmentLocationExtend == null) {
            if (contentAttributes.dataDefinition == DataDefinition.EMPTY) {
                // data为空时直接迭代组装子节点
                forEachAssemble(context);
            } else {
                final Optional<Object> dataValue = KiteUtils.getDataValue(context, this);
                if (dataValue.isPresent()) {
                    context.prepareNextOnlyValue(dataValue.get(), this::forEachAssemble);
                } else if (!contentAttributes.nullHidden) {
                    context.nodeStack.peek().putNull(displayName(context));
                }
            }
        } else {
            context.slotStack.push(this);
            context
                    .extractFragment(fragmentLocationExtend.getNamespace(), fragmentLocationExtend.getFragmentId())
                    .assemble(context);
            context.slotStack.pop();
        }
    }

    /**
     * 解析extend指向的模板地址
     */
    private FragmentLocation parseExtend(ElementDefinition elementDefinition) {
        String extend = elementDefinition.getString(ElementDefinition.Attribute.EXTEND);
        if (extend != null) {
            // 兼容老版本
            final int i = extend.lastIndexOf(":");
            if (i > 0) {
                extend = extend.substring(0, i);
            }
            if (extend.matches("^(.+\\.)?.+$")) {
                final String[] parts = extend.split("\\.");
                if (parts.length == 1) {
                    return new FragmentLocation(fragmentLocation.getNamespace(), parts[0]);
                } else {
                    return new FragmentLocation(parts[0], parts[1]);
                }
            } else {
                throw new InvalidAttributeException(ElementDefinition.Attribute.EXTEND, extend, "格式错误");
            }
        }
        return null;
    }
}
