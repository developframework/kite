package com.github.developframework.kite.core.exception;

import com.github.developframework.kite.core.structs.FragmentLocation;

/**
 * 一对一链接数组大小不一致异常
 *
 * @author qiuzhenhao
 */
public class LinkSizeNotEqualException extends KiteException {

    public LinkSizeNotEqualException(FragmentLocation fragmentLocation, int length, int parentLength) {
        super("link length %d is not equal to parent array length %d, position is \"%s\"", length, parentLength, fragmentLocation.toString());
    }
}
