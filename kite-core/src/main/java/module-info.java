/**
 * @author qiushui on 2021-06-29.
 */
module kite.core {
    requires dom4j;
    requires expression;
    requires java.sql;
    requires lombok;
    requires org.apache.commons.lang3;
    requires org.slf4j;

    exports com.github.developframework.kite.core;
    exports com.github.developframework.kite.core.data;
    exports com.github.developframework.kite.core.dynamic;
    exports com.github.developframework.kite.core.element;
    exports com.github.developframework.kite.core.exception;
    exports com.github.developframework.kite.core.node;
    exports com.github.developframework.kite.core.parser;
    exports com.github.developframework.kite.core.strategy;
    exports com.github.developframework.kite.core.source;
    exports com.github.developframework.kite.core.structs;
    exports com.github.developframework.kite.core.utils;
}