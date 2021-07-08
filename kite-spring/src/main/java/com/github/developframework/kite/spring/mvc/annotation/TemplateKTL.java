package com.github.developframework.kite.spring.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 模板注解，加在controller方法上
 *
 * @author qiushui on 2021-07-06.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TemplateKTL {

    /**
     * ktl 字符串
     */
    String value();

    /**
     * 生成类型
     */
    TemplateType type() default TemplateType.JSON;
}
