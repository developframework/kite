package com.github.developframework.kite.boot;

import com.github.developframework.kite.core.strategy.NamingStrategy;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qiuzhenhao
 */
@Getter
@Setter
public class KiteJsonProperties {

    private NamingStrategy namingStrategy = NamingStrategy.FRAMEWORK;
}
