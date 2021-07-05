package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.exception.InvalidAttributeException;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.util.Optional;

/**
 * 片段
 *
 * @author qiushui on 2021-07-05.
 */
public class Fragment extends ContainerKiteElement {

    // 片段ID
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
                    context.pushValue(dataValue.get());
                    forEachAssemble(context);
                    context.pop();
                } else if (!contentAttributes.nullHidden) {
                    context.peekNodeProxy().putNull(displayName(context));
                }
            }
        } else {
            context.slotStack.push(this);
            context
                    .getConfiguration()
                    .getTemplatePackageRegistry()
                    .extractFragment(fragmentLocationExtend)
                    .assemble(context);
            context.slotStack.pop();
        }
    }

    /**
     * 解析extend指向的模板地址
     */
    private FragmentLocation parseExtend(ElementDefinition elementDefinition) {
        final String extend = elementDefinition.getString(ElementDefinition.Attribute.EXTEND);
        if (extend != null) {
            if (extend.matches("^(.+\\.)?.+$")) {
                final String[] parts = extend.split("\\.");
                if (parts.length == 1) {
                    return new FragmentLocation(fragmentLocation.getNamespace(), parts[0]);
                } else {
                    return new FragmentLocation(parts[0], parts[1]);
                }
            } else {
                throw new InvalidAttributeException(ElementDefinition.Attribute.EXTEND, extend, "format error");
            }
        }
        return null;
    }
}
