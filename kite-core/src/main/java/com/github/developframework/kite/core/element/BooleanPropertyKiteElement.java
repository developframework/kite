package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.node.ObjectNodeProxy;
import com.github.developframework.kite.core.structs.FragmentLocation;

import java.util.HashSet;
import java.util.Set;

/**
 * 布尔型属性节点处理器
 *
 * @author qiushui on 2021-06-24.
 */
public class BooleanPropertyKiteElement extends PropertyKiteElement {

    /* 允许类型列表 */
    private static final Set<Class<?>> ACCEPT_CLASS_SET = new HashSet<>();

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

    public BooleanPropertyKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    protected boolean support(Class<?> sourceClass) {
        return ACCEPT_CLASS_SET.contains(sourceClass);
    }

    @Override
    protected void handle(ObjectNodeProxy parentNode, Object value, String displayName) {
        final Class<?> clazz = value.getClass();
        boolean v;
        if (clazz == Boolean.class) {
            v = (Boolean) value;
        } else if (clazz == Integer.class) {
            v = (Integer) value != 0;
        } else if (clazz == Long.class) {
            v = (Long) value != 0;
        } else if (clazz == Short.class) {
            v = (Short) value != 0;
        } else {
            parentNode.putNull(displayName);
            return;
        }
        parentNode.putValue(displayName, v, contentAttributes.xmlCDATA);
    }
}
