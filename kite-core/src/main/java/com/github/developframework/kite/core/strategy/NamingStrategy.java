package com.github.developframework.kite.core.strategy;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 命名策略
 *
 * @author qiushui on 2020-08-26.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum NamingStrategy {

    ORIGINAL(new OriginalKitePropertyNamingStrategy()),

    JACKSON(new JacksonKitePropertyNamingStrategy()),

    LOWER_CASE(new LowerCaseKitePropertyNamingStrategy()),

    UNDERLINE(new UnderlineKitePropertyNamingStrategy()),

    MIDDLE_LINE(new MiddleLineKitePropertyNamingStrategy());

    @Getter
    private final KitePropertyNamingStrategy namingStrategy;
}
