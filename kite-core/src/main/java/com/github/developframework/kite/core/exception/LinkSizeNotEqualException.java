package com.github.developframework.kite.core.exception;

import com.github.developframework.kite.core.structs.FragmentLocation;

/**
 * 一对一链接数组大小不一致异常
 *
 * @author qiuzhenhao
 */
public class LinkSizeNotEqualException extends KiteException {

    public LinkSizeNotEqualException(FragmentLocation fragmentLocation) {
        super("The element <link> size is not equals parent array size in \"%s\"", fragmentLocation.toString());
    }
}
