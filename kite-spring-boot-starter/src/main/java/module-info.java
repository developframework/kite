/**
 * @author qiushui on 2019-02-18.
 */
module kite.spring.boot.starter {

    requires com.fasterxml.jackson.databind;
    requires kite.core;
    requires kite.spring;
    requires lombok;
    requires org.apache.commons.lang3;
    requires spring.beans;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.context;
    requires spring.web;
    requires spring.webmvc;

    exports com.github.developframework.kite.boot.annotation;
    exports com.github.developframework.kite.boot;

}