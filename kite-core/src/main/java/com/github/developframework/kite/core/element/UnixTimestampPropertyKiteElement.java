package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.node.ObjectNodeProxy;
import com.github.developframework.kite.core.structs.FragmentLocation;

import java.util.Objects;

/**
 * unix时间戳型属性节点处理器
 *
 * @author qiushui on 2021-06-24.
 */
public class UnixTimestampPropertyKiteElement extends DatePropertyKiteElement {

    public UnixTimestampPropertyKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    protected void handle(ObjectNodeProxy parentNode, Object value, String displayName) {
        java.util.Date date = transformDate(value);
        if (Objects.isNull(date)) {
            parentNode.putNull(displayName);
            return;
        }
        parentNode.putValue(displayName, date.getTime() / 1000, contentAttributes.xmlCDATA);
    }
}
