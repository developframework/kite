package com.github.developframework.kite.dom4j;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.node.ArrayNodeProxy;
import com.github.developframework.kite.core.node.ObjectNodeProxy;
import lombok.RequiredArgsConstructor;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author qiushui on 2021-06-28.
 */
@RequiredArgsConstructor
public class Dom4jObjectNodeProxy implements ObjectNodeProxy {

    private final Element element;

    @Override
    public Object getNode() {
        return element;
    }

    @Override
    public ObjectNodeProxy putObjectNode(String name) {
        return new Dom4jObjectNodeProxy(element.addElement(name));
    }

    @Override
    public ArrayNodeProxy putArrayNode(String name) {
        return new Dom4jArrayNodeProxy(element.addElement(name));
    }

    @Override
    public void putRaw(String name, String raw) {
        try {
            element.addElement(name).add(DocumentHelper.parseText(raw).getRootElement());
        } catch (DocumentException e) {
            throw new KiteException("XML解析失败");
        }
    }

    @Override
    public void putPrototype(AssembleContext context, String name, Object prototype) {
        throw new KiteException("dom4j不支持prototype功能");
    }

    @Override
    public void putValue(String name, Object value, boolean xmlCDATA) {
        final Element item = element.addElement(name);
        if (value != null) {
            if (xmlCDATA) {
                item.addCDATA(value.toString());
            } else {
                item.addText(value.toString());
            }
        }
    }

    @Override
    public void putNull(String name) {
        element.addElement(name);
    }

    @Override
    public void putAttribute(String name, Object value) {
        element.addAttribute(name, value.toString());
    }
}
