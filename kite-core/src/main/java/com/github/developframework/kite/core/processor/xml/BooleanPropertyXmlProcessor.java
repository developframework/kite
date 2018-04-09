package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.element.PropertyKiteElement;

import java.util.HashSet;
import java.util.Set;

/**
 * 布尔型属性节点处理器
 * @author qiuzhenhao
 */
public class BooleanPropertyXmlProcessor extends PropertyXmlProcessor {

    /* 允许类型列表 */
    private static final Set<Class<?>> ACCEPT_CLASS_SET = new HashSet<>(8);

    static {
        ACCEPT_CLASS_SET.add(boolean.class);
        ACCEPT_CLASS_SET.add(Boolean.class);
        ACCEPT_CLASS_SET.add(int.class);
        ACCEPT_CLASS_SET.add(Integer.class);
        ACCEPT_CLASS_SET.add(long.class);
        ACCEPT_CLASS_SET.add(Long.class);
        ACCEPT_CLASS_SET.add(short.class);
        ACCEPT_CLASS_SET.add(Short.class);
    }

    public BooleanPropertyXmlProcessor(XmlProcessContext xmlProcessContext, PropertyKiteElement element, Expression parentExpression) {
        super(xmlProcessContext, element, parentExpression);
    }

    @Override
    protected boolean support(Class<?> sourceClass) {
        return ACCEPT_CLASS_SET.contains(sourceClass);
    }

    @Override
    protected void handle(Class<?> clazz, Object value) {
        boolean v;
        if (clazz == Boolean.class) {
            v = ((Boolean) value).booleanValue();
        } else if (clazz == Integer.class) {
            v = ((Integer) value).intValue() != 0;
        } else if (clazz == Long.class) {
            v = ((Long) value).longValue() != 0;
        } else if (clazz == Short.class) {
            v = ((Short) value).shortValue() != 0;
        } else {
            return;
        }
        this.elementAddContent(String.valueOf(v));
    }
}
