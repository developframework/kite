/**
 * @author qiushui on 2021-06-29.
 */
module kite.jackson {
    requires lombok;
    requires com.fasterxml.jackson.databind;
    requires kite.core;

    exports com.github.developframework.kite.jackson;
}