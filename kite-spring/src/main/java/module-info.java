/**
 * @author qiushui on 2021-06-29.
 */
module kite.spring {
    requires javax.servlet.api;
    requires kite.core;
    requires lombok;
    requires org.apache.commons.lang3;
    requires spring.beans;
    requires spring.context;
    requires spring.core;
    requires spring.web;

    exports com.github.developframework.kite.spring;
    exports com.github.developframework.kite.spring.mvc;
    exports com.github.developframework.kite.spring.mvc.response;
    exports com.github.developframework.kite.spring.mvc.annotation;
}