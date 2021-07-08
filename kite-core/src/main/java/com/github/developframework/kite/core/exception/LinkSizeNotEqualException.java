package com.github.developframework.kite.core.exception;

import com.github.developframework.kite.core.structs.FragmentLocation;

/**
 * 一对一链接数组大小不一致异常
 *
 * @author qiuzhenhao
 */
public class LinkSizeNotEqualException extends KiteException {

    public LinkSizeNotEqualException(FragmentLocation fragmentLocation, int length, int parentLength) {
        super("link的长度%d不等于父数组长度%d，位置在“%s”", length, parentLength, fragmentLocation.toString());
    }
}
