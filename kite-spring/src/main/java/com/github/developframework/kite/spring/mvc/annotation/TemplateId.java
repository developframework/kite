package com.github.developframework.kite.spring.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 模板ID注解
 *
 * @author qiuzhenhao
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TemplateId {

    /**
     * 模板id
     */
    String value();

    /**
     * 生成类型
     */
    TemplateType type() default TemplateType.JSON;
}
