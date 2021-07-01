package com.github.developframework.kite.core.strategy;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 命名策略
 *
 * @author qiushui on 2020-08-26.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum NamingStrategy {

    FRAMEWORK(null),

    ORIGINAL(new OriginalKitePropertyNamingStrategy()),

    LOWER_CASE(new LowerCaseKitePropertyNamingStrategy()),

    UNDERLINE(new UnderlineKitePropertyNamingStrategy()),

    MIDDLE_LINE(new MiddleLineKitePropertyNamingStrategy());

    @Getter
    private final KitePropertyNamingStrategy namingStrategy;
}
