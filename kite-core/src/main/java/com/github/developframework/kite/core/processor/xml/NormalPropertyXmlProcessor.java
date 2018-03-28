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
        parentNode.addElement(showName).addText(value.toString());
//        if (clazz == String.class) {
//            parentNode.put(showName, (String) value);
//        } else if (clazz == Integer.class) {
//            parentNode.put(showName, (Integer) value);
//        } else if (clazz == Long.class) {
//            parentNode.put(showName, (Long) value);
//        } else if (clazz == Boolean.class) {
//            parentNode.put(showName, (Boolean) value);
//        } else if (clazz == Float.class) {
//            parentNode.put(showName, (Float) value);
//        } else if (clazz == Double.class) {
//            parentNode.put(showName, (Double) value);
//        } else if (clazz == BigDecimal.class) {
//            parentNode.put(showName, (BigDecimal) value);
//        } else {
//            parentNode.put(showName, value.toString());
//        }
    }
}
