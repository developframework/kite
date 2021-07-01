package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.exception.InvalidAttributeException;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.TemplateLocation;
import com.github.developframework.kite.core.utils.KiteUtils;
import lombok.Getter;

import java.util.Optional;

/**
 * 模板
 *
 * @author qiushui on 2021-06-23.
 */
@Getter
public final class Template extends ContainerKiteElement {

    // 模板的ID
    private String id;

    // xml根节点名称
    private String xmlRoot;

    // extend指向的模板地址
    private TemplateLocation templateLocationExtend;

    // 内置array节点
    private ArrayKiteElement innerArrayKiteElement;

    public Template(TemplateLocation templateLocation) {
        super(templateLocation);
    }

    @Override
    public void configure(ElementDefinition elementDefinition) {
        super.configure(elementDefinition);
        id = elementDefinition.getString(ElementDefinition.Attribute.ID);
        xmlRoot = elementDefinition.getString(ElementDefinition.Attribute.XML_ROOT, "xml");
        if (contentAttributes.dataDefinition != DataDefinition.EMPTY) {
            // 如果data不为空的话需要构建一个内置array节点
            innerArrayKiteElement = new ArrayKiteElement(templateLocation);
            innerArrayKiteElement.configure(elementDefinition);
        }
        // 解析extend指向的模板地址
        templateLocationExtend = parseExtend(elementDefinition);
    }

    @Override
    public void assemble(AssembleContext context) {
        if (templateLocationExtend == null) {
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
                    .extractTemplate(templateLocationExtend)
                    .assemble(context);
            context.slotStack.pop();
        }
    }

    /**
     * 解析extend指向的模板地址
     */
    private TemplateLocation parseExtend(ElementDefinition elementDefinition) {
        final String extend = elementDefinition.getString(ElementDefinition.Attribute.EXTEND);
        if (extend != null) {
            if (extend.matches("^(.+\\.)?.+$")) {
                final String[] parts = extend.split("\\.");
                if (parts.length == 1) {
                    return new TemplateLocation(templateLocation.getNamespace(), parts[0]);
                } else {
                    return new TemplateLocation(parts[0], parts[1]);
                }
            } else {
                throw new InvalidAttributeException(ElementDefinition.Attribute.EXTEND, extend, "format error");
            }
        }
        return null;
    }
}
