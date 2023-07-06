/**
 * @author qiushui on 2021-06-29.
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
    requires spring.webmvc;
    requires spring.web;
    requires kite.jackson;
    requires kite.dom4j;
    requires dom4j;
    requires org.slf4j;


    exports com.github.developframework.kite.boot;
}