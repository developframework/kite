package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.node.ObjectNodeProxy;
import com.github.developframework.kite.core.structs.FragmentLocation;

/**
 * property-unix 时间戳型属性元素
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
        if (date == null) {
            parentNode.putNull(displayName);
        } else {
            parentNode.putValue(displayName, date.getTime() / 1000, contentAttributes.xmlCDATA);
        }
    }
}
