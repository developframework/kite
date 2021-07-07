package com.github.developframework.kite.core;

import com.github.developframework.kite.core.strategy.NamingStrategy;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qiushui on 2021-06-28.
 */
@Getter
public class KiteOptions {

    // ktl缩进空格数
    @Setter
    private int ktlIndent = 4;

    private final JsonOptions json = new JsonOptions();

    private final XmlOptions xml = new XmlOptions();

    @Getter
    @Setter
    public static class JsonOptions {
        private NamingStrategy namingStrategy = NamingStrategy.FRAMEWORK;
    }

    @Getter
    @Setter
    public static class XmlOptions {
        private NamingStrategy namingStrategy = NamingStrategy.FRAMEWORK;
        private boolean suppressDeclaration = true;
    }
}
