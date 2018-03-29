package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.element.PropertyKiteElement;
import org.dom4j.Element;

/**
 * 普通属性节点处理器
 * @author qiuzhenhao
 */
public class NormalPropertyXmlProcessor extends PropertyXmlProcessor {

    public NormalPropertyXmlProcessor(XmlProcessContext context, PropertyKiteElement element, Expression parentExpression) {
        super(context, element, parentExpression);
    }

    @Override
    protected boolean support(Class<?> sourceClass) {
        // 这里总是为true
        return true;
    }

    @Override
    protected void handle(Element parentNode, Class<?> clazz, Object value, String showName) {
        this.elementAddContent(parentNode, showName, value.toString());
    }
}
