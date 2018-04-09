package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.element.PropertyKiteElement;
import org.dom4j.Element;

import java.util.Objects;

/**
 * unix时间戳型属性节点处理器
 * @author qiuzhenhao
 */
public class UnixTimestampPropertyXmlProcessor extends DatePropertyXmlProcessor {

    public UnixTimestampPropertyXmlProcessor(XmlProcessContext context, PropertyKiteElement element, Expression parentExpression) {
        super(context, element, parentExpression);
    }

    @Override
    protected void handle(Class<?> clazz, Object value) {
        java.util.Date date = transformDate(clazz, value);
        this.elementAddContent(String.valueOf(date.getTime() / 1000));
    }
}
