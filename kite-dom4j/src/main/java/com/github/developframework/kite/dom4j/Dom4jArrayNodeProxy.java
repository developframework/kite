package com.github.developframework.kite.dom4j;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.node.ArrayNodeProxy;
import com.github.developframework.kite.core.node.ObjectNodeProxy;
import com.github.developframework.kite.core.structs.ArrayAttributes;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dom4j.Element;

/**
 * @author qiushui on 2021-06-28.
 */
@RequiredArgsConstructor
public final class Dom4jArrayNodeProxy implements ArrayNodeProxy {

    @Getter
    private final Element node;

    @Override
    public void addValue(ArrayAttributes arrayAttributes, Object value) {
        final Element item = node.addElement(arrayAttributes.xmlItem);
        if (value != null) {
            item.addText(value.toString());
        }
    }

    @Override
    public ObjectNodeProxy addObject(ArrayAttributes arrayAttributes, AssembleContext context) {
        return new Dom4jObjectNodeProxy(node.addElement(arrayAttributes.xmlItem));
    }

    @Override
    public ArrayNodeProxy addArray(ArrayAttributes arrayAttributes, AssembleContext context) {
        return new Dom4jArrayNodeProxy(node.addElement(arrayAttributes.xmlItem));
    }

}
