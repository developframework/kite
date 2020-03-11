/**
 * @author qiushui on 2020-03-11.
 */
module kite.core {
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires dom4j;
    requires expression;
    requires java.sql;
    requires java.xml;
    requires lombok;
    requires org.apache.commons.lang3;
    requires org.slf4j;

    exports com.github.developframework.kite.core.data;
    exports com.github.developframework.kite.core.dynamic;
    exports com.github.developframework.kite.core.exception;
    exports com.github.developframework.kite.core.saxparser;
    exports com.github.developframework.kite.core.strategy;
    exports com.github.developframework.kite.core;
}