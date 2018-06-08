package com.github.developframework.kite.boot.annotation;

import com.github.developframework.kite.boot.KiteComponentAutoConfiguration;
import com.github.developframework.kite.boot.KiteProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableConfigurationProperties(KiteProperties.class)
@Import(KiteComponentAutoConfiguration.class)
public @interface EnableKite {

}
