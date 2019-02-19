/**
 * @author qiushui on 2019-02-18.
 */
module kite.spring {

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.xml;
    requires kite.core;
    requires lombok;
    requires org.apache.commons.lang3;
    requires servlet.api;
    requires spring.beans;
    requires spring.context;
    requires spring.core;
    requires spring.web;

    exports com.github.developframework.kite.spring.mvc.annotation;
    exports com.github.developframework.kite.spring.mvc.response;
    exports com.github.developframework.kite.spring.mvc;
    exports com.github.developframework.kite.spring;
}