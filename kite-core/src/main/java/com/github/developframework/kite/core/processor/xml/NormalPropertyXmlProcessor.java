package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.element.PropertyKiteElement;

/**
 * 普通属性节点处理器
 * @author qiuzhenhao
 */
public class NormalPropertyXmlProcessor extends PropertyXmlProcessor {

    public NormalPropertyXmlProcessor(XmlProcessContext context, PropertyKiteElement element) {
        super(context, element);
    }

    @Override
    protected boolean support(Class<?> sourceClass) {
        // 这里总是为true
        return true;
    }

    @Override
    protected void handle(Class<?> clazz, Object value) {
        this.elementAddContent(value.toString());
    }
}
