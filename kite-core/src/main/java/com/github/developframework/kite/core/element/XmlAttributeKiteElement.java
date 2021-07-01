package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.TemplateLocation;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.util.Optional;

/**
 * XML属性节点
 *
 * @author qiushui on 2021-06-29.
 */
public final class XmlAttributeKiteElement extends ContentKiteElement {

    public XmlAttributeKiteElement(TemplateLocation templateLocation) {
        super(templateLocation);
    }

    @Override
    public void assemble(AssembleContext context) {
        final Optional<Object> dataValue = KiteUtils.getDataValue(context, this);
        dataValue.ifPresent(o -> context.peekNodeProxy().putAttribute(displayName(context), o));
    }
}
