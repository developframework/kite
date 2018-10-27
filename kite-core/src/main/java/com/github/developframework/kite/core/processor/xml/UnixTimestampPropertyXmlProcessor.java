package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.element.PropertyKiteElement;

/**
 * unix时间戳型属性节点处理器
 * @author qiuzhenhao
 */
public class UnixTimestampPropertyXmlProcessor extends DatePropertyXmlProcessor {

    public UnixTimestampPropertyXmlProcessor(XmlProcessContext context, PropertyKiteElement element) {
        super(context, element);
    }

    @Override
    protected void handle(Class<?> clazz, Object value) {
        java.util.Date date = transformDate(clazz, value);
        this.elementAddContent(String.valueOf(date.getTime() / 1000));
    }
}
