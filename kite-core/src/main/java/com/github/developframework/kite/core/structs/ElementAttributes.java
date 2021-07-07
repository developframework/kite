package com.github.developframework.kite.core.structs;

import com.github.developframework.kite.core.element.AbstractKiteElement;

import java.lang.annotation.*;

/**
 * @author qiushui on 2021-07-07.
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElementAttributes {

    String[] value() default {};

    Class<? extends AbstractKiteElement> baseClass() default AbstractKiteElement.class;
}
