package com.github.developframework.kite.dom4j;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.node.ArrayNodeProxy;
import com.github.developframework.kite.core.node.ObjectNodeProxy;
import com.github.developframework.kite.core.structs.ArrayAttributes;
import lombok.RequiredArgsConstructor;
import org.dom4j.Element;

/**
 * @author qiushui on 2021-06-28.
 */
@RequiredArgsConstructor
public class Dom4jArrayNodeProxy implements ArrayNodeProxy {

    private final Element element;

    @Override
    public void addValue(ArrayAttributes arrayAttributes, Object value) {
        final Element item = element.addElement(arrayAttributes.xmlItem);
        if (value != null) {
            item.addText(value.toString());
        }
    }

    @Override
    public ObjectNodeProxy addObject(ArrayAttributes arrayAttributes, AssembleContext context) {
        return new Dom4jObjectNodeProxy(element.addElement(arrayAttributes.xmlItem));
    }

    @Override
    public Object getNode() {
        return element;
    }
}
