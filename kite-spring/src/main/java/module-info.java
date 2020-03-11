/**
 * @author qiushui on 2020-03-11.
 */
module kite.spring {
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.xml;
    requires javax.servlet.api;
    requires kite.core;
    requires lombok;
    requires org.apache.commons.lang3;
    requires spring.beans;
    requires spring.context;
    requires spring.core;
    requires spring.web;

    exports com.github.developframework.kite.spring.mvc.annotation;
    exports com.github.developframework.kite.spring.mvc.response;
    exports com.github.developframework.kite.spring.mvc;
    exports com.github.developframework.kite.spring;
}